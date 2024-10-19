package com.market.marketplace.servlet.Command;

import com.market.marketplace.Utils.DateUtil;
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
import java.util.List;

public class GetAllCommandsServlet extends HttpServlet {

    private EntityManagerFactory emf;
    private CommandService commandService;

    @Override
    public void init() throws ServletException {
        // Initialize the EntityManagerFactory
        emf = Persistence.createEntityManagerFactory("marketPlace");
        EntityManager em = emf.createEntityManager();

        // Initialize CommandDAO and CommandService
        CommandDao commandDAO = new CommandDaoImpl(em) ;
        this.commandService = new CommandServiceImpl(commandDAO);
    }


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Set the content type to HTML
        response.setContentType("text/html;charset=UTF-8");

        // Retrieve the Thymeleaf TemplateEngine instance from the servlet context
        TemplateEngine templateEngine = (TemplateEngine) getServletContext().getAttribute(ThymeleafConfig.TEMPLATE_ENGINE_ATTR);

        // Create a WebContext for the current request
        WebContext context = new WebContext(request, response, getServletContext());

        // Retrieve all commands
        List<Command> commands = commandService.getAllCommandsOrderedByLatest();

        for (Command command : commands) {
            command.setOrderDateString(DateUtil.localDateToString(command.getOrderDate())); // Ajoutez un attribut temporaire si nécessaire
        }

        context.setVariable("commands", commands);

        templateEngine.process("Command/Admin_Command", context, response.getWriter());
    }
}
