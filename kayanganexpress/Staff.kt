package com.example.kayanganexpress

import android.os.Parcel
import android.os.Parcelable

data class Staff(
    var id: String? = null,
    var name: String? = null,
    var position: String? = null,
    var email: String? = null,
    var phoneNumber: String? = null

    ) : Parcelable {
        constructor(parcel: Parcel) : this(
            // Read parcel data and initialize properties
        )

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            // Write properties to parcel
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<Staff> {
            override fun createFromParcel(parcel: Parcel): Staff {
                return Staff(parcel)
            }

            override fun newArray(size: Int): Array<Staff?> {
                return arrayOfNulls(size)
            }
        }
    }


