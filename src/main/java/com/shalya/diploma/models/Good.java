package com.shalya.diploma.models;

import com.shalya.diploma.knapsack.Packable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Table(name = "goods")
@AllArgsConstructor
@Data
@NoArgsConstructor
public class Good implements Packable {
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

    @Column(name = "is_active")
    private Boolean isActive;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "good", cascade = CascadeType.REMOVE)
    private Set<Rating> ratings;

    @Override
    public int getWeight() {
        return (int) Math.round(this.price);
    }

    @Override
    public int getValue() {
        return this.rating;
    }
}
