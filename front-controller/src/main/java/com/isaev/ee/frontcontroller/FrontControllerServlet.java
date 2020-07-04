package com.isaev.ee.frontcontroller;

import com.isaev.ee.frontcontroller.frontcommands.FrontCommand;
import com.isaev.ee.frontcontroller.frontcommands.UnknownFrontCommand;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class FrontControllerServlet extends HttpServlet {
    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {
        FrontCommand command = getCommand(request);
        command.init(getServletContext(), request, response);
        command.process();
    }

    private FrontCommand getCommand(HttpServletRequest request) {
        try {
            Class type = Class.forName(
                    String.format(
                            "com.isaev.ee.frontcontroller.frontcommands.%sFrontCommand",
                            request.getParameter("command")
                    )
            );
            return (FrontCommand) type
                    .asSubclass(FrontCommand.class)
                    .newInstance();
        } catch (Exception e) {
            return new UnknownFrontCommand(e.getMessage());
        }
    }
}
