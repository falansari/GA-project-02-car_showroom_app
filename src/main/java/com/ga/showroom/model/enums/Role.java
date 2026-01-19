package com.ga.showroom.model.enums;

/**
 * Allowed roles:
 * ADMIN all system access + soft delete users
 * SALESMAN view all system data + create data
 * view their own data + view public system data (option categories, car models etc) + update their user profile
 */
public enum Role {
    ADMIN,
    SALESMAN,
    CUSTOMER
}
