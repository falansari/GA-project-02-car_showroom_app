package com.ga.showroom.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Setter@Getter
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column
    private Double totalPrice;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "car_id", referencedColumnName = "id")
    private Car car;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private User customer;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @ManyToOne
    @JoinColumn(name = "salesman_id")
    private User salesman;

    //@OneToMany(fetch = FetchType.EAGER, mappedBy = "order", orphanRemoval = true)
    //private List<OrderLine> orderLines;

    @CreationTimestamp
    @Column
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column
    private LocalDateTime updatedAt;
}
