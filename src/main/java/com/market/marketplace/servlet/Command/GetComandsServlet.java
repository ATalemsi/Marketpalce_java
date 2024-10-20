package com.market.marketplace.servlet.Command;

import com.market.marketplace.Utils.DateUtil;
import com.market.marketplace.config.ThymeleafConfig;
import com.market.marketplace.dao.CommandDao;
import com.market.marketplace.dao.daoImpl.CommandDaoImpl;

import com.market.marketplace.entities.Command;
import com.market.marketplace.service.CommandService;
import com.market.marketplace.service.serviceImpl.CommandServiceImpl;
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
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

public class GetComandsServlet extends HttpServlet {

    private EntityManagerFactory emf;
    private CommandService commandService;
    public ThymeleafUtil templateEngine;

    @Override
    public void init() throws ServletException {
        // Initialize the EntityManagerFactory
        emf = Persistence.createEntityManagerFactory("marketPlace");
        EntityManager em = emf.createEntityManager();
        templateEngine = new ThymeleafUtil(getServletContext());

        // Initialize CommandDAO and CommandService
        CommandDao commandDAO = new CommandDaoImpl(em) ;
        this.commandService = new CommandServiceImpl(commandDAO);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Set the content type to HTML
        response.setContentType("text/html;charset=UTF-8");

    WebContext context = new WebContext(request, response, getServletContext());
        HttpSession session = request.getSession(false);

        Integer clientId = (Integer) session.getAttribute("userId");

        List<Command> commands = commandService.getCommandsForClient(clientId);

        for (Command command : commands) {
            command.setOrderDateString(DateUtil.localDateToString(command.getOrderDate())); // Ajoutez un attribut temporaire si nécessaire
        }

        context.setVariable("commands", commands);

        templateEngine.returnView( context, response,"Command/Client_Command");
    }

    @Override
    public void destroy() {
        if (emf != null) {
            emf.close(); // Close the EntityManagerFactory when the servlet is destroyed
        }
    }
}
