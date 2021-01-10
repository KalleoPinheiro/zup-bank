package com.plan.formation.zupbank.customer

import javax.persistence.*
import javax.validation.constraints.*

@Entity
@Table(name="customer")
data class CustomerModel(
    @field:Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @field:NotNull
    @field:Column(length=200)
    var name: String,

    @field:NotNull
    @field:Column(length=11)
    var document: String
)