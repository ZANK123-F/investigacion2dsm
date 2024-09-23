package com.example.investigacion

import com.google.gson.annotations.SerializedName

data class PostModel (
    @SerializedName("userId")
    var usuarioId: Int,
    @SerializedName("id")
    var Id: Int,
    @SerializedName("title")
    var title: String,
    @SerializedName("body")
    var body: String
)
