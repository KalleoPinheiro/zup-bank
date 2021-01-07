package com.plan.formation.zupbank.customer

import com.plan.formation.zupbank.customer.useCases.ValidateRequestBodyUseCase
import com.plan.formation.zupbank.customer.useCases.ValidatesIfCustomerExistsUseCase
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class CustomerService {
    @Autowired private lateinit var customerRepository: CustomerRepository
    @Autowired private lateinit var validateRequestBodyUseCase: ValidateRequestBodyUseCase
    @Autowired private lateinit var validatesIfCustomerExistsUseCase: ValidatesIfCustomerExistsUseCase

    fun listUsers(): MutableIterable<CustomerModel> = customerRepository.findAll()

    fun createUser(customerRequest: CustomerModel): CustomerModel {
        validateRequestBodyUseCase.handle(customerRequest)
        validatesIfCustomerExistsUseCase.handle(customerRequest)

        return customerRepository.save(customerRequest)
    }
}