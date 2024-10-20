package com.market.marketplace.servlet.Command;

import com.market.marketplace.dao.CommandDao;
import com.market.marketplace.dao.CommandProductDAO;
import com.market.marketplace.dao.daoImpl.CommandDaoImpl;
import com.market.marketplace.dao.daoImpl.CommandProductDAOImpl;
import com.market.marketplace.entities.Client;
import com.market.marketplace.entities.Command;
import com.market.marketplace.entities.User;
import com.market.marketplace.entities.enums.CommandStatus;
import com.market.marketplace.service.CommandProductService;
import com.market.marketplace.service.CommandService;
import com.market.marketplace.service.serviceImpl.CommandProductServiceImpl;
import com.market.marketplace.service.serviceImpl.CommandServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDate;

public class CheckoutServlet extends HttpServlet {

    final Logger logger = LoggerFactory.getLogger(CheckoutServlet.class);

    private CommandProductService commandProductService;
    private EntityManagerFactory emf;

    @Override
    public void init() throws ServletException {
        emf = Persistence.createEntityManagerFactory("marketPlace");
        EntityManager em = emf.createEntityManager();
        CommandProductDAO commandProductDAO = new CommandProductDAOImpl(em);
        this.commandProductService = new CommandProductServiceImpl(commandProductDAO);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String shippingAddress = req.getParameter("shippingAddress");
        String paymentMethod = req.getParameter("paymentMethod");

//        update client

            HttpSession session = req.getSession(false);
        if (session != null) {
            Integer commandId = (Integer) session.getAttribute("currentCommandId");


            commandProductService.confirmCommand(commandId);


            Integer clientId = (Integer) session.getAttribute("userId");
            Client client = (Client) session.getAttribute("user");
            Command defaultCommand = new Command();
            defaultCommand.setClient(client);
            defaultCommand.setStatus(CommandStatus.PENDING);
            defaultCommand.setOrderDate(LocalDate.now());


            EntityManager em = this.emf.createEntityManager();
            CommandDao commandProductDAO = new CommandDaoImpl(em);
            CommandService commandService = new CommandServiceImpl(commandProductDAO);
            commandService.saveCommand(defaultCommand);
            session.setAttribute("currentCommandId", commandService.findLastCommandByClientId(clientId).getId());
        }
            resp.sendRedirect(req.getContextPath() + "/Commands");

    }


}
