package com.example.kayanganexpress

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AddStaffActivity : AppCompatActivity() {

    private lateinit var editTextName: EditText
    private lateinit var editTextPosition: EditText
    private lateinit var editTextEmail: EditText
    private lateinit var editTextPhoneNumber: EditText
    private lateinit var buttonAdd: Button
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addstaff)

        // Initialize Firebase database reference
        database = FirebaseDatabase.getInstance().reference.child("Staff")

        // Get references to UI views
        editTextName = findViewById(R.id.editTextName)
        editTextPosition = findViewById(R.id.editTextPosition)
        editTextEmail = findViewById(R.id.editTextEmail)
        editTextPhoneNumber = findViewById(R.id.editTextPhoneNumber)
        buttonAdd = findViewById(R.id.buttonAdd)

        // Set OnClickListener for the Add button
        buttonAdd.setOnClickListener {
            addStaff()
        }
    }

    private fun addStaff() {
        // Retrieve entered text from EditText fields
        val name = editTextName.text.toString().trim()
        val position = editTextPosition.text.toString().trim()
        val email = editTextEmail.text.toString().trim()
        val phoneNumber = editTextPhoneNumber.text.toString().trim()

        // Validate input fields
        if (name.isEmpty() || position.isEmpty() || email.isEmpty() || phoneNumber.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        // Create a new Staff object
        val newStaff = Staff(name = name, position = position, email = email, phoneNumber = phoneNumber)

        // Push the new staff object to the database
        val staffId = database.push().key
        staffId?.let {
            database.child(it).setValue(newStaff)
                .addOnSuccessListener {
                    Toast.makeText(this, "Staff added successfully", Toast.LENGTH_SHORT).show()
                    // Clear EditText fields after adding staff
                    editTextName.text.clear()
                    editTextPosition.text.clear()
                    editTextEmail.text.clear()
                    editTextPhoneNumber.text.clear()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Failed to add staff: ${it.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }
}
