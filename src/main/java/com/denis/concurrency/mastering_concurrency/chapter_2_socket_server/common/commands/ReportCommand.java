package com.denis.concurrency.mastering_concurrency.chapter_2_socket_server.common.commands;

import com.denis.concurrency.mastering_concurrency.chapter_2_socket_server.common.Command;
import com.denis.concurrency.mastering_concurrency.chapter_2_socket_server.dao.WDIDAO;

public class ReportCommand extends Command {

    public ReportCommand(String[] command) {
        super(command);
    }

    @Override
    public String execute() {
        WDIDAO dao = WDIDAO.getDAO();
        return dao.report(command[1]);
    }
}
