package com.dosparta.triviagame.common

import java.net.InetAddress

class Utils {
    companion object {
        // todo research some url constructor
        const val TRIVIA_API_URL = "https://opentdb.com/api.php?amount="
        const val INTERNAL_SERVER_ERROR = 500

        fun isInternetAvailable(): Boolean {
            return try {
                val ipAddr: InetAddress = InetAddress.getByName("google.com")
                !ipAddr.equals("")
            } catch (e: Exception) {
                false
            }
        }
    }
}