package com.market.marketplace.servlet.Cart;

import com.market.marketplace.config.ThymeleafConfig;
import com.market.marketplace.dao.CommandProductDAO;
import com.market.marketplace.dao.daoImpl.CommandProductDAOImpl;
import com.market.marketplace.entities.CommandProduct;
import com.market.marketplace.service.CommandProductService;
import com.market.marketplace.service.serviceImpl.CommandProductServiceImpl;
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
import java.io.IOException;
import java.util.List;

public class ShowCartServlet extends HttpServlet {

    final Logger logger = LoggerFactory.getLogger(addProductCartServlet.class);

    private CommandProductService commandProductService;
    private EntityManagerFactory emf;

    public ShowCartServlet() {
        super();
    }

    @Override
    public void init() throws ServletException {
        emf = Persistence.createEntityManagerFactory("marketPlace");
        EntityManager em = emf.createEntityManager();
        CommandProductDAO commandProductDAO = new CommandProductDAOImpl(em);
        this.commandProductService = new CommandProductServiceImpl(commandProductDAO);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        List<CommandProduct> cartProducts = commandProductService.getCurrentCartForClient(2);

        TemplateEngine templateEngine = (TemplateEngine) getServletContext().getAttribute(ThymeleafConfig.TEMPLATE_ENGINE_ATTR);

        WebContext context = new WebContext(request, response, getServletContext());

        context.setVariable("cartProducts", cartProducts);

        templateEngine.process("Cart/Show", context, response.getWriter());
    }

}
