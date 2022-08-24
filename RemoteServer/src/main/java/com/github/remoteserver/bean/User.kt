package com.github.remoteserver.bean

import android.os.Parcel
import android.os.Parcelable

/**
 *  Author : github.
 *  Date   : 2022/8/23
 *  Description :
 */
//@Parcelize
data class User(var name: String?, var age: Int) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeInt(age)
    }

    override fun describeContents(): Int {
        return 0
    }

     fun readFromParcel(parcel: Parcel){
         parcel.readString()
         parcel.readInt()
    }

    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }


}