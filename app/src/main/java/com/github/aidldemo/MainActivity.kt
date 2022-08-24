package com.github.aidldemo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
//import com.github.client.TestActivity1

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        startActivity(Intent(this@MainActivity,TestActivity1::class.java))
    }
}