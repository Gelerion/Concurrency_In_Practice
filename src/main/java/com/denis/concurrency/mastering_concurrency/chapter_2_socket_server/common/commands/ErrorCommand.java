package com.denis.concurrency.mastering_concurrency.chapter_2_socket_server.common.commands;

import com.denis.concurrency.mastering_concurrency.chapter_2_socket_server.common.Command;

public class ErrorCommand extends Command {

    public ErrorCommand(String[] command) {
        super(command);
    }

    @Override
    public String execute() {
        return "Unknown command: " + command[0];
    }
}
