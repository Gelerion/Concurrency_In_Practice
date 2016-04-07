package com.denis.concurrency.mastering_concurrency.chapter_2_socket_server.common.commands;

import com.denis.concurrency.mastering_concurrency.chapter_2_socket_server.common.Command;

public class StopCommand extends Command {

    public StopCommand(String[] command) {
        super(command);
    }

    @Override
    public String execute() {
        return "Server stopped";
    }
}
