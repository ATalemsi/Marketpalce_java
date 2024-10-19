package com.market.marketplace.service.serviceImpl;

import com.market.marketplace.dao.AdminDao;
import com.market.marketplace.dao.daoImpl.AdminDaoImpl;
import com.market.marketplace.entities.Admin;
import com.market.marketplace.entities.Client;
import com.market.marketplace.service.AdminService;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.Collections;
import java.util.List;

public class AdminServiceImpl implements AdminService {
    private AdminDao adminDao;

    public AdminServiceImpl(AdminDao adminDao) {
        this.adminDao = adminDao;
    }

    @Override
    public List<Admin> findAllAdmins() {
        return adminDao.findAllAdmins();
    }

    @Override
    public List<Admin> findSuperAdmins() {
        return adminDao.findSuperAdmins();
    }

    @Override
    public List<Client> findAllClients() {
        return adminDao.findAllClients();
    }

    @Override
    public void assignRole(int userId, int niveauAcces) {
        adminDao.assignRole(userId, niveauAcces);

    }

    @Override
    public void addAdminNormal(Admin admin) {
        // Set access level to 0 for normal admin
        admin.setAccessLevel(0);
        adminDao.addAdminNormal(admin);
    }

    @Override
    public void updateAdminNormal(Admin admin) {
        adminDao.updateAdminNormal(admin);
    }

    @Override
    public void deleteAdminNormal(int adminId) {
        adminDao.deleteAdminNormal(adminId);
    }

    @Override
    public void addSuperAdmin(Admin admin) {
        // Set access level to 1 for super admin
        admin.setAccessLevel(1);
        adminDao.addSuperAdmin(admin);
    }

    @Override
    public void updateSuperAdmin(Admin admin) {
        adminDao.updateSuperAdmin(admin);
    }

    @Override
    public void deleteSuperAdmin(int adminId) {
        adminDao.deleteSuperAdmin(adminId);
    }

}

