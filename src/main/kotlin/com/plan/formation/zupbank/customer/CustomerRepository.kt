package com.plan.formation.zupbank.customer

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface CustomerRepository : CrudRepository<CustomerModel, Long> {
    fun findByDocument(document: String): Optional<CustomerModel>
}