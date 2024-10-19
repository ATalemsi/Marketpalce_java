package com.market.marketplace.servlet.Command;

import com.google.gson.Gson;
import com.market.marketplace.dao.CommandDao;
import com.market.marketplace.dao.daoImpl.CommandDaoImpl;
import com.market.marketplace.entities.Command;
import com.market.marketplace.service.CommandService;
import com.market.marketplace.service.serviceImpl.CommandServiceImpl;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SearchCommandServlet extends HttpServlet {

    private EntityManagerFactory emf;
    private CommandService commandService;

    @Override
    public void init() throws ServletException {
        // Initialize the EntityManagerFactory
        emf = Persistence.createEntityManagerFactory("marketPlace");
        EntityManager em = emf.createEntityManager();

        // Initialize CommandDAO and CommandService
        CommandDao commandDAO = new CommandDaoImpl(em);
        this.commandService = new CommandServiceImpl(commandDAO);
    }

//    @Override
//    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        String idParam = request.getParameter("id");
//        String statusParam = request.getParameter("status");
//
//        System.out.println("sdkljfjfffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff");
//        List<Command> commands;
//        if (idParam != null) {
//            int id = Integer.parseInt(idParam);
//            commands = commandService.findCommandBySearch(id, null);
//        } else if (statusParam != null) {
//            commands = commandService.findCommandBySearch(null, statusParam);
//        } else {
//            commands = new ArrayList<>(); // Return an empty list if no parameters are provided
//        }
//        System.out.println(commands);
//        request.setAttribute("commands", commands);
//        RequestDispatcher dispatcher = request.getRequestDispatcher("/searchResults.jsp");
//        dispatcher.forward(request, response);
//    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idParam = request.getParameter("id");
        String statusParam = request.getParameter("status");

        List<Command> commands;
        if (idParam != null) {
            int id = Integer.parseInt(idParam);
            commands = commandService.findCommandBySearch(id, null);
        } else if (statusParam != null) {
            commands = commandService.findCommandBySearch(null, statusParam);
        } else {
            commands = new ArrayList<>(); // Retourne une liste vide si aucun paramètre n'est fourni
        }
        System.out.println(commands);
        // Définissez le type de contenu sur JSON
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // Convertissez les commandes en JSON et envoyez-les dans la réponse
        String json = new Gson().toJson(commands);
        response.getWriter().write(json);
    }


}
