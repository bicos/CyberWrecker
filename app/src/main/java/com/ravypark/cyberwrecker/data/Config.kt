package com.ravypark.cyberwrecker.data

import com.google.gson.annotations.SerializedName

class Config(
    val id: String,
    val cp: String,
    @SerializedName("cp_name")
    val cpName: String,
    @SerializedName("bbs_name")
    val bbsName: String
)