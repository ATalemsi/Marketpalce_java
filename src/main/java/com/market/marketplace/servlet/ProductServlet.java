package com.market.marketplace.servlet;

import com.market.marketplace.dao.daoImpl.ProductDaoImpl;
import com.market.marketplace.entities.Admin;
import com.market.marketplace.entities.Product;
import com.market.marketplace.service.ProductService;
import com.market.marketplace.service.serviceImpl.ProductServiceImpl;
import com.market.marketplace.util.ThymeleafUtil;
import nz.net.ultraq.thymeleaf.layoutdialect.LayoutDialect;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProductServlet extends HttpServlet {

    public ProductService productService;
    public ThymeleafUtil templateEngine;

    private static final Logger logger = LoggerFactory.getLogger(ProductServlet.class);

    @Override
    public void init() throws ServletException {
         templateEngine = new ThymeleafUtil(getServletContext());

        productService = new ProductServiceImpl(new ProductDaoImpl());
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        String search = req.getParameter("search");
        String adminIdParam = req.getParameter("adminId");

        if (search != null && !search.isEmpty()) {
            // Reset page when searching
            req.setAttribute("currentPage", 0);

            if (adminIdParam != null && !adminIdParam.isEmpty()) {
                try {
                    int adminId = Integer.parseInt(adminIdParam);
                    searchProductsByAdmin(req, resp, search, adminId);
                } catch (NumberFormatException e) {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid admin ID");
                }
            } else {
                searchProducts(req, resp, search);
            }
        } else if (adminIdParam != null && !adminIdParam.isEmpty()) {
            try {
                int adminId = Integer.parseInt(adminIdParam);
                listProductsByAdmin(req, resp, adminId);
            } catch (NumberFormatException e) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid admin ID");
            }
        } else {
            if ("delete".equals((action != null ? action : ""))) {
                deleteProduct(req, resp);
            } else {
                listProducts(req, resp);
            }
        }
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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
    public void listProducts(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int page = Integer.parseInt(req.getParameter("page") != null ? req.getParameter("page") : "0");
        int size = 8;
        List<Product> products = productService.getAllProducts(page, size);

        int totalProducts = productService.getTotalProductsCount();
        int totalPages = (int) Math.ceil((double) totalProducts / size);

        WebContext context = new WebContext(req, resp, getServletContext(), req.getLocale());
        context.setVariable("products", products);
        context.setVariable("currentPage", page);
        context.setVariable("totalPages", totalPages);

        // Check if products are correctly passed to the view
        System.out.println("Products test: " + products);  // This shows products in the console

        // Pass the context directly to the view
        ThymeleafUtil templateEngine = new ThymeleafUtil(getServletContext());
        templateEngine.returnView(context, resp, "products");
        logger.info("Products fetched: " + products);
    }

    public void searchProducts(HttpServletRequest req, HttpServletResponse resp, String search) throws ServletException, IOException {
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

        templateEngine.returnView(context, resp, "products");

        logger.info("Search results for '" + search + "': " + searchResults);
    }

    // Add a new product
    public void addProduct(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        Object adminIdObj = session.getAttribute("userId");
        int adminId = Integer.parseInt(adminIdObj.toString());
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

        resp.sendRedirect(req.getContextPath() + "/products/admin?adminId=" + adminId);
    }


    // Update an existing product
    public void updateProduct(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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

        resp.sendRedirect(req.getContextPath() + "/products/admin?adminId=" + product.getAdmin().getId());
    }

    // Delete a product
    public void deleteProduct(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        Product product = productService.getProductById(id);
        productService.removeProduct(product);
        logger.info("Deleted product: " + product);
        resp.sendRedirect(req.getContextPath() + "/products/admin?adminId=" + product.getAdmin().getId());

    }


    // List products by admin
    public void listProductsByAdmin(HttpServletRequest req, HttpServletResponse resp, int adminId) throws ServletException, IOException {
        int page = Integer.parseInt(req.getParameter("page") != null ? req.getParameter("page") : "0");
        int size = 8;

        List<Product> products = productService.getAllProductsByAdmin(adminId, page, size);
        int totalProducts = productService.getTotalProductsCountByAdmin(adminId);
        int totalPages = (int) Math.ceil((double) totalProducts / size);

        WebContext context = new WebContext(req, resp, getServletContext(), req.getLocale());
        context.setVariable("products", products);
        context.setVariable("currentPage", page);
        context.setVariable("totalPages", totalPages);
        context.setVariable("adminId", adminId);

        templateEngine.returnView(context, resp, "my-products");

        logger.info("Products for admin {} fetched: {}", adminId, products);
    }


    private void searchProductsByAdmin(HttpServletRequest req, HttpServletResponse resp, String search, int adminId) throws ServletException, IOException {
        int page = 0;
        int size = 8;

        List<Product> searchResults = productService.searchProductsByNameAndAdmin(search, adminId, page, size);

        resp.reset();

        int totalProducts = searchResults.size();
        int totalPages = (int) Math.ceil((double) totalProducts / size);

        WebContext context = new WebContext(req, resp, getServletContext(), req.getLocale());
        context.setVariable("products", searchResults);
        context.setVariable("currentPage", page);
        context.setVariable("totalPages", totalPages);
        context.setVariable("adminId", adminId);

        templateEngine.returnView(context, resp, "my-products");

        logger.info("Search results for admin {} with search term '{}': {}", adminId, search, searchResults);
    }


}
