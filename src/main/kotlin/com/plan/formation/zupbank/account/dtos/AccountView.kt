package com.plan.formation.zupbank.account.dtos

import com.plan.formation.zupbank.customer.CustomerModel

data class AccountView(
    val account_number: String,
    val balance: String,
    var create_date: String,
    var end_date: String,
    var customer_name: String,
    var customer_document: String,
)