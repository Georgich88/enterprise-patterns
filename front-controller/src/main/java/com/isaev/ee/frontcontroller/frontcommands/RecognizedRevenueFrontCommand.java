package com.isaev.ee.frontcontroller.frontcommands;

import com.isaev.ee.frontcontroller.scripts.RecognizedRevenueCommand;

import javax.servlet.ServletException;
import java.io.IOException;
import java.time.LocalDate;

/**
 * Calculates the revenue recognitions for a contract from front.
 *
 * @author Georgy Isaev
 */
public class RecognizedRevenueFrontCommand extends FrontCommand {

    public RecognizedRevenueFrontCommand() {
    }

    @Override
    public void process() throws ServletException, IOException {
        try {
            int contractId = Integer.parseInt(request.getParameter("contract-id"));
            var currentDate = LocalDate.parse(request.getParameter("date"));
            RecognizedRevenueCommand command = new RecognizedRevenueCommand(contractId, currentDate);
            command.run();
            request.setAttribute("amount", command.getRecognizedRevenueAmount().toString());
            forward("recognized-revenue-amount");
        } catch (Exception exception) {
            request.setAttribute("error-message", exception.getMessage());
            forward("unknown");
        }
    }
}
