package com.plan.formation.zupbank.customer

import com.plan.formation.zupbank.customer.dtos.CustomerView
import com.plan.formation.zupbank.customer.useCases.ValidatesWhetherDocumentIsValid
import com.plan.formation.zupbank.customer.useCases.ValidatesWhetherCustomerExistsUseCase
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class CustomerService {
    @Autowired private lateinit var customerRepository: CustomerRepository
    @Autowired private lateinit var validatesWhetherDocumentIsValid: ValidatesWhetherDocumentIsValid
    @Autowired private lateinit var validatesWhetherCustomerExistsUseCase: ValidatesWhetherCustomerExistsUseCase

    fun listUsers(): List<CustomerModel> = customerRepository.findAll()

    fun createUser(customerRequest: CustomerModel): CustomerView {
        validatesWhetherDocumentIsValid.handle(customerRequest.document)
        validatesWhetherCustomerExistsUseCase.handle(customerRequest)

        customerRepository.save(customerRequest)
        return customerRequest.toView()
    }

    fun updateUser(document: String, customerToUpdate: CustomerModel): CustomerView {
        validatesWhetherDocumentIsValid.handle(customerToUpdate.document)
        val customerToSave = customerRepository.findByDocument((document))
            .orElseThrow { RuntimeException ("User not found") }
            .apply {
                this.document = customerToUpdate.document
                this.name = customerToUpdate.name
            }

        customerRepository.save(customerToSave)
        return customerToSave.toView()

    }

    fun deleteUser(document: String) {
        val customerToDelete = customerRepository.findByDocument((document))
            .orElseThrow { RuntimeException ("User not found") }

        return customerRepository.delete(customerToDelete)
    }

    fun findUser(document: String): CustomerView {
        val customerFound = customerRepository.findByDocument((document))
            .orElseThrow { RuntimeException ("User not found") }
        return customerFound.toView()
    }
}

fun CustomerModel.toView() = CustomerView(
    name = "$name",
    document = "$document"
)
