package com.market.marketplace.servlet.Command;

import com.market.marketplace.config.ThymeleafConfig;
import com.market.marketplace.dao.CommandDao;
import com.market.marketplace.dao.daoImpl.CommandDaoImpl;
import com.market.marketplace.entities.Command;
import com.market.marketplace.service.CommandService;
import com.market.marketplace.service.serviceImpl.CommandServiceImpl;
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

public class ViewCommandServlet extends HttpServlet {

    private CommandService commandService;
    private TemplateEngine templateEngine;

    @Override
    public void init() throws ServletException {
        // Initialisation de l'EntityManagerFactory et du CommandService
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("marketPlace");
        EntityManager em = emf.createEntityManager();
        CommandDao commandDAO = new CommandDaoImpl(em);
        this.commandService = new CommandServiceImpl(commandDAO);

       this.templateEngine = (TemplateEngine) getServletContext().getAttribute(ThymeleafConfig.TEMPLATE_ENGINE_ATTR);

     }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {



        String commandIdStr = request.getParameter("commandId");

        if (commandIdStr != null && !commandIdStr.isEmpty()) {
            try {
                int commandId = Integer.parseInt(commandIdStr);

                Command command = commandService.findCommandById(commandId);

                if (command != null) {
                    WebContext context = new WebContext(request, response, getServletContext());

                    context.setVariable("command", command);
                    context.setVariable("products", command.getCommandProducts());

                    templateEngine.process("Command/commandDetails", context, response.getWriter());

                } else {
                    request.setAttribute("error", "Command not found.");
                    WebContext context = new WebContext(request, response, getServletContext());
                    templateEngine.process("error", context, response.getWriter());
                }
            } catch (NumberFormatException e) {
                request.setAttribute("error", "Invalid command ID.");
                WebContext context = new WebContext(request, response, getServletContext());
                templateEngine.process("error", context, response.getWriter());
            }
        } else {
            request.setAttribute("error", "No command ID provided.");
            WebContext context = new WebContext(request, response, getServletContext());
            templateEngine.process("error", context, response.getWriter());
        }
    }
}
