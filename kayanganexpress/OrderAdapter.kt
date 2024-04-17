package com.example.kayanganexpress

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener

class OrderAdapter(
    private val context: Context,
    private val databaseReference: DatabaseReference // Add this parameter
) : RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

    private var reservationList: MutableList<Reservation> = mutableListOf()
    private lateinit var valueEventListener: ValueEventListener

    // Listener for real-time updates
    private val childEventListener = object : ChildEventListener {
        override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
            val reservation = snapshot.getValue(Reservation::class.java)
            reservation?.let {
                reservationList.add(it)
                notifyDataSetChanged()
            }
        }

        override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            val updatedReservation = snapshot.getValue(Reservation::class.java)
            updatedReservation?.let {
                val index = reservationList.indexOfFirst { reservation -> reservation.reservationId == it.reservationId }
                if (index != -1) {
                    reservationList[index] = it
                    notifyItemChanged(index)
                }
            }
        }

        override fun onChildRemoved(snapshot: DataSnapshot) {
            val removedReservation = snapshot.getValue(Reservation::class.java)
            removedReservation?.let {
                val index = reservationList.indexOfFirst { reservation -> reservation.reservationId == it.reservationId }
                if (index != -1) {
                    reservationList.removeAt(index)
                    notifyItemRemoved(index)
                }
            }
        }

        override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            // Not implemented for this example
        }

        override fun onCancelled(error: DatabaseError) {
            // Handle errors
        }
    }

    init {
        // Start listening for changes when the adapter is initialized
        databaseReference.addChildEventListener(childEventListener)
        setupValueEventListener()
    }

    private fun setupValueEventListener() {
        valueEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val newList = mutableListOf<Reservation>()
                for (reservationSnapshot in snapshot.children) {
                    val reservation = reservationSnapshot.getValue(Reservation::class.java)
                    reservation?.let {
                        newList.add(it)
                    }
                }
                updateData(newList)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle errors
            }
        }
    }

    fun startListening() {
        databaseReference.addValueEventListener(valueEventListener)
    }

    fun stopListening() {
        databaseReference.removeEventListener(valueEventListener)
    }

    inner class OrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.viewName)
        private val phoneTextView: TextView = itemView.findViewById(R.id.viewPhoneNumber)
        private val dateTextView: TextView = itemView.findViewById(R.id.viewDate)
        private val timeTextView: TextView = itemView.findViewById(R.id.viewTime)
        private val personTextView: TextView = itemView.findViewById(R.id.viewNoofPerson)
        private val seatingTypeTextView: TextView = itemView.findViewById(R.id.viewSeatingType)
        private val statusTextView: TextView = itemView.findViewById(R.id.viewStatus)

        fun bind(reservation: Reservation) {
            nameTextView.text = "Name: ${reservation.customerName}"
            phoneTextView.text = "Phone: ${reservation.customerPhoneNo}"
            dateTextView.text = "Date: ${reservation.date}"
            timeTextView.text = "Time: ${reservation.time}"
            personTextView.text = "Persons: ${reservation.noOfPerson}"
            seatingTypeTextView.text = "Seating Type: ${reservation.seatingType}"
            statusTextView.text = "Status: ${reservation.status}"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.order_item, parent, false)
        return OrderViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val reservation = reservationList[position]
        holder.bind(reservation)
    }

    override fun getItemCount(): Int {
        return reservationList.size
    }

    // Function to update data externally if needed
    fun updateData(newList: List<Reservation>) {
        reservationList.clear()
        reservationList.addAll(newList)
        notifyDataSetChanged()
    }
}
