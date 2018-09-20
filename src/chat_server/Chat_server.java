/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat_server;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author Joseph
 */
public class Chat_server {

    /**
     * @param args the command line arguments
     */
    
    static ServerSocket ss = null;
    
    public static void main(String[] args) {
        // TODO code application logic here
        
        try {
            ss = new ServerSocket(5190);
        }
        catch(IOException e) {System.out.println("could not create new server\n");}
        
        new ConnectionManager(ss).start();
    }
    
}

class ConnectionManager extends Thread {
    ArrayList<Client1> clients = new ArrayList();
    ServerSocket server;
    ConnectionManager(ServerSocket serv) {
        server = serv;
    }
    @Override
    public void run() {
        while (true) {
            try {
                Socket s = server.accept();
                System.out.println("connection made with: "+s.getInetAddress().toString());
                Client1 new_guy = new Client1(s);
                clients.add(new_guy);
                new_guy.start();
            }
            catch(IOException e) {System.out.println("Could not connect to new client");}
        }
    }
    
        class Client1 extends Thread {
        Socket s;
        PrintStream outgoing;
        Scanner incoming;
        String screen_name;

        Client1(Socket newClient) {
            s = newClient;
            try {
                outgoing = new PrintStream(s.getOutputStream());
                incoming = new Scanner(s.getInputStream());
            }
            catch (IOException e) {System.out.println("client streams not created.");}
        }

        @Override
        public void run() {
            String msg = incoming.nextLine();
            screen_name = msg;
            while (!msg.equalsIgnoreCase("exit")) {
                msg = incoming.nextLine();
                this.outputToClients(screen_name+": "+msg+"\r\n");
                
            }
            try {
                s.close();
            } catch(IOException e) {System.out.println("could not close socket");}
        }

        void outputToClients(String msg) {
            for (int i = 0; i < clients.size(); ++i) {
                clients.get(i).outgoing.print(msg);
            }
            
        }  
    }  
}

