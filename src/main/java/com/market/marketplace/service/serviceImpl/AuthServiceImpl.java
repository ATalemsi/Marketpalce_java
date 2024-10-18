package com.market.marketplace.service.serviceImpl;

import com.market.marketplace.dao.AdminDao;
import com.market.marketplace.dao.UserDao;
import com.market.marketplace.entities.Admin;
import com.market.marketplace.entities.Client;
import com.market.marketplace.entities.User;
import com.market.marketplace.service.UserService;

public class AuthServiceImpl implements UserService {
    private UserDao userDao;
    private AdminDao adminDao;

    public AuthServiceImpl(UserDao userDao) {
        this.userDao = userDao;
        this.adminDao = null;  // Ensure adminDao is initialized to null
    }
    public AuthServiceImpl(UserDao userDao, AdminDao adminDao) {
        this.userDao = userDao;
        this.adminDao = adminDao;
    }

    @Override
    public boolean validateUser(String username, String password) {
        return userDao.validateUser(username, password);
    }

    @Override
    public boolean registerUser(Client client) {
        return userDao.registerUser(client);
    }

    @Override
    public boolean userExists(String username) {
        return userDao.userExists(username);
    }

    @Override
    public User getUserByEmail(String email) {
        return userDao.getUserByEmail(email);
    }
    public Admin getAdminInfoByEmail(String email) {
        if (adminDao == null) {
            throw new IllegalStateException("AdminDao is not initialized");
        }

        return adminDao.getAdminInfoByEmail(email);
    }
}
