package com.audhil.medium.xexpandablelist

import android.os.Parcel
import android.os.Parcelable

data class Comment(
    val commentName: String,
    val replies: MutableList<Reply>
) : Parcelable {
    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Comment> = object : Parcelable.Creator<Comment> {
            override fun createFromParcel(source: Parcel): Comment = Comment(source)
            override fun newArray(size: Int): Array<Comment?> = arrayOfNulls(size)
        }
    }

    constructor(source: Parcel) : this(
        source.readString(),
        source.createTypedArrayList(Reply.CREATOR)
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(commentName)
        writeTypedList(replies)
    }
}

data class Reply(
    val replyName: String? = null
) : Parcelable {
    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Reply> = object : Parcelable.Creator<Reply> {
            override fun createFromParcel(source: Parcel): Reply = Reply(source)
            override fun newArray(size: Int): Array<Reply?> = arrayOfNulls(size)
        }
    }

    constructor(source: Parcel) : this(
        source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(replyName)
    }
}