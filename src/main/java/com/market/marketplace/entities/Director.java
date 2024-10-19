package com.market.marketplace.entities;

import com.market.marketplace.entities.User;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("DIRECTOR")
public class Director extends User {
    
}
