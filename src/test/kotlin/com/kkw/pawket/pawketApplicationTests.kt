package com.kkw.pawket

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository

@SpringBootTest
class pawketApplicationTests {

    @MockBean
    lateinit var clientRegistrationRepository: ClientRegistrationRepository

    @Test
    fun contextLoads() {
        // 테스트 코드
    }
}
