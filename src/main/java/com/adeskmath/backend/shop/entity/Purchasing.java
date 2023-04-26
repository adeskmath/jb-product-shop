package com.adeskmath.backend.shop.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.sql.Date;
import java.util.Objects;

@Entity
@Table(name = "purchasing", schema = "shop", catalog = "product-shop")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Purchasing {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    private Customer customer;
    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;
    @Column(name = "purchasing_date", nullable = false)
    private Date purchasingDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Purchasing that = (Purchasing) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Purchasing{" +
                "id=" + id +
                ", customerId=" + customer +
                ", productId=" + product +
                ", purchasingDate=" + purchasingDate +
                '}';
    }
}
