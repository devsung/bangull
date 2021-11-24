package com.devsung.bangull.data

data class User(
    val email: String,
    val password: String,
    val salt: String
) {

    fun isEmpty() = email == "" || password == ""
}