package com.ikvych.cocktail.presentation.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentTransaction
import com.ikvych.cocktail.R
import com.ikvych.cocktail.databinding.FragmentTestBinding
import com.ikvych.cocktail.presentation.fragment.base.BaseFragment
import com.ikvych.cocktail.viewmodel.base.BaseViewModel
import java.util.*
import kotlin.reflect.KClass

const val ARBITRARY_NUMBER = "com.ikvych.cocktail.ArbitraryNumber"
const val OPTIONAL_STRING = "com.ikvych.cocktail.OptionalString"

class TestFragment : BaseFragment<BaseViewModel, FragmentTestBinding>() {

    override var contentLayoutResId: Int = R.layout.fragment_test
    override val viewModelClass: KClass<BaseViewModel>
    get() = BaseViewModel::class

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
            val ft1: FragmentTransaction = parentFragmentManager.beginTransaction()
            ft1.add(R.id.fcv_profile_fragment, testFragment1)
            ft1.addToBackStack("transaction1")
            ft1.commit()

            arbitraryNumber  = arbitraryNumber!!.inc()
            val testFragment2 = newInstance(arbitraryNumber!!)
            arbitraryNumber  = arbitraryNumber!!.inc()
            val testFragment3 = newInstance(arbitraryNumber!!)
            val ft2: FragmentTransaction = parentFragmentManager.beginTransaction()
            ft2.add(R.id.fcv_profile_fragment, testFragment2)
            ft2.add(R.id.fcv_profile_fragment, testFragment3)
            ft2.addToBackStack("transaction2")
            ft2.commit()

            val testFragment4 = newInstance(arbitraryNumber!!.inc())
            val testFragment5 = newInstance(arbitraryNumber!!, "Some text")
            val ft3: FragmentTransaction = parentFragmentManager.beginTransaction()
            ft3.add(R.id.fcv_profile_fragment, testFragment4)
            ft3.add(R.id.fcv_profile_fragment, testFragment5)
/*            ft3.addToBackStack("transaction3")*/
            ft3.commit()

            if (optionalString != null) {
                val ft4: FragmentTransaction = parentFragmentManager.beginTransaction()
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