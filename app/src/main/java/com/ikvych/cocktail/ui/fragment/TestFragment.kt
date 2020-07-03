package com.ikvych.cocktail.ui.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.viewModels
import com.ikvych.cocktail.R
import com.ikvych.cocktail.ui.base.BaseFragment
import com.ikvych.cocktail.ui.base.FRAGMENT_ID
import com.ikvych.cocktail.viewmodel.base.BaseViewModel
import java.util.*

const val ARBITRARY_NUMBER = "com.ikvych.cocktail.ArbitraryNumber"
const val OPTIONAL_STRING = "com.ikvych.cocktail.OptionalString"

class TestFragment() : BaseFragment<BaseViewModel>() {

    override var contentLayoutResId: Int = R.layout.fragment_test
    override val viewModel: BaseViewModel by viewModels()

    lateinit var testTextView: TextView
    private var optionalString: String? = null
    private var arbitraryNumber: Int? = null

    override fun configureView(view: View, savedInstanceState: Bundle?) {
        testTextView = view.findViewById(R.id.tv_test_text)
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

        testTextView.setOnClickListener {
            arbitraryNumber  = arbitraryNumber!!.inc()
            val testFragment1 = newInstance(arbitraryNumber!!)
            val ft1: FragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
            ft1.add(R.id.fcv_container, testFragment1)
            ft1.addToBackStack("transaction1")
            ft1.commit()

            arbitraryNumber  = arbitraryNumber!!.inc()
            val testFragment2 = newInstance(arbitraryNumber!!)
            arbitraryNumber  = arbitraryNumber!!.inc()
            val testFragment3 = newInstance(arbitraryNumber!!)
            val ft2: FragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
            ft2.add(R.id.fcv_container, testFragment2)
            ft2.add(R.id.fcv_container, testFragment3)
            ft2.addToBackStack("transaction2")
            ft2.commit()

            val testFragment4 = newInstance(arbitraryNumber!!.inc())
            val testFragment5 = newInstance(arbitraryNumber!!, "Some text")
            val ft3: FragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
            ft3.add(R.id.fcv_container, testFragment4)
            ft3.add(R.id.fcv_container, testFragment5)
/*            ft3.addToBackStack("transaction3")*/
            ft3.commit()

            if (optionalString != null) {
                val ft4: FragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
                ft4.remove(this)
                ft4.addToBackStack("transaction4")
                ft4.commit()
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(arbitraryNumber: Int, optionalString: String? = null) =
            TestFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARBITRARY_NUMBER, arbitraryNumber)
                    if (optionalString != null) {
                        putString(OPTIONAL_STRING, optionalString)
                    }
                }
            }
    }
}