package com.example.kayanganexpress

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.Calendar

class ReservationActivity : AppCompatActivity() {

    private lateinit var databaseReference: DatabaseReference
    private lateinit var availableTimeSlots: MutableList<String>
    private lateinit var bookedTimeSlots: MutableList<String>
    private lateinit var spinnerAdapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reservation)

        // Initialize Firebase Database
        databaseReference = FirebaseDatabase.getInstance().reference.child("Reservation")

        // Set click listener for submit button
        val submitButton = findViewById<Button>(R.id.btnSubmit)
        submitButton.setOnClickListener { saveReservation() }

        // Set click listener for date EditText
        val dateEditText = findViewById<EditText>(R.id.etDate)
        dateEditText.setOnClickListener { showDatePicker() }

        // Initialize lists
        availableTimeSlots = generateTimeSlots().toMutableList()
        bookedTimeSlots = mutableListOf()

        // Initialize spinner adapter
        spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, availableTimeSlots)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        val spinnerTime = findViewById<Spinner>(R.id.spinnerTime)
        spinnerTime.adapter = spinnerAdapter

        // Fetch booked time slots from Firebase
        fetchBookedTimeSlots()
    }

    private fun fetchBookedTimeSlots() {
        // Query Firebase to get all booked time slots
        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                bookedTimeSlots.clear()
                for (reservationSnapshot in dataSnapshot.children) {
                    val reservation = reservationSnapshot.getValue(Reservation::class.java)
                    reservation?.let {
                        // Add booked time slots to the list
                        bookedTimeSlots.add("${it.date} ${it.time}")
                    }
                }
                // Update UI to reflect booked time slots
                updateSpinner()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e(TAG, "Error fetching booked time slots: ${databaseError.message}")
                // Handle error
            }
        })
    }

    private fun updateSpinner() {
        // Remove booked time slots from availableTimeSlots
        availableTimeSlots.removeAll(bookedTimeSlots)
        // Notify spinnerAdapter of the changes
        spinnerAdapter.notifyDataSetChanged()
    }

    private fun saveReservation() {
        val name = findViewById<EditText>(R.id.eTname).text.toString()
        val phoneNumber = findViewById<EditText>(R.id.eTPhone).text.toString()
        val date = findViewById<EditText>(R.id.etDate).text.toString()
        val time = findViewById<Spinner>(R.id.spinnerTime).selectedItem.toString()
        val noOfPerson = findViewById<EditText>(R.id.etNoperson).text.toString()
        val seatingType = findViewById<RadioButton>(findViewById<RadioGroup>(R.id.radioGroup).checkedRadioButtonId).text.toString()
        val specialRequests = findViewById<EditText>(R.id.etSpecialRequests).text.toString()

        // Check if the selected time slot is available
        if (bookedTimeSlots.contains("$date $time")) {
            // Time slot already booked, show error message
            Toast.makeText(this@ReservationActivity, "This time slot is already booked.", Toast.LENGTH_LONG).show()
            return
        }

        // Time slot available, proceed with reservation
        val reservationId = databaseReference.push().key // Generate a unique key for the reservation
        val reservation = Reservation(reservationId,name, phoneNumber, date, time, noOfPerson, seatingType, specialRequests, "Pending")

        reservationId?.let {
            databaseReference.child(it).setValue(reservation)
                .addOnSuccessListener {
                    // Add the booked time slot to bookedTimeSlots list
                    bookedTimeSlots.add("$date $time")
                    // Update the spinner adapter
                    updateSpinner()

                    Toast.makeText(this@ReservationActivity, "Reservation saved successfully", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener {
                    Log.e(TAG, "Failed to save reservation: ${it.message}")
                    Toast.makeText(this@ReservationActivity, "Failed to save reservation", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { view, selectedYear, selectedMonth, selectedDay ->
                // Set selected date to the EditText
                findViewById<EditText>(R.id.etDate).setText("$selectedDay/${selectedMonth + 1}/$selectedYear")
            },
            year,
            month,
            day
        )
        datePickerDialog.show()
    }

    private fun generateTimeSlots(): List<String> {
        return listOf(
            "5:00 PM",
            "5:30 PM",
            "6:00 PM",
            "6:30 PM",
            "7:00 PM",
            "8:30 PM",
            "9:00 PM",
            "9:30 PM",
            "10:00 PM",
            "10:30 PM",
            "11:00 PM"
        )
    }

    companion object {
        private const val TAG = "ReservationActivity"
    }
}
