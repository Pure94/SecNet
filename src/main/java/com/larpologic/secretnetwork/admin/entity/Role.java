package com.larpologic.secretnetwork.admin.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "roles")
@Getter
@Setter
public class Role {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID uuid;

    @Column(name = "name")
    private String name;

}