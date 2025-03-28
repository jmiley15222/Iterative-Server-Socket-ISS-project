import java.util.*;
import java.io.*;
import java.net.*;
import java.util.concurrent.*;

public class Client {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        // Validate input arguments
        if (args.length < 2) {
            System.out.println("Usage: java MultiClientSessionClient <server IP> <port>");
            scan.close();
            return;
        }

        String address = args[0];
        int port = Integer.parseInt(args[1]);

        // Prompt for number of client sessions
        System.out.print("Enter number of client sessions (1, 5, 10, 15, 20, 25): ");
        int numSessions = scan.nextInt();
        scan.nextLine(); // Consume newline

        // Track total turnaround time
        long totalTurnaroundTime = 0;

        // Create multiple client sessions
        for (int sessionId = 1; sessionId <= numSessions; sessionId++) {
            // Measure turnaround time for each session
            long sessionStartTime = System.currentTimeMillis();

            // Create a new client session
            ClientSession clientSession = new ClientSession(address, port, sessionId);
            long sessionTurnaroundTime = clientSession.executeSession(scan);

            long sessionEndTime = System.currentTimeMillis();
            long sessionTotalTime = sessionEndTime - sessionStartTime;

            // Print session-specific information
            System.out.println("\nSession " + sessionId + " Turnaround Time: " + sessionTurnaroundTime + " ms");
            System.out.println("Session " + sessionId + " Total Execution Time: " + sessionTotalTime + " ms");

            // Accumulate total turnaround time
            totalTurnaroundTime += sessionTurnaroundTime;
        }

        // Calculate and print overall statistics
        double averageTurnaroundTime = (double) totalTurnaroundTime / numSessions;
        System.out.println("\nTotal Turnaround Time: " + totalTurnaroundTime + " ms");
        System.out.println("Average Turnaround Time: " + averageTurnaroundTime + " ms");

        scan.close();
    }

    // Represents a single client session
    static class ClientSession {
        private String serverAddress;
        private int serverPort;
        private int sessionId;

        public ClientSession(String address, int port, int sessionId) {
            this.serverAddress = address;
            this.serverPort = port;
            this.sessionId = sessionId;
        }

        public long executeSession(Scanner scan) {
            long startTime = System.currentTimeMillis();

            try (Socket socket = new Socket(serverAddress, serverPort);
                 BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                 PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)) {

                int choice = 0;
                while (choice != 7) {
                    // Print menu for this session
                    System.out.println("\nSession " + sessionId + " - Menu:");
                    System.out.println("1. Date and Time");
                    System.out.println("2. Uptime");
                    System.out.println("3. Memory Use");
                    System.out.println("4. Netstat");
                    System.out.println("5. Current Users");
                    System.out.println("6. Running Processes");
                    System.out.println("7. End Program");

                    // Get user input for this session
                    System.out.print("Enter choice for Session " + sessionId + ": ");
                    choice = scan.nextInt();
                    scan.nextLine(); // Consume newline

                    // Send choice to server
                    writer.println(choice);

                    // Read server response
                    StringBuilder responseBuilder = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        if (line.equals("END_OF_RESPONSE")) {
                            break;
                        }
                        responseBuilder.append(line).append("\n");
                    }

                    // Print response for this session
                    System.out.println("Session " + sessionId + " Response:\n" + 
                                       responseBuilder.toString().trim());

                    // Break if user chooses to end
                    if (choice == 7) {
                        break;
                    }
                }
            } catch (UnknownHostException ex) {
                System.out.println("Session " + sessionId + " - Unknown host: " + ex.getMessage());
                return 0L;
            } catch (IOException ex) {
                System.out.println("Session " + sessionId + " - I/O error: " + ex.getMessage());
                return 0L;
            }

            long endTime = System.currentTimeMillis();
            return endTime - startTime;
        }
    }
}
