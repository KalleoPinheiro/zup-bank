package com.plan.formation.zupbank.account

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface AccountRepository : JpaRepository<AccountModel, Long> {
    fun findByAccountNumber(accountNumber: String): Optional<AccountModel>
}