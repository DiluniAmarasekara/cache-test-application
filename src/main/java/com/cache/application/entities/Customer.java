package com.cache.application.entities;

import lombok.Data;

import javax.persistence.*;

/**
 * Created by Diluni on 25/08/20.
 */
@Entity
@Data
public class Customer extends baseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true)
    private String nic;

    private String address;

    private String gender;

    public Customer(){

    }

    public Customer(Long id, String name, String nic, String address, String gender) {
        this.id = id;
        this.name = name;
        this.nic = nic;
        this.address = address;
        this.gender = gender;
    }

}
