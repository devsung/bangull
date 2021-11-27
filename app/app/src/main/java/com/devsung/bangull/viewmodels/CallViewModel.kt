package com.devsung.bangull.viewmodels

import androidx.lifecycle.ViewModel
import com.devsung.bangull.data.Customer

class CallViewModel(customer: Customer) : ViewModel() {

    val name = customer.name
    val order = customer.order
    val address = customer.address
}