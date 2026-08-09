package moe.shizuku.manager.adb

import android.content.Context
import android.database.ContentObserver
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import moe.shizuku.manager.ShizukuSettings
import moe.shizuku.manager.utils.ShizukuStateMachine
import moe.shizuku.manager.worker.AdbStartWorker

object AdbConnectivityManager {

    private val handler = Handler(Looper.getMainLooper())

    private val adbWifiObserver = object : ContentObserver(handler) {
        override fun onChange(selfChange: Boolean) {
            val context = moe.shizuku.manager.ShizukuApplication.appContext
            if (Settings.Global.getInt(context.contentResolver, "adb_wifi_enabled", 0) == 1) {
                if (ShizukuSettings.getLastLaunchMode() == ShizukuSettings.LaunchMethod.ADB && !ShizukuStateMachine.isRunning()) {
                    AdbStartWorker.enqueue(context, force = true)
                }
            }
        }
    }

    fun start(context: Context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) return

        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val request = NetworkRequest.Builder()
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .build()
        
        try {
            cm.registerNetworkCallback(request, object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    if (ShizukuSettings.getLastLaunchMode() == ShizukuSettings.LaunchMethod.ADB && !ShizukuStateMachine.isRunning()) {
                        AdbStartWorker.enqueue(context)
                    }
                }
            })
        } catch (e: Exception) {
            // Log or ignore
        }

        try {
            context.contentResolver.registerContentObserver(
                Settings.Global.getUriFor("adb_wifi_enabled"),
                false,
                adbWifiObserver
            )
        } catch (e: Exception) {
            // Log or ignore
        }
    }
}
