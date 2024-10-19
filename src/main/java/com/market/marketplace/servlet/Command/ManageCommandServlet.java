package com.market.marketplace.servlet.Command;

import com.market.marketplace.dao.CommandDao;
import com.market.marketplace.dao.daoImpl.CommandDaoImpl;
import com.market.marketplace.service.CommandService;
import com.market.marketplace.service.serviceImpl.CommandServiceImpl;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ManageCommandServlet extends HttpServlet {

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

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, IOException {

        String commandIdParam = req.getParameter("commandId");
        String statusParam = req.getParameter("status");

        if (commandIdParam != null && statusParam != null) {
            try {
                int commandId = Integer.parseInt(commandIdParam);

                commandService.updateCommandStatus(commandId, statusParam);

                resp.sendRedirect(req.getContextPath() + "/commands");

            } catch (Exception e) {
                e.printStackTrace();
                resp.sendRedirect(req.getContextPath() + "/error");
            }
        } else {
            resp.sendRedirect(req.getContextPath() + "/error");
        }
    }
}
