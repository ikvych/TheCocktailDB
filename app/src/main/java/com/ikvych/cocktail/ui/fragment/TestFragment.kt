package com.ikvych.cocktail.ui.fragment

import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.ikvych.cocktail.R
import com.ikvych.cocktail.ui.base.BaseFragment
import com.ikvych.cocktail.ui.base.FRAGMENT_ID
import java.util.*

const val ARBITRARY_NUMBER = "com.ikvych.cocktail.ArbitraryNumber"
const val OPTIONAL_STRING = "com.ikvych.cocktail.OptionalString"

class TestFragment : BaseFragment() {

    lateinit var testTextView: TextView
    var optionalString: String? = null
    var arbitraryNumber: Int? = null

    override fun configureView(savedInstanceState: Bundle?) {
        testTextView = requireView().findViewById(R.id.tv_test)
        arbitraryNumber = requireArguments().getInt(ARBITRARY_NUMBER)

        if (requireArguments().containsKey(OPTIONAL_STRING)) {
            optionalString = requireArguments().getString(OPTIONAL_STRING)
            val finalString = "$arbitraryNumber $optionalString"
            testTextView.text = finalString
            requireView().setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.color_primary))
        } else {
            val rnd = Random()
            val color: Int = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
            testTextView.text = "$arbitraryNumber"
            requireView().setBackgroundColor(color)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(fragmentId: Int, arbitraryNumber: Int, optionalString: String? = null) =
            TestFragment().apply {
                arguments = Bundle().apply {
                    putInt(FRAGMENT_ID, fragmentId)
                    putInt(ARBITRARY_NUMBER, arbitraryNumber)
                    if (optionalString != null) {
                        putString(OPTIONAL_STRING, optionalString)
                    }
                }
            }
    }
}