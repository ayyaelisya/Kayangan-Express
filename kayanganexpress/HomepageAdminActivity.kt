package com.example.kayanganexpress

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class HomepageAdminActivity : AppCompatActivity() {

    private lateinit var btnViewOrders : Button
    private lateinit var btnlogout : ImageView
    private lateinit var ManageStaff : Button
    private lateinit var Reservation : Button

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homepageadmin)

       btnViewOrders = findViewById(R.id.btnViewOrders)
        Reservation = findViewById(R.id.btnReservation)
       btnlogout = findViewById(R.id.btnLogout)
        ManageStaff= findViewById(R.id.btnManageStaff)

        btnViewOrders.setOnClickListener {
            val i = Intent(this, ReservationManagementActivity::class.java)
            startActivity(i)
        }
        Reservation.setOnClickListener {
            val i = Intent(this, AdminActivity2::class.java)
            startActivity(i)
        }

        btnlogout.setOnClickListener {
            val i = Intent(this, LoginActivity::class.java)
            startActivity(i)
        }

        ManageStaff.setOnClickListener {
            val i = Intent(this, StaffManagementActivity::class.java)
            startActivity(i)
        }
    }
}
