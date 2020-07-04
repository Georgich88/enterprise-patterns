package com.isaev.ee.frontcontroller.frontcommands;

import javax.servlet.ServletException;
import java.io.IOException;

public class UnknownFrontCommand extends FrontCommand {

    private String errorMessage = "";

    public UnknownFrontCommand() {
    }

    public UnknownFrontCommand(String message) {
        this.errorMessage = message;
    }

    @Override
    public void process() throws ServletException, IOException {
        request.setAttribute("error-message", errorMessage);
        forward("unknown");
    }
}
