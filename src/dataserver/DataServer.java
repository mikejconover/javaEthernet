/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.io.FileWriter;

/**
 * A TCP server that runs on port 9090.  When a client connects, it
 * sends the client the current date and time, then closes the
 * connection with that client.  Arguably just about the simplest
 * server you can write.
 */
public class DataServer {

    /**
     * Runs the server.
     */   
    public static void main(String[] args) throws Exception {
        ServerSocket listener = new ServerSocket(5000);
        System.out.println("Starting server application.");
        int clientNumber = 0;
        try {
            while (true) {
                new ServerThread(listener.accept(), clientNumber++).start();
            }
        } finally {
            listener.close();
        }
    }
        
    private static class ServerThread extends Thread {
        private Socket socket;
        private int clientNumber;

        public ServerThread(Socket socket, int clientNumber) {
            this.socket = socket;
            this.clientNumber = clientNumber;
            log("New connection with client# " + clientNumber + " at " + socket);
        }

        /**
         * Services this thread's client by first sending the
         * client a welcome message then repeatedly reading strings
         * and sending back the capitalized version of the string.
         */
        public void run() {
            try {
                // Decorate the streams so we can send characters
                // and not just bytes.  Ensure output is flushed
                // after every newline.
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                
                System.out.println("Sending welcome message.");
                // Send a welcome message to the client.
                out.println("Hello, you are client #" + clientNumber + ".");
                out.println("Enter a line with only a period to quit\n");
                    
                out.println(new Date().toString());
                out.println("Sent Date to client socket.");
                while(true)
                {
                    String input = in.readLine();
                    System.out.println(input);
                    FileWriter fw = new FileWriter("test.txt",true);
                    fw.write(input);
                    fw.close();
                }
            } catch (IOException e) {
                log("Error handling client# " + clientNumber + ": " + e);
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    log("Couldn't close a socket, what's going on?");
                }
                log("Connection with client# " + clientNumber + " closed");
            }
        }

        /**
         * Logs a simple message.  In this case we just write the
         * message to the server applications standard output.
         */
        private void log(String message) {
            System.out.println(message);
        }
    }    
        /*try {
            while (true) {
                Socket socket = listener.accept();
                try {
                    System.out.println("Client connected to server.");
                    PrintWriter out =
                        new PrintWriter(socket.getOutputStream(), true);
                    out.println(new Date().toString());
                    out.println("Sent Date to client socket.");
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    while(true)
                    {
                        String input = in.readLine();
                        System.out.println(input);
                        FileWriter fw = new FileWriter("test.txt",true);
                        fw.write(input);
                        fw.close();
                    }
                } finally {
                    System.out.println("Closing socket.");
                    socket.close();
                }
            }
        }
        finally {
            listener.close();
        }*/
    
}
