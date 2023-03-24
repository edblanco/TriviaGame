package com.dosparta.triviagame.common

import android.content.Context
import android.content.res.AssetManager
import java.io.IOException
import java.io.InputStream
import java.net.InetAddress
import java.util.*


class Utils {
    companion object {
        fun isInternetAvailable(): Boolean {
            return try {
                val ipAddr: InetAddress = InetAddress.getByName("google.com")
                !ipAddr.equals("")
            } catch (e: Exception) {
                false
            }
        }

        @Throws(IOException::class)
        fun getProperty(fileName: String, context: Context): Properties {
            val properties = Properties()
            val assetManager: AssetManager = context.assets
            val inputStream: InputStream = assetManager.open(fileName)
            properties.load(inputStream)
            return properties
        }
    }
}