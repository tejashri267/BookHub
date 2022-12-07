package com.example.bookhub.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.getSystemService

class ConnectionManager {
    fun CheckConnectivity(context: Context):Boolean{
        val conectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE )as ConnectivityManager
        val activeNetwork: NetworkInfo? = conectivityManager.activeNetworkInfo
        if (activeNetwork?.isConnected!= null )
            return activeNetwork.isConnected
        else{
            return false
        }
    }
}