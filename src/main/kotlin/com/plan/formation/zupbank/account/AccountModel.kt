package com.plan.formation.zupbank.account

import com.plan.formation.zupbank.customer.CustomerModel
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*
import javax.persistence.*
import javax.validation.constraints.*

@Entity
@Table(name="account")
data class AccountModel(
    @field:Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @field:Column(name = "account_number")
    val accountNumber: String? = UUID.randomUUID().toString().substring(0, 8),

    @field:NotNull
    @field:Column(columnDefinition = "NUMERIC(10, 2)")
    var balance: BigDecimal? = BigDecimal.ZERO,

    @field:NotNull
    @field:Column(name="create_date", columnDefinition="TIMESTAMP")
    val createDate: LocalDateTime = LocalDateTime.now(),

    @field:Column(name="end_date", columnDefinition="TIMESTAMP")
    var endDate: LocalDateTime? = null,

    @OneToOne(cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    val customer: CustomerModel
)