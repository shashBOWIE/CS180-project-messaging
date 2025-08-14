import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client extends JFrame implements Runnable {
    private static final String SERVER_IP = "127.0.0.1"; // Use server's IP address
    private static final int SERVER_PORT = 12345; // Use the same port as the server
    private static JFrame frame;
    private static JPanel mainPanel;
    private static JPanel loginPanel;
    @Override
    public void run() {
        try {
            Socket socket = new Socket(SERVER_IP, SERVER_PORT);
            System.out.println("Connected to server.");
            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            while (true) { // Loop indefinitely until user chooses to exit
                final String[] loginS = {""};

                frame = new JFrame("Chat Client");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                mainPanel = new JPanel(new GridBagLayout());
                loginPanel = new JPanel(new GridBagLayout());
                GridBagConstraints gbc = new GridBagConstraints();
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.gridx = 0;
                gbc.gridy = 0;
                gbc.insets = new Insets(3, 10, 3, 10);

                // Create first button
                JButton login = new JButton("Login");
                login.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        loginS[0] = showLoginPanel();
                    }
                });
                mainPanel.add(login, gbc);

                JButton createAccount = new JButton("Create Account");
                createAccount.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        loginS[0] = showCreateAccountPanel();
                        out.println(loginS[0]);
                        String response = null;
                        try {
                            response = in.readLine();
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                        System.out.println("Server response: " + response);
                    }
                });
                gbc.gridy = 1; // Move to the next row
                mainPanel.add(createAccount, gbc);

                // Add main panel to the frame
                frame.add(mainPanel);

                // Set frame size and make it visible
                frame.setSize(300, 200);
                frame.setLocationRelativeTo(null); // Center the frame on the screen
                frame.setVisible(true);

                Scanner scanner = new Scanner(System.in);
                System.out.println("Do you want to create an account? (Y/N)");
                String choice = scanner.nextLine().trim();

                if (choice.equalsIgnoreCase("Y")) {
                    // Account creation
                    System.out.println("Enter new username:");
                    String newUsername = scanner.nextLine();
                    System.out.println("Enter password:");
                    String password = scanner.nextLine();
                    System.out.println("Enter your real name:");
                    String realName = scanner.nextLine();
                    System.out.println("Enter your age:");
                    int age = Integer.parseInt(scanner.nextLine());
                    System.out.println("Enter your location:");
                    String location = scanner.nextLine();

                    // Send account creation request to server
                    out.println(loginS[0]);

                    // Receive server response
                    String response = in.readLine();
                    System.out.println("Server response: " + response);
                } else {
                    // Message sending
                    System.out.println("Enter your username:");
                    String username = scanner.nextLine();
                    System.out.println("Enter your password:");
                    String password = scanner.nextLine();
                    out.println(loginS[0]);
                    String response = in.readLine();
                    System.out.println(response); // Prints login success or failure message

                    // Check if login was successful
                    if (response.startsWith("Login successful")) {
                        boolean done = false;
                        while (!done) {
                            System.out.println("Enter command ('send' to send a message, 'logout' to log out , 'block' to block, 'unblock' to unblock, 'view' to view, 'add friend' and 'remove friend' to add and remove friends):");
                            String command = scanner.nextLine();

                            if (command.equals("send")) {
                                System.out.println("do you want to send only to friends or all? Y for yes N for All");
                                String choiceSend = scanner.nextLine().trim();
                                if(choiceSend.equalsIgnoreCase("Y")){
                                    System.out.println("Enter recipient's username:");
                                    String recipient = scanner.nextLine();
                                    System.out.println("Enter message:");
                                    String message = scanner.nextLine();
                                    out.println("**SEND_MESSAGE:" + username + ":" + recipient + ":" + message);
                                    System.out.println(in.readLine());
                                } else if (choiceSend.equalsIgnoreCase("N")) {
                                    System.out.println("Enter recipient's username:");
                                    String recipient = scanner.nextLine();
                                    System.out.println("Enter message:");
                                    String message = scanner.nextLine();
                                    out.println("SEND_MESSAGE:" + username + ":" + recipient + ":" + message);
                                    System.out.println(in.readLine());
                                }
                                //System.out.println("Enter recipient's username:");
                                //String recipient = scanner.nextLine();
                                //System.out.println("Enter message:");
                                //String message = scanner.nextLine();

                                // Send message to server
                                //out.println("SEND_MESSAGE:" + username + ":" + recipient + ":" + message);
                                //System.out.println(in.readLine()); // Server response after sending the message
                            }else if (command.equals("block")) {
                                // Block user
                                System.out.println("Enter the username you want to block:");
                                String userToBlock = scanner.nextLine();
                                out.println("BLOCK_USER:" + username + ":" + userToBlock);
                                System.out.println(in.readLine()); // Print server response
                            } else if (command.equals("unblock")) {
                                // Unblock user
                                System.out.println("Enter the username you want to unblock:");
                                String userToUnblock = scanner.nextLine();
                                out.println("UNBLOCK_USER:" + username + ":" + userToUnblock);
                                System.out.println(in.readLine()); // Print server response
                            } else if (command.equals("view")) {
                                System.out.println("Who do you want to view");
                                String viewee = scanner.nextLine();
                                System.out.println("View sent or recieved");
                                String message = scanner.nextLine();
                                if(message.equals("sent")){
                                    out.println("SENT_MESSAGE:" + username + ":" + viewee);
                                    System.out.println(in.readLine());
                                } else if(message.equals("recieved")){
                                    out.println("RECIEVED_MESSAGE:" + username + ":" + viewee);
                                    System.out.println(in.readLine());

                                }

                            } else if (command.equals("add friend")) {
                                System.out.println("Who do you want to add");
                                String newFreind= scanner.nextLine();
                                out.println("FRIENDADD:" + username + ":" + newFreind);
                                System.out.println(in.readLine());
                            }
                            else if (command.equals("remove friend")) {
                                System.out.println("Who do you want to remove");
                                String newFreind = scanner.nextLine();
                                out.println("FRIENDREMOVE:" + username + ":" + newFreind);
                                System.out.println(in.readLine());
                            }
                            else if (command.equals("logout")) {

                                done = true;
                            }
                        }
                    }
                }

                // Close the socket after each iteration

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static String showCreateAccountPanel() {
        mainPanel.setVisible(false); // Hide the main panel
        frame.remove(mainPanel); // Remove the main panel from the frame
        frame.setSize(500, 300);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(3, 10, 3, 10);

        // Create username label and field
        JLabel usernameLabel = new JLabel("Username:");
        loginPanel.add(usernameLabel, gbc);

        gbc.gridx = 1;
        JTextField usernameField = new JTextField(10);
        loginPanel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;

        // Create password label and field
        JLabel passwordLabel = new JLabel("Password:");
        loginPanel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        JPasswordField passwordField = new JPasswordField(10);
        loginPanel.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;

        JLabel realName = new JLabel("Real Name:");
        loginPanel.add(realName, gbc);

        gbc.gridx = 1;
        JTextField realNameField = new JTextField(10);
        loginPanel.add(realNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;

        JLabel age = new JLabel("Age");
        loginPanel.add(age, gbc);

        gbc.gridx = 1;
        JTextField ageField = new JTextField(10);
        loginPanel.add(ageField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;

        JLabel location = new JLabel("Location");
        loginPanel.add(location, gbc);

        gbc.gridx = 1;
        JTextField locationField = new JTextField(10);
        loginPanel.add(locationField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;



        // Create login button
        JButton loginButton = new JButton("Create");

        gbc.gridwidth = 2; // Span across two columns
        gbc.anchor = GridBagConstraints.CENTER; // Align center
        loginPanel.add(loginButton, gbc);
        final String[] rString = {""};
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Handle login action here
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                String realName = realNameField.getText();
                String age = ageField.getText();
                String location = locationField.getText();
                rString[0] = "CREATE_ACCOUNT:" + username + ":" + password + ":" + realName + ":" + age + ":" + location;

            }
        });
        loginPanel.add(loginButton, gbc);

        // Add login panel to the frame
        frame.add(loginPanel);

        frame.revalidate(); // Revalidate the frame to reflect changes
        frame.repaint(); // Repaint the frame

        return rString[0];
    }

    private static String showLoginPanel() {
        mainPanel.setVisible(false); // Hide the main panel
        frame.remove(mainPanel); // Remove the main panel from the frame

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(3, 10, 3, 10);

        // Create username label and field
        JLabel usernameLabel = new JLabel("Username:");
        loginPanel.add(usernameLabel, gbc);

        gbc.gridx = 1;
        JTextField usernameField = new JTextField(10);
        loginPanel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;

        // Create password label and field
        JLabel passwordLabel = new JLabel("Password:");
        loginPanel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        JPasswordField passwordField = new JPasswordField(10);
        loginPanel.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;

        // Create login button
        JButton loginButton = new JButton("Login");

        gbc.gridwidth = 2; // Span across two columns
        gbc.anchor = GridBagConstraints.CENTER; // Align center
        loginPanel.add(loginButton, gbc);
        final String[] result = {""};
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Handle login action here
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                result[0] = "LOGIN:" + username + ":" + password;
            }
        });
        loginPanel.add(loginButton, gbc);

        // Add login panel to the frame
        frame.add(loginPanel);

        frame.revalidate(); // Revalidate the frame to reflect changes
        frame.repaint(); // Repaint the frame
        return result[0];
    }

    public static void main(String[] args) {
        Client_it1 clientIt1 = new Client_it1(); // Create an instance of Client
        Thread thread = new Thread(clientIt1); // Create a Thread passing the Client instance
        thread.start(); // Start the thread
    }
}