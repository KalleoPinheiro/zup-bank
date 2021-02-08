package com.plan.formation.zupbank.customer

import com.plan.formation.zupbank.customer.dtos.CustomerView
import com.plan.formation.zupbank.customer.useCases.ValidatesExistenceCustomerUseCase
import com.plan.formation.zupbank.customer.useCases.ValidatesWhetherDocumentIsValidUseCase
import org.springframework.stereotype.Service

@Service
class CustomerService(
    private var customerRepository: CustomerRepository,
    private var validatesWhetherDocumentIsValidUseCase: ValidatesWhetherDocumentIsValidUseCase,
    private var validatesExistenceCustomerUseCase: ValidatesExistenceCustomerUseCase
) {

    fun listCustomers(): List<CustomerView> = customerRepository.findAll().map { customer -> customer.toView() }

    fun createCustomer(customerRequest: CustomerModel): CustomerView {
        validatesWhetherDocumentIsValidUseCase.handle(customerRequest.document)
        validatesExistenceCustomerUseCase.exists(customerRequest.document)

        customerRepository.save(customerRequest)
        return customerRequest.toView()
    }

    fun updateCustomer(document: String, customerToUpdate: CustomerModel): CustomerView {
        validatesWhetherDocumentIsValidUseCase.handle(customerToUpdate.document)
        val customerToSave = customerRepository.findByDocument(document)
            .orElseThrow { RuntimeException("Customer not found") }
            .apply {
                this.name = customerToUpdate.name
                this.document = customerToUpdate.document
            }

        customerRepository.save(customerToSave)
        return customerToSave.toView()

    }

    fun deleteCustomer(document: String) {
        val customerToDelete = customerRepository.findByDocument(document)
            .orElseThrow { RuntimeException("Customer not found") }

        return customerRepository.delete(customerToDelete)
    }

    fun findCustomer(document: String): CustomerView {
        val customerFound = customerRepository.findByDocument(document)
            .orElseThrow { RuntimeException("Customer not found") }

        return customerFound.toView()
    }
}

fun CustomerModel.toView() = CustomerView(
    name = "$name",
    document = "$document"
)
