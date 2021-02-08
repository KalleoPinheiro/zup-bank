package com.plan.formation.zupbank.transaction.dtos

import java.math.BigDecimal

data class TransactionTransferRequestView(val transaction_amount: BigDecimal, val account: String)