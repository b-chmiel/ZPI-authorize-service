package com.zpi.e2e


import com.zpi.CommonFixtures
import com.zpi.MvcRequestHelpers
import com.zpi.ResultHelpers
import com.zpi.domain.authCode.consentRequest.TicketRepository
import com.zpi.domain.client.ClientRepository
import com.zpi.domain.user.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.web.util.UriComponentsBuilder
import spock.lang.Specification

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
class AuthenticationTicketE2E extends Specification {
    @Autowired
    private MockMvc mockMvc

    @Autowired
    private ClientRepository clientRepository

    @Autowired
    private UserRepository userRepository

    @Autowired
    private TicketRepository ticketRepository

    @Autowired
    private MvcRequestHelpers mvcHelpers

    private static final String clientRegisterUrl = "/api/client/register"
    private static final String userRegisterUrl = "/api/user/register"
    private static final String authorizeRequestUrl = "/api/authorize"
    private static final String authenticateRequestUrl = "/api/authenticate"
    private static final String consentRequestUrl = "/api/consent"

    def setup() {
        clientRepository.clear()
        userRepository.clear()
        ticketRepository.clear()
    }

    def "should get authentication ticket for newly registered user and client"() {
        given:
            def client = CommonFixtures.clientDTO()
            def user = CommonFixtures.userDTO()
            def request = CommonFixtures.requestDTO()

        when:
            mvcHelpers.postRequest(client, clientRegisterUrl)
            mvcHelpers.postRequest(user, userRegisterUrl)

        and:
            def authorizeResponse = mvcHelpers.getRequest(ResultHelpers.authParametersToUrl(request, authorizeRequestUrl))

        then:
            authorizeResponse.andExpect(status().isFound())
            authorizeResponse.andExpect(header().exists("Location"))

        when:
            def authenticateResponse = mvcHelpers.postRequest(user, ResultHelpers.authParametersToUrl(request, authenticateRequestUrl))

        then:
            authenticateResponse.andExpect(status().isOk())
            def ticket = ResultHelpers.attributeFromResult("ticket", authenticateResponse)

        when:
            def consentRequest = CommonFixtures.consentRequestDTO(ticket)
            def consentResponse = mvcHelpers.postRequest(consentRequest, ResultHelpers.authParametersToUrl(request, consentRequestUrl))

        then:
            consentResponse.andExpect(status().isFound())
        and:
            var uri = consentResponse.andReturn().getResponse().getHeader("Location")
            var path = UriComponentsBuilder.fromUriString(uri).build().getPath()
            path == CommonFixtures.redirectUri
    }
}
