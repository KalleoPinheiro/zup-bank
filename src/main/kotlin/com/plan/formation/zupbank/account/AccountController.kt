package com.plan.formation.zupbank.account

import com.plan.formation.zupbank.account.dtos.AccountView
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException


@RestController
@RequestMapping("accounts")
class AccountController(
    private var accountService: AccountService
) {
    @PostMapping("/{document}")
    fun create(@PathVariable("document") document: String): ResponseEntity<AccountView> {
        return try {
            ResponseEntity(accountService.createAccount(document), HttpStatus.CREATED)
        } catch (err: RuntimeException) {
            throw ResponseStatusException(
                HttpStatus.INTERNAL_SERVER_ERROR, "Account cannot be created", err
            )
        }
    }

    @GetMapping
    fun list(): ResponseEntity<List<AccountView>> {
        return try {
            ResponseEntity.ok(accountService.listAccounts())
        } catch (err: RuntimeException) {
            throw ResponseStatusException(
                HttpStatus.INTERNAL_SERVER_ERROR, "Accounts could not be found", err
            )
        }
    }

    @GetMapping("/{document}")
    fun find(@PathVariable("document") document: String): ResponseEntity<AccountView> {
        return try {
            ResponseEntity.ok(accountService.findAccount(document))
        } catch (err: RuntimeException) {
            throw ResponseStatusException(
                HttpStatus.INTERNAL_SERVER_ERROR, "Customer could not be found", err
            )
        }
    }

    @PutMapping("/{document}")
    fun update(
        @PathVariable("document") accountNumber: String,
        @RequestBody account: AccountModel
    ): ResponseEntity<AccountView> {
        return try {
            ResponseEntity.ok(accountService.updateAccount(accountNumber, account))
        } catch (err: RuntimeException) {
            throw ResponseStatusException(
                HttpStatus.INTERNAL_SERVER_ERROR, "Account cannot be updated", err
            )
        }
    }

    @DeleteMapping("/{accountNumber}")
    fun remove(@PathVariable("accountNumber") accountNumber: String): ResponseEntity<Void> {
        return try {
            accountService.deleteAccount(accountNumber)
            ResponseEntity(HttpStatus.ACCEPTED)
        } catch (err: RuntimeException) {
            throw ResponseStatusException(
                HttpStatus.INTERNAL_SERVER_ERROR, "Account cannot be deleted", err
            )
        }
    }
}