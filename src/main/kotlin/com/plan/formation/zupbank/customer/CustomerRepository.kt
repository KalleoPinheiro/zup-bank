package com.plan.formation.zupbank.customer

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface CustomerRepository : JpaRepository<CustomerModel, Long> {
    fun findByDocument(document: String): Optional<CustomerModel>
}