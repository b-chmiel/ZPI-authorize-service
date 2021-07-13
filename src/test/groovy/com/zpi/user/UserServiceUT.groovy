package com.zpi.user

import com.zpi.user.api.UserDTO
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import spock.lang.Specification
import spock.lang.Subject

class UserServiceUT extends Specification {
    def userRepository = Mock(UserRepository)

    @Subject
    private UserService userService = new UserService(userRepository);

    def "should create user"() {
        given:
            UserDTO user = UserDTO.builder()
                    .login("Login")
                    .build()

            User hashedUser = user.toHashedDomain();

            userRepository.getByKey(hashedUser.getLogin()) >> Optional.empty()

        when:
            def response = userService.createUser(user)

        then:
            response == new ResponseEntity<>(HttpStatus.CREATED)
    }

    def "should return conflict if user exists"() {
        given:
            UserDTO user = UserDTO.builder()
                    .login("Login")
                    .build()

            User hashedUser = user.toHashedDomain();
            userRepository.getByKey(hashedUser.getLogin()) >> Optional.of(user.toHashedDomain())

        when:
            def response = userService.createUser(user)

        then:
            response == new ResponseEntity<>(HttpStatus.CONFLICT)
    }
}
