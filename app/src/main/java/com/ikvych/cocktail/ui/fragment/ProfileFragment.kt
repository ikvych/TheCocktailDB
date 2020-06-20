package com.ikvych.cocktail.ui.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.ikvych.cocktail.R
import com.ikvych.cocktail.ui.activity.AuthActivity
import com.ikvych.cocktail.ui.activity.MainActivity
import com.ikvych.cocktail.ui.base.FRAGMENT_ID
import com.ikvych.cocktail.ui.base.BaseFragment

class ProfileFragment : BaseFragment() {

    private lateinit var logOut: Button
    lateinit var startTestFragmentBtn: Button

    lateinit var listener: ProfileFragmentListener

    interface ProfileFragmentListener {
        fun startTestFragment()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as MainActivity
        } catch (exception: ClassCastException) {
            throw ClassCastException("${activity.toString()} must implement ProfileFragmentListener")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        logOut = requireView().findViewById(R.id.b_log_out)
        logOut.setOnClickListener {
            val intent = Intent(requireContext(), AuthActivity::class.java)
            requireContext().startActivity(intent)
        }
        startTestFragmentBtn = requireView().findViewById(R.id.b_test_fragment)
        startTestFragmentBtn.setOnClickListener {
            listener.startTestFragment()
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