package com.sukdeb.weatherApp.utils.ext

import android.app.Dialog
import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.sukdeb.weatherApp.R
import java.io.File

fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Context.toast(message: String, gravity: Int) {
    val toast = Toast.makeText(this, message, Toast.LENGTH_SHORT)
    toast.show()
}

fun Fragment.toast(msg: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(requireContext(), msg, duration).show()
}

fun ProgressBar.show() {
    visibility = View.VISIBLE
}

fun ProgressBar.hide() {
    visibility = View.GONE
}

fun View.snackBar(message: String) {
    Snackbar.make(this, message, Snackbar.LENGTH_SHORT).also { snack ->
        snack.setAction("Ok") {
            snack.dismiss()
        }
    }.show()
}

/**View Visibility Ext*/
fun View.gone() {
    if (visibility != View.GONE) visibility = View.GONE
}

fun View.visible() {
    if (visibility != View.VISIBLE) visibility = View.VISIBLE
}

fun View.invisible() {
    if (visibility != View.INVISIBLE) visibility = View.INVISIBLE
}

/**View Get Value Ext*/
val Button.value
    get() = text?.toString() ?: ""

val EditText.value
    get() = text?.toString() ?: ""

val TextView.value
    get() = text?.toString() ?: ""

/**View Empty validation Ext*/
fun EditText.isEmpty() = value.isEmpty()

fun TextView.isEmpty() = value.isEmpty()

/**Image SetUp Using Glide*/
@Nullable
@NonNull
fun ImageView.loadImage(imageId: String) {
    Glide.with(this)
        .asBitmap()
        .load(imageId)
        .circleCrop()
        .into(this)
}

/**Image SetUp Using Glide*/
@Nullable
@NonNull
fun ImageView.loadFileImage(imageId: File) {
    Glide.with(this)
        .asBitmap()
        .load(imageId)
        .circleCrop()
        .skipMemoryCache(true)
        .into(this)
}

@Nullable
@NonNull
fun ImageView.loadImageWithOutCenter(imageId: Any) {
    Glide.with(this)
        .asBitmap()
        .load(imageId)
        .into(this)
}

/**View Enable/Disable Ext*/
fun View.enable() {
    isEnabled = true
    alpha = 1f
}

fun View.disable() {
    isEnabled = false
    alpha = 0.5f
}

/**Extension method for Material Alert Dialog Using Higher order function.*/
inline fun Context.showAlertFunction(title: String, message: String, positiveBtnTxt: String = "Yes", negativeBtnTxt: String = "No", crossinline positiveBtnClick: () -> Unit) {
    MaterialAlertDialogBuilder(this, R.style.AlertDialogTheme)
        .setCancelable(false)
        .setTitle(title)
        .setMessage(message)
        .setPositiveButton(positiveBtnTxt) { _, _ ->
            positiveBtnClick()
        }
        .setNegativeButton(negativeBtnTxt, null)
        .show()
}

/**Extension method for Material Alert Dialog Using Higher order function.*/
inline fun Context.showOkayAlertFunction(title: String, message: String, positiveBtnTxt: String = "Okay", crossinline positiveBtnClick: () -> Unit) {
    MaterialAlertDialogBuilder(this, R.style.AlertDialogTheme)
        .setCancelable(false)
        .setTitle(title)
        .setMessage(message)
        .setPositiveButton(positiveBtnTxt) { _, _ ->
            positiveBtnClick()
        }
        .show()
}

fun Context.showAlertFunction(title: String, message: String) {
    MaterialAlertDialogBuilder(this, R.style.AlertDialogTheme)
        .setCancelable(false)
        .setTitle(title)
        .setMessage(message)
        .show()
}

/**
 * Call this method (in onActivityCreated or later) to set
 * the width of the dialog to a percentage of the current
 * screen width.
 */
fun DialogFragment.setWidthPercent(percentage: Int) {
    val percent = percentage.toFloat() / 100
    //val height = height.toFloat()/100
    val dm = Resources.getSystem().displayMetrics
    val rect = dm.run { Rect(10, 10, widthPixels, heightPixels) }
    val percentWidth = rect.width() * percent
    dialog?.window?.setLayout(percentWidth.toInt(), ViewGroup.LayoutParams.WRAP_CONTENT)
    dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

    fun EditText.showKeyboard() {
        requestFocus()
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
    }

    fun EditText.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(this.windowToken, 0)
    }
}

open class BaseBottomSheetFragment : BottomSheetDialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        if (dialog.window != null) {
            dialog.window!!.setGravity(Gravity.BOTTOM)
            dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
            dialog.window!!.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
            dialog.setCancelable(false)
        }
        return dialog
    }
}

fun TextView.addReadMore(maxLength: Int) {
    if (text.length <= maxLength) return
    var text: String = this.text.toString()
    if (text.contains("...Less")) text = text.replace("...Less", "")
    val ss = SpannableString(text.substring(0, maxLength) + "...More")
    val clickableSpan: ClickableSpan = object : ClickableSpan() {
        override fun onClick(view: View) {
            addReadLess(text, maxLength)
        }
        override fun updateDrawState(ds: TextPaint) {
            super.updateDrawState(ds)
            ds.isUnderlineText = false
            ds.color = resources.getColor(R.color.black)
        }
    }
    ss.setSpan(clickableSpan, ss.length - 7, ss.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    this.text = ss
    this.movementMethod = LinkMovementMethod.getInstance()
}
fun TextView.addReadLess(text: String, maxLength: Int) {
    val ss = SpannableString("$text...Less")
    val clickableSpan: ClickableSpan = object : ClickableSpan() {
        override fun onClick(view: View) {
            addReadMore(maxLength)
        }
        override fun updateDrawState(ds: TextPaint) {
            super.updateDrawState(ds)
            ds.isUnderlineText = false
            ds.color = resources.getColor(R.color.black)
        }
    }
    ss.setSpan(clickableSpan, ss.length - 7, ss.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    this.text = ss
    this.movementMethod = LinkMovementMethod.getInstance()
}