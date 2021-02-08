package com.plan.formation.zupbank.transaction

import com.plan.formation.zupbank.transaction.dtos.*
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException


@RestController
@RequestMapping("transactions")
class TransactionController(
    private var transactionService: TransactionService
) {
    @PostMapping("deposit/{customer}")
    fun deposit(
        @PathVariable("customer") customer: String,
        @RequestBody transactionRequest: TransactionDepositWithdrawalView
    ): ResponseEntity<TransactionView> {
        return try {
            ResponseEntity.ok(transactionService.deposit(customer, transactionRequest))
        } catch (err: RuntimeException) {
            throw ResponseStatusException(
                HttpStatus.INTERNAL_SERVER_ERROR, "Deposit cannot be performed", err
            )
        }
    }

    @PostMapping("withdrawal/{customer}")
    fun withdrawal(
        @PathVariable("customer") customer: String,
        @RequestBody transactionRequest: TransactionDepositWithdrawalView
    ): ResponseEntity<TransactionView> {
        return try {
            ResponseEntity.ok(transactionService.withdrawal(customer, transactionRequest))
        } catch (err: RuntimeException) {
            throw ResponseStatusException(
                HttpStatus.INTERNAL_SERVER_ERROR, "Withdrawal cannot be performed", err
            )
        }
    }

    @PostMapping("transfer/{customer}")
    fun transfer(
        @PathVariable("customer") customer: String,
        @RequestBody transactionRequest: TransactionTransferRequestView
    ): ResponseEntity<TransactionTransferView> {
        return try {
            ResponseEntity.ok(transactionService.transfer(customer, transactionRequest))
        } catch (err: RuntimeException) {
            throw ResponseStatusException(
                HttpStatus.INTERNAL_SERVER_ERROR, "Transfer cannot be performed", err
            )
        }
    }

    @GetMapping("statement/{customer}")
    fun statement(@PathVariable("customer") customer: String): ResponseEntity<List<TransactionModel>> {
        return try {
            ResponseEntity.ok(transactionService.statement(customer))
        } catch (err: RuntimeException) {
            throw ResponseStatusException(
                HttpStatus.INTERNAL_SERVER_ERROR, "Transfer cannot be performed", err
            )
        }
    }

    @GetMapping("/balance/{document}")
    fun balance(@PathVariable("document") document: String): ResponseEntity<TransactionBalanceView> {
        return try {
            ResponseEntity.ok(transactionService.balance(document))
        } catch (err: RuntimeException) {
            throw ResponseStatusException(
                HttpStatus.INTERNAL_SERVER_ERROR, "Balance cannot be performed", err
            )
        }
    }

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
}