package com.example.kayanganexpress

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ReservationAdapter(
    private val context: Context,
    private var reservationList: List<Reservation>,
    private val confirmClickListener: (Reservation) -> Unit,
    private val deleteClickListener: (Reservation) -> Unit
) : RecyclerView.Adapter<ReservationAdapter.ReservationViewHolder>() {

    inner class ReservationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.viewName)
        private val phoneTextView: TextView = itemView.findViewById(R.id.viewPhoneNumber)
        private val dateTextView: TextView = itemView.findViewById(R.id.viewDate)
        private val timeTextView: TextView = itemView.findViewById(R.id.viewTime)
        private val personTextView: TextView = itemView.findViewById(R.id.viewNoofPerson)
        private val seatingTypeTextView: TextView = itemView.findViewById(R.id.viewSeatingType)
        private val specialRequestTextView: TextView = itemView.findViewById(R.id.viewSpecialRequest)
        private val confirmButton: Button = itemView.findViewById(R.id.buttonConfirm)
        private val deleteButton: Button = itemView.findViewById(R.id.buttonDelete)

        fun bind(reservation: Reservation) {
            nameTextView.text = "Name: ${reservation.customerName}"
            phoneTextView.text = "Phone: ${reservation.customerPhoneNo}"
            dateTextView.text = "Date: ${reservation.date}"
            timeTextView.text = "Time: ${reservation.time}"
            personTextView.text = "Persons: ${reservation.noOfPerson}"
            seatingTypeTextView.text = "Seating Type: ${reservation.seatingType}"
            specialRequestTextView.text = "Special Request: ${reservation.specialRequests ?: "None"}"

            // Set click listeners for confirm and delete buttons
            confirmButton.setOnClickListener { confirmClickListener(reservation) }
            deleteButton.setOnClickListener { deleteClickListener(reservation) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReservationViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.data_reservation, parent, false)
        return ReservationViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReservationViewHolder, position: Int) {
        val reservation = reservationList[position]
        holder.bind(reservation)
    }

    override fun getItemCount(): Int {
        return reservationList.size
    }

    // Function to update data and notify adapter
    fun updateData(newList: List<Reservation>) {
        reservationList = newList
        notifyDataSetChanged()
    }
}
