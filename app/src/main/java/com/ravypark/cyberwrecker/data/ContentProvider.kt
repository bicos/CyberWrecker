package com.ravypark.cyberwrecker.data

import com.google.gson.annotations.SerializedName

data class ContentProvider(
    val id: String,
    val cp: String,
    @SerializedName("cp_name")
    val cpName: String
)