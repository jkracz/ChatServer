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
import java.util.Scanner;

/**
 *
 * @author Joseph
 */
public class tester {
    public static void main(String[] args) {
        ServerSocket ss=null;
        try{
            ss = new ServerSocket(5190);
            int id=0;
            while(true){
                System.out.println("Waiting for a connection on port 5190");
                Socket client = ss.accept();
                
                new ProcessClient1(client,id++).start();
            }
        }
        catch (IOException e){
            System.out.println("Could not get the socket to work!");
            System.exit(1);
        }
    }
    
}

class ProcessClient1 extends Thread{
    Socket client;
    int id;
    ProcessClient1(Socket newclient, int newid){client=newclient; id=newid;}
    public void run(){
        try{
            System.out.println(id+": Got connection on port 7 from: "+client.getInetAddress().toString());
  
            PrintStream sout = new PrintStream(client.getOutputStream());
            Scanner sin = new Scanner(client.getInputStream());
            sout.print("Hello, welcome to my Echo server!\r\n");
            String line = sin.nextLine();
            while (!line.equalsIgnoreCase("exit")){
                System.out.println(id+": "+line);
                sout.print(line+"\r\n");
                line = sin.nextLine();
            }
            client.close();
        }
        catch(IOException e){}//Who cares, it was just one client!
    }
}