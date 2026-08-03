package moe.shizuku.manager.home

import android.app.NotificationManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import moe.shizuku.manager.utils.UpdateChecker
import android.os.Bundle
import android.os.Process
import android.text.method.LinkMovementMethod
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import moe.shizuku.manager.R
import moe.shizuku.manager.ShizukuSettings
import moe.shizuku.manager.adb.AdbPairingService
import moe.shizuku.manager.app.AppBarActivity
import moe.shizuku.manager.app.SnackbarHelper
import moe.shizuku.manager.databinding.HomeActivityBinding
import moe.shizuku.manager.home.showAccessibilityDialog
import moe.shizuku.manager.ktx.toHtml
import moe.shizuku.manager.management.AppsViewModel
import moe.shizuku.manager.settings.SettingsActivity
import moe.shizuku.manager.utils.AppIconCache
import moe.shizuku.manager.utils.EnvironmentUtils
import moe.shizuku.manager.utils.SettingsHelper
import moe.shizuku.manager.utils.ShizukuStateMachine
import rikka.core.content.asActivity
import rikka.core.ktx.unsafeLazy
import rikka.lifecycle.Status
import rikka.recyclerview.addEdgeSpacing
import rikka.recyclerview.addItemSpacing
import rikka.recyclerview.fixEdgeEffect
import rikka.shizuku.Shizuku

abstract class HomeActivity : AppBarActivity() {

    private val homeModel: HomeViewModel by viewModels()
    private val appsModel: AppsViewModel by viewModels()
    private val adapter by unsafeLazy { HomeAdapter(homeModel, appsModel, lifecycleScope) }

    private val stateListener: (ShizukuStateMachine.State) -> Unit = {
        if (ShizukuStateMachine.isRunning()) {
            checkServerStatus()
            appsModel.load()
        } else if (ShizukuStateMachine.isDead()) {
            checkServerStatus()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitle("${getString(R.string.app_name)} v${moe.shizuku.manager.BuildConfig.VERSION_NAME}")

        val binding = HomeActivityBinding.inflate(layoutInflater, rootView, true)

        homeModel.serviceStatus.observe(this) {
            if (it.status == Status.SUCCESS) {
                val status = homeModel.serviceStatus.value?.data ?: return@observe
                adapter.updateData()
                ShizukuSettings.setLastLaunchMode(if (status.uid == 0) ShizukuSettings.LaunchMethod.ROOT else ShizukuSettings.LaunchMethod.ADB)
            }
        }

        homeModel.shouldShowRebootDialog.observe(this) { shouldShow ->
            if (shouldShow) showExitDialog(
                getString(R.string.home_dialog_reboot_required_title),
                getString(R.string.home_dialog_reboot_required_message)
            )
        }

        homeModel.shouldShowUninstallDialog.observe(this) { shouldShow ->
            if (shouldShow) showExitDialog(
                getString(R.string.home_dialog_duplicate_app_detected_title),
                getString(R.string.home_dialog_duplicate_app_detected_message)
            )
        }

        homeModel.shouldShowBatteryOptimizationSnackbar.observe(this) { shouldShow ->
            if (shouldShow) SnackbarHelper.show(
                this,
                binding.root,
                msg = getString(R.string.snackbar_battery_optimization_home),
                duration = Snackbar.LENGTH_INDEFINITE,
                actionText = getString(R.string.snackbar_action_fix),
                action = { SettingsHelper.requestIgnoreBatteryOptimizations(this, null) }
            )
        }
        homeModel.checkBatteryOptimization()

        appsModel.grantedCount.observe(this) {
            if (it.status == Status.SUCCESS) {
                adapter.updateData()
            }
        }

        val recyclerView = binding.list
        recyclerView.adapter = adapter
        recyclerView.fixEdgeEffect()

        val cardSpacing = resources.getDimension(R.dimen.card_spacing)
        val marginHorizontal = resources.getDimension(R.dimen.margin_horizontal)
        val marginVertical = resources.getDimension(R.dimen.margin_vertical)

        val itemSpacing = cardSpacing / 2f
        val edgeSpacingH = marginHorizontal
        val edgeSpacingV = marginVertical - itemSpacing

        recyclerView.addItemSpacing(top = itemSpacing, bottom = itemSpacing)
        recyclerView.addEdgeSpacing(top = edgeSpacingV, bottom = edgeSpacingV, left = edgeSpacingH, right = edgeSpacingH)

        ShizukuStateMachine.addListener(stateListener)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.let {
            val showDialog = it.getBooleanExtra(HomeActivity.EXTRA_SHOW_PAIRING_DIALOG, false)
            if (showDialog) showAccessibilityDialog()

            val startWadb = it.getBooleanExtra(HomeActivity.EXTRA_START_SERVICE_VIA_WADB, false)
            if (startWadb) {
                val nm = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                nm.cancel(AdbPairingService.NOTIFICATION_ID)
                StartWirelessAdbViewHolder.start(this, lifecycleScope)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        checkServerStatus()
        appsModel.load()
    }

    override fun onPause() {
        super.onPause()
        SnackbarHelper.dismiss()
    }

    private fun showExitDialog(title: String, message: String) {
        val dialog = MaterialAlertDialogBuilder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(R.string.home_dialog_button_exit, null)
            .setOnDismissListener {
                this.finishAffinity()
            }
            .create()

        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
    }

    private fun checkServerStatus() {
        homeModel.reload()
    }

    override fun onDestroy() {
        ShizukuStateMachine.removeListener(stateListener)
        super.onDestroy()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_update -> {
                checkUpdate()
                true
            }
            R.id.action_settings -> {
                startActivity(Intent(this, SettingsActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun checkUpdate() {
        if (!UpdateChecker.isNetworkAvailable(this)) {
            MaterialAlertDialogBuilder(this)
                .setTitle(R.string.error)
                .setMessage(R.string.update_check_failed)
                .setPositiveButton(android.R.string.ok, null)
                .show()
            return
        }

        val progressDialog = MaterialAlertDialogBuilder(this)
            .setMessage(R.string.update_checking)
            .setCancelable(false)
            .show()

        lifecycleScope.launch {
            val result = UpdateChecker.checkUpdate(moe.shizuku.manager.BuildConfig.VERSION_NAME)
            progressDialog.dismiss()

            when (result) {
                is UpdateChecker.UpdateResult.NewVersion -> {
                    MaterialAlertDialogBuilder(this@HomeActivity)
                        .setTitle(R.string.action_update)
                        .setMessage(R.string.update_available)
                        .setPositiveButton(R.string.update_download) { _, _ ->
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(result.downloadUrl))
                            try {
                                startActivity(intent)
                            } catch (e: Exception) {
                                SnackbarHelper.show(this@HomeActivity, rootView, getString(R.string.dialog_cannot_open_browser_title), Snackbar.LENGTH_SHORT)
                            }
                        }
                        .setNegativeButton(android.R.string.cancel, null)
                        .show()
                }
                is UpdateChecker.UpdateResult.NoUpdate -> {
                    MaterialAlertDialogBuilder(this@HomeActivity)
                        .setTitle(R.string.action_update)
                        .setMessage(R.string.update_not_available)
                        .setPositiveButton(android.R.string.ok, null)
                        .show()
                }
                is UpdateChecker.UpdateResult.Error -> {
                    MaterialAlertDialogBuilder(this@HomeActivity)
                        .setTitle(R.string.error)
                        .setMessage(R.string.update_check_failed)
                        .setPositiveButton(android.R.string.ok, null)
                        .show()
                }
            }
        }
    }

    companion object {
        const val EXTRA_SHOW_PAIRING_DIALOG = "show_pairing_dialog"
        const val EXTRA_START_SERVICE_VIA_WADB = "start_service_via_wadb"
    }

}
