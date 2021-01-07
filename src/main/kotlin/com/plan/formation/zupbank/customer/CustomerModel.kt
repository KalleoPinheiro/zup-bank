package com.plan.formation.zupbank.customer

import javax.persistence.*
import javax.validation.constraints.*

@Entity @Table(name="customer") data class CustomerModel(
    @field:NotNull
    @field:Column(length=200)
    val name: String,

    @field:NotNull
    @field:Column(length=11)
    val document: String,

    @field:Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
)