package com.example.backend.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@Builder
@Entity
@Getter
@NoArgsConstructor
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String purchaserEmail;
    private String purchaserPhoneNumber;
    private String purchaserName;
    private String purchaserSurname;
    private String address;
    private String preferredDeliveryTime;
    //private StatusType status;

    @OneToMany(cascade = CascadeType.ALL)
    @Setter
    private List<OrderPosition> orderPositions;
}
