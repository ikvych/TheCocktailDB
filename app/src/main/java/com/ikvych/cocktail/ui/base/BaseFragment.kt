package com.ikvych.cocktail.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

const val FRAGMENT_ID = "com.ikvych.cocktail.ViewId"
const val ALCOHOL_FILTER_KEY = "com.ikvych.cocktail.AlcoholFilterKey"
const val ALCOHOL_FILTER_BUNDLE_KEY = "com.ikvych.cocktail.AlcoholFilterBundleKey"

abstract class BaseFragment() : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(requireArguments().getInt(FRAGMENT_ID), container, false)
    }

    protected open fun configureView(savedInstanceState: Bundle?) {
        // stub
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        configureView(savedInstanceState)
    }

}