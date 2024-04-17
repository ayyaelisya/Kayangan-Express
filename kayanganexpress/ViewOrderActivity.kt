package com.example.kayanganexpress

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ViewOrderActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var recyclerView: RecyclerView
    private lateinit var orderAdapter: OrderAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_order)

        // Initialize Firebase Authentication
        auth = FirebaseAuth.getInstance()

        // Initialize Firebase Database reference
        database = FirebaseDatabase.getInstance().reference.child("Reservation")

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recyclerViewReservations)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Initialize OrderAdapter with database reference
        orderAdapter = OrderAdapter(this, database)

        // Set the adapter to RecyclerView
        recyclerView.adapter = orderAdapter
    }

    override fun onStart() {
        super.onStart()
        orderAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        orderAdapter.stopListening()
    }
}
