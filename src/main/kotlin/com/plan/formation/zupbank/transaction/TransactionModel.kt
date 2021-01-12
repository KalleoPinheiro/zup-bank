package com.plan.formation.zupbank.transaction

import com.fasterxml.jackson.annotation.JsonIgnore
import com.plan.formation.zupbank.account.AccountModel
import com.plan.formation.zupbank.customer.CustomerModel
import com.plan.formation.zupbank.utils.enums.TransactionType
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*
import javax.persistence.*
import javax.validation.constraints.*

@Entity
@Table(name="transaction")
data class TransactionModel(
    @field:Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @field:NotNull
    @field:Column(name = "transaction_type")
    @Enumerated(EnumType.STRING)
    val transactionType: TransactionType,

    @field:NotNull
    @field:Column(name="transaction_amount", columnDefinition = "NUMERIC(10, 2)")
    var transactionAmount: BigDecimal,

    @field:NotNull
    @field:Column(name="transaction_date", columnDefinition="TIMESTAMP")
    val transactionDate: LocalDateTime = LocalDateTime.now(),

    @ManyToOne(cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    @JsonIgnore
    val customer: CustomerModel,

    @ManyToOne(cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    val account: AccountModel
)