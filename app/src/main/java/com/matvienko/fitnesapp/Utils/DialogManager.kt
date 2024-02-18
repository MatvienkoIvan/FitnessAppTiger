package com.matvienko.fitnesapp.Utils

import android.app.AlertDialog
import android.content.Context
import com.matvienko.fitnesapp.R

object DialogManager {

    fun showDialog(context: Context, mId: String, listener: Listener) {
        val builder = AlertDialog.Builder(context)
        var dialog: AlertDialog? = null
        builder.setTitle(context.getString(R.string.Attention))
        builder.setMessage(mId)
        builder.setPositiveButton(context.getString(R.string.ok)) { _, _ ->
            listener.onClick()
            dialog?.dismiss()
        }
        builder.setNegativeButton(context.getString(R.string.back)) { _, _ ->
            dialog?.dismiss()
        }

        dialog = builder.create()
        dialog?.show()
    }
    interface Listener {
        fun onClick()
    }

}