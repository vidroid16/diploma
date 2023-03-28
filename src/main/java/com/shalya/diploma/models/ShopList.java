package com.shalya.diploma.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "lists")
@AllArgsConstructor
@Data
@NoArgsConstructor
public class ShopList {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userId")
    private User user;

    @Column(name = "name")
    private String name;

    @Column(name = "budget")
    private Integer budget;

    @Column(name = "totalPrice")
    private Double totalPrice;
}
