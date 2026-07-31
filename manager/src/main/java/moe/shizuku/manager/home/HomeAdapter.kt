package moe.shizuku.manager.home

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.view.View
import kotlinx.coroutines.CoroutineScope
import moe.shizuku.manager.R
import moe.shizuku.manager.management.AppsViewModel
import moe.shizuku.manager.utils.EnvironmentUtils
import moe.shizuku.manager.utils.UserHandleCompat
import rikka.recyclerview.BaseViewHolder
import rikka.recyclerview.BaseViewHolder.Creator
import rikka.recyclerview.IdBasedRecyclerViewAdapter
import rikka.recyclerview.IndexCreatorPool
import rikka.shizuku.Shizuku

class HomeAdapter(private val homeModel: HomeViewModel, private val appsModel: AppsViewModel, private val scope: CoroutineScope) :
    IdBasedRecyclerViewAdapter(ArrayList()) {

    init {
        updateData()
        setHasStableIds(true)
    }

    companion object {

        private const val ID_STATUS = 0L
        private const val ID_APPS = 1L
        private const val ID_TERMINAL = 2L
        private const val ID_START_ROOT = 3L
        private const val ID_START_WADB = 4L
        private const val ID_START_ADB = 5L
        private const val ID_ADB_PERMISSION_LIMITED = 7L
        private const val ID_AUTOMATION = 8L
        private const val ID_FOOTER = 10L
    }

    override fun onCreateCreatorPool(): IndexCreatorPool {
        return IndexCreatorPool()
    }

    fun updateData() {
        val status = homeModel.serviceStatus.value?.data ?: return
        val grantedCount = appsModel.grantedCount.value?.data ?: 0
        val adbPermission = status.permission
        val running = status.isRunning
        val isPrimaryUser = UserHandleCompat.myUserId() == 0

        clear()
        addItem(ServerStatusViewHolder.CREATOR, status, ID_STATUS)

        if (adbPermission) {
            addItem(ManageAppsViewHolder.CREATOR, status to grantedCount, ID_APPS)
            addItem(TerminalViewHolder.CREATOR, status, ID_TERMINAL)
        }

        if (running && !adbPermission) {
            addItem(AdbPermissionLimitedViewHolder.CREATOR, status, ID_ADB_PERMISSION_LIMITED)
        }

        if (isPrimaryUser) {
            val rootRestart = running && status.uid == 0

            if (EnvironmentUtils.isRooted()) addItem(StartRootViewHolder.CREATOR, rootRestart, ID_START_ROOT)

            if (!running && (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R ||
                EnvironmentUtils.isTelevision() ||
                EnvironmentUtils.getAdbTcpPort() > 0)
            ) addItem(StartWirelessAdbViewHolder.creator(scope), null, ID_START_WADB)

            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
                addItem(StartAdbViewHolder.CREATOR, null, ID_START_ADB)
            }
        }
        addItem(AutomationViewHolder.CREATOR, null, ID_AUTOMATION)

        addItem(FooterViewHolder.CREATOR, null, ID_FOOTER)

        notifyDataSetChanged()
    }
}

class FooterViewHolder(root: View) : BaseViewHolder<Any?>(root) {
    companion object {
        val CREATOR = Creator<Any> { inflater, parent ->
            FooterViewHolder(inflater.inflate(R.layout.home_footer, parent, false))
        }
    }

    init {
        root.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/symbuzzer/fork-Shizuku"))
            it.context.startActivity(intent)
        }
    }
}
