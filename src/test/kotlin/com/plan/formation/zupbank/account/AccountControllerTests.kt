package com.plan.formation.zupbank.account

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post

@SpringBootTest
@AutoConfigureMockMvc
class AccountControllerTests {
    @Autowired
    private val mockMvc: MockMvc? = null

    @Test
    fun create() {
        mockMvc!!.post("/accounts/{document}", "15298101089") {
        }.andExpect {
            status { isCreated() }
        }
    }

    @Test
    fun list() {
        mockMvc!!.get("/accounts") {
        }.andDo {
            print()
        }.andExpect {
            status { isOk() }
            jsonPath("$") { isArray() }
            jsonPath("$") { isNotEmpty() }
        }
    }

    @Test
    fun find() {
        mockMvc!!.get("/accounts/{account_number}", "c8f273da") {
            accept = MediaType.APPLICATION_JSON
        }.andDo {
            print()
        }.andExpect {
            status { isOk() }
            jsonPath("$.account_number") { value("c8f273da") }
        }
    }

    @Test
    fun remove() {
        mockMvc!!.delete("/accounts/{account_number}", "77d0e4bd") {
        }.andDo {
            print()
        }.andExpect {
            status { isAccepted() }
        }
    }

}
