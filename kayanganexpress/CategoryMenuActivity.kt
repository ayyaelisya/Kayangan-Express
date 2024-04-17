package com.example.kayanganexpress

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class CategoryMenuActivity : AppCompatActivity() {

    private lateinit var pasta: Button
    private lateinit var grill: Button
    private lateinit var sidedish: Button
    private lateinit var drink: Button


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_categorymenu)

        pasta = findViewById(R.id.btnPasta)
        grill = findViewById(R.id.btnGrill)
        sidedish = findViewById(R.id.btnSideDish)
        drink = findViewById(R.id.btnDrink)

        pasta.setOnClickListener {
            val i = Intent(this, PastaActivity::class.java)
            startActivity(i)
        }
        grill.setOnClickListener {
            val i = Intent(this, GrillActivity::class.java)
            startActivity(i)
        }
        sidedish.setOnClickListener {
            val i = Intent(this, SideDishActivity::class.java)
            startActivity(i)
        }
        drink.setOnClickListener {
            val i = Intent(this, DrinkActivity::class.java)
            startActivity(i)
        }
    }
}