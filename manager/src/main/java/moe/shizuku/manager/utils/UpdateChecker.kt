package moe.shizuku.manager.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream

object UpdateChecker {

    private const val GITHUB_API_URL = "https://api.github.com/repos/symbuzzer/fork-Shizuku/releases"

    private val client = OkHttpClient()

    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
        return when {
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }

    suspend fun checkUpdate(currentVersion: String): UpdateResult = withContext(Dispatchers.IO) {
        try {
            val request = Request.Builder()
                .url(GITHUB_API_URL)
                .header("User-Agent", "Shizuku-fork")
                .build()
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) return@withContext UpdateResult.Error("API call failed: ${response.code}")
                
                val body = response.body?.string() ?: return@withContext UpdateResult.Error("Empty response")
                val releases = JSONArray(body)
                if (releases.length() == 0) return@withContext UpdateResult.NoUpdate

                val latestRelease = releases.getJSONObject(0)
                val latestTag = latestRelease.getString("tag_name")
                
                if (compareVersions(latestTag, currentVersion) > 0) {
                    val assets = latestRelease.getJSONArray("assets")
                    var downloadUrl: String? = null
                    
                    for (i in 0 until assets.length()) {
                        val asset = assets.getJSONObject(i)
                        val name = asset.getString("name")
                        if (name.endsWith(".apk", ignoreCase = true)) {
                            downloadUrl = asset.getString("browser_download_url")
                            break
                        }
                    }

                    if (downloadUrl != null) {
                        UpdateResult.NewVersion(latestTag, downloadUrl)
                    } else {
                        // If no APK found in the latest release, check if it's actually an update
                        UpdateResult.NoUpdate 
                    }
                } else {
                    UpdateResult.NoUpdate
                }
            }
        } catch (e: Exception) {
            UpdateResult.Error(e.message ?: "Unknown error")
        }
    }

    suspend fun downloadApk(
        context: Context,
        url: String,
        targetFile: File,
        onProgress: (Int) -> Unit
    ): Boolean = withContext(Dispatchers.IO) {
        try {
            val request = Request.Builder().url(url).build()
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) return@withContext false
                val body = response.body ?: return@withContext false
                val contentLength = body.contentLength()
                
                body.byteStream().use { inputStream ->
                    FileOutputStream(targetFile).use { outputStream ->
                        val buffer = ByteArray(8192)
                        var bytesRead: Int
                        var totalBytesRead = 0L
                        
                        while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                            outputStream.write(buffer, 0, bytesRead)
                            totalBytesRead += bytesRead
                            if (contentLength > 0) {
                                val progress = (totalBytesRead * 100 / contentLength).toInt()
                                withContext(Dispatchers.Main) {
                                    onProgress(progress)
                                }
                            }
                        }
                    }
                }
                true
            }
        } catch (e: Exception) {
            false
        }
    }

    private fun compareVersions(latest: String, current: String): Int {
        val v1 = latest.trimStart('v').split(".")
        val v2 = current.trimStart('v').split(".")
        
        val length = maxOf(v1.size, v2.size)
        for (i in 0 until length) {
            val part1 = v1.getOrNull(i)?.takeWhile { it.isDigit() }?.toIntOrNull() ?: 0
            val part2 = v2.getOrNull(i)?.takeWhile { it.isDigit() }?.toIntOrNull() ?: 0
            if (part1 > part2) return 1
            if (part1 < part2) return -1
        }
        return 0
    }

    sealed class UpdateResult {
        object NoUpdate : UpdateResult()
        data class NewVersion(val tag: String, val downloadUrl: String) : UpdateResult()
        data class Error(val message: String) : UpdateResult()
    }
}
