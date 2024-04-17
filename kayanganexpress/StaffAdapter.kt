package com.example.kayanganexpress

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class StaffAdapter(
    private val context: Context,
    private val listener: StaffClickListener
) : ListAdapter<Staff, StaffAdapter.StaffViewHolder>(StaffDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StaffViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.data_staff, parent, false)
        return StaffViewHolder(view)
    }

    override fun onBindViewHolder(holder: StaffViewHolder, position: Int) {
        val currentStaff = getItem(position)
        holder.bind(currentStaff)
    }

    inner class StaffViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.textViewName)
        private val positionTextView: TextView = itemView.findViewById(R.id.textViewPosition)
        private val emailTextView: TextView = itemView.findViewById(R.id.textEmail)
        private val phoneNumberTextView: TextView = itemView.findViewById(R.id.textPhoneNumber)
        private val editButton: Button = itemView.findViewById(R.id.buttonEdit)
        private val deleteButton: Button = itemView.findViewById(R.id.buttonDelete)

        fun bind(staff: Staff) {
            nameTextView.text = staff.name
            positionTextView.text = staff.position
            emailTextView.text = staff.email
            phoneNumberTextView.text = staff.phoneNumber

            // Set click listeners for edit and delete buttons
            editButton.setOnClickListener { listener.onEditClick(staff) }
            deleteButton.setOnClickListener { listener.onDeleteClick(staff) }
        }
    }

    interface StaffClickListener {
        fun onEditClick(staff: Staff)
        fun onDeleteClick(staff: Staff)
    }

    private class StaffDiffCallback : DiffUtil.ItemCallback<Staff>() {
        override fun areItemsTheSame(oldItem: Staff, newItem: Staff): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Staff, newItem: Staff): Boolean {
            return oldItem == newItem
        }
    }
}
