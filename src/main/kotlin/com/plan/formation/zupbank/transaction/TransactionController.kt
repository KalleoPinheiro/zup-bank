package com.plan.formation.zupbank.transaction

import com.plan.formation.zupbank.transaction.dtos.TransactionDepositView
import com.plan.formation.zupbank.transaction.dtos.TransactionView
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException


@RestController
@RequestMapping("transactions")
class TransactionController {

    @Autowired private lateinit var transactionService: TransactionService

    @PostMapping("deposit/{customer}")
    fun deposit(@PathVariable("customer") customer: String, @RequestBody transactionRequest: TransactionDepositView): ResponseEntity<TransactionView> {
        return try {
            ResponseEntity.ok(transactionService.deposit(customer, transactionRequest))
        } catch (err: RuntimeException) {
            throw ResponseStatusException(
                HttpStatus.INTERNAL_SERVER_ERROR, "Deposit cannot be performed", err
            )
        }
    }

    @PostMapping("withdrawal/{customer}")
    fun withdrawal(@PathVariable("customer") customer: String, @RequestBody transactionRequest: TransactionDepositView): ResponseEntity<TransactionView> {
        return try {
            ResponseEntity.ok(transactionService.withdrawal(customer, transactionRequest))
        } catch (err: RuntimeException) {
            throw ResponseStatusException(
                HttpStatus.INTERNAL_SERVER_ERROR, "Withdrawal cannot be performed", err
            )
        }
    }

//    @PostMapping("transfer")
//    fun createTransfer(@RequestBody transaction: TransactionDepositViewModel): ResponseEntity<TransactionModel> {
//        return try {
//            ResponseEntity.ok(transactionService.transfer(transaction))
//        } catch (err: RuntimeException) {
//            throw ResponseStatusException(
//                HttpStatus.INTERNAL_SERVER_ERROR, "Transfer cannot be performed", err
//            )
//        }
//    }

    @GetMapping
    fun list(): ResponseEntity<List<TransactionModel>> {
        return try {
            ResponseEntity.ok(transactionService.listTransactions())
        } catch (err: RuntimeException) {
            throw ResponseStatusException(
                HttpStatus.INTERNAL_SERVER_ERROR, "Transactions could not be found", err
            )
        }
    }
//
//    @GetMapping("/{document}")
//    fun find(@PathVariable("document") document: String): ResponseEntity<CustomerView> {
//        return try {
//            ResponseEntity.ok(customerService.findCustomer(document))
//        } catch (err: RuntimeException) {
//            throw ResponseStatusException(
//                HttpStatus.INTERNAL_SERVER_ERROR, "Customer could not be found", err
//            )
//        }
//    }
//
//    @PutMapping("/{document}")
//    fun update(@PathVariable("document") document: String, @RequestBody customer: CustomerModel): ResponseEntity<CustomerView> {
//        return try {
//            ResponseEntity.ok(customerService.updateCustomer(document, customer))
//        } catch (err: RuntimeException) {
//            throw ResponseStatusException(
//                HttpStatus.INTERNAL_SERVER_ERROR, "Customer cannot be updated", err
//            )
//        }
//    }
//
//    @DeleteMapping("/{document}")
//    fun remove(@PathVariable("document") document: String): ResponseEntity<Void> {
//        return try {
//            customerService.deleteCustomer(document)
//            ResponseEntity(HttpStatus.ACCEPTED)
//        } catch (err: RuntimeException) {
//            throw ResponseStatusException(
//                HttpStatus.INTERNAL_SERVER_ERROR, "Customer cannot be deleted", err
//            )
//        }
//    }
}