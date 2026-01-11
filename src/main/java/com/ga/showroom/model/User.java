package com.ga.showroom.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"password", "userProfile"})
public class User {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userName;

    @Column(unique = true)
    private String emailAddress;

    @Column
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Column
    private String role;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "customer", orphanRemoval = true)
    private List<Order> ordersRequested;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "salesman", orphanRemoval = true)
    private List<Order> ordersMonitored;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "owner", orphanRemoval = true)
    private List<Car> cars;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", referencedColumnName = "id")
    private UserProfile userProfile;

    @JsonIgnore
    public String getPassword() {
        return password;
    }
}
