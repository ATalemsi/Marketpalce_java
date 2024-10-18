package com.market.marketplace.servlet.auth;

import com.market.marketplace.dao.daoImpl.AdminDaoImpl;
import com.market.marketplace.dao.daoImpl.ClientDaoImpl;
import com.market.marketplace.entities.Admin;
import com.market.marketplace.entities.User;
import com.market.marketplace.service.serviceImpl.AuthServiceImpl;
import com.market.marketplace.service.serviceImpl.ClientServiceImpl;
import org.mindrot.jbcrypt.BCrypt;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;
import sun.security.mscapi.CPublicKey;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private AuthServiceImpl authServiceImpl;

    private EntityManagerFactory entityManagerFactory;
    private TemplateEngine templateEngine;

    public LoginServlet() {
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

        EntityManager entityManager = entityManagerFactory.createEntityManager();

        ClientDaoImpl clientServiceImpl = new ClientDaoImpl(entityManagerFactory);
        AdminDaoImpl adminServiceImpl = new AdminDaoImpl(entityManager);

        this.authServiceImpl = new AuthServiceImpl(clientServiceImpl, adminServiceImpl);

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        WebContext context = new WebContext(request, response, request.getServletContext(), request.getLocale());
        context.setVariable("error", request.getParameter("error"));
        templateEngine.process("auth/login", context, response.getWriter());

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        if (authServiceImpl.userExists(email)) {
            User user = authServiceImpl.getUserByEmail(email);

            HttpSession session = request.getSession(true);
            session.setAttribute("email", email);
            session.setAttribute("role", user.getRole().name());
            session.setAttribute("userId", user.getId());
            session.setMaxInactiveInterval(30 * 60);

            String redirectUrl = request.getContextPath();

            if ("DIRECTOR".equals(user.getRole().name())) {
                redirectUrl = request.getContextPath() + "/admin?tableType=superAdmin";
            } else if ("ADMIN".equals(user.getRole().name())) {
                Admin adminInfo = authServiceImpl.getAdminInfoByEmail(email);
                int niveauAcces = adminInfo.getAccessLevel();

                if (niveauAcces == 0) {
                    session.setAttribute("email", email);
                    session.setAttribute("role", user.getRole().name());
                    session.setAttribute("niveauAcces", adminInfo.getAccessLevel());
                    redirectUrl = request.getContextPath() + "/admin?tableType=clients";
                } else if (niveauAcces == 1) {
                    session.setAttribute("email", email);
                    session.setAttribute("role", user.getRole().name());
                    session.setAttribute("niveauAcces", adminInfo.getAccessLevel());
                    redirectUrl = request.getContextPath() + "/admin?tableType=admins";
                } else {
                    session.setAttribute("email", email);
                    session.setAttribute("role", user.getRole().name());
                    redirectUrl = request.getContextPath() + "/admin?tableType=admins";
                }
            }
            response.sendRedirect(redirectUrl);
        } else {
            response.sendRedirect(request.getContextPath() + "/login?error=User does not exist");
        }
    }
}
