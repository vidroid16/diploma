package com.shalya.diploma.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "lists_goods")
@AllArgsConstructor
@Data
@NoArgsConstructor
public class ListsGoods {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "list_id")
    private ShopList shopList;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "good_id")
    private Good good;

    @Column(name = "is_checked")
    private Boolean isChecked;
}
