package com.market.marketplace.service;
import com.market.marketplace.entities.Admin;
import com.market.marketplace.service.serviceImpl.AdminServiceImpl;
import com.market.marketplace.servlet.AdminServlet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class AdminServletTest {

    // Mocking the service that will be used in the servlet
    @Mock
    private AdminServiceImpl adminServiceImpl;

    // Injecting mocks into the AdminServlet
    @InjectMocks
    private AdminServlet adminServlet;

    // Mocking request and response objects
    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @BeforeEach
    void setUp() {
        // Initializing the mocks
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testHandleAddAdminNormal() throws Exception {
        // Arrange
        when(request.getParameter("firstname")).thenReturn("John");
        when(request.getParameter("lastname")).thenReturn("Doe");
        when(request.getParameter("email")).thenReturn("john.doe@example.com");
        when(request.getParameter("password")).thenReturn("password123");

        // Act
        adminServlet.handleAddAdminNormal(request, response);

        // Assert
        verify(adminServiceImpl).addAdminNormal(any(Admin.class)); // Verify that addAdminNormal was called
    }
    @Test
    void testHandleUpdateAdminNormal() throws Exception {
        // Arrange
        when(request.getParameter("adminId")).thenReturn("1");
        when(request.getParameter("firstname")).thenReturn("John");
        when(request.getParameter("lastname")).thenReturn("Doe");
        when(request.getParameter("email")).thenReturn("john.doe@example.com");
        when(request.getParameter("password")).thenReturn("password123");

        Admin admin = new Admin();
        admin.setId(1);
        admin.setFirstName("John");
        admin.setLastName("Doe");
        admin.setEmail("john.doe@example.com");

        // Act
        adminServlet.handleUpdateAdminNormal(request, response);

        // Assert
        verify(adminServiceImpl).updateAdminNormal(any(Admin.class));
        verify(response).sendRedirect(request.getContextPath() + "/admin?tableType=admins");
    }

    @Test
    void testHandleDeleteAdmin() throws Exception {
        when(request.getParameter("adminId")).thenReturn("1");

        adminServlet.handleDeleteAdminNormal(request, response);

        verify(adminServiceImpl).deleteAdminNormal(1); // Verifies that the service method to delete admin was called with ID 1
        verify(response).sendRedirect(request.getContextPath() +"/admin?tableType=admins"); // Ensure correct redirection after deletion
    }

    @Test
    void testHandleDeleteSuperAdmin() throws Exception {
        when(request.getParameter("superAdminId")).thenReturn("1");

        adminServlet.handleDeleteSuperAdmin(request, response);

        verify(adminServiceImpl).deleteSuperAdmin(1); // Verifies that the service method to delete admin was called with ID 1
        verify(response).sendRedirect(request.getContextPath() +"/admin?tableType=superAdmin"); // Ensure correct redirection after deletion
    }
}
