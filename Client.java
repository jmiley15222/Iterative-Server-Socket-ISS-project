
import java.util.*;
import java.io.*;
import java.net.*;

public class Client {
    public static void main(String[] args) throws IOException {
        Scanner scan = new Scanner(System.in);

        if (args.length < 2) {
            System.out.println("Usage: java Client <server IP> <port>");
            scan.close();
            return;
        }

        String address = args[0];
        int port = Integer.parseInt(args[1]);
        System.out.print("Enter number of client requests (1, 5, 10, 15, 20, 25): ");
        int numRequests = scan.nextInt();
        scan.nextLine(); // Consume newline

        long totalTurnaroundTime = 0;

        for (int i = 0; i < numRequests; i++) {
            long startTime = System.currentTimeMillis();
            try (Socket socket = new Socket(address, port);
                // InputStream input = socket.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)) {

                int choice = 0;
                
                while (choice != 7) {
                    // Print the menu before taking input
                    System.out.println("\nWelcome to the ISS Socket project"
                            + "\nPlease input the desired command: "
                            + "\n1. Date and Time"
                            + "\n2. Uptime"
                            + "\n3. Memory Use"
                            + "\n4. Netstat"
                            + "\n5. Current Users"
                            + "\n6. Running Processes"
                            + "\n7. End Program");
                    
                    System.out.print("Enter choice: ");
                    
                    if (scan.hasNextInt()) {
                        choice = scan.nextInt();
                        scan.nextLine(); // Consume newline
                        
                        // Send choice to server
                        writer.println(choice);
                    } else {
                        System.out.println("Invalid input, please enter a number.");
                        scan.next(); // Clear invalid input
                        continue;
                    }

                // Read response from server
                String response = reader.readLine();
                if (response != null) {
                    System.out.println(response);
                }

                if (choice == 7) {
                    System.out.println("Ending client session...");
                    break;
                }
            }

                // switch (choice) {
                //     case 1:
                //     	//needs to show date and time
                //         String currentTime = reader.readLine();
                //         System.out.println(currentTime);
                //         break;
                //     case 2:
                //     	//needs to show how long server has been active
                //         System.out.println("case2\n");
                //         break;
                //     case 3:
                //     	//shows current memory usage
                //         System.out.println("case3\n");
                //         break;
                //     case 4:
                //     	//shows list of network connections on the server
                //         System.out.println("case4\n");
                //         break;
                //     case 5:
                //     	//lists the current users currently connected to the server
                //         System.out.println("case5\n");
                //         break;
                //     case 6:
                //     	//List the programs currently running on the server
                //         System.out.println("case6\n");
                //         break;
                //     case 7:
                //     	//ends program
                //         System.out.println("case7\n");
                //         break;
                //     default:
                //         System.out.println("No such choice exists... Try again.");
                // }
            
        } catch (UnknownHostException ex) {
            System.out.println("Unknown host: " + ex.getMessage());
        } //end catch
        catch (IOException ex) {
            System.out.println("I/O error: " + ex.getMessage());
        } //end catch
        
        long endTime = System.currentTimeMillis();
        long turnaroundTime = endTime - startTime;
        totalTurnaroundTime += turnaroundTime;
        System.out.println("Request " + (i + 1) + " turnaround time: " + turnaroundTime + " ms");
    }//end for loop
    double averageTurnaroundTime = (double) totalTurnaroundTime / numRequests;
    System.out.println("Total Turnaround Time: " + totalTurnaroundTime + " ms");
    System.out.println("Average Turnaround Time: " + averageTurnaroundTime + " ms");
        
    scan.close();
    }// end main
}// end class
