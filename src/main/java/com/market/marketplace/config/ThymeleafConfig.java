package com.market.marketplace.config;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

public class ThymeleafConfig {

    private static TemplateEngine templateEngine;

    static {
        // Create a template resolver
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix("/WEB-INF/templates/"); // Specify the template directory
        templateResolver.setSuffix(".html"); // Specify the template file suffix
        templateResolver.setTemplateMode("HTML5"); // Set the template mode
        templateResolver.setCharacterEncoding("UTF-8"); // Set character encoding

        templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);
    }

    public static TemplateEngine getTemplateEngine() {
        return templateEngine;
    }

}