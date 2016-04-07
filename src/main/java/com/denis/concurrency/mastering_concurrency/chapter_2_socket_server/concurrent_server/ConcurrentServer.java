package com.denis.concurrency.mastering_concurrency.chapter_2_socket_server.concurrent_server;

import com.denis.concurrency.mastering_concurrency.chapter_2_socket_server.common.Constants;
import com.denis.concurrency.mastering_concurrency.chapter_2_socket_server.dao.WDIDAO;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static com.denis.concurrency.mastering_concurrency.chapter_2_socket_server.common.Constants.CONCURRENT_PORT;

public class ConcurrentServer {
/*    private static ThreadPoolExecutor executor;
    private static ParallelCache cache;
    private static ServerSocket serverSocket;
    private static volatile boolean stopped = false;

    public static void main(String[] args) throws IOException {
        WDIDAO dao = WDIDAO.getDAO();
        executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        cache = new ParallelCache();
        CustomLogger.initializeLog();
        System.out.println("Initialization completed.");

        startServer();
    }

    private static void startServer() throws IOException, InterruptedException {
        serverSocket = new ServerSocket(Constants.CONCURRENT_PORT);

        do {
            try {

                Socket clientSocket = serverSocket.accept();
                RequestTask task = new RequestTask(clientSocket);
                executor.execute(task);

            } catch (IOException e) {
                e.printStackTrace();
            }
        } while (!stopped);

        executor.awaitTermination(1, TimeUnit.DAYS);
        System.out.println("Shutting down cache");
        cache.shutdown();
        System.out.println("Cache ok");
        System.out.println("Main server thread ended");
    }

    public static void shutdown() {
        stopped = true;
        System.out.println("Shutting down the server…");
        System.out.println("Shutting down executor");

        executor.shutdown();
        System.out.println("Executor ok");

        System.out.println("Closing socket");
        try {
            serverSocket.close();
            System.out.println("Socket ok");
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Shutting down logger");
        CustomLogger.sendMessage("Shutting down the logger");
        CustomLogger.shutdown();

        System.out.println("Logger ok");
    }*/

}

