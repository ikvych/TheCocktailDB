package com.ikvych.cocktail.filter

import android.text.InputFilter
import android.text.Spanned

class TextInputFilter : InputFilter {

    override fun filter(
        source: CharSequence?,
        start: Int,
        end: Int,
        dest: Spanned?,
        dstart: Int,
        dend: Int
    ): CharSequence? {
        if (source != null) {
            var text = source.toString()
            text = text.replace(" ", "")
            text = text.replace("\t", "")
            return text
        }
        return source
    }
}