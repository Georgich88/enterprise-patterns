package com.isaev.ee.frontcontroller.scripts;

import java.util.LinkedList;

public class TransactionScriptInvoker {

    private LinkedList<TransactionScriptCommand> scriptCommands = new LinkedList<>();

    public TransactionScriptInvoker addCommand(TransactionScriptCommand command) {
        this.scriptCommands.add(command);
        return this;
    }

    public void runCommands() throws Exception {
        while (!scriptCommands.isEmpty()) {
            var command = scriptCommands.removeFirst();
            command.run();
        }
    }

}
