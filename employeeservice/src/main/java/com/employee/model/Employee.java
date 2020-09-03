package com.employee.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.jetbrains.annotations.NotNull;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Data
@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode
public class Employee {

   private Integer id;

   @NotNull
   @NotEmpty(message = "employeeName cannot be empty")
   @JsonProperty(value = "employeename")
   private String employeeName;
   @NotNull
   @NotEmpty(message = "firstName cannot be empty")
   private String firstName;
   @NotNull
   @NotEmpty(message = "lastName cannot be empty")
   private String lastName;
   @NotNull
   @Email
   @NotEmpty(message = "email cannot be empty")
   private String email;
   @NotNull
   @NotEmpty(message = "password cannot be empty")
   private String password;
   @NotNull
   private String phone;

   @NotNull
   private Integer employeeStatus;
}
