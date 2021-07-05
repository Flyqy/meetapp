package com.example.meetapp.network

class Response<out T>(
    val status: String?,
    val code: Int,
    val data: T
)