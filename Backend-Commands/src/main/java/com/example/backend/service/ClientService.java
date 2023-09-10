//package com.example.backend.service;
//
//import com.example.backend.domain.entity.Client;
//import com.example.backend.repository.ClientRepository;
//import lombok.AllArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.util.Optional;
//
//@AllArgsConstructor
//@Service
//public class ClientService {
//    private final ClientRepository repository;
//
//    public Client create(Client client) {
//        return repository.save(client);
//    }
//
//    public Optional<Client> findById(Long id) {
//        return repository.findById(id);
//    }
//}
