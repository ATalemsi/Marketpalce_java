package com.market.marketplace.servlet.Cart;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.market.marketplace.dao.CommandProductDAO;
import com.market.marketplace.dao.daoImpl.CommandProductDAOImpl;
import com.market.marketplace.entities.Command;
import com.market.marketplace.entities.CommandProduct;
import com.market.marketplace.entities.Product;
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
import java.io.BufferedReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UpdateCartServlet extends HttpServlet {

    final Logger logger = LoggerFactory.getLogger(UpdateCartServlet.class);

    private CommandProductService commandProductService;
    private EntityManagerFactory emf;
    private final Gson gson = new Gson();

    @Override
    public void init() throws ServletException {
        emf = Persistence.createEntityManagerFactory("marketPlace");
        EntityManager em = emf.createEntityManager();
        CommandProductDAO commandProductDAO = new CommandProductDAOImpl(em);
        this.commandProductService = new CommandProductServiceImpl(commandProductDAO);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            BufferedReader reader = request.getReader();
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            String jsonString = sb.toString();

            Type type = new TypeToken<Map<String, Object>>() {}.getType();
            Map<String, Object> jsonData = gson.fromJson(jsonString, type);

            List<Map<String, Object>> items = (List<Map<String, Object>>) jsonData.get("items");
            Integer orderId = null;
            if (jsonData.get("orderId") != null) {
                if (jsonData.get("orderId") instanceof Number) {
                    orderId = ((Number) jsonData.get("orderId")).intValue();
                } else if (jsonData.get("orderId") instanceof String) {
                    orderId = Integer.parseInt((String) jsonData.get("orderId"));
                }
            }

            List<CommandProduct> commandProducts = new ArrayList<>();

            for (Map<String, Object> item : items) {
                int productId = getIntValue(item.get("id"));
                int quantity = getIntValue(item.get("quantity"));

                CommandProduct commandProduct = new CommandProduct();
                commandProduct.setQuantity(quantity);
                commandProduct.setValid(false);
                Product product = new Product();
                product.setId(productId);
                commandProduct.setProduct(product);

                Command command = new Command();
                command.setId(orderId != null ? orderId : 1);
                commandProduct.setCommand(command);

                commandProducts.add(commandProduct);
            }

            commandProductService.updateCart(orderId != null ? orderId : 1, commandProducts);

            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write("{\"status\":\"success\",\"message\":\"Cart updated successfully\"}");
        } catch (Exception e) {
            logger.error("Error updating cart", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"status\":\"error\",\"message\":\"" + e.getMessage() + "\"}");
        }
    }

    private int getIntValue(Object value) {
        if (value instanceof Number) {
            return ((Number) value).intValue();
        } else if (value instanceof String) {
            return Integer.parseInt((String) value);
        }
        throw new IllegalArgumentException("Cannot convert to int: " + value);
    }
}