//package com.example.backend.domain.entity;
//
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//
//import java.util.List;
//
//@AllArgsConstructor
//@Builder
//@Entity
//@Getter
//@NoArgsConstructor
//@Table(name = "clients")
//public class Client {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @Column(unique = true)
//    private String email;
//
//    private String password;
//
//    private String name;
//
//    private String surname;
//
//    private String address;
//
//    private String phoneNumber;
//
//    @OneToMany
//    private List<Order> orderList;
//}
