package com.denis.concurrency.mastering_concurrency.chapter_2_socket_server.client;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

import static com.denis.concurrency.mastering_concurrency.chapter_2_socket_server.common.Constants.SERIAL_PORT;

public class Client
{
    public static void main(String[] args) throws IOException {

        try(Socket socket = new Socket(InetAddress.getLocalHost(), SERIAL_PORT);
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream())))
        {
            System.out.println("Just connected to " + socket.getRemoteSocketAddress());
//            q;codCountry;codIndicator;year
            String query = "q;ARB;PA.NUS.PPP.05";
            out.writeUTF("z");
//            out.flush();
            String answer = in.readLine();
            System.out.println("answer = " + answer);
        }
    }
}
