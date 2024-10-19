package com.market.marketplace.servlet.auth;

import com.market.marketplace.dao.daoImpl.ClientDaoImpl;
import com.market.marketplace.entities.Client;
import com.market.marketplace.entities.User;
import com.market.marketplace.entities.enums.Role;
import com.market.marketplace.service.serviceImpl.AuthServiceImpl;
import org.mindrot.jbcrypt.BCrypt;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RegisterServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private AuthServiceImpl authServiceImpl;
    private EntityManagerFactory entityManagerFactory;
    private TemplateEngine templateEngine;


    public RegisterServlet() {
        super();
    }

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        this.templateEngine = new TemplateEngine();
        ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(config.getServletContext());
        templateResolver.setPrefix("/WEB-INF/templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);

        templateEngine.setTemplateResolver(templateResolver);

        entityManagerFactory = Persistence.createEntityManagerFactory("marketPlace");
        if (entityManagerFactory == null) {
            throw new ServletException("EntityManagerFactory could not be created.");
        }
        ClientDaoImpl clientServiceImpl = new ClientDaoImpl(entityManagerFactory);
        this.authServiceImpl = new AuthServiceImpl(clientServiceImpl);
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        WebContext context = new WebContext(request, response, request.getServletContext(), request.getLocale());
        context.setVariable("error", request.getParameter("error"));
        templateEngine.process("auth/register", context, response.getWriter());

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String firstname = request.getParameter("firstname");
        String lastname = request.getParameter("lastname");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        if (authServiceImpl.userExists(email)) {
            response.sendRedirect("/auth/register?error=User already exists");
        } else {
            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
            Client newUser = new Client();
            newUser.setFirstName(firstname);
            newUser.setLastName(lastname);
            newUser.setEmail(email);
            newUser.setPassword(hashedPassword);

            newUser.setRole(Role.CLIENT);

            boolean isRegistered = authServiceImpl.registerUser(newUser);

            if (isRegistered) {
                response.sendRedirect(request.getContextPath() + "/login");
            } else {
                response.sendRedirect(request.getContextPath() + "/register?error=Registration failed");
            }
        }

    }
}
