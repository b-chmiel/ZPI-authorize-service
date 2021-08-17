package com.zpi.user

import com.zpi.CommonFixtures
import com.zpi.domain.user.UserRepository
import com.zpi.domain.user.UserManager
import spock.lang.Specification
import spock.lang.Subject

class UserRegistrationUT extends Specification {
    def userRepository = Mock(UserRepository)

    @Subject
    private UserManager userService = new UserManager(userRepository)

    def "should create user"() {
        given:
            def user = CommonFixtures.userDTO().toHashedDomain()

            userRepository.getByKey(user.getLogin()) >> Optional.empty()

        when:
            def isSuccess = userService.createUser(user)

        then:
            isSuccess
    }

    def "should return conflict if user exists"() {
        given:
            def user = CommonFixtures.userDTO().toHashedDomain()

            userRepository.getByKey(user.getLogin()) >> Optional.of(user)

        when:
            def isSuccess = userService.createUser(user)

        then:
            !isSuccess
    }
}
