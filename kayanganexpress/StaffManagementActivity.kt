package com.example.kayanganexpress

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class StaffManagementActivity : AppCompatActivity(), StaffAdapter.StaffClickListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var database: DatabaseReference
    private lateinit var staffAdapter: StaffAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_staffmanagement)

        database = FirebaseDatabase.getInstance().reference.child("Staff")
        recyclerView = findViewById(R.id.recyclerViewStaff)

        recyclerView.layoutManager = LinearLayoutManager(this)
        staffAdapter = StaffAdapter(this, this)
        recyclerView.adapter = staffAdapter

        loadStaffData()

        val addButton: Button = findViewById(R.id.buttonAdd)
        addButton.setOnClickListener {
            // Launch the activity to add a new staff member
            startActivity(Intent(this, AddStaffActivity::class.java))
        }
    }

    private fun loadStaffData() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val staffList = mutableListOf<Staff>()
                for (staffSnapshot in snapshot.children) {
                    val staff = staffSnapshot.getValue(Staff::class.java)
                    staff?.let {
                        staffList.add(it)
                    }
                }
                staffAdapter.submitList(staffList)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@StaffManagementActivity, "Failed to load staff data: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onEditClick(staff: Staff) {
        val intent = Intent(this, AddStaffActivity::class.java)
        intent.putExtra("staff", staff)
        startActivity(intent)
    }

    override fun onDeleteClick(staff: Staff) {
        val staffId = staff.id
        if (staffId != null) {
            val staffRef = database.child(staffId)
            staffRef.removeValue()
                .addOnSuccessListener {
                    Toast.makeText(this, "Staff member deleted successfully", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Failed to delete staff member: ${it.message}", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(this, "Invalid staff member", Toast.LENGTH_SHORT).show()
        }
    }
}
