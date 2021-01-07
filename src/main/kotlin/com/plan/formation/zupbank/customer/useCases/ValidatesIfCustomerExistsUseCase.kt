package com.plan.formation.zupbank.customer.useCases

import com.plan.formation.zupbank.customer.CustomerModel
import com.plan.formation.zupbank.customer.CustomerRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ValidatesIfCustomerExistsUseCase() {

    @Autowired lateinit var repository: CustomerRepository

    fun handle(customerRequest: CustomerModel) {
        val existingCustomer = repository.findByDocument((customerRequest.document))

        if (existingCustomer.isPresent) {
            throw RuntimeException ("User ${customerRequest.name} already exists!")
        }
    }
}