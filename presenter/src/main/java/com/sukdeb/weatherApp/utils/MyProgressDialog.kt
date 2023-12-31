package com.sukdeb.weatherApp.utils

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import com.sukdeb.weatherApp.R

class MyProgressDialog(mContext: Context) : Dialog(mContext, R.style.RoundedCornersDialog) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_my_progress)
        setCancelable(false)
    }
}