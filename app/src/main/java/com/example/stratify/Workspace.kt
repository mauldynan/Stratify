package com.example.stratify

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Workspace(
    val id: String,
    val name: String,
    val creatorName: String,
    var status: String = "To Do",
    var department: String = "",
    var details: String = ""
) : Parcelable