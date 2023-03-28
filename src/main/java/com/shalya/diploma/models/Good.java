package com.shalya.diploma.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "goods")
@AllArgsConstructor
@Data
@NoArgsConstructor
public class Good {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "categoryId")
    private Category category;

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private Double price;

    @Column(name = "photoUrl")
    private String photoUrl;

    @Column(name = "amount")
    private Integer amount;

    @Column(name = "unit")
    private String unit;

    @Column(name = "rating")
    private Integer rating;
}
