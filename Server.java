import java.util.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.time.Duration;
import java.time.Instant;

public class Server {
	private static Instant startTime;
	public static void main(String[] args) {

		//input query for port # here
		//create server socket(port)
		if (args.length < 1) return;
		int port = Integer.parseInt(args[0]);
		startTime = Instant.now();

		
		try (ServerSocket serverSocket = new ServerSocket(port)){
	        	System.out.println("Server now connected on port " + port);
	        	//Currently in progress...It works tho
	        	while(true) {
	    			try (Socket socket = serverSocket.accept();
					BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)) {
	    			System.out.println("Client connected");
	    			
                    String clientMessage;
                    while ((clientMessage = reader.readLine()) != null) {
                        int choice;
                        try {
                            choice = Integer.parseInt(clientMessage);
                        } catch (NumberFormatException e) {
                            writer.println("Invalid input. Please enter a valid command.");
                            continue;
                        }

						// Handle choices
						switch (choice) {
							case 1: 
								//shows date and time
								writer.println("Server Date and Time: " + new Date().toString());
								break;
							case 2:
								// shows how long server has been active
								writer.println("Server Uptime: " + Duration.between(startTime, Instant.now()).toSeconds() + " seconds");
								break;
							case 3:
								// shows memory use
								writer.println("Memory Usage: " + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024 + " KB");
								break;
							case 4:
								// shows list of network connections on the server
								writer.println(executeCommand(List.of("netstat", "-an")));
								break;
							case 5:
								//shows list of users currently connected to the server
								writer.println(executeCommand(List.of("who")));
								break;
							case 6:
								//shows list of programs currently running on the server
								writer.println(executeCommand(List.of("ps", "-aux")));
								break;
							case 7:
								//closes connection
								writer.println("Closing connection.");
								return;
							default:
								writer.println("Invalid choice");

						}
					}//end while
				} catch (IOException | NumberFormatException e) {
					System.out.println("Error: " + e.getMessage());
				}//end catch
	    		}//end while
		} catch(IOException ex) {
			System.out.println("Server exception: " + ex.getMessage());
			ex.printStackTrace();
		}//end catch
		
		

	}//end main

    private static String executeCommand(List<String> command) {
        StringBuilder output = new StringBuilder();
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(command);
            processBuilder.redirectErrorStream(true); 
            Process process = processBuilder.start();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                }
            }
            process.waitFor(); 
        } catch (IOException | InterruptedException e) {
            return "Error executing command: " + e.getMessage();
        }
        return output.toString();
    }

}//end class
