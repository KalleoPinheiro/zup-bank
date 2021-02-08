package com.plan.formation.zupbank.customer

import com.google.gson.Gson
import com.plan.formation.zupbank.customer.CustomerModel
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.*

@SpringBootTest
@AutoConfigureMockMvc
class CustomerControllerTests {

    @Autowired
    private val mockMvc: MockMvc? = null

    val gson = Gson()

    @Test
    fun create() {
        val customerToSave = CustomerModel(name = "Teste3", document = "39258996003")

        mockMvc!!.post("/customers") {
            contentType = MediaType.APPLICATION_JSON
            content = gson.toJson(customerToSave)
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isCreated() }
            content { contentType(MediaType.APPLICATION_JSON) }
            content { customerToSave }
        }
    }

    @Test
    fun list() {
        mockMvc!!.get("/customers") {
            accept = MediaType.APPLICATION_JSON
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
        mockMvc!!.get("/customers/{document}", "01943939179") {
            accept = MediaType.APPLICATION_JSON
        }.andDo {
            print()
        }.andExpect {
            status { isOk() }
            jsonPath("$.document") { value("01943939179") }
        }
    }

    @Test
    fun remove() {
        mockMvc!!.delete("/customers/{document}", "39258996003") {
        }.andDo {
            print()
        }.andExpect {
            status { isAccepted() }
        }
    }

}
