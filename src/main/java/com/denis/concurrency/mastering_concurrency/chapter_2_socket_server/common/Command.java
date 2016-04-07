package com.denis.concurrency.mastering_concurrency.chapter_2_socket_server.common;

/**
 * The commands part is an intermediary between the DAO and the server parts.
 */
public abstract class Command {
    protected String[] command;

    public Command(String[] command) {
        this.command = command;
    }

    public abstract String execute();
}
