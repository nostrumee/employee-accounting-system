package com.innowise.accountingsystem.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
@ToString
@Builder
public class LoggingEmployeeDto {

    @JsonProperty("email")
    String email;

    @JsonProperty("password")
    String password;
}
