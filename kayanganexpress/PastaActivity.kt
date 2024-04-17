package com.example.kayanganexpress

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class PastaActivity : AppCompatActivity() {

    private val cartItems = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pasta)
    }
}
