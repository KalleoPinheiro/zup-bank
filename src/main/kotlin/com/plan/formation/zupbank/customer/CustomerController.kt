package com.plan.formation.zupbank.customer

import com.plan.formation.zupbank.customer.dtos.CustomerView
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import java.util.*


@RestController
@RequestMapping("customers")
class CustomerController {

    @Autowired private lateinit var customerService: CustomerService

    @PostMapping
    fun create(@RequestBody customer: CustomerModel): ResponseEntity<CustomerView> {
        return try {
            ResponseEntity(customerService.createCustomer(customer), HttpStatus.CREATED)
        } catch (err: RuntimeException) {
            throw ResponseStatusException(
                HttpStatus.INTERNAL_SERVER_ERROR, "Customer cannot be created", err
            )
        }
    }

    @GetMapping
    fun list(): ResponseEntity<List<CustomerView>> {
        return try {
            ResponseEntity.ok(customerService.listCustomers())
        } catch (err: RuntimeException) {
            throw ResponseStatusException(
                HttpStatus.INTERNAL_SERVER_ERROR, "Customers could not be found", err
            )
        }
    }

    @GetMapping("/{document}")
    fun find(@PathVariable("document") document: String): ResponseEntity<CustomerView> {
        return try {
            ResponseEntity.ok(customerService.findCustomer(document))
        } catch (err: RuntimeException) {
            throw ResponseStatusException(
                HttpStatus.INTERNAL_SERVER_ERROR, "Customer could not be found", err
            )
        }
    }

    @PutMapping("/{document}")
    fun update(@PathVariable("document") document: String, @RequestBody customer: CustomerModel): ResponseEntity<CustomerView> {
        return try {
            ResponseEntity.ok(customerService.updateCustomer(document, customer))
        } catch (err: RuntimeException) {
            throw ResponseStatusException(
                HttpStatus.INTERNAL_SERVER_ERROR, "Customer cannot be updated", err
            )
        }
    }

    @DeleteMapping("/{document}")
    fun remove(@PathVariable("document") document: String): ResponseEntity<Void> {
        return try {
            customerService.deleteCustomer(document)
            ResponseEntity(HttpStatus.ACCEPTED)
        } catch (err: RuntimeException) {
            throw ResponseStatusException(
                HttpStatus.INTERNAL_SERVER_ERROR, "Customer cannot be deleted", err
            )
        }
    }
}