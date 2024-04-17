package com.example.kayanganexpress
data class Reservation(
    val reservationId: String? = null,
    val customerName: String? = null,
    val customerPhoneNo: String? = null,
    val date: String? = null,
    val time: String? = null,
    val noOfPerson: String? = null,
    val seatingType: String? = null,
    val specialRequests: String? = null, // New field for special requests
    val status: String? = null
)
