package com.example.kayanganexpress

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class EditProfileActivity : AppCompatActivity() {
    private lateinit var editUsername: EditText
    private lateinit var editPhone: EditText
    private lateinit var editEmail: EditText
    private lateinit var saveChangesButton: Button
    private lateinit var usernameUser: String
    private lateinit var phoneUser: String
    private lateinit var emailUser: String
    private lateinit var reference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        reference = FirebaseDatabase.getInstance().getReference("Customer")

        editUsername = findViewById(R.id.editUsername)
        editPhone = findViewById(R.id.editPhone)
        editEmail = findViewById(R.id.editEmail)
        saveChangesButton = findViewById(R.id.saveChanges)

        showData()

        saveChangesButton.setOnClickListener {
            if (isUsernameChanged() || isPhoneChanged() || isEmailChanged()) {
                Toast.makeText(this@EditProfileActivity, "Changes saved", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this@EditProfileActivity, "No changes found", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun isUsernameChanged(): Boolean {
        val newUsername = editUsername.text.toString()
        return if (newUsername != usernameUser) {
            reference.child(usernameUser).child("customerUsername").setValue(newUsername)
            usernameUser = newUsername
            true
        } else {
            false
        }
    }

    private fun isPhoneChanged(): Boolean {
        val newPhone = editPhone.text.toString()
        return if (newPhone != phoneUser) {
            reference.child(usernameUser).child("customerPhone").setValue(newPhone)
            phoneUser = newPhone
            true
        } else {
            false
        }
    }

    private fun isEmailChanged(): Boolean {
        val newEmail = editEmail.text.toString()
        return if (newEmail != emailUser) {
            reference.child(usernameUser).child("customerEmail").setValue(newEmail)
            emailUser = newEmail
            true
        } else {
            false
        }
    }

    private fun showData() {
        val intent = intent
        usernameUser = intent.getStringExtra("customerUsername") ?: ""
        phoneUser = intent.getStringExtra("customerPhone") ?: ""
        emailUser = intent.getStringExtra("customerEmail") ?: ""
        editUsername.setText(usernameUser)
        editPhone.setText(phoneUser)
        editEmail.setText(emailUser)
    }
}
