package com.example.stratify

import java.util.Date

data class InformationItem(
    val id: Long = System.currentTimeMillis(),
    var details: String,
    var isSaved: Boolean = false,
    var savedDate: Date? = null,
    var isEditing: Boolean = false,
    var isDeleting: Boolean = false,
    var isSelectedForDeletion: Boolean = false

)