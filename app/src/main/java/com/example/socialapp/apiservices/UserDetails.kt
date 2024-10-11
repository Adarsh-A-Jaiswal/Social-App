package com.example.socialapp.apiservices

import java.io.Serializable

class UserDetails(
    val id: Int,
    val name: String,
    val email: String,
    val gender: String,
    val status: String
):Serializable
