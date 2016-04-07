package com.denis.concurrency.mastering_concurrency.chapter_2_socket_server.serial_server;

import com.denis.concurrency.mastering_concurrency.chapter_2_socket_server.common.Command;
import com.denis.concurrency.mastering_concurrency.chapter_2_socket_server.common.Constants;
import com.denis.concurrency.mastering_concurrency.chapter_2_socket_server.common.commands.ErrorCommand;
import com.denis.concurrency.mastering_concurrency.chapter_2_socket_server.common.commands.QueryCommand;
import com.denis.concurrency.mastering_concurrency.chapter_2_socket_server.common.commands.ReportCommand;
import com.denis.concurrency.mastering_concurrency.chapter_2_socket_server.common.commands.StopCommand;
import com.denis.concurrency.mastering_concurrency.chapter_2_socket_server.dao.WDIDAO;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

import static com.denis.concurrency.mastering_concurrency.chapter_2_socket_server.common.Constants.SERIAL_PORT;

/**
 * Receives a query for a client
 * Parses and splits the elements of the query
 * Calls the corresponding command
 * Returns the results to the client
 */
public class SerialServer {

    public static void main(String[] args) throws IOException {
        WDIDAO dao = WDIDAO.getDAO();
        boolean stopServer = false;
        System.out.println("Initialization completed.");

        try (ServerSocket serverSocket = new ServerSocket(SERIAL_PORT)) {
            do {
                try (Socket clientSocket = serverSocket.accept();
                     PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
//                     BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))
                     DataInputStream in = new DataInputStream(clientSocket.getInputStream());
                )
                {
                    System.out.println("Just connected to " + clientSocket.getRemoteSocketAddress());

//                    String line = in.readLine();
                    String line = in.readUTF();
                    Command command;
                    String[] commandData = line.split(";");
                    System.out.println("Command: " + commandData[0]);
                    switch (commandData[0]) {
                        case "q":
                            System.out.println("Query");
                            command = new QueryCommand(commandData);
                            break;
                        case "r":
                            System.out.println("Report");
                            command = new ReportCommand(commandData);
                            break;
                        case "z":
                            System.out.println("Stop");
                            command = new StopCommand(commandData);
                            stopServer = true;
                            break;
                        default:
                            System.out.println("Error");
                            command = new ErrorCommand(commandData);
                    }

                    String response = command.execute();
                    System.out.println(response);
                    out.println(response);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } while (!stopServer);
        }

    }
}
