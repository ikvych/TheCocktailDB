package com.ikvych.cocktail.map

import com.ikvych.cocktail.map.databinding.FragmentMyMapFragmentBinding
import com.ikvych.cocktail.prresentation.ui.base.fragment.BaseFragment
import com.ikvych.cocktail.prresentation.ui.base.viewmodel.BaseViewModel
import kotlin.reflect.KClass

class MyMapFragmentFragment : BaseFragment<BaseViewModel, FragmentMyMapFragmentBinding>()
   /* OnMapReadyCallback*/ {

    override var contentLayoutResId: Int
        get() = R.layout.fragment_my_map_fragment
        set(value) {}
    override val viewModelClass: KClass<BaseViewModel>
        get() = BaseViewModel::class

/*
    override fun configureView(view: View, savedInstanceState: Bundle?) {
        super.configureView(view, savedInstanceState)
        mv_map?.onCreate(savedInstanceState)
        mv_map.getMapAsync(this)
        */
/*(this as SupportMapFragment)
        getMapAsync(this)*//*

    }

    override fun onStart() {
        super.onStart()
        mv_map?.onStart()
    }

    override fun onResume() {
        super.onResume()
        mv_map?.onResume()
    }

    override fun onPause() {
        mv_map?.onPause()
        super.onPause()
    }

    override fun onStop() {
        mv_map?.onStop()
        super.onStop()
    }

    override fun onDestroy() {
        mv_map?.onDestroy()
        super.onDestroy()
    }

    override fun onLowMemory() {
        mv_map?.onLowMemory()
        super.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mv_map?.onSaveInstanceState(outState)
    }

    companion object {
        @JvmStatic
        fun newInstance() = MyMapFragmentFragment()
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        if (googleMap == null) return
        googleMap.mapType = GoogleMap.MAP_TYPE_NORMAL
        googleMap.uiSettings.apply {

        }

        googleMap.isBuildingsEnabled = true
    }
*/

}