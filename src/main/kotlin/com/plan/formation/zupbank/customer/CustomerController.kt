package com.plan.formation.zupbank.customer

import com.plan.formation.zupbank.customer.useCases.ValidatesIfCustomerExistsUseCase
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException


@RestController
@RequestMapping("customers")
class CustomerController {

    @Autowired private lateinit var customerService: CustomerService

    @PostMapping
    fun create(@RequestBody customer: CustomerModel): ResponseEntity<CustomerModel> {
        return try {
            ResponseEntity.ok(customerService.createUser(customer))
        } catch (err: RuntimeException) {
            throw ResponseStatusException(
                HttpStatus.INTERNAL_SERVER_ERROR, "Customer cannot be created", err
            )
        }
    }

    @GetMapping
    fun list(): ResponseEntity<MutableIterable<CustomerModel>> {
        return try {
            ResponseEntity.ok(customerService.listUsers())
        } catch (err: RuntimeException) {
            throw ResponseStatusException(
                HttpStatus.INTERNAL_SERVER_ERROR, "Customers could not be found", err
            )
        }
    }
}