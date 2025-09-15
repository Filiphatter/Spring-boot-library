package com.example.librarysystem.entity;

import jakarta.persistence.*;

    @Entity
    @Table(name = "roles")
    public class Role {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id")
        private Long id;

    @Column(unique = true, nullable = false)
        private String name; //role_user + admin

        //constructor
        public Role() {}

        //getter setter
        public Long getId() {return id;}
        public void setId(Long id) {
            this.id = id;}

        public String getName() {return name;}
        public void setName(String name) {
            this.name = name;}
    }

