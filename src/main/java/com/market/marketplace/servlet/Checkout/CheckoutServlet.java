package com.market.marketplace.servlet.Checkout;

import com.market.marketplace.dao.CommandProductDAO;
import com.market.marketplace.dao.daoImpl.CommandProductDAOImpl;
import com.market.marketplace.service.CommandProductService;
import com.market.marketplace.service.serviceImpl.CommandProductServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CheckoutServlet extends HttpServlet {

    final Logger logger = LoggerFactory.getLogger(CheckoutServlet.class);

    private CommandProductService commandProductService;
    private EntityManagerFactory emf;

    @Override
    public void init() throws ServletException {
        emf = Persistence.createEntityManagerFactory("marketPlace");
        EntityManager em = emf.createEntityManager();
        CommandProductDAO commandProductDAO = new CommandProductDAOImpl(em);
        this.commandProductService = new CommandProductServiceImpl(commandProductDAO);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String shippingAddress = req.getParameter("shippingAddress");
        String paymentMethod = req.getParameter("paymentMethod");

//        update client

        int commandId = 1;

        commandProductService.confirmCommand(commandId);

        resp.sendRedirect(req.getContextPath() + "/order-confirmation");
    }


}
