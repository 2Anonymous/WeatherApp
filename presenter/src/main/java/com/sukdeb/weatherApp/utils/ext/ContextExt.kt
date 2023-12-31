package com.sukdeb.weatherApp.utils.ext

import android.content.*
import android.database.Cursor
import android.location.LocationManager
import android.media.RingtoneManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.provider.ContactsContract
import android.util.Log
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.SettingsClient

fun Context.shareUrl(url: String) {
    try {
        Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, url)
            type = "text/plain"
            startActivity(this)
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun Context.openEmail() {
    try {
        Intent().apply {
            action = Intent.ACTION_MAIN
            addCategory(Intent.CATEGORY_APP_EMAIL)
            startActivity(this)
        }
    } catch (e: ActivityNotFoundException) {
        toast("There is no email client installed.")
    }
}

fun Context.Invite(packageName: String, contentBody: String) {
    val intent = packageManager.getLaunchIntentForPackage(packageName)
    if (intent != null) {
        // The application exists
        Intent().apply {
            action = Intent.ACTION_SEND
            /*`package` = packageName*/
            putExtra(Intent.EXTRA_TEXT, contentBody)
            type = "text/plain"
            // Start the specific social application
            startActivity(this)
        }
    } else {
        // The application does not exist
        // Open GooglePlay or use the default system picker
        toast("The application does not exist")
    }
}

/**Extension method: to another activity*/
fun Context.gotToActivity(cls: Class<*>) {
    try {
        Intent(this, cls).also {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                it.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            this.startActivity(it)
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

/**Extension method: to another activity without stack. it clear all previous stack*/
fun Context.gotToActivityWithoutStack(cls: Class<*>) {
    try {
        Intent(this, cls).also {
            it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            this.startActivity(it)
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

/**Extension method: to another activity with parameters.*/
fun Context.gotToActivityWithHashMap(cls: Class<*>?, myMap: HashMap<String, Double>) {
    try {
        Intent(this, cls).also {
            for (key in myMap.keys) {
                it.putExtra(key, myMap[key])
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                it.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            this.startActivity(it)
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

/**Extension method: to play notification sound.*/
fun Context.playNotificationSound() {
    try {
        val notification: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val r = RingtoneManager.getRingtone(this, notification)
        r.play()
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

/**Check Connectivity*/
val Context.isConnected: Boolean
    get() {
        return when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> {
                val nw = connectivityManager?.activeNetwork ?: return false
                val actNw = connectivityManager?.getNetworkCapabilities(nw) ?: return false
                when {
                    actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                    actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                    else -> false
                }
            }
            else -> {
                // Use depreciated methods only on older devices
                val nwInfo = connectivityManager?.activeNetworkInfo ?: return false
                nwInfo.isConnected
            }
        }
    }

/**Check if the device has an internet connection
 * @return True if the device is connected to a network which also gives it access to the internet.
 * False otherwise.
 */
val Context.isInternetConnectionAvailable: Boolean
    get() {
        return if (isConnected) {
            when {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> {
                    val nw = connectivityManager?.activeNetwork ?: return false
                    val actNw = connectivityManager?.getNetworkCapabilities(nw) ?: return false
                    /*If we check only for "NET_CAPABILITY_INTERNET", we get "true" if we are connected to a wifi which has no access to the internet.
                    "NET_CAPABILITY_VALIDATED" also verifies that we are online*/
                    when {
                        actNw.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) && actNw.hasCapability(
                            NetworkCapabilities.NET_CAPABILITY_VALIDATED
                        ) -> true
                        else -> false
                    }
                }
                else -> {
                    // Use depreciated methods only on older devices
                    val netInfo = connectivityManager?.activeNetworkInfo
                    netInfo?.isConnectedOrConnecting == true
                }
            }
        } else {
            false
        }
    }

/**Extension method to get connectivityManager for Context.*/
inline val Context.connectivityManager: ConnectivityManager?
    get() = getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager

/**Extension method to get Location Manager for Context.*/
inline val Context.locationManager: LocationManager
    get() = getSystemService(Context.LOCATION_SERVICE) as LocationManager

/**Extension method to get Location Client for Context.*/
inline val Context.locationClient : SettingsClient
    get() = LocationServices.getSettingsClient(this)

fun Context.copyToClipboard(text: CharSequence) {
    val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("label", text)
    clipboard.setPrimaryClip(clip)
    toast("Copied")
}

fun Context.openUrl(sourceUrl: String) {
    val openURL = Intent(Intent.ACTION_VIEW)
    openURL.addCategory(Intent.CATEGORY_BROWSABLE)
    openURL.data = Uri.parse(sourceUrl)
    startActivity(openURL)
}

val projection = arrayOf(
    ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
    ContactsContract.CommonDataKinds.Email.ADDRESS,
    ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
    ContactsContract.CommonDataKinds.Phone.NUMBER
)