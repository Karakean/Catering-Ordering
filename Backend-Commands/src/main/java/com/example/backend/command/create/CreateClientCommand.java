//package com.example.backend.model.command.create;
//
//import com.fasterxml.jackson.annotation.JsonCreator;
//import com.fasterxml.jackson.annotation.JsonProperty;
//import lombok.Getter;
//
//@Getter
//public class CreateClientCommand {
//    private final Long clientId;
//    private final String email;
//    private final String password;
//    private final String name;
//    private final String surname;
//    private final String address;
//    private final String phoneNumber;
//
//    @JsonCreator
//    public CreateClientCommand(@JsonProperty("clientId") Long clientId,
//                               @JsonProperty("email") String email,
//                               @JsonProperty("password") String password,
//                               @JsonProperty("name") String name,
//                               @JsonProperty("surname") String surname,
//                               @JsonProperty("password") String address,
//                               @JsonProperty("password") String phoneNumber) {
//        this.clientId = clientId;
//        this.email = email;
//        this.password = password;
//        this.name = name;
//        this.surname = surname;
//        this.address = address;
//        this.phoneNumber = phoneNumber;
//    }
//}
