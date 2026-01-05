package com.ga.showroom.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.catalina.User;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "cars")
public class Car {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String vinNumber;

    @Column
    private String registrationNumber;

    @Column
    private String insurancePolicy;

    @Column
    private String image;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "car_model_id")
    private CarModel carModel;

    @JsonIgnore
    @OneToOne(mappedBy = "car", fetch = FetchType.EAGER)
    private OrderLine orderLine;


    @OneToMany(fetch = FetchType.EAGER, mappedBy = "car", orphanRemoval = true)
    private List<CarOption> carOptions;

    @CreationTimestamp
    @Column
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column
    private LocalDateTime updatedAt;
}
