package com.market.marketplace.servlet;

import com.market.marketplace.dao.daoImpl.ProductDaoImpl;
import com.market.marketplace.entities.Admin;
import com.market.marketplace.entities.Product;
import com.market.marketplace.service.ProductService;
import com.market.marketplace.service.serviceImpl.ProductServiceImpl;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProductServlet extends HttpServlet {

    private ProductService productService;
    private TemplateEngine templateEngine;

    private static final Logger logger = LoggerFactory.getLogger(ProductServlet.class);

    @Override
    public void init() throws ServletException {
        // Initialize Thymeleaf template engine and resolver
        ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(getServletContext());
        templateResolver.setPrefix("/WEB-INF/templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode("HTML");

        templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);

        productService = new ProductServiceImpl(new ProductDaoImpl());
    }

    @Override

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        String search = req.getParameter("search");

        if (search != null && !search.isEmpty()) {
            // Reset page when searching
            req.setAttribute("currentPage", 0);
            searchProducts(req, resp, search);
        } else {
            // Handle actions like edit or delete
            if ("delete".equals((action != null ? action : ""))) {
                deleteProduct(req, resp);
            } else {
                listProducts(req, resp);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");

        if (action.equals("add")) {
            addProduct(req, resp);
        } else if (action.equals("update")) {
            updateProduct(req, resp);
        } else {
            listProducts(req, resp);
        }
    }

    // List all products
    private void listProducts(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int page = Integer.parseInt(req.getParameter("page") != null ? req.getParameter("page") : "0");
        int size = 8;
        List<Product> products = productService.getAllProducts(page, size);
        int totalProducts = productService.getTotalProductsCount();
        int totalPages = (int) Math.ceil((double) totalProducts / size);

        WebContext context = new WebContext(req, resp, getServletContext(), req.getLocale());
        context.setVariable("products", products);
        context.setVariable("currentPage", page);
        context.setVariable("totalPages", totalPages);

        // Process the template
        templateEngine.process("products", context, resp.getWriter());
        logger.info("Products fetched: " + products);
    }

    private void searchProducts(HttpServletRequest req, HttpServletResponse resp, String search) throws ServletException, IOException {
        int page = 0;
        int size = 8;
        List<Product> searchResults = productService.searchProductsByName(search, page, size);

        resp.reset();

        int totalProducts = searchResults.size();
        int totalPages = (int) Math.ceil((double) totalProducts / size);

        WebContext context = new WebContext(req, resp, getServletContext(), req.getLocale());
        context.setVariable("products", searchResults);
        context.setVariable("currentPage", page);
        context.setVariable("totalPages", totalPages);

        templateEngine.process("products", context, resp.getWriter());

        logger.info("Search results for '" + search + "': " + searchResults);
    }

    // Add a new product
    private void addProduct(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int adminId = Integer.parseInt(req.getParameter("adminId"));
        String name = req.getParameter("name");
        String description = req.getParameter("description");
        double price = Double.parseDouble(req.getParameter("price"));
        int stock = Integer.parseInt(req.getParameter("stock"));

        Admin admin = new Admin();
        admin.setId(adminId);

        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setStock(stock);
        product.setAdmin(admin);

        productService.addProduct(product);
        logger.info("Added product: " + product);

        resp.sendRedirect(req.getContextPath() + "/products");
    }


    // Update an existing product
    private void updateProduct(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        String name = req.getParameter("name");
        String description = req.getParameter("description");
        double price = Double.parseDouble(req.getParameter("price"));
        int stock = Integer.parseInt(req.getParameter("stock"));

        Product product = productService.getProductById(id);
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setStock(stock);

        logger.info("Updating product: id={}, name={}, description={}, price={}, stock={}",
                id, name, description, price, stock);

        productService.updateProduct(product);
        logger.info("Updated product: " + product);

        resp.sendRedirect(req.getContextPath() + "/products");
    }

    // Delete a product
    private void deleteProduct(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        Product product = productService.getProductById(id);
        productService.removeProduct(product);
        logger.info("Deleted product: " + product);

        resp.sendRedirect(req.getContextPath() + "/products");
    }
}
