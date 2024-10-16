package com.market.marketplace.servlet.Cart;

import com.market.marketplace.dao.CommandProductDAO;
import com.market.marketplace.dao.daoImpl.CommandProductDAOImpl;
import com.market.marketplace.service.CommandProductService;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import org.json.JSONObject;
import com.market.marketplace.service.serviceImpl.CommandProductServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;

public class addProductCartServlet extends HttpServlet {
    final Logger logger = LoggerFactory.getLogger(addProductCartServlet.class);

    private CommandProductService commandProductService;
    private EntityManagerFactory emf;

    public addProductCartServlet() {
        super();
    }

    @Override
    public void init() throws ServletException {
        emf = Persistence.createEntityManagerFactory("marketPlace");
        EntityManager em = emf.createEntityManager();
        CommandProductDAO commandProductDAO = new CommandProductDAOImpl(em);
        this.commandProductService = new CommandProductServiceImpl(commandProductDAO);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.info("Servlet invoked");
        StringBuilder sb = new StringBuilder();
        String line;

        try (BufferedReader reader = req.getReader()) {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }

        try {
            JSONObject jsonRequest = new JSONObject(sb.toString());
            int productId = jsonRequest.getInt("productId");
            int quantity = jsonRequest.getInt("quantity");
            int commandId = jsonRequest.getInt("commandId");

            logger.debug(productId + " " + quantity +" " + commandId);

            commandProductService.addProductToCommand(productId, quantity, commandId);

            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("success", true);
            resp.setContentType("application/json");
            resp.getWriter().write(jsonResponse.toString());

        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("success", false);
            jsonResponse.put("error", e.getMessage());
            resp.setContentType("application/json");
            resp.getWriter().write(jsonResponse.toString());
        }
    }
}