package com.example.contacts

import android.os.Bundle
import android.util.Log.d
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStop() {
        super.onStop()
        d("sadasdasdasdas", "activity on onstop")
    }
    override fun onDestroy() {
        super.onDestroy()
        d("sadasdasdasdas", "activity on destroy")
    }
}
