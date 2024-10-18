package com.market.marketplace.util;

import com.market.marketplace.servlet.ProductServlet;
import nz.net.ultraq.thymeleaf.layoutdialect.LayoutDialect;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
public class ThymeleafUtil {

    private static TemplateEngine templateEngine;
    private static final Logger logger = LoggerFactory.getLogger(ProductServlet.class);


    public ThymeleafUtil(ServletContext servletContext) {
            ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(servletContext);
            templateResolver.setTemplateMode("HTML5");
            templateResolver.setPrefix("/WEB-INF/templates/");
            templateResolver.setSuffix(".html");
            templateResolver.setCharacterEncoding("UTF-8");
            templateResolver.setCacheable(false);

            templateEngine = new TemplateEngine();
            templateEngine.setTemplateResolver(templateResolver);


    }

    public void returnView(WebContext context, HttpServletResponse resp, String view) {
        try {
            templateEngine.process(view, context, resp.getWriter());
        } catch (IOException e) {
            logger.error("Error processing Thymeleaf template: {}", e.getMessage(), e);
        }
    }}
