//package com.example.backend.mapper;
//
//import com.example.backend.domain.entity.Client;
//import com.example.backend.event.ClientCreatedEvent;
//import com.example.backend.model.command.create.CreateClientCommand;
//import lombok.AllArgsConstructor;
//import org.springframework.stereotype.Component;
//
//@AllArgsConstructor
//@Component
//public class ClientMapper {
//    public Client mapCommandToEntity(CreateClientCommand command) {
//        return Client.builder()
//                .email(command.getEmail())
//                .password(command.getPassword())
//                .name(command.getName())
//                .surname(command.getSurname())
//                .address(command.getAddress())
//                .phoneNumber(command.getPhoneNumber())
//                .build();
//    }
//
//    public ClientCreatedEvent mapEntityToEvent(Client client) {
//        return ClientCreatedEvent.builder()
//                .clientId(client.getId())
//                .email(client.getEmail())
//                .name(client.getName())
//                .surname(client.getSurname())
//                .address(client.getAddress())
//                .phoneNumber(client.getPhoneNumber())
//                .build();
//    }
//}
