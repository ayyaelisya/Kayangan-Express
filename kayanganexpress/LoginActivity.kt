package com.example.kayanganexpress



import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class LoginActivity : AppCompatActivity() {

    //initialize the component
    private lateinit var btnsub: Button
    private lateinit var username: EditText
    private lateinit var password: EditText
    private lateinit var register: TextView

    //declare the firebase
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference : DatabaseReference


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btnsub= findViewById(R.id.btnRegister)
        username = findViewById(R.id.eTUsername)
        password = findViewById(R.id.eTpassword)
        register = findViewById(R.id.textRegister)

        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference.child("Customer")

        register.setOnClickListener{
            val i = Intent(this, RegistrationActivity::class.java)
            startActivity(i)
        }


        btnsub.setOnClickListener{
            val usernameText = username.text.toString()
            val passwordText = password.text.toString()

            if (usernameText.isNotEmpty() && passwordText.isNotEmpty()) {
                login(usernameText, passwordText)
            } else {
                Toast.makeText(this@LoginActivity, "All fields are mandatory", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun login(username: String, password: String) {
        if (username == "admin" && password == "1234") {
            Toast.makeText(this@LoginActivity, "Admin login Successful", Toast.LENGTH_LONG).show()
            startActivity(Intent(this@LoginActivity, HomepageAdminActivity::class.java))
            finish()
            return
        }

        databaseReference.orderByChild("customerUsername").equalTo(username)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        val model = dataSnapshot.children.first().getValue(Model::class.java)

                        if (model != null && model.customerPassword == password) {
                            // Pass the customer ID to MainActivity3
                            val intent = Intent(this@LoginActivity, HomePageActivity::class.java)
                            intent.putExtra("customerId", model.customerId)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this@LoginActivity, "Login Failed", Toast.LENGTH_LONG).show()
                        }
                    } else {
                        Toast.makeText(this@LoginActivity, "Login Failed", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Toast.makeText(this@LoginActivity, "Database Error: ${databaseError.message}", Toast.LENGTH_LONG).show()
                }
            })
    }

}

