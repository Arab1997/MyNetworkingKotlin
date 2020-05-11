package com.example.myapplication.model

import com.google.gson.annotations.SerializedName
import java.sql.ClientInfoStatus

//data class Member(val firstname: String, val lastName: String)
data class PlayerDatas(
    @SerializedName("status" ) val status: String,
    @SerializedName("message" ) val message: String,
    @SerializedName("data" ) val data: List<Player>
)
