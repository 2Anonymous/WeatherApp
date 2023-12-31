package com.sukdeb.weatherApp.utils.ext

import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.os.SystemClock
import android.text.format.DateUtils
import android.view.View
import android.webkit.URLUtil
import androidx.annotation.RequiresApi
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sukdeb.weatherApp.app.AppData.EMAIL_PATTERN
import com.sukdeb.weatherApp.app.AppData.MOBILE_PATTERN
import com.sukdeb.weatherApp.domain.responseModel.Resource
import org.json.JSONArray
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.regex.Pattern

/**Convert simple object to String with Gson*/
inline fun <reified T : Any> T.toSimpleJson() : String =  Gson().toJson(this, T::class.java)

/**Convert String Json to Object*/
inline fun <reified T : Any> String.fromJsonToObject() : T =  Gson().fromJson(this ,  T::class.java)

/**Convert String List Json to Object*/
inline fun <reified T : Any> String.fromJsonToObjectList() : MutableList <T> =  when( this.isNotEmpty()){
    true -> Gson().fromJson(this, object : TypeToken<MutableList<T>>() {}.type)
    false -> mutableListOf()
}

fun JSONArray.toMutableList(): MutableList<Any> = MutableList(length(), this::get)

inline fun <T> safeCall(action: () -> Resource<T>): Resource<T> {
    return try {
        action()
    } catch (e: Exception) {
        Resource.Error(message = e.message ?: "An unknown Error Occurred")
    }
}

fun Context.BitmapToFile(bitmap: Bitmap, filename: String): File? { // File name like "image.png"
    //create a file to write bitmap data
    var file: File? = null
    return try {
        /*file = File(Environment.getExternalStorageDirectory().toString() + File.separator + fileNameToSave)*/
        file = File(cacheDir, filename)
        file.createNewFile()

        //Convert bitmap to byte array
        val bos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos) // YOU can also save it in JPEG
        val bitmapdata = bos.toByteArray()

        //write the bytes in file
        val fos = FileOutputStream(file)
        fos.write(bitmapdata)
        fos.flush()
        fos.close()
        file
    } catch (e: Exception) {
        e.printStackTrace()
        file // it will return null
    }
}
 fun Context.randomAlphaNumericString(length: Int): String {
    val alphaNumeric = ('A'..'Z') + ('0'..'9')
    var testString = ""
    for(i in 1.. 5){
        testString = "$testString-${alphaNumeric.shuffled().take(length).joinToString("")}"
    }
    return testString.drop(1)
}

fun String.dateFormatChange(serverFormat:String,updateFormat:String): String {
    val dateFormat1 = SimpleDateFormat(serverFormat)
    val date = dateFormat1.parse(this)
    val dateFormat = SimpleDateFormat(updateFormat)
    return dateFormat.format(date)
}

fun Long.toDateString(datFormat: String): String {
    val dateTimeFormatter = SimpleDateFormat(datFormat)
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = this
    return dateTimeFormatter.format(calendar.time)
}

fun String.dateFormatChangeForCalender(oldDateFormat: String,newDateFormat :String): String? {
    val oldDateFormat = SimpleDateFormat(oldDateFormat, Locale.getDefault())
    val newDateFormat = SimpleDateFormat(newDateFormat, Locale.getDefault())
    return oldDateFormat.parse(this)?.let { newDateFormat.format(it) }
}
fun String.convertToDate(format: String = "yyyy-MM-dd", localeId: Locale = Locale.getDefault()): Date? {
    try {
        return SimpleDateFormat(format, localeId).parse(this)
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return null
}
fun Date.calculateAgoTime(): String = DateUtils.getRelativeTimeSpanString(time,
    Calendar.getInstance().timeInMillis, DateUtils.MINUTE_IN_MILLIS).toString()

fun currentDateAsDF(format: String = "yyyy-MM-dd HH:mm:ss"): String? {
    return SimpleDateFormat(format, Locale.getDefault()).format(Date())
}
@RequiresApi(Build.VERSION_CODES.O)
fun LocalDate.monthYearFromDate(datFormat: String): String {
    val dateTimeFormatter = DateTimeFormatter.ofPattern(datFormat)
    return this.format(dateTimeFormatter)
}

val emailValid = fun(mail: String): Boolean {
    return Pattern.compile(EMAIL_PATTERN).matcher(mail).matches()
}

val phoneValid = fun(phoneNumber: String): Boolean {
    return Pattern.compile(MOBILE_PATTERN).matcher(phoneNumber).matches()
}

val urlValid = fun(url:String):Boolean{
    return URLUtil.isValidUrl(url)
}

fun View.clickWithDebounce(debounceTime: Long = 900L, action: () -> Unit) {
    this.setOnClickListener(object : View.OnClickListener {
        private var lastClickTime: Long = 0

        override fun onClick(v: View) {
            if (SystemClock.elapsedRealtime() - lastClickTime < debounceTime) return
            else action()
            lastClickTime = SystemClock.elapsedRealtime()
        }
    })
}