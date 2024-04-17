package com.example.kayanganexpress

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ReservationManagementActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var recyclerView: RecyclerView
    private lateinit var reservationAdapter: ReservationAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reservationmanagement)

        // Initialize Firebase Database reference
        database = FirebaseDatabase.getInstance().reference.child("Reservation")

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recyclerViewReservations)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Initialize ReservationAdapter with click listeners
        reservationAdapter = ReservationAdapter(this, mutableListOf(),
            confirmClickListener = { reservation ->
                reservation.reservationId?.let { reservationId ->
                    val reservationRef = database.child(reservationId)
                    reservationRef.child("status").setValue("Confirmed")
                        .addOnSuccessListener {
                            // Reservation confirmed successfully
                            Toast.makeText(this, "Reservation confirmed for ${reservation.customerName}", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener { error ->
                            // Failed to confirm reservation
                            Toast.makeText(this, "Failed to confirm reservation: ${error.message}", Toast.LENGTH_SHORT).show()
                        }
                }
            },
            deleteClickListener = { reservation ->
                // Handle delete button click
                // For example, delete the reservation from the database
                reservation.reservationId?.let { reservationId ->
                    database.child(reservationId).removeValue()
                        .addOnSuccessListener {
                            // Reservation deleted successfully
                            Toast.makeText(this, "Reservation deleted successfully", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener { error ->
                            // Failed to delete reservation
                            Toast.makeText(this, "Failed to delete reservation: ${error.message}", Toast.LENGTH_SHORT).show()
                        }
                }
            }
        )
        recyclerView.adapter = reservationAdapter

        // Load reservations from the database
        loadReservations()
    }

    private fun loadReservations() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val reservations = mutableListOf<Reservation>()
                for (reservationSnapshot in snapshot.children) {
                    val reservationId = reservationSnapshot.child("reservationId").getValue(String::class.java)
                    val customerName = reservationSnapshot.child("customerName").getValue(String::class.java)
                    val customerPhoneNo = reservationSnapshot.child("customerPhoneNo").getValue(String::class.java)
                    val date = reservationSnapshot.child("date").getValue(String::class.java)
                    val time = reservationSnapshot.child("time").getValue(String::class.java)
                    val noofperson = reservationSnapshot.child("noOfPerson").getValue(String::class.java)
                    val seatingType = reservationSnapshot.child("seatingType").getValue(String::class.java)
                    val specialRequest = reservationSnapshot.child("specialRequests").getValue(String::class.java)
                    val status = reservationSnapshot.child("status").getValue(String::class.java)

                    val reservation = Reservation( reservationId,customerName, customerPhoneNo, date, time, noofperson, seatingType,specialRequest, status)
                    reservations.add(reservation)
                }
                // Notify the adapter with the new data
                reservationAdapter.updateData(reservations)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle errors
                Log.e("AdminActivity3", "Database operation canceled: ${error.message}")
                Toast.makeText(this@ReservationManagementActivity, "Database operation canceled: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
