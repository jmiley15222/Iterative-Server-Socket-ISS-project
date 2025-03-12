import java.util.*;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.*;
public class Server {

	public static void main(String[] args) {
		//input query for port # here
		//create server socket(port)
		if (args.length < 1) return;
		int port = Integer.parseInt(args[0]);
		 
		try (ServerSocket serverSocket = new ServerSocket(port)){
	        	System.out.println("Server now connected on port " + port);
	        	//Currently in progress...It works tho
	        	while(true) {
	    			Socket socket = serverSocket.accept();
	    			System.out.println("Client connected");
	    			
	    			OutputStream output = socket.getOutputStream();
	                PrintWriter writer = new PrintWriter(output, true);
	 
	                writer.println(new Date().toString());
	    		}//end while
		} catch(Exception io) {
			
		}
		
		

	}//end main

}//end class
