package com.ga.showroom.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.time.Year;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "car_models")
public class CarModel {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private Year makeYear;

    @Column
    private String manufacturer;

    @Column
    @JsonIgnore
    private String image;

    @Column
    private Double price;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "carModel", orphanRemoval = true)
    @JsonBackReference
    private List<Car> cars;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "carModel", orphanRemoval = true)
    private List<Option> options;

    @CreationTimestamp
    @Column
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column
    private LocalDateTime updatedAt;
}
