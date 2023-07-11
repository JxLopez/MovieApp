package com.jxlopez.movieapp.util.components

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.widget.Button
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatTextView
import com.jxlopez.movieapp.R
import java.util.concurrent.Callable

@SuppressLint("SupportAnnotationUsage")
class MovieStyleAlertDialog(builder: Builder) {

    @DrawableRes
    private val message: String?
    private val ctx: Context?
    private val activity: Activity?
    private val func: Callable<Int>?
    private var buildDialog: AlertDialog? = null
    private val cancelable: Boolean
    private val isSuccess: Boolean

    init {
        message = builder.message
        ctx = builder.ctx
        activity = builder.activity
        func = builder.func
        cancelable = builder.cancelable
        isSuccess = builder.isSuccess
    }

    fun showAlert() {
        val buildMD = activity?.layoutInflater?.inflate(R.layout.dialog_custom_error, null)
        buildDialog = AlertDialog.Builder(ctx!!, R.style.RoundedCornersDialog).apply {
            setView(buildMD)
            setCancelable(cancelable)

            val btnOk = buildMD?.findViewById<Button>(R.id.btnOk)
            val txtMessage = buildMD?.findViewById<AppCompatTextView>(R.id.tvError)

            txtMessage?.text = message


            btnOk?.setOnClickListener {
                try {
                    func?.call()
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    dismiss()
                }
            }

        }.create()

        buildDialog?.show()
    }

    fun dismiss() {
        buildDialog?.dismiss()
    }

    class Builder {
        var message: String? = null
        var cancelable: Boolean = false
        var ctx: Context? = null
        var activity: Activity? = null
        var func: Callable<Int>? = null
        var isSuccess = true

        fun message(message: String): Builder {
            this.message = message
            return this
        }

        fun isSuccess(isSuccess: Boolean): Builder {
            this.isSuccess = isSuccess
            return this
        }

        fun cancelable(cancelable: Boolean): Builder {
            this.cancelable = cancelable
            return this
        }

        fun context(ctx: Context): Builder {
            this.ctx = ctx
            return this
        }

        fun activity(activity: Activity): Builder {
            this.activity = activity
            return this
        }

        fun calledAction(func: Callable<Int>): Builder {
            this.func = func
            return this
        }

        fun build(): MovieStyleAlertDialog {
            return MovieStyleAlertDialog(this)
        }
    }

}