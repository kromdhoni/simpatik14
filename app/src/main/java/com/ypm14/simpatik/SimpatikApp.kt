package com.ypm14.simpatik

import android.app.Application
import android.os.Handler
import android.os.Looper
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout

class SimpatikApp : Application() {
    override fun onCreate() {
        super.onCreate()
        // Firebase init di background thread agar tidak block main thread (ANR)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                withTimeout(5000) {
                    com.google.firebase.FirebaseApp.initializeApp(this@SimpatikApp)
                }
                Log.i("SimpatikApp", "Firebase init sukses")
            } catch (e: Exception) {
                Log.w("SimpatikApp", "Firebase init dilewati: ${e.message}")
            }
        }
    }
}
