package com.market.marketplace.dao;

import com.market.marketplace.entities.Client;
import com.market.marketplace.entities.User;

public interface UserDao {
    boolean validateUser(String email, String password);
    boolean registerUser(Client client);
    boolean userExists(String email);
    User getUserByEmail(String email);
}
