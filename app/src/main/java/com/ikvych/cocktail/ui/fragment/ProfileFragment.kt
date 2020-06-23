package com.ikvych.cocktail.ui.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.FragmentTransaction
import com.ikvych.cocktail.R
import com.ikvych.cocktail.ui.activity.AuthActivity
import com.ikvych.cocktail.ui.activity.MainActivity
import com.ikvych.cocktail.ui.base.FRAGMENT_ID
import com.ikvych.cocktail.ui.base.BaseFragment

class ProfileFragment : BaseFragment() {

    private lateinit var logOut: Button
    lateinit var startTestFragmentBtn: Button
    lateinit var testFragment: TestFragment

    override fun configureView(view: View, savedInstanceState: Bundle?) {
        super.configureView(view, savedInstanceState)
        logOut = view.findViewById(R.id.b_log_out)
        logOut.setOnClickListener {
            val intent = Intent(requireContext(), AuthActivity::class.java)
            requireContext().startActivity(intent)
        }
        startTestFragmentBtn = view.findViewById(R.id.b_test_fragment)
        startTestFragmentBtn.setOnClickListener {
            testFragment = TestFragment.newInstance(R.layout.fragment_test, 5, "Ivan Kvych")
            val fragmentTransaction: FragmentTransaction = parentFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.fcv_main, testFragment)
            fragmentTransaction.addToBackStack(TestFragment::class.java.name)
            fragmentTransaction.commit()
        }

    }

    companion object {
        @JvmStatic
        fun newInstance(fragmentId: Int) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                    putInt(FRAGMENT_ID, fragmentId)
                }
            }
    }
}