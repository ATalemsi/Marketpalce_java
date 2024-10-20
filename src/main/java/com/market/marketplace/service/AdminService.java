package com.market.marketplace.service;

import com.market.marketplace.entities.Admin;
import com.market.marketplace.entities.Client;

import java.util.List;

public interface AdminService {
    List<Admin> findAllAdmins(int page, int size);
    long countAdmins();
    List<Admin> findSuperAdmins(int page, int size);
    long countSuperAdmins();
    List<Client> findAllClients(int page, int size);
    long countClients();
    void assignRole(int userId, int niveauAcces);
    void addAdminNormal(Admin admin);
    void updateAdminNormal(Admin admin);
    void deleteAdminNormal(int adminId);
    void addSuperAdmin(Admin admin);
    void updateSuperAdmin(Admin admin);
    void deleteSuperAdmin(int adminId);
    void deleteClientById(int clientId);
    List<Admin> findSuperAdminsByEmail(String email);
    List<Admin> findAdminsByEmail(String email);
    List<Client> findClientsByEmail(String email);
}
