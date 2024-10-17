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

            Type type = new TypeToken<Map<String, List<Map<String, Integer>>>>(){}.getType();
            Map<String, List<Map<String, Integer>>> jsonData = gson.fromJson(jsonString, type);
            List<Map<String, Integer>> items = jsonData.get("items");

            List<CommandProduct> commandProducts = new ArrayList<>();

            for (Map<String, Integer> item : items) {
                int productId = item.get("id");
                int quantity = item.get("quantity");

                CommandProduct commandProduct = new CommandProduct();
                commandProduct.setQuantity(quantity);
                commandProduct.setValid(false);
                Product product = new Product();
                product.setId(productId);
                commandProduct.setProduct(product);

                Command command = new Command();
                command.setId(1);
                commandProduct.setCommand(command);

                commandProducts.add(commandProduct);
            }

            commandProductService.updateCart(1, commandProducts);
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            logger.error("Error updating cart", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"status\":\"error\",\"message\":\"" + e.getMessage() + "\"}");
        }
    }


}