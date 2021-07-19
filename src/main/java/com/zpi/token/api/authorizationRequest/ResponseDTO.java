package com.zpi.token.api.authorizationRequest;

import com.zpi.token.domain.authorizationRequest.response.Response;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class ResponseDTO {
    private final String code;
    private final String state;

    public ResponseDTO(Response response) {
        this.code = response.getCode();
        this.state = response.getState();
    }
}
