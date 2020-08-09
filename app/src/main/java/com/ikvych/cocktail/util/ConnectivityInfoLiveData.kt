package com.ikvych.cocktail.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.util.Log
import androidx.core.content.getSystemService
import androidx.lifecycle.LiveData

class ConnectivityInfoLiveData(context: Context) : LiveData<Boolean>() {

    private val connectedNetworks: MutableSet<Network> = mutableSetOf()
    private val connectivityManager = context.getSystemService<ConnectivityManager>()

    private val networkRequestBuilder = NetworkRequest.Builder()
        .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
        .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {

        override fun onAvailable(network: Network?) {
            network ?: return
            connectedNetworks.add(network)
            postValue(true)
        }

        override fun onLost(network: Network?) {
            network ?: return
            connectedNetworks.remove(network)

            if (connectedNetworks.isEmpty()) postValue(false)
        }
    }

    override fun postValue(value: Boolean?) {
        if (this.value == null || this.value != value)
            super.postValue(value)
    }

    override fun setValue(value: Boolean?) {
        if (this.value == null || this.value != value)
            super.setValue(value)
    }

    override fun onActive() {
        value = connectivityManager?.activeNetworkInfo?.isConnected ?: false
        connectivityManager?.registerNetworkCallback(networkRequestBuilder.build(), networkCallback)
            ?: Log.e("ConnectivityInfo", "ConnectivityManager is null!")
    }

    override fun onInactive() {
        connectivityManager?.unregisterNetworkCallback(networkCallback)
    }
}