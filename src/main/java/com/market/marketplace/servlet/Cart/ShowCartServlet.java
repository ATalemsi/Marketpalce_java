package com.market.marketplace.servlet.Cart;

import com.market.marketplace.config.ThymeleafConfig;
import com.market.marketplace.dao.CommandProductDAO;
import com.market.marketplace.dao.daoImpl.CommandProductDAOImpl;
import com.market.marketplace.entities.CommandProduct;
import com.market.marketplace.service.CommandProductService;
import com.market.marketplace.service.serviceImpl.CommandProductServiceImpl;
import com.market.marketplace.util.ThymeleafUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

public class ShowCartServlet extends HttpServlet {

    final Logger logger = LoggerFactory.getLogger(addProductCartServlet.class);

    private CommandProductService commandProductService;
    private EntityManagerFactory emf;
    public ThymeleafUtil templateEngine;

    public ShowCartServlet() {
        super();
    }

    @Override
    public void init() throws ServletException {
        emf = Persistence.createEntityManagerFactory("marketPlace");
        EntityManager em = emf.createEntityManager();
        CommandProductDAO commandProductDAO = new CommandProductDAOImpl(em);
        this.commandProductService = new CommandProductServiceImpl(commandProductDAO);
        templateEngine = new ThymeleafUtil(getServletContext());
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        HttpSession session = request.getSession(false);
        if (session != null) {
            Integer clientId = (Integer) session.getAttribute("userId");
            Integer commandId = (Integer) session.getAttribute("currentCommandId");

            if (clientId != null && commandId != null) {
                List<CommandProduct> cartProducts = commandProductService.getCurrentCartForClient(clientId, commandId);

                WebContext context = new WebContext(request, response, request.getServletContext(), request.getLocale());
                context.setVariable("cartProducts", cartProducts);

                templateEngine.returnView(context, response, "Cart/Show");
            } else {
                response.sendRedirect(request.getContextPath() + "/login");
            }
        } else {
            response.sendRedirect(request.getContextPath() + "/login");
        }
    }

}
