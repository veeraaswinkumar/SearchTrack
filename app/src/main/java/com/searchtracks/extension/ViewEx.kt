package com.searchtracks.extension

import android.content.Context
import android.graphics.Typeface
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.*
import androidx.core.content.ContextCompat
import com.searchtracks.R
import com.google.android.material.textfield.TextInputLayout

fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.hide() {
    this.visibility = View.GONE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun RadioButton.setTextStyle(textStyle: Int) {
    this.setTypeface(null, textStyle)
}

fun RadioButton.setTextStyleBold() {
    this.setTypeface(null, Typeface.BOLD)
}

fun CheckBox.setTextStyleBold() {
    this.setTypeface(null, Typeface.BOLD)
}

fun RadioButton.setTextStyleNormal() {
    this.setTypeface(null, Typeface.NORMAL)
}

fun CheckBox.setTextStyleNormal() {
    this.setTypeface(null, Typeface.NORMAL)
}

fun RadioButton.removeDrawable() {
    this.setCompoundDrawablesWithIntrinsicBounds(0, 0,0, 0)
}

fun CheckBox.removeDrawable() {
    this.setCompoundDrawablesWithIntrinsicBounds(0, 0,0, 0)
}

fun EditText.validate(message: String, updateUI: Boolean, validator: (String) -> Boolean) {
    if (updateUI) {
        setError(this, null)
        val error = if (validator(this.text.toString())) null else message
        setError(this, error)
    } else
        this.afterTextChanged {
            setError(this, null)
            val error = if (validator(it)) null else message
            setError(this, error)
        }
}


private fun setError(data: Any, error: String?) {
    if (data is EditText) {
        if (data.parent.parent is TextInputLayout) {
            (data.parent.parent as TextInputLayout).error = null
            (data.parent.parent as TextInputLayout).error = error
            (data.parent.parent as TextInputLayout).isErrorEnabled = error != null
        } else {
            data.error = error
        }
    }
}

fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            afterTextChanged.invoke(s.toString())
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    })
}

fun TextView.setMultiText(context : Context, start : String, end: String,onClickListener : OnTextClickListener,
                          color : Int =  R.color.colorPrimary) {
    val hintButton = "$start $end"
    val spannableTerms = SpannableString(hintButton)
    spannableTerms.setSpan(
            ForegroundColorSpan(
                    ContextCompat.getColor(
                            context,
                            color
                    )
            ),
            start.length + 1, hintButton.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )

    val clickableSpanTerms = object : ClickableSpan() {
        override fun onClick(textView: View) {
            onClickListener.onClick()
        }

        override fun updateDrawState(ds: TextPaint) {
            super.updateDrawState(ds)
            ds.isUnderlineText = true
            ds.color =  ContextCompat.getColor(
                    context,
                    color)
            ds.typeface = Typeface.create(ds.typeface,Typeface.BOLD)
        }
    }
    spannableTerms.setSpan(
            clickableSpanTerms,
            start.length + 1,
            hintButton.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    this.text = spannableTerms
    this.movementMethod = LinkMovementMethod.getInstance()
    this.highlightColor =  ContextCompat.getColor(
            context,
            color
    )
}

interface OnTextClickListener {
    fun onClick()
}

fun Spinner.disableControls() {
    this.background = null
    this.isEnabled = false
}

fun EditText.disableControls() {
    this.isFocusable = false
    this.isFocusableInTouchMode = false
}

