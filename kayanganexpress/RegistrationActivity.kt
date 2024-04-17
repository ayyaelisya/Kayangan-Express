package com.example.kayanganexpress

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegistrationActivity : AppCompatActivity() {

    //initialize the component
    private lateinit var btnRegister: Button
    private lateinit var username: EditText
    private lateinit var phoneNum: EditText
    private lateinit var email: EditText
    private lateinit var password: EditText

    //declare the firebase
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference : DatabaseReference

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        btnRegister = findViewById(R.id.btnRegister)
        username = findViewById(R.id.eTUsername)
        password = findViewById(R.id.eTPassword)
        phoneNum = findViewById(R.id.eTpassword)
        email = findViewById(R.id.eTemail)


        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference.child("Customer")

        //function button register to activity MainActivity4
        btnRegister.setOnClickListener{

            saveData(username.text.toString(), password.text.toString(),
                phoneNum.text.toString(),email.text.toString()
            )
        }


    }

    private fun saveData(u:String, p:String, n:String, e:String)
    {
        val dbRef = FirebaseDatabase.getInstance().getReference("Customer")

        val customerId = dbRef.push().key!!

        val em = Model(u,customerId, p , n ,e)

        dbRef.child(customerId).setValue(em)

            .addOnCompleteListener{
                Toast.makeText(this,"Success", Toast.LENGTH_LONG).show()

            }.addOnFailureListener {
                Toast.makeText(this,"Failure",Toast.LENGTH_LONG).show()
            }

        val i = Intent(this, LoginActivity::class.java)
        startActivity(i)
    }
}