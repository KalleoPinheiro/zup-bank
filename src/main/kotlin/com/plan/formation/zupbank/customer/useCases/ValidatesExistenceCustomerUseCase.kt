package com.plan.formation.zupbank.customer.useCases

import com.plan.formation.zupbank.customer.CustomerRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ValidatesExistenceCustomerUseCase {
    @Autowired
    lateinit var repository: CustomerRepository

    fun exists(document: String) {
        val existingCustomer = repository.findByDocument((document))

        if (existingCustomer.isPresent) {
            throw RuntimeException ("User already exists!")
        }
    }
}