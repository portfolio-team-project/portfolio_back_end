package com.api.domain.base.ds.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class DS {
    
    @Id
    @GeneratedValue
    private Integer privateKey;
}
