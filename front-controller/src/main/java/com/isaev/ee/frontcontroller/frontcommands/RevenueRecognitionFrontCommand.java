package com.isaev.ee.frontcontroller.frontcommands;

import com.isaev.ee.frontcontroller.scripts.RevenueRecognitionCommand;
import com.isaev.ee.frontcontroller.scripts.TransactionScriptCommand;

import javax.servlet.ServletException;
import java.io.IOException;

/**
 * Calculates the revenue recognitions for a contract from front.
 *
 * @author Georgy Isaev
 */
public class RevenueRecognitionFrontCommand extends FrontCommand {

    public RevenueRecognitionFrontCommand() {
    }

    @Override
    public void process() throws ServletException, IOException {
        try {
            int contractId = Integer.parseInt(request.getParameter("contract-id"));
            TransactionScriptCommand command = new RevenueRecognitionCommand(contractId);
            command.run();
            request.setAttribute("contract-id", contractId);
            forward("revenue-recognized");
        } catch (Exception exception) {
            request.setAttribute("error-message", exception.getMessage());
            forward("unknown");
        }
    }
}