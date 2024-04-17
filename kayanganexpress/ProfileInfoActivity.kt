package com.example.kayanganexpress

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ProfileInfoActivity : AppCompatActivity() {
    private lateinit var profileName: TextView
    private lateinit var profileEmail: TextView
    private lateinit var profilePhoneNumber: TextView
    private lateinit var editProfile: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profileinfo)

        profileName = findViewById(R.id.profileName)
        profileEmail = findViewById(R.id.profileEmail)
        profilePhoneNumber = findViewById(R.id.profilePhoneNumber)
        editProfile = findViewById(R.id.editButton)

        // Retrieve customer data from Firebase Realtime Database
        retrieveCustomerData()

                editProfile.setOnClickListener {
                    // Start the profile edit activity
                    val intent = Intent(this, EditProfileActivity::class.java)
                    startActivity(intent)
                }
    }

    private fun retrieveCustomerData() {
        val databaseReference = FirebaseDatabase.getInstance().getReference("Customer")

        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (customerSnapshot in dataSnapshot.children) {
                        val name = customerSnapshot.child("customerUsername").getValue(String::class.java)
                        val email = customerSnapshot.child("customerEmail").getValue(String::class.java)
                        val phoneNumber =customerSnapshot.child("customerPhone").getValue(String::class.java)

                        // Update UI with retrieved data
                        updateUI(name, email, phoneNumber)
                    }
                } else {
                    // Handle case where no data exists
                    profileName.text = "No data available"
                    profileEmail.text = "No data available"
                    profilePhoneNumber.text = "No data available"
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle errors here
            }
        })
    }

    private fun updateUI(name: String?, email: String?, phoneNumber: String?) {
        profileName.text = name
        profileEmail.text = email
        profilePhoneNumber.text = phoneNumber
    }

}
