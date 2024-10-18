package com.market.marketplace.service;

import com.market.marketplace.entities.Client;
import com.market.marketplace.entities.User;

public interface UserService {
    boolean validateUser(String username, String password);
    boolean registerUser(Client client);
    boolean userExists(String username);
    User getUserByEmail(String email);
}
