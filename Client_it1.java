import java.awt.event.ItemEvent;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Client_it1 implements Runnable {
    private static final String SERVER_IP = "127.0.0.1"; // Use server's IP address
    private static final int SERVER_PORT = 12345; // Use the same port as the server
    private static JFrame frame;
    private static JPanel mainPanel;
    private static JPanel loginPanel;
    private static JPanel createAccountPanel;
    private static JPanel sendMessageMainPanel;
    private static JPanel loginFailedPanel;
    private static JPanel userManagementPanel;
    private static JPanel sendMessagePanel;
    private static JPanel viewMessagePanel;


    @Override
    public void run() {

        frame = new JFrame("ALI Systems");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        mainPanel = new JPanel(new GridBagLayout());
        loginPanel = new JPanel(new GridBagLayout());
        createAccountPanel = new JPanel(new GridBagLayout());
        loginFailedPanel = new JPanel(new GridBagLayout());
        userManagementPanel = new JPanel();
        sendMessageMainPanel = new JPanel();
        sendMessagePanel = new JPanel();
        viewMessagePanel = new JPanel();

        launch();

    }

    private static void launch() {
        try {
            Socket socket = new Socket(SERVER_IP, SERVER_PORT);
            //viewMessageInterface(socket, "cat");
            mainInterface(socket);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void mainInterface(Socket socket) {
        mainPanel = new JPanel(new GridBagLayout());
        frame.remove(sendMessageMainPanel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(3, 10, 3, 10);

        // Create first button
        JButton login = new JButton("Login");
        login.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showLoginPanel(socket);
            }
        });
        mainPanel.add(login, gbc);

        JButton createAccount = new JButton("Create Account");
        createAccount.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showCreateAccountPanel(socket);
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
    }

    private static void showLoginPanel(Socket socket) {
        loginPanel = new JPanel(new GridBagLayout());
        try {
            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            mainPanel.setVisible(false);// Hide the main panel
            frame.remove(mainPanel); // Remove the main panel from the frame
            loginFailedPanel.setVisible(false);// Hide the login
            frame.remove(loginFailedPanel); // Remove the login panel from

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
            loginButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    // Handle login action here
                    String username = usernameField.getText();
                    String password = new String(passwordField.getPassword());
                    String response = "";
                    if (username.isBlank() || password.isBlank()) {
                        showLoginFailed(socket, 2);
                        return;
                    } else {
                        out.println("LOGIN:" + username + ":" + password);
                        try {
                            response = in.readLine();
                        } catch (IOException ex) {

                        }
                        if (response.startsWith("Login successful")) {
                            sendMessageMainInterface(socket, username);
                        } else {
                            showLoginFailed(socket, 0);
                        }
                    }
                }
            });
            loginPanel.add(loginButton, gbc);

            // Add login panel to the frame
            frame.add(loginPanel);
            frame.revalidate(); // Revalidate the frame to reflect changes
            frame.repaint();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void showLoginFailed(Socket socket, int prevPage) {
        loginFailedPanel = new JPanel(new GridBagLayout());
        final String[] prePageActual = {String.valueOf(prevPage)};
        frame.remove(mainPanel);
        frame.remove(loginPanel);
        frame.remove(createAccountPanel);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(3, 10, 3, 10);

        JLabel failed = new JLabel();
        if (prePageActual[0].equals("2"))
            failed.setText("Username or Password Blank");
        else if (prePageActual[0].equals("1"))
            failed.setText("User Creation Failed");
        else
            failed.setText("Username or Password Incorrect");
        loginFailedPanel.add(failed, gbc);

        JButton login = new JButton("Retry");
        login.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (prePageActual[0].equals("1"))
                    showCreateAccountPanel(socket);
                else
                    showLoginPanel(socket);
            }
        });
        gbc.gridy = 1;
        loginFailedPanel.add(login, gbc);

        JButton createAccount = new JButton();
        if (prevPage == 0)
            createAccount.setText("Login");

        else
            createAccount.setText("Create Account");


        createAccount.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (prePageActual[0].equals("1"))
                    showLoginPanel(socket);
                else
                    showCreateAccountPanel(socket);
            }
        });
        gbc.gridy = 2; // Move to the next row
        loginFailedPanel.add(createAccount, gbc);
        frame.add(loginFailedPanel);
        frame.setLocationRelativeTo(null); // Center the frame on the screen
        frame.setVisible(true);
    }

    private static void showCreateAccountPanel(Socket socket) {

        try {
            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            createAccountPanel = new JPanel(new GridBagLayout());
            mainPanel.setVisible(false); // Hide the main panel
            frame.remove(mainPanel); // Remove the main panel from the frame
            loginFailedPanel.setVisible(false);// Hide the login
            frame.remove(loginFailedPanel); // Remove the login panel from
            frame.setSize(500, 300);
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.insets = new Insets(3, 10, 3, 10);

            // Create username label and field
            JLabel usernameLabel = new JLabel("Username:");
            createAccountPanel.add(usernameLabel, gbc);

            gbc.gridx = 1;
            JTextField usernameField = new JTextField(10);
            createAccountPanel.add(usernameField, gbc);

            gbc.gridx = 0;
            gbc.gridy = 1;

            // Create password label and field
            JLabel passwordLabel = new JLabel("Password:");
            createAccountPanel.add(passwordLabel, gbc);

            gbc.gridx = 1;
            JPasswordField passwordField = new JPasswordField(10);
            createAccountPanel.add(passwordField, gbc);

            gbc.gridx = 0;
            gbc.gridy = 2;

            JLabel realName = new JLabel("Real Name:");
            createAccountPanel.add(realName, gbc);

            gbc.gridx = 1;
            JTextField realNameField = new JTextField(10);
            createAccountPanel.add(realNameField, gbc);

            gbc.gridx = 0;
            gbc.gridy = 3;

            JLabel age = new JLabel("Age");
            createAccountPanel.add(age, gbc);

            gbc.gridx = 1;
            JTextField ageField = new JTextField(10);
            createAccountPanel.add(ageField, gbc);

            gbc.gridx = 0;
            gbc.gridy = 4;

            JLabel location = new JLabel("Location");
            createAccountPanel.add(location, gbc);

            gbc.gridx = 1;
            JTextField locationField = new JTextField(10);
            createAccountPanel.add(locationField, gbc);

            gbc.gridx = 0;
            gbc.gridy = 5;


            // Create login button
            JButton loginButton = new JButton("Create");

            gbc.gridwidth = 2; // Span across two columns
            gbc.anchor = GridBagConstraints.CENTER; // Align center
            createAccountPanel.add(loginButton, gbc);
            loginButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {

                    // Handle login action here

                    String username = usernameField.getText();
                    String password = new String(passwordField.getPassword());
                    String realName = realNameField.getText();
                    String age = ageField.getText();
                    String location = locationField.getText();
                    String response = "";

                    if (age.matches("\\D")) {
                        JOptionPane.showMessageDialog(frame, "Age must be a number");
                        return;
                    } else if (username.isBlank()) {
                        JOptionPane.showMessageDialog(frame, "Username is blank");
                        return;
                    } else if (password.isBlank()) {
                        JOptionPane.showMessageDialog(frame, "Password is blank");
                        return;
                    } else if (realName.isBlank()) {
                        JOptionPane.showMessageDialog(frame, "Real Name is blank");
                        return;
                    } else if (age.isBlank()) {
                        JOptionPane.showMessageDialog(frame, "Age is blank");
                        return;
                    } else if (location.isBlank()) {
                        JOptionPane.showMessageDialog(frame, "Location is blank");
                        return;
                    } else {
                        out.println("CREATE_ACCOUNT:" + username + ":" + password + ":" + realName + ":" + age + ":" +
                                location);
                        try {
                            response = in.readLine();
                        } catch (IOException ex) {

                        }
                        if (response.startsWith("Account created for")) {
                            out.println("LOGIN:" + username + ":" + password);
                            sendMessageMainInterface(socket, username);
                            try {
                                in.readLine();
                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            }
                        } else {
                            showLoginFailed(socket, 1);
                        }
                    }


                }
            });
            createAccountPanel.add(loginButton, gbc);

            // Add login panel to the frame
            frame.add(createAccountPanel);

            frame.revalidate(); // Revalidate the frame to reflect changes
            frame.repaint(); // Repaint the frame
        } catch (IOException e) {
            ;
        }
    }

    public static void sendMessageMainInterface(Socket socket, String activeUser) {
        sendMessageMainPanel = new JPanel();
        mainPanel.setVisible(false); // Hide the main panel
        frame.remove(sendMessageMainPanel);
        frame.remove(mainPanel);
        frame.remove(loginPanel);
        frame.remove(createAccountPanel);
        frame.remove(userManagementPanel);

        JLabel loggedInUser = new JLabel();
        loggedInUser.setText("Current User: " + activeUser);

        JButton sendMessage = new JButton();
        sendMessage.setText("Send Message");
        sendMessage.setAlignmentY(0.0F);

        sendMessage.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.remove(sendMessageMainPanel);
                sendMessage(socket, activeUser);
            }
        });

        JButton ViewMessage = new JButton();
        ViewMessage.setText("View Message");
        ViewMessage.setAlignmentY(0.0F);

        ViewMessage.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.remove(sendMessageMainPanel);
                viewMessageInterface(socket, activeUser);
            }
        });

        JButton userManagement = new JButton();
        userManagement.setText("User Management");
        userManagement.setAlignmentY(0.0F);

        userManagement.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.remove(sendMessageMainPanel);
                userManagementInterface(socket, activeUser);
            }
        });

        JButton logout = new JButton();
        logout.setText("Logout");
        logout.setAlignmentY(0.0F);

        logout.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.remove(sendMessageMainPanel);
                mainInterface(socket);
                mainPanel.setVisible(true);
            }
        });
        GroupLayout messageLayout = new GroupLayout(sendMessageMainPanel);
        sendMessageMainPanel.setLayout(messageLayout);

        messageLayout.setHorizontalGroup(
                messageLayout.createParallelGroup()
                        .addGroup(messageLayout.createSequentialGroup()
                                .addGroup(messageLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                        .addGroup(GroupLayout.Alignment.LEADING,
                                                messageLayout.createSequentialGroup()
                                                        .addGap(40, 40, 40)
                                                        .addComponent(loggedInUser, GroupLayout.PREFERRED_SIZE, 150,
                                                                GroupLayout.PREFERRED_SIZE)
                                                        .addGap(0, 0, Short.MAX_VALUE))
                                        .addGroup(messageLayout.createSequentialGroup()
                                                .addContainerGap(226, Short.MAX_VALUE)
                                                .addGroup(messageLayout.createParallelGroup(
                                                                GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(sendMessage, GroupLayout.DEFAULT_SIZE, 150,
                                                                Short.MAX_VALUE)
                                                        .addComponent(ViewMessage, GroupLayout.DEFAULT_SIZE, 150,
                                                                Short.MAX_VALUE)
                                                        .addComponent(userManagement, GroupLayout.DEFAULT_SIZE, 150,
                                                                Short.MAX_VALUE))))
                                .addContainerGap(220, Short.MAX_VALUE))
                        .addGroup(GroupLayout.Alignment.TRAILING, messageLayout.createSequentialGroup()
                                .addGap(0, 406, Short.MAX_VALUE)
                                .addComponent(logout, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE)
                                .addGap(20, 20, 20))

        );
        messageLayout.setVerticalGroup(
                messageLayout.createParallelGroup()
                        .addGroup(messageLayout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addComponent(loggedInUser, GroupLayout.PREFERRED_SIZE, 30,
                                        GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 68, Short.MAX_VALUE)
                                .addComponent(sendMessage, GroupLayout.PREFERRED_SIZE, 30,
                                        GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(ViewMessage, GroupLayout.PREFERRED_SIZE, 30,
                                        GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(userManagement, GroupLayout.PREFERRED_SIZE, 30,
                                        GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 103, Short.MAX_VALUE)
                                .addComponent(logout, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                                        GroupLayout.PREFERRED_SIZE)
                                .addGap(20, 20, 20))

        );
        frame.setSize(600, 400);
        frame.add(sendMessageMainPanel);
        frame.setLocationRelativeTo(null); // Center the frame on the screen
        frame.setVisible(true);
    }

    public static void userManagementInterface(Socket socket, String activeUser) {
        try {
            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            userManagementPanel = new JPanel();
            mainPanel.setVisible(false); // Hide the main panel
            frame.remove(mainPanel);
            frame.remove(sendMessageMainPanel);

            JLabel confirmation = new JLabel();

            JLabel loggedInUser = new JLabel();
            loggedInUser.setText("Current User:" + activeUser);

            JLabel usernamePrompt = new JLabel();
            usernamePrompt.setText("Username:");

            JTextField username = new JTextField();


            JButton block = new JButton();
            block.setText("Block");
            block.setAlignmentY(0.0F);
            final String[] user = {""};
            block.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    user[0] = username.getText();
                    if (user[0].isEmpty()) {
                        confirmation.setText("Please enter a username");
                        frame.revalidate();
                        frame.repaint();
                        return;
                    } else {
                        user[0] = username.getText();
                        out.println("BLOCK_USER:" + activeUser + ":" + user[0]);
                        try {
                            confirmation.setText(in.readLine());
                            frame.revalidate();
                            frame.repaint();
                        } catch (IOException ex) {
                        }
                    }

                }
            });

            JButton unblock = new JButton();
            unblock.setText("Unblock");
            unblock.setAlignmentY(0.0F);

            unblock.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {

                    user[0] = username.getText();
                    if (user[0].isEmpty()) {
                        confirmation.setText("Please enter a username");
                        frame.revalidate();
                        frame.repaint();
                        return;
                    } else {
                        user[0] = username.getText();
                        out.println("UNBLOCK_USER:" + activeUser + ":" + user[0]);
                        try {
                            confirmation.setText(in.readLine());
                            frame.revalidate();
                            frame.repaint();
                        } catch (IOException ex) {
                        }
                    }

                }
            });

            JButton addFriend = new JButton();
            addFriend.setText("Add Friend");
            addFriend.setAlignmentY(0.0F);

            addFriend.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    user[0] = username.getText();
                    if (user[0].isEmpty()) {
                        confirmation.setText("Please enter a username");
                        frame.revalidate();
                        frame.repaint();
                        return;
                    } else {
                        user[0] = username.getText();
                        out.println("FRIENDADD:" + activeUser + ":" + user[0]);
                        try {
                            confirmation.setText(in.readLine());
                            frame.revalidate();
                            frame.repaint();
                        } catch (IOException ex) {
                        }
                    }

                }
            });

            JButton removeFriend = new JButton();
            removeFriend.setText("Remove Friend");
            removeFriend.setAlignmentY(0.0F);

            removeFriend.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {

                    user[0] = username.getText();
                    if (user[0].isEmpty()) {
                        confirmation.setText("Please enter a username");
                        frame.revalidate();
                        frame.repaint();
                        return;
                    } else {
                        user[0] = username.getText();
                        out.println("FRIENDREMOVE:" + activeUser + ":" + user[0]);
                        try {
                            confirmation.setText(in.readLine());
                            frame.revalidate();
                            frame.repaint();
                        } catch (IOException ex) {
                        }
                    }

                }
            });

            JButton logout = new JButton();
            logout.setText("Logout");
            logout.setAlignmentY(0.0F);
            logout.setMaximumSize(new

                    Dimension(169, 30));
            logout.setMinimumSize(new

                    Dimension(169, 30));
            logout.setPreferredSize(new

                    Dimension(169, 30));

            logout.addActionListener(new

                                             ActionListener() {
                                                 public void actionPerformed(ActionEvent e) {
                                                     frame.remove(userManagementPanel);
                                                     frame.revalidate();
                                                     frame.repaint();
                                                     mainInterface(socket);
                                                 }
                                             });

            JButton back = new JButton();
            back.setText("Back");
            back.setAlignmentY(0.0F);
            back.setMaximumSize(new

                    Dimension(169, 30));
            back.setMinimumSize(new

                    Dimension(169, 30));
            back.setPreferredSize(new

                    Dimension(169, 30));

            back.addActionListener(new

                                           ActionListener() {
                                               public void actionPerformed(ActionEvent e) {
                                                   frame.remove(userManagementPanel);
                                                   frame.revalidate();
                                                   frame.repaint();
                                                   sendMessageMainInterface(socket, activeUser);
                                               }
                                           });

            GroupLayout managementLayout = new GroupLayout(userManagementPanel);
            userManagementPanel.setLayout(managementLayout);

            managementLayout.setHorizontalGroup(
                    managementLayout.createParallelGroup()
                                    .

                            addGroup(managementLayout.createSequentialGroup()
                                            .

                                    addGroup(managementLayout.createParallelGroup()
                                                    .

                                            addGroup(managementLayout.createSequentialGroup()
                                                            .

                                                    addGap(40, 40, 40)
                                                            .

                                                    addComponent(loggedInUser, GroupLayout.PREFERRED_SIZE, 150,
                                                            GroupLayout.PREFERRED_SIZE)
                                                            .

                                                    addPreferredGap(LayoutStyle.ComponentPlacement.RELATED,
                                                            GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                    .

                                            addGroup(GroupLayout.Alignment.TRAILING,
                                                    managementLayout.createSequentialGroup()
                                                                    .

                                                            addContainerGap(145, Short.MAX_VALUE)
                                                                    .

                                                            addComponent(usernamePrompt, GroupLayout.PREFERRED_SIZE,
                                                                    80,
                                                                    GroupLayout.PREFERRED_SIZE)))
                                            .

                                    addGroup(
                                            managementLayout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
                                                            .

                                                    addComponent(confirmation, GroupLayout.DEFAULT_SIZE, 150,
                                                            Short.MAX_VALUE)
                                                            .

                                                    addComponent(removeFriend, GroupLayout.DEFAULT_SIZE, 150,
                                                            Short.MAX_VALUE)
                                                            .

                                                    addComponent(addFriend, GroupLayout.Alignment.LEADING,
                                                            GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
                                                            .

                                                    addComponent(unblock, GroupLayout.Alignment.LEADING,
                                                            GroupLayout.DEFAULT_SIZE,
                                                            GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                            .

                                                    addComponent(block, GroupLayout.Alignment.LEADING,
                                                            GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
                                                            Short.MAX_VALUE)
                                                            .

                                                    addComponent(username, GroupLayout.Alignment.LEADING,
                                                            GroupLayout.DEFAULT_SIZE,
                                                            150, Short.MAX_VALUE))
                                            .

                                    addContainerGap(225, Short.MAX_VALUE))
                                    .

                            addGroup(GroupLayout.Alignment.TRAILING, managementLayout.createSequentialGroup()
                                            .

                                    addGap(0, 500, Short.MAX_VALUE)
                                            .

                                    addGroup(managementLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                                    .

                                            addComponent(logout, GroupLayout.PREFERRED_SIZE, 80,
                                                    GroupLayout.PREFERRED_SIZE)
                                                    .

                                            addComponent(back, GroupLayout.PREFERRED_SIZE, 80,
                                                    GroupLayout.PREFERRED_SIZE))
                                            .

                                    addGap(20, 20, 20))
            );
            managementLayout.setVerticalGroup(
                    managementLayout.createParallelGroup()
                                    .

                            addGroup(managementLayout.createSequentialGroup()
                                            .

                                    addGap(20, 20, 20)
                                            .

                                    addComponent(loggedInUser, GroupLayout.PREFERRED_SIZE, 30,
                                            GroupLayout.PREFERRED_SIZE)
                                            .

                                    addGap(47, 47, 47)
                                            .

                                    addGroup(managementLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                    .

                                            addComponent(username, GroupLayout.PREFERRED_SIZE,
                                                    GroupLayout.DEFAULT_SIZE,
                                                    GroupLayout.PREFERRED_SIZE)
                                                    .

                                            addComponent(usernamePrompt, GroupLayout.PREFERRED_SIZE, 30,
                                                    GroupLayout.PREFERRED_SIZE))
                                            .

                                    addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 16, Short.MAX_VALUE)
                                            .

                                    addComponent(block, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
                                            .

                                    addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                            .

                                    addComponent(unblock, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
                                            .

                                    addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                            .

                                    addComponent(addFriend, GroupLayout.PREFERRED_SIZE, 30,
                                            GroupLayout.PREFERRED_SIZE)
                                            .

                                    addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                            .

                                    addComponent(removeFriend, GroupLayout.PREFERRED_SIZE, 30,
                                            GroupLayout.PREFERRED_SIZE)
                                            .

                                    addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 27, Short.MAX_VALUE)
                                            .

                                    addComponent(confirmation, GroupLayout.PREFERRED_SIZE, 30,
                                            GroupLayout.PREFERRED_SIZE)
                                            .

                                    addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE,
                                            Short.MAX_VALUE)
                                            .

                                    addComponent(back, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
                                            .

                                    addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                            .

                                    addComponent(logout, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                                            GroupLayout.PREFERRED_SIZE)
                                            .

                                    addGap(20, 20, 20))
            );

            // Add main panel to the frame
            frame.add(userManagementPanel);

            // Set frame size and make it visible
            frame.setSize(600, 450);
            frame.setLocationRelativeTo(null); // Center the frame on the screen
            frame.setVisible(true);
        } catch (IOException e) {

        }

    }

    public static void sendMessage(Socket socket, String activeUser) {
        final String[] sendMessageHeader = {"SEND_MESSAGE:"};
        try {
            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            frame.remove(viewMessagePanel);
            frame.remove(sendMessageMainPanel);
            sendMessagePanel = new JPanel();
            JLabel loggedInUser = new JLabel();
            loggedInUser.setText("Current User: " + activeUser);

            JLabel recipient = new JLabel();
            recipient.setText("Recipient:");

            JTextField recipientActual = new JTextField();

            JTextField userMessage = new JTextField();


            JButton send = new JButton();
            send.setText("Send");
            send.setAlignmentY(0.0F);
            send.setMaximumSize(new Dimension(169, 30));
            send.setMinimumSize(new Dimension(169, 30));
            send.setPreferredSize(new Dimension(169, 30));

            JLabel serverReturn = new JLabel();

            send.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    String recipientName = recipientActual.getText();
                    String userMessageText = userMessage.getText();
                    String clientOut =
                            sendMessageHeader[0] + activeUser + ":" + recipientName + ":" + userMessageText;
                    out.println(clientOut);
                    try {
                        serverReturn.setText(in.readLine());
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            });

            JButton viewMessage = new JButton();
            viewMessage.setText("View Messages");
            viewMessage.setAlignmentY(0.0F);
            viewMessage.setMaximumSize(new Dimension(169, 30));
            viewMessage.setMinimumSize(new Dimension(169, 30));
            viewMessage.setPreferredSize(new Dimension(169, 30));

            viewMessage.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    frame.remove(sendMessagePanel);
                    viewMessageInterface(socket, activeUser);
                }
            });

            JButton userManagement = new JButton();
            userManagement.setText("User Management");
            userManagement.setAlignmentY(0.0F);
            userManagement.setMaximumSize(new Dimension(169, 30));
            userManagement.setMinimumSize(new Dimension(169, 30));
            userManagement.setPreferredSize(new Dimension(169, 30));

            userManagement.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    frame.remove(sendMessagePanel);
                    userManagementInterface(socket, activeUser);
                }
            });
            JLabel friendsOnlyPrompt = new JLabel();
            friendsOnlyPrompt.setText("Friends Only:");

            JToggleButton friendsOnly = new JToggleButton("No");
            friendsOnly.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (friendsOnly.isSelected()) {
                        friendsOnly.setText("Yes");
                        sendMessageHeader[0] = "**SEND_MESSAGE:";
                    } else {
                        friendsOnly.setText("No");
                        sendMessageHeader[0] = "SEND_MESSAGE:";
                    }
                }
            });


            GroupLayout layout = new GroupLayout(sendMessagePanel);
            sendMessagePanel.setLayout(layout);
//            layout.setHorizontalGroup(
//                    layout.createParallelGroup()
//                            .addGroup(layout.createSequentialGroup()
//                                    .addGap(40, 40, 40)
//                                    .addGroup(layout.createParallelGroup()
//                                            .addGroup(layout.createSequentialGroup()
//                                                    .addComponent(userMessage, GroupLayout.PREFERRED_SIZE, 370,
//                                                            GroupLayout.PREFERRED_SIZE)
//                                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
//                                                    .addGroup(layout.createParallelGroup()
//                                                            .addGroup(layout.createSequentialGroup()
//                                                                    .addGap(2, 2, 2)
//                                                                    .addComponent(friendsOnlyPrompt,
//                                                                            GroupLayout.DEFAULT_SIZE,
//                                                                            GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
//                                                                    .addGap(4, 4, 4)
//                                                                    .addComponent(friendsOnly,
//                                                                            GroupLayout.PREFERRED_SIZE,
//                                                                            GroupLayout.DEFAULT_SIZE,
//                                                                            Short.MAX_VALUE))
//                                                            .addComponent(userManagement, GroupLayout.DEFAULT_SIZE,
//                                                                    GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
//                                                            .addComponent(viewMessage, GroupLayout.DEFAULT_SIZE,
//                                                                    GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
//                                                            .addComponent(serverReturn, GroupLayout.DEFAULT_SIZE,
//                                                                    GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
//                                                            .addComponent(back, GroupLayout.DEFAULT_SIZE,
//                                                                    GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
//                                            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
//                                                    .addComponent(loggedInUser, GroupLayout.Alignment.LEADING,
//                                                            GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
//                                                            Short.MAX_VALUE)
//                                                    .addGroup(layout.createSequentialGroup()
//                                                            .addComponent(recipient)
//                                                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
//                                                            .addComponent(userMessage, GroupLayout.PREFERRED_SIZE, 100,
//                                                                    GroupLayout.PREFERRED_SIZE))))
//                                    .addGap(116, 116, 116))
//            );
//            layout.setVerticalGroup(
//                    layout.createParallelGroup()
//                            .addGroup(layout.createSequentialGroup()
//                                    .addGap(20, 20, 20)
//                                    .addComponent(loggedInUser, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
//                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
//                                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
//                                            .addComponent(recipient, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
//                                            .addComponent(userMessage, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
//                                                    GroupLayout.PREFERRED_SIZE))
//                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
//                                    .addGroup(layout.createParallelGroup()
//                                            .addComponent(userMessage, GroupLayout.DEFAULT_SIZE, 292,
//                                                    GroupLayout.DEFAULT_SIZE )
//                                            .addGroup(layout.createSequentialGroup()
//                                                    .addComponent(viewMessage, GroupLayout.PREFERRED_SIZE,
//                                                            GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
//                                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
//                                                    .addComponent(userManagement, GroupLayout.PREFERRED_SIZE,
//                                                            GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
//                                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
//                                                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
//                                                            .addComponent(friendsOnlyPrompt)
//                                                            .addComponent(friendsOnly))
//                                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 119,
//                                                            Short.MAX_VALUE)
//                                                    .addComponent(serverReturn, GroupLayout.PREFERRED_SIZE, 30,
//                                                            GroupLayout.PREFERRED_SIZE)
//                                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
//                                                    .addComponent(back, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
//                                                            GroupLayout.PREFERRED_SIZE)))
//                                    .addGap(20, 20, 20))
//            );
            layout.setHorizontalGroup(
                    layout.createParallelGroup()
                            .addGroup(layout.createSequentialGroup()
                                    .addGap(40)
                                    .addGroup(layout.createParallelGroup()
                                            .addGroup(layout.createSequentialGroup()
                                                    .addComponent(userMessage, GroupLayout.PREFERRED_SIZE, 340,
                                                            GroupLayout.PREFERRED_SIZE)
                                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                    .addGroup(layout.createParallelGroup()
                                                            .addGroup(layout.createSequentialGroup()
                                                                    .addGap(2, 2, 2)
                                                                    .addComponent(friendsOnlyPrompt)
                                                                    .addGap(4, 4, 4)
                                                                    .addComponent(friendsOnly,
                                                                            GroupLayout.DEFAULT_SIZE,
                                                                            GroupLayout.DEFAULT_SIZE,
                                                                            Short.MAX_VALUE))
                                                            .addComponent(userManagement, GroupLayout.DEFAULT_SIZE,
                                                                    GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                            .addComponent(viewMessage, GroupLayout.DEFAULT_SIZE,
                                                                    GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                            .addComponent(serverReturn, GroupLayout.DEFAULT_SIZE,
                                                                    GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                            .addComponent(send, GroupLayout.DEFAULT_SIZE,
                                                                    GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING,
                                                            false)
                                                    .addComponent(loggedInUser, GroupLayout.Alignment.LEADING,
                                                            GroupLayout.DEFAULT_SIZE
                                                            , GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                    .addGroup(layout.createSequentialGroup()
                                                            .addComponent(recipient)
                                                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                            .addComponent(recipientActual,
                                                                    GroupLayout.PREFERRED_SIZE,
                                                                    100,
                                                                    GroupLayout.PREFERRED_SIZE))))
                                    .addGap(116))
            );
            layout.setVerticalGroup(
                    layout.createSequentialGroup()
                            .addGap(20)
                            .addComponent(loggedInUser, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                    .addComponent(recipient, GroupLayout.PREFERRED_SIZE, 30,
                                            GroupLayout.PREFERRED_SIZE)
                                    .addComponent(recipientActual, GroupLayout.PREFERRED_SIZE,
                                            GroupLayout.DEFAULT_SIZE,
                                            GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(layout.createParallelGroup()
                                    .addComponent(userMessage, GroupLayout.PREFERRED_SIZE, 200,
                                            Short.MAX_VALUE)
                                    .addGroup(layout.createSequentialGroup()
                                            .addComponent(viewMessage, GroupLayout.PREFERRED_SIZE,
                                                    GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(userManagement, GroupLayout.PREFERRED_SIZE,
                                                    GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                    .addComponent(friendsOnlyPrompt)
                                                    .addComponent(friendsOnly))
                                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 119,
                                                    Short.MAX_VALUE)
                                            .addComponent(serverReturn, GroupLayout.PREFERRED_SIZE, 30,
                                                    GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(send, GroupLayout.PREFERRED_SIZE,
                                                    GroupLayout.DEFAULT_SIZE,
                                                    GroupLayout.PREFERRED_SIZE)))
                            .addGap(20)
            );
            frame.add(sendMessagePanel);
            frame.setSize(600, 400);
            frame.setLocationRelativeTo(null); // Center the frame on the screen
            frame.setVisible(true);
        } catch (IOException e) {
        }
    }

    public static void viewMessageInterface(Socket socket, String activeUser) {
        final String[] sendReceiveHeader = {"RECIEVED_MESSAGE:"};
        viewMessagePanel = new JPanel();
        final String[][] individualMessages = new String[1][1];
        final int[] pageNum = {0};
        try {
            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            JLabel loggedInUser = new JLabel();

            JLabel label2 = new JLabel();
            JTextField receiver = new JTextField();
            String recipient = receiver.getText();
            JLabel message1 = new JLabel();
            JLabel message2 = new JLabel();
            JLabel message3 = new JLabel();
            JLabel message4 = new JLabel();
            JCheckBox checkBox1 = new JCheckBox();
            checkBox1.setVisible(false);
            JCheckBox checkBox2 = new JCheckBox();
            checkBox2.setVisible(false);
            JCheckBox checkBox3 = new JCheckBox();
            checkBox3.setVisible(false);
            JCheckBox checkBox4 = new JCheckBox();
            checkBox4.setVisible(false);
            JButton down = new JButton();
            down.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (individualMessages[0].length - (pageNum[0] + 1) * 4 > 0) {
                        pageNum[0]++;
                        int index = pageNum[0] * 4;
                        int additional = individualMessages[0].length % 4;
                        if (individualMessages[0].length - index - additional < 2) {
                            message1.setText(individualMessages[0][0 + index]);
                            message2.setText("");
                            checkBox2.setVisible(false);
                            message3.setText("");
                            checkBox3.setVisible(false);
                            message4.setText("");
                            checkBox4.setVisible(false);
                        } else if (individualMessages[0].length - index - additional < 3) {
                            message1.setText(individualMessages[0][0 + index]);
                            message2.setText(individualMessages[0][1 + index]);
                            message3.setText("");
                            checkBox3.setVisible(false);
                            message4.setText("");
                            checkBox4.setVisible(false);

                        } else if (individualMessages[0].length - index - additional < 4) {
                            message1.setText(individualMessages[0][0 + index]);
                            message2.setText(individualMessages[0][1 + index]);
                            message3.setText(individualMessages[0][2 + index]);
                            message4.setText("");
                            checkBox4.setVisible(false);

                        } else if (individualMessages[0].length - index - additional < 5) {
                            message1.setText(individualMessages[0][0 + index]);
                            message2.setText(individualMessages[0][1 + index]);
                            message3.setText(individualMessages[0][2 + index]);
                            message4.setText(individualMessages[0][3 + index]);
                        }
                    }
                    frame.revalidate();
                    frame.repaint();
                }
            });

            JButton up = new JButton();
            up.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (pageNum[0] > 0) {
                        pageNum[0]--;
                        int index = pageNum[0] * 4;
                        int additional = individualMessages[0].length % 4;
                        if (individualMessages[0].length - index - additional < 2) {
                            message1.setText(individualMessages[0][0 + index]);
                            checkBox1.setVisible(true);
                            message2.setText("");
                            checkBox2.setVisible(false);
                            message3.setText("");
                            checkBox3.setVisible(false);
                            message4.setText("");
                            checkBox4.setVisible(false);
                        } else if (individualMessages[0].length - index - additional < 3) {
                            message1.setText(individualMessages[0][0 + index]);
                            checkBox1.setVisible(true);
                            message2.setText(individualMessages[0][1 + index]);
                            checkBox2.setVisible(true);
                            message3.setText("");
                            checkBox3.setVisible(false);
                            message4.setText("");
                            checkBox4.setVisible(false);
                        } else if (individualMessages[0].length - index - additional < 4) {
                            message1.setText(individualMessages[0][0 + index]);
                            checkBox1.setVisible(true);
                            message2.setText(individualMessages[0][1 + index]);
                            checkBox2.setVisible(true);
                            message3.setText(individualMessages[0][2 + index]);
                            checkBox3.setVisible(true);
                            message4.setText("");
                            checkBox4.setVisible(false);

                        } else if (individualMessages[0].length - index - additional < 5) {
                            message1.setText(individualMessages[0][0 + index]);
                            checkBox1.setVisible(true);
                            message2.setText(individualMessages[0][1 + index]);
                            checkBox2.setVisible(true);
                            message3.setText(individualMessages[0][2 + index]);
                            checkBox3.setVisible(true);
                            message4.setText(individualMessages[0][3 + index]);
                            checkBox4.setVisible(true);
                        }
                    }
                    frame.revalidate();
                    frame.repaint();
                }
            });

            JToggleButton sendReceive = new JToggleButton();
            sendReceive.setText("Received");
            sendReceive.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (sendReceive.isSelected()) {
                        sendReceive.setText("Sent");
                        sendReceiveHeader[0] = "SENT_MESSAGE:";
                        checkBox1.setVisible(true);
                        checkBox2.setVisible(true);
                        checkBox3.setVisible(true);
                        checkBox4.setVisible(true);
                    } else {
                        sendReceive.setText("Received");
                        sendReceiveHeader[0] = "RECIEVED_MESSAGE:";
                        checkBox1.setVisible(false);
                        checkBox2.setVisible(false);
                        checkBox3.setVisible(false);
                        checkBox4.setVisible(false);
                    }
                    frame.revalidate();
                    frame.repaint();
                }
            });
            JButton viewMessage = new JButton();
            viewMessage.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String recipient = receiver.getText();
                    if (recipient.isBlank()) {
                        message1.setText("Recipient is blank");
                    } else {
                        System.out.println(sendReceiveHeader[0] + activeUser + ":" + recipient);
                        out.println(sendReceiveHeader[0] + activeUser + ":" + recipient);
                        try {
                            String message = in.readLine();
                            System.out.println(message);
                            if (message.equals("[]")) {
                                message1.setText("No messages exists");
                            } else {
                                message = message.substring(1, message.length() - 1);
                                System.out.println(message);
                                individualMessages[0] = message.split(",");
                                int index = pageNum[0] * 4;
                                int additional = individualMessages[0].length % 4;
                                if (individualMessages[0].length - pageNum[0] - additional < 1) {
                                    message1.setText("No messages exists");

                                } else if (individualMessages[0].length - index - additional < 2) {
                                    message1.setText(individualMessages[0][0 + index]);

                                } else if (individualMessages[0].length - index - additional < 3) {
                                    message1.setText(individualMessages[0][0 + index]);
                                    message2.setText(individualMessages[0][1 + index]);

                                } else if (individualMessages[0].length - index - additional < 4) {
                                    message1.setText(individualMessages[0][0 + index]);
                                    message2.setText(individualMessages[0][1 + index]);
                                    message3.setText(individualMessages[0][2 + index]);

                                } else if (individualMessages[0].length - index - additional < 5) {
                                    message1.setText(individualMessages[0][0 + index]);
                                    message2.setText(individualMessages[0][1 + index]);
                                    message3.setText(individualMessages[0][2 + index]);
                                    message4.setText(individualMessages[0][3 + index]);
                                }
                            }
                            frame.revalidate();
                            frame.repaint();
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }

                    }


                }
            });
            JButton sendMessage = new JButton();
            sendMessage.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    sendMessage(socket, activeUser);
                }
            });
            final String[] cb1 = new String[1];
            checkBox1.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (checkBox1.isSelected()) {
                        cb1[0] = "selected";
                    }
                }
            });
            final String[] cb2 = new String[1];
            checkBox2.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (checkBox2.isSelected()) {
                        cb2[0] = "selected";
                    }
                }
            });
            final String[] cb3 = new String[1];
            checkBox3.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (checkBox3.isSelected()) {
                        cb3[0] = "selected";
                    }
                }
            });
            final String[] cb4 = new String[1];
            checkBox4.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (checkBox4.isSelected()) {
                        cb4[0] = "selected";
                    }
                }
            });
            cb1[0] = "";
            cb2[0] = "";
            cb3[0] = "";
            cb4[0] = "";

            JButton deleteMessage = new JButton();
            deleteMessage.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int index = pageNum[0] * 4;
                    if (cb1[0].equals("selected")) {
                        out.println("REMOVE_MESSAGE:" + individualMessages[0][0 + index]);
                    }
                    if (cb2[0].equals("selected")) {
                        out.println("REMOVE_MESSAGE:" + individualMessages[0][1 + index]);
                    }
                    if (cb3[0].equals("selected")) {
                        out.println("REMOVE_MESSAGE:" + individualMessages[0][2 + index]);
                    }
                    if (cb4[0].equals("selected")) {
                        out.println("REMOVE_MESSAGE:" + individualMessages[0][3 + index]);
                    }
                    String recipient = receiver.getText();
                    if (recipient.isBlank()) {
                        message1.setText("Recipient is blank");
                    } else {
                        System.out.println(sendReceiveHeader[0] + activeUser + ":" + recipient);
                        try {
                            String message = in.readLine();
                            System.out.println(message);
                            if (message.equals("[]")) {
                                message1.setText("No messages exists");
                            } else {
                                message = message.substring(1, message.length() - 1);
                                System.out.println(message);
                                individualMessages[0] = message.split(",");
                                int additional = individualMessages[0].length % 4;
                                if (individualMessages[0].length - pageNum[0] - additional < 1) {
                                    message1.setText("No messages exists");

                                } else if (individualMessages[0].length - index - additional < 2) {
                                    message1.setText(individualMessages[0][0 + index]);

                                } else if (individualMessages[0].length - index - additional < 3) {
                                    message1.setText(individualMessages[0][0 + index]);
                                    message2.setText(individualMessages[0][1 + index]);

                                } else if (individualMessages[0].length - index - additional < 4) {
                                    message1.setText(individualMessages[0][0 + index]);
                                    message2.setText(individualMessages[0][1 + index]);
                                    message3.setText(individualMessages[0][2 + index]);

                                } else if (individualMessages[0].length - index - additional < 5) {
                                    message1.setText(individualMessages[0][0 + index]);
                                    message2.setText(individualMessages[0][1 + index]);
                                    message3.setText(individualMessages[0][2 + index]);
                                    message4.setText(individualMessages[0][3 + index]);
                                }
                            }
                            frame.revalidate();
                            frame.repaint();

                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    }

                }
            });

            //---- label1 ----
            loggedInUser.setText("User: " + activeUser);

            //---- label2 ----
            label2.setText("Receiver:");

            //---- receiver ----
            receiver.setText("");

            //---- down ----
            down.setText("Down");

            //---- up ----
            up.setText("Up");

            //---- viewMessage ----
            viewMessage.setText("View Messages");

            //---- DeleteMessage ----
            deleteMessage.setText("Delete Messages");

            //---- sendMessage ----
            sendMessage.setText("Send Messages");

            GroupLayout layout = new GroupLayout(viewMessagePanel);
            viewMessagePanel.setLayout(layout);
            layout.setHorizontalGroup(
                    layout.createParallelGroup()
                            .addGroup(layout.createSequentialGroup()
                                    .addGap(12, 12, 12)
                                    .addGroup(layout.createParallelGroup()
                                            .addGroup(layout.createSequentialGroup()
                                                    .addGap(27, 27, 27)
                                                    .addGroup(layout.createParallelGroup(
                                                                    GroupLayout.Alignment.LEADING,
                                                                    false)
                                                            .addComponent(loggedInUser,
                                                                    GroupLayout.PREFERRED_SIZE, 150,
                                                                    GroupLayout.PREFERRED_SIZE)
                                                            .addGroup(layout.createSequentialGroup()
                                                                    .addComponent(label2,
                                                                            GroupLayout.PREFERRED_SIZE,
                                                                            80,
                                                                            GroupLayout.PREFERRED_SIZE)
                                                                    .addPreferredGap(
                                                                            LayoutStyle.ComponentPlacement.RELATED)
                                                                    .addComponent(receiver,
                                                                            GroupLayout.DEFAULT_SIZE,
                                                                            GroupLayout.DEFAULT_SIZE,
                                                                            Short.MAX_VALUE)))
                                                    .addGap(20, 20, 20))
                                            .addGroup(layout.createSequentialGroup()
                                                    .addGroup(layout.createParallelGroup(
                                                                    GroupLayout.Alignment.TRAILING)
                                                            .addComponent(checkBox1)
                                                            .addComponent(checkBox3)
                                                            .addComponent(checkBox4)
                                                            .addComponent(checkBox2))
                                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                    .addGroup(layout.createParallelGroup(
                                                                    GroupLayout.Alignment.LEADING,
                                                                    false)
                                                            .addComponent(message1,
                                                                    GroupLayout.Alignment.TRAILING,
                                                                    GroupLayout.DEFAULT_SIZE, 400,
                                                                    Short.MAX_VALUE)
                                                            .addComponent(message2, GroupLayout.DEFAULT_SIZE,
                                                                    400,
                                                                    Short.MAX_VALUE)
                                                            .addComponent(message3, GroupLayout.DEFAULT_SIZE,
                                                                    400,
                                                                    Short.MAX_VALUE)
                                                            .addComponent(message4, GroupLayout.DEFAULT_SIZE,
                                                                    400,
                                                                    Short.MAX_VALUE))
                                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 7,
                                                            Short.MAX_VALUE)
                                                    .addGroup(layout.createParallelGroup()
                                                            .addGroup(GroupLayout.Alignment.TRAILING,
                                                                    layout.createSequentialGroup()
                                                                            .addGroup(
                                                                                    layout.createParallelGroup(
                                                                                                    GroupLayout.Alignment.TRAILING)
                                                                                            .addComponent(down,
                                                                                                    GroupLayout.PREFERRED_SIZE,
                                                                                                    100,
                                                                                                    GroupLayout.PREFERRED_SIZE)
                                                                                            .addComponent(up,
                                                                                                    GroupLayout.PREFERRED_SIZE,
                                                                                                    100
                                                                                                    ,
                                                                                                    GroupLayout.PREFERRED_SIZE))
                                                                            .addGap(20, 20, 20))
                                                            .addGroup(GroupLayout.Alignment.TRAILING,
                                                                    layout.createSequentialGroup()
                                                                            .addComponent(sendMessage,
                                                                                    GroupLayout.DEFAULT_SIZE,
                                                                                    GroupLayout.DEFAULT_SIZE,
                                                                                    Short.MAX_VALUE)
                                                                            .addContainerGap())
                                                            .addGroup(GroupLayout.Alignment.TRAILING,
                                                                    layout.createSequentialGroup()
                                                                            .addGroup(
                                                                                    layout.createParallelGroup()
                                                                                            .addComponent(
                                                                                                    viewMessage,
                                                                                                    GroupLayout.DEFAULT_SIZE,
                                                                                                    GroupLayout.DEFAULT_SIZE,
                                                                                                    Short.MAX_VALUE)
                                                                                            .addComponent(
                                                                                                    deleteMessage,
                                                                                                    GroupLayout.DEFAULT_SIZE,
                                                                                                    GroupLayout.DEFAULT_SIZE,
                                                                                                    Short.MAX_VALUE))
                                                                            .addContainerGap())
                                                            .addGroup(layout.createSequentialGroup()
                                                                    .addComponent(sendReceive,
                                                                            GroupLayout.DEFAULT_SIZE,
                                                                            GroupLayout.DEFAULT_SIZE,
                                                                            Short.MAX_VALUE)
                                                                    .addContainerGap())))))
            );
            layout.setVerticalGroup(
                    layout.createParallelGroup()
                            .addGroup(layout.createSequentialGroup()
                                    .addComponent(loggedInUser, GroupLayout.PREFERRED_SIZE, 30,
                                            GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                            .addComponent(label2, GroupLayout.PREFERRED_SIZE, 30,
                                                    GroupLayout.PREFERRED_SIZE)
                                            .addComponent(receiver, GroupLayout.PREFERRED_SIZE, 30,
                                                    GroupLayout.PREFERRED_SIZE))
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING,
                                                    false)
                                            .addGroup(layout.createSequentialGroup()
                                                    .addGroup(layout.createParallelGroup()
                                                            .addComponent(message1, GroupLayout.PREFERRED_SIZE,
                                                                    60,
                                                                    GroupLayout.PREFERRED_SIZE)
                                                            .addComponent(checkBox1))
                                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                    .addGroup(layout.createParallelGroup()
                                                            .addComponent(message2, GroupLayout.PREFERRED_SIZE,
                                                                    60,
                                                                    GroupLayout.PREFERRED_SIZE)
                                                            .addGroup(layout.createSequentialGroup()
                                                                    .addGap(6, 6, 6)
                                                                    .addComponent(sendMessage))
                                                            .addComponent(checkBox2)))
                                            .addGroup(layout.createSequentialGroup()
                                                    .addComponent(viewMessage)
                                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                    .addComponent(deleteMessage)))
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(layout.createParallelGroup()
                                            .addGroup(layout.createSequentialGroup()
                                                    .addGap(72, 72, 72)
                                                    .addComponent(up)
                                                    .addPreferredGap(
                                                            LayoutStyle.ComponentPlacement.RELATED)
                                                    .addComponent(down))
                                            .addGroup(layout.createSequentialGroup()
                                                    .addPreferredGap(
                                                            LayoutStyle.ComponentPlacement.RELATED)
                                                    .addGroup(layout.createParallelGroup()
                                                            .addComponent(message3,
                                                                    GroupLayout.PREFERRED_SIZE, 60,
                                                                    GroupLayout.PREFERRED_SIZE)
                                                            .addComponent(checkBox3))
                                                    .addPreferredGap(
                                                            LayoutStyle.ComponentPlacement.RELATED)
                                                    .addGroup(layout.createParallelGroup()
                                                            .addComponent(checkBox4)
                                                            .addComponent(message4,
                                                                    GroupLayout.PREFERRED_SIZE, 60,
                                                                    GroupLayout.PREFERRED_SIZE)))
                                            .addComponent(sendReceive))
                                    .addGap(44, 44, 44))
            );
        } catch (
                IOException e) {
            throw new RuntimeException(e);
        }
        frame.setSize(600, 400);
        frame.add(viewMessagePanel);
        frame.setLocationRelativeTo(null); // Center the frame on the screen
        frame.setVisible(true);

    }

    public static void main(String[] args) {
        Client_it1 clientIt1 = new Client_it1(); // Create an instance of Client
        Thread thread = new Thread(clientIt1); // Create a Thread passing the Client instance
        thread.start(); // Start the thread
    }
}