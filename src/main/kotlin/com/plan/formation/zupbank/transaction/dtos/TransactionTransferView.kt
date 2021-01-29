package com.plan.formation.zupbank.transaction.dtos

data class TransactionTransferView (
    val transaction_type: String,
    val transaction_amount: String,
    val transaction_date: String,
    val customer_origin: String,
    val customer_destiny: String,
    val account_number_origin: String,
    val account_number_destiny: String,
    val checking_copy: String
)