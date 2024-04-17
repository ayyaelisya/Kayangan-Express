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

class ProfileActivity : AppCompatActivity() {

    private lateinit var profile: Button
    private lateinit var termspolicy: Button
    private lateinit var usermanual: Button
    private lateinit var viewOrder: Button
    private lateinit var logout: Button
    private lateinit var textViewName1: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        profile = findViewById(R.id.editProfile)
        termspolicy = findViewById(R.id.termsandpolicy)
        usermanual = findViewById(R.id.UserManual)
        viewOrder = findViewById(R.id.button2)
        logout = findViewById(R.id.btnLogout)
        textViewName1 = findViewById(R.id.textViewName1)

        retrieveCustomerData()



        profile.setOnClickListener {
            // Start the profile edit activity
            val intent = Intent(this, ProfileInfoActivity::class.java)
            startActivity(intent)
        }
        viewOrder.setOnClickListener {
            // Start the profile edit activity
            val intent = Intent(this, ViewOrderActivity::class.java)
            startActivity(intent)
        }

        termspolicy.setOnClickListener {
            // Start the terms and policy activity
            val intent = Intent(this, TermsPolicyActivity::class.java)
            startActivity(intent)
        }

        usermanual.setOnClickListener {
            // Start the user manual activity
            val intent = Intent(this, UserManualActivity::class.java)
            startActivity(intent)
        }
        logout.setOnClickListener {
            // Start the user manual activity
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
    private fun retrieveCustomerData() {
        val databaseReference = FirebaseDatabase.getInstance().getReference("Customer")

        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (customerSnapshot in dataSnapshot.children) {
                        val name = customerSnapshot.child("cutomerUsername")
                            .getValue(String::class.java)

                        // Update UI with retrieved data
                        updateUI(name)
                    }
                } else {
                    // Handle case where no data exists
                    textViewName1.text = "No data available"
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle errors here
            }
        })
    }
    private fun updateUI(name: String?) {
        textViewName1.text = name
    }

}
