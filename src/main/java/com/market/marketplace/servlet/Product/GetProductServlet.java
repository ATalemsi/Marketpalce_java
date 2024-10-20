package com.market.marketplace.servlet.Product;

import com.market.marketplace.config.ThymeleafConfig;
import com.market.marketplace.dao.ProductDao;
import com.market.marketplace.dao.daoImpl.ProductDaoImpl;
import com.market.marketplace.entities.Product;
import com.market.marketplace.service.ProductService;
import com.market.marketplace.service.serviceImpl.ProductServiceImpl;
import com.market.marketplace.util.ThymeleafUtil;
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

public class GetProductServlet extends HttpServlet {

    private  ProductService productService;
    public ThymeleafUtil templateEngine;


    public GetProductServlet() {
        super();
    }

    @Override
    public void init() throws ServletException {

        ProductDao productDao = new ProductDaoImpl();
        this.productService = new ProductServiceImpl(productDao);
        templateEngine = new ThymeleafUtil(getServletContext());

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int productId = Integer.parseInt(request.getParameter("productId"));

        Product product = productService.getProductById(productId);



        WebContext context = new WebContext(request, response, request.getServletContext(), request.getLocale());
        context.setVariable("product", product);
        templateEngine.returnView( context, response , "Products/productDetails");
    }
}
