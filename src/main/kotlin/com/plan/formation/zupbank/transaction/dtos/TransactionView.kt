package com.plan.formation.zupbank.transaction.dtos

data class TransactionView (
    val transaction_type: String,
    val transaction_amount: String,
    val transaction_date: String,
    val customer: String,
    val account_number: String,
    val account_balance: String,
)