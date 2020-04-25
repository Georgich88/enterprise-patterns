package com.isaev.ee.transactionscript.scripts;

import java.util.LinkedList;

public class TransactionScriptInvoker {

    private LinkedList<TransactionScriptCommand> scriptCommands = new LinkedList<>();

    public TransactionScriptInvoker addCommand(TransactionScriptCommand command) {
        this.scriptCommands.add(command);
        return this;
    }

    public void runCommands(){
        while (!scriptCommands.isEmpty()){
            var command = scriptCommands.removeFirst();
            command.run();
        }
    }

}
