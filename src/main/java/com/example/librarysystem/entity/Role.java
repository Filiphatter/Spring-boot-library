package com.example.librarysystem.entity;

import jakarta.persistence.*;

    @Entity
    @Table(name = "roles")
    public class Role {
        @Id
        @GeneratedValue
        private Long id;

    @Column(unique = true, nullable = false)
        private String name; //role_user + admin

        //getter setter
        public Long getId() {return id;}
        public void setId(long id) {
            this.id = id;}

        public String getName() {return name;}
        public void setName(String name) {
            this.name = name;}
    }

