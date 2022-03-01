package com.bignerdranch.android.photogallery


import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.WindowManager
import android.widget.Toast

class ProgressDialog(context: Context) : Dialog(context) {

    private val handler: Handler?
    fun showProgress() {
        Log.d("progress","showProgress!!")
        if (!isShowing) {
            show()
            startTimer()
        }
    }

    fun hideProgress() {
        handler?.removeCallbacksAndMessages(null)
        dismiss()
    }

    private fun startTimer() {
        handler?.postDelayed({
            Toast.makeText(context, "네트워크 연결을 확인해주세요.", Toast.LENGTH_SHORT).show()
            hideProgress()
        }, 10000L)
    }

    init {
        setContentView(R.layout.dialog_progress)
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window!!.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        setCanceledOnTouchOutside(false)
        setCancelable(false)
        handler = Handler(Looper.getMainLooper())
    }
}