import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class scratch extends JFrame {
    private static JFrame frame;
    private static JPanel mainPanel;
    private static JPanel loginPanel;
    private static JPanel createAccountPanel;
    private static JPanel sendMessageMainPanel;
    private static JPanel loginFailedPanel;
    private static JPanel userManagementPanel;
    public scratch() {
        frame = new JFrame("Chat Client");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        mainPanel = new JPanel(new GridBagLayout());
        loginPanel = new JPanel(new GridBagLayout());
        createAccountPanel = new JPanel(new GridBagLayout());
        loginFailedPanel = new JPanel(new GridBagLayout());
        userManagementPanel = new JPanel();
        sendMessageMainPanel = new JPanel();

        mainInterface();

    }
    private static void mainInterface() {
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
                showLoginPanel();
            }
        });
        mainPanel.add(login, gbc);

        JButton createAccount = new JButton("Create Account");
        createAccount.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showCreateAccountPanel();
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

    private static void showCreateAccountPanel() {
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
        JPasswordField realNameField = new JPasswordField(10);
        createAccountPanel.add(realNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;

        JLabel age = new JLabel("Age");
        createAccountPanel.add(age, gbc);

        gbc.gridx = 1;
        JPasswordField ageField = new JPasswordField(10);
        createAccountPanel.add(ageField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;

        JLabel location = new JLabel("Location");
        createAccountPanel.add(location, gbc);

        gbc.gridx = 1;
        JPasswordField locationField = new JPasswordField(10);
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
                String password = new String(locationField.getPassword());
                // Perform login logic
                // For demonstration purpose, just print username and password
                System.out.println("Username: " + username);
                System.out.println("Password: " + password);
            }
        });
        createAccountPanel.add(loginButton, gbc);

        // Add login panel to the frame
        frame.add(createAccountPanel);

        frame.revalidate(); // Revalidate the frame to reflect changes
        frame.repaint(); // Repaint the frame
    }

    private static void showLoginPanel() {
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
                // Perform login logic
                // For demonstration purpose, just print username and password
                sendMessageMainInterface("Bbored");
            }
        });
        loginPanel.add(loginButton, gbc);

        // Add login panel to the frame
        frame.add(loginPanel);

        frame.revalidate(); // Revalidate the frame to reflect changes
        frame.repaint(); // Repaint the frame
    }

    public static void showLoginFailed() {
        mainPanel.setVisible(false); // Hide the main panel
        frame.remove(mainPanel);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(3, 10, 3, 10);

        JTextArea failed = new JTextArea("Username or Password Incorrect");
        loginFailedPanel.add(failed, gbc);

        JButton login = new JButton("Retry");
        login.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showLoginPanel();
            }
        });
        gbc.gridy = 1;
        loginFailedPanel.add(login, gbc);

        JButton createAccount = new JButton("Create Account");
        createAccount.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showCreateAccountPanel();
            }
        });
        gbc.gridy = 2; // Move to the next row
        loginFailedPanel.add(createAccount, gbc);

        frame.add(loginFailedPanel);
        frame.revalidate(); // Revalidate the frame to reflect changes
        frame.repaint(); // Repaint the frame
    }

    public static void userManagementInterface() {
        mainPanel.setVisible(false); // Hide the main panel
        frame.remove(mainPanel);
        frame.remove(sendMessageMainPanel);

//        GridBagConstraints gbc = new GridBagConstraints();
//        gbc.fill = GridBagConstraints.HORIZONTAL;
//        gbc.gridx = 0;
//        gbc.gridy = 0;
//        gbc.insets = new Insets(3, 10, 3, 10);
//
//        JLabel usernameLabel = new JLabel("Username:");
//        userManagementPanel.add(usernameLabel, gbc);
//        gbc.gridx = 1;
//        JTextField usernameField = new JTextField(10);
//        loginPanel.add(usernameField, gbc);
//
//        JButton blockUser = new JButton("Block User");
//        blockUser.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//            }
//        });
//        gbc.gridy = 1; // Move to the next row
//        userManagementPanel.add(blockUser, gbc);
//
//        JButton unblockUser = new JButton("Unblock User");
//        unblockUser.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//            }
//        });
//        gbc.gridy = 2; // Move to the next row
//        userManagementPanel.add(unblockUser, gbc);
//
//        JButton addFriend = new JButton("Add Friend");
//        addFriend.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//            }
//        });
//        gbc.gridy = 3; // Move to the next row
//        userManagementPanel.add(addFriend, gbc);
//        JButton removeFriend = new JButton("Remove Friend");
//        removeFriend.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//            }
//        });
//        gbc.gridy = 4; // Move to the next row
//        userManagementPanel.add(removeFriend, gbc);
//
//        JButton returnToInterface = new JButton("Return");
//        returnToInterface.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//
//            }
//        });
//        gbc.gridy = 4; // Move to the next row
//        userManagementPanel.add(returnToInterface, gbc);

        JLabel loggedInUser = new JLabel();
        loggedInUser.setText("Current User:");

        JButton sendMessage = new JButton();
        sendMessage.setText("Block");
        sendMessage.setAlignmentY(0.0F);

        JButton userManagement = new JButton();
        userManagement.setText("Add Friend");
        userManagement.setAlignmentY(0.0F);

        JButton logout = new JButton();
        logout.setText("Logout");
        logout.setAlignmentY(0.0F);
        logout.setMaximumSize(new Dimension(169, 30));
        logout.setMinimumSize(new Dimension(169, 30));
        logout.setPreferredSize(new Dimension(169, 30));

        JButton unblock = new JButton();
        unblock.setText("Unblock");
        unblock.setAlignmentY(0.0F);

        JButton removeFriend = new JButton();
        removeFriend.setText("Remove Friend");
        removeFriend.setAlignmentY(0.0F);

        JButton back = new JButton();
        back.setText("Back");
        back.setAlignmentY(0.0F);
        back.setMaximumSize(new Dimension(169, 30));
        back.setMinimumSize(new Dimension(169, 30));
        back.setPreferredSize(new Dimension(169, 30));

        JLabel usernamePrompt = new JLabel();
        usernamePrompt.setText("Username:");

        JTextField username = new JTextField();

        GroupLayout managementLayout = new GroupLayout(userManagementPanel);
        userManagementPanel.setLayout(managementLayout);

        managementLayout.setHorizontalGroup(
                managementLayout.createParallelGroup()
                        .addGroup(managementLayout.createSequentialGroup()
                                .addGroup(managementLayout.createParallelGroup()
                                        .addGroup(managementLayout.createSequentialGroup()
                                                .addGap(40, 40, 40)
                                                .addComponent(loggedInUser, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addGroup(GroupLayout.Alignment.TRAILING, managementLayout.createSequentialGroup()
                                                .addContainerGap(145, Short.MAX_VALUE)
                                                .addComponent(usernamePrompt, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE)))
                                .addGroup(managementLayout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(removeFriend, GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
                                        .addComponent(userManagement, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
                                        .addComponent(unblock, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(sendMessage, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(username, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE))
                                .addContainerGap(225, Short.MAX_VALUE))
                        .addGroup(GroupLayout.Alignment.TRAILING, managementLayout.createSequentialGroup()
                                .addGap(0, 500, Short.MAX_VALUE)
                                .addGroup(managementLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                        .addComponent(logout, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(back, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE))
                                .addGap(20, 20, 20))
        );
        managementLayout.setVerticalGroup(
                managementLayout.createParallelGroup()
                        .addGroup(managementLayout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addComponent(loggedInUser, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
                                .addGap(47, 47, 47)
                                .addGroup(managementLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(username, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(usernamePrompt, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 16, Short.MAX_VALUE)
                                .addComponent(sendMessage, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(unblock, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(userManagement, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(removeFriend, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 27, Short.MAX_VALUE)
                                .addComponent(back, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(logout, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addGap(20, 20, 20))
        );
//        managementLayout.setHorizontalGroup(
//                managementLayout.createParallelGroup()
//                        .addGroup(GroupLayout.Alignment.TRAILING, managementLayout.createSequentialGroup()
//                                .addGap(0, 0, Short.MAX_VALUE)
//                                .addGroup(managementLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
//                                        .addComponent(logout, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE)
//                                        .addComponent(back, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE))
//                                .addGap(20, 20, 20))
//                        .addGroup(managementLayout.createSequentialGroup()
//                                .addGap(40, 40, 40)
//                                .addGroup(managementLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
//                                        .addComponent(loggedInUser, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)
//                                        .addComponent(usernamePrompt, GroupLayout.PREFERRED_SIZE, 68, GroupLayout.PREFERRED_SIZE))
//                                .addGap(0, 0, Short.MAX_VALUE)
//                                .addGroup(managementLayout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
//                                        .addComponent(removeFriend, GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
//                                        .addComponent(userManagement, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
//                                        .addComponent(unblock, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
//                                        .addComponent(sendMessage, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
//                                        .addComponent(username, GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE))
//                                .addContainerGap(260, Short.MAX_VALUE))
//        );
//        managementLayout.setVerticalGroup(
//                managementLayout.createParallelGroup()
//                        .addGroup(managementLayout.createSequentialGroup()
//                                .addGap(20, 20, 20)
//                                .addComponent(loggedInUser, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
//                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 64, Short.MAX_VALUE)
//                                .addGroup(managementLayout.createParallelGroup()
//                                        .addComponent(username, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
//                                        .addComponent(usernamePrompt, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE))
//                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
//                                .addComponent(sendMessage, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
//                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
//                                .addComponent(unblock, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
//                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
//                                .addComponent(userManagement, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
//                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
//                                .addComponent(removeFriend, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
//                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
//                                .addComponent(back, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
//                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
//                                .addComponent(logout, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
//                                .addGap(20, 20, 20))
//        );
//        GroupLayout messageLayout = new GroupLayout(userManagementPanel);
//            message.setLayout(messageLayout);
//            messageLayout.setHorizontalGroup(
//                    messageLayout.createParallelGroup()
//                            .add(messageLayout.createSequentialGroup()
//                                    .add(messageLayout.createParallelGroup()
//                                            .add(messageLayout.createSequentialGroup()
//                                                    .add(40, 40, 40)
//                                                    .add(loggedInUser, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)
//                                                    .addPreferredGap(LayoutStyle.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
//                                            .add(GroupLayout.TRAILING, messageLayout.createSequentialGroup()
//                                                    .addContainerGap(145, Short.MAX_VALUE)
//                                                    .add(usernamePrompt, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE)))
//                                    .add(messageLayout.createParallelGroup(GroupLayout.TRAILING, false)
//                                            .add(removeFriend, GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
//                                            .add(GroupLayout.LEADING, userManagement, GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
//                                            .add(GroupLayout.LEADING, unblock, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
//                                            .add(GroupLayout.LEADING, sendMessage, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
//                                            .add(GroupLayout.LEADING, username, GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE))
//                                    .add(225, 225, 225))
//                            .add(GroupLayout.TRAILING, messageLayout.createSequentialGroup()
//                                    .add(0, 500, Short.MAX_VALUE)
//                                    .add(messageLayout.createParallelGroup(GroupLayout.LEADING, false)
//                                            .add(logout, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE)
//                                            .add(back, GroupLayout.DEFAULT_SIZE, 0, Short.MAX_VALUE))
//                                    .add(20, 20, 20))
//            );
//            messageLayout.setVerticalGroup(
//                    messageLayout.createParallelGroup()
//                            .add(messageLayout.createSequentialGroup()
//                                    .add(20, 20, 20)
//                                    .add(loggedInUser, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
//                                    .add(47, 47, 47)
//                                    .add(messageLayout.createParallelGroup(GroupLayout.BASELINE)
//                                            .add(username, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
//                                            .add(usernamePrompt, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE))
//                                    .addPreferredGap(LayoutStyle.RELATED, 16, Short.MAX_VALUE)
//                                    .add(sendMessage, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
//                                    .addPreferredGap(LayoutStyle.RELATED)
//                                    .add(unblock)
//                                    .addPreferredGap(LayoutStyle.RELATED)
//                                    .add(userManagement, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
//                                    .addPreferredGap(LayoutStyle.RELATED)
//                                    .add(removeFriend)
//                                    .addPreferredGap(LayoutStyle.RELATED, 27, Short.MAX_VALUE)
//                                    .add(back, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
//                                    .addPreferredGap(LayoutStyle.RELATED)
//                                    .add(logout, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
//                                    .add(20, 20, 20))
//            );

                    // Add main panel to the frame
            frame.add(userManagementPanel);

            // Set frame size and make it visible
            frame.setSize(600, 400);
            frame.setLocationRelativeTo(null); // Center the frame on the screen
            frame.setVisible(true);

        }
        public static void sendMessageMainInterface (String user){
            mainPanel.setVisible(false); // Hide the main panel
            frame.remove(mainPanel);
            frame.remove(loginPanel);
            frame.remove(createAccountPanel);
            frame.remove(userManagementPanel);

            JLabel loggedInUser = new JLabel();
            loggedInUser.setText("Current User: " + user);

            JButton sendMessage = new JButton();
            sendMessage.setText("Send Message");
            sendMessage.setAlignmentY(0.0F);

            JButton ViewMessage = new JButton();
            ViewMessage.setText("View Message");
            ViewMessage.setAlignmentY(0.0F);

            JButton userManagement = new JButton();
            userManagement.setText("Contact Management");
            userManagement.setAlignmentY(0.0F);

            userManagement.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    userManagementInterface();
                }
            });

            JButton logout = new JButton();
            logout.setText("Logout");
            logout.setAlignmentY(0.0F);

            logout.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    mainInterface();
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

        public static void sendMessageInterface () {

        }
        public static void vieMessageInterface () {
        }


        public static void main (String[]args){
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    scratch swingGUI = new scratch();
                    swingGUI.setVisible(true);
                }
            });
        }
    }