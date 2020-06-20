package com.ikvych.cocktail.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.ikvych.cocktail.R
import com.ikvych.cocktail.ui.activity.AuthActivity
import com.ikvych.cocktail.ui.base.FRAGMENT_ID
import com.ikvych.cocktail.ui.base.BaseFragment

class ProfileFragment : BaseFragment() {

    lateinit var logOut: Button

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        logOut = requireView().findViewById(R.id.b_log_out)
        logOut.setOnClickListener {
            val intent = Intent(requireContext(), AuthActivity::class.java)
            requireContext().startActivity(intent)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(fragmentId: Int) =
            MainFragment().apply {
                arguments = Bundle().apply {
                    putInt(FRAGMENT_ID, fragmentId)
                }
            }
    }
}