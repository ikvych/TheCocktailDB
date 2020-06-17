package com.ikvych.cocktail.filter

import android.R.attr
import android.text.InputFilter
import android.text.SpannableString
import android.text.Spanned
import android.text.TextUtils


class TextInputFilter : InputFilter {

    override fun filter(
        source: CharSequence?,
        start: Int,
        end: Int,
        dest: Spanned?,
        dstart: Int,
        dend: Int
    ): CharSequence? {
        var keepOriginal = true
        val sb = StringBuilder(end - start)
        source!!.forEach { c ->
            if (c != ' ') {
                sb.append(c)
            } else {
                keepOriginal = false
            }
        }

        return if (keepOriginal) {
            null
        } else {
            if (source is Spanned) {
                val sp = SpannableString(sb)
                TextUtils.copySpansFrom(source, start, end, null, sp, 0)
                sp
            } else {
                sb
            }
        }
    }
}