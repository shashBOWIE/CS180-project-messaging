import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.Iterator;
public class Server {
    private static final int PORT = 12345; // Choose any available port
    private static ArrayList<User> users = new ArrayList<>();

    public static void main(String[] args) {
        loadUsersFromDatabase();
        for (int i = 0; i < users.size(); i++) {
            System.out.println(users.get(i).getName());// Load users from the database file
        }

        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Server started. Waiting for clients...");
            int conClients = 0;

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket);
                conClients += 1;
                System.out.println("concurrent clients " + conClients);

                // Handle client in a new thread
                new Thread(() -> {
                    handleClient(clientSocket);
                }).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleClient(Socket clientSocket) {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            String inputLine;
            LoggedUser loggedInUser = null;
            User selfuser = null;
            while ((inputLine = in.readLine()) != null) {
                if (inputLine.startsWith("CREATE_ACCOUNT:")) {
                    synchronized (users) {
                        // Split the input line to extract user details
                        String[] userDetails = inputLine.split(":");
                        if (userDetails.length < 6) {
                            out.println("Error: Missing user details");
                            continue;
                        }
                        // Parse user details
                        String username = userDetails[1];
                        String password = userDetails[2];
                        String realName = userDetails[3];
                        int age = Integer.parseInt(userDetails[4]);
                        String location = userDetails[5];

                        // Check if username already exists
                        boolean usernameTaken = false;
                        for (User user : users) {
                            if (user.getUsername().equals(username)) {
                                usernameTaken = true;
                                break;
                            }
                        }

                        // If username is already taken, notify the client
                        if (usernameTaken) {
                            out.println("Error: Username already taken");
                            continue;
                        }

                        // Create new user and add to the list
                        User newUser = new User(username, password, age, location, realName);
                        users.add(newUser);

                        // Confirm creation to client
                        out.println("Account created for user " + username);
                        for (int i = 0; i < users.size(); i++) {
                            System.out.println(users.get(i).getName());// Load users from the database file
                        }
                    }
                } else if (inputLine.startsWith("LOGIN:")) {
                    String[] userDetail = inputLine.split(":");
                    String username = userDetail[1];
                    String password = userDetail[2];


                    synchronized (users) {

                        for (User user : users) {
                            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                                selfuser = user;
                                loggedInUser = new LoggedUser(user);
                                break;
                            }
                        }
                    }

                    if(loggedInUser != null){
                        synchronized (users) {
                            ArrayList<String> userMessages = readMessagesForUser(loggedInUser.getUsername());
                            ArrayList<String> userMessagesSEd = readMessagesForUserGet(loggedInUser.getUsername());
                            if (userMessages.isEmpty()) {
                                System.out.println("No messages found for user " + loggedInUser.getUsername());
                            } else {
                                loggedInUser.setMessSent(userMessages);
                                selfuser.setMessSent(userMessages);
                                ArrayList<String> deletedMessages = selfuser.getDeletedMessage();
                                ArrayList<String> receivedMessages = selfuser.getSentMessage();

                                // Remove deleted messages from received messages
                                receivedMessages.removeAll(deletedMessages);
                                selfuser.setMessSent(receivedMessages);

                                ArrayList<String> deletedMessagesL = loggedInUser.getDeletedMessage();
                                ArrayList<String> receivedMessagesL = loggedInUser.getSentMessage();

                                // Remove deleted messages from received messages
                                receivedMessagesL.removeAll(deletedMessagesL);
                                loggedInUser.setMessSent(receivedMessages);
                                for (String message : loggedInUser.getSentMessage()) {
                                    System.out.println(message);
                                }

                            }
                            if (userMessagesSEd.isEmpty()) {
                                System.out.println("No messages recived  for user " + loggedInUser.getUsername());
                            } else {
                                System.out.println(" for getter ----");
                                loggedInUser.setMessRec(userMessagesSEd);
                                selfuser.setMessRec(userMessagesSEd);
                                for (String message : loggedInUser.getReceivedMessages()) {
                                    System.out.println(message);
                                }
                            }
                        }
                    }

                    if (loggedInUser == null) {
                        out.println("Login failed: Invalid username or password");
                    }else{
                        out.println("Login successful: Welcome " + loggedInUser.getName());
                    }
                } else if (inputLine.startsWith("SEND_MESSAGE:") && loggedInUser != null) {
                    String[] messageParts = inputLine.split(":");
                    if (messageParts.length < 4) {
                        out.println("Error: Incorrect message format.");
                        continue;
                    }
                    String receiverUsername = messageParts[2];
                    String messageContent = messageParts[3];

                    synchronized (users) {
                        User receiver = getUserByUsername(receiverUsername);
                        if (receiver != null && !(selfuser.isBlocked(receiver.getUsername()))) {
                            loggedInUser.sendMessageLog(receiver, messageContent);
                            out.println("Message sent to " + receiver.getUsername());
                            ArrayList<String> userMessages = readMessagesForUser(loggedInUser.getUsername());
                            ArrayList<String> userMessagesSEd = readMessagesForUserGet(loggedInUser.getUsername());
                            if (userMessages.isEmpty()) {
                                System.out.println("No messages found for user " + loggedInUser.getUsername());
                            } else {
                                loggedInUser.setMessSent(userMessages);
                                selfuser.setMessSent(userMessages);
                                ArrayList<String> deletedMessages = selfuser.getDeletedMessage();
                                ArrayList<String> receivedMessages = selfuser.getSentMessage();

                                // Remove deleted messages from received messages
                                receivedMessages.removeAll(deletedMessages);
                                selfuser.setMessSent(receivedMessages);

                                ArrayList<String> deletedMessagesL = loggedInUser.getDeletedMessage();
                                ArrayList<String> receivedMessagesL = loggedInUser.getSentMessage();

                                // Remove deleted messages from received messages
                                receivedMessagesL.removeAll(deletedMessagesL);
                                loggedInUser.setMessSent(receivedMessages);
                                for (String message : loggedInUser.getSentMessage()) {
                                    System.out.println(message);
                                }

                            }
                            if (userMessagesSEd.isEmpty()) {
                                System.out.println("No messages recived  for user " + loggedInUser.getUsername());
                            } else {
                                System.out.println(" for getter ----");
                                loggedInUser.setMessRec(userMessagesSEd);
                                for (String message : loggedInUser.getReceivedMessages()) {
                                    System.out.println(message);
                                }
                            }
                        } else {
                            out.println("Error: Receiver not found.");
                        }
                    }
                }
                else if (inputLine.startsWith("**SEND_MESSAGE:") && loggedInUser != null) {
                    String[] messageParts = inputLine.split(":");
                    if (messageParts.length < 4) {
                        out.println("Error: Incorrect message format.");
                        continue;
                    }
                    String receiverUsername = messageParts[2];
                    String messageContent = messageParts[3];
                    User holderone = getUserByUsername(receiverUsername);
                    ArrayList<User> friendsHolderoo = new ArrayList<>();
                    friendsHolderoo = selfuser.getFriends();
                    Boolean condition = false;
                    for(int i = 0; i < friendsHolderoo.size(); i++){
                        if(friendsHolderoo.get(i).compare(holderone));
                        System.out.println("yay");
                        condition = true;
                    }

                    if(!(friendsHolderoo.contains(holderone))) {
                        out.println("Error: Receiver not found.");

                    }
                    if(condition) {
                        synchronized (users) {
                            User receiver = getUserByUsername(receiverUsername);
                            if (receiver != null && !(selfuser.isBlocked(receiver.getUsername()))) {
                                loggedInUser.sendMessageLog(receiver, messageContent);
                                out.println("Message sent to " + receiver.getUsername());
                                ArrayList<String> userMessages = readMessagesForUser(loggedInUser.getUsername());
                                ArrayList<String> userMessagesSEd = readMessagesForUserGet(loggedInUser.getUsername());
                                if (userMessages.isEmpty()) {
                                    System.out.println("No messages found for user " + loggedInUser.getUsername());
                                } else {
                                    loggedInUser.setMessSent(userMessages);
                                    selfuser.setMessSent(userMessages);
                                    ArrayList<String> deletedMessages = selfuser.getDeletedMessage();
                                    ArrayList<String> receivedMessages = selfuser.getSentMessage();

                                    // Remove deleted messages from received messages
                                    receivedMessages.removeAll(deletedMessages);
                                    selfuser.setMessSent(receivedMessages);

                                    ArrayList<String> deletedMessagesL = loggedInUser.getDeletedMessage();
                                    ArrayList<String> receivedMessagesL = loggedInUser.getSentMessage();

                                    // Remove deleted messages from received messages
                                    receivedMessagesL.removeAll(deletedMessagesL);
                                    loggedInUser.setMessSent(receivedMessages);
                                    for (String message : loggedInUser.getSentMessage()) {
                                        System.out.println(message);
                                    }

                                }
                                if (userMessagesSEd.isEmpty()) {
                                    System.out.println("No messages recived  for user " + loggedInUser.getUsername());
                                } else {
                                    System.out.println("for getter ----");
                                    loggedInUser.setMessRec(userMessagesSEd);
                                    for (String message : loggedInUser.getReceivedMessages()) {
                                        System.out.println(message);
                                    }
                                }
                            } else {
                                out.println("Error: Receiver not found.");
                            }
                        }
                    }

                }


                else if (inputLine.startsWith("BLOCK_USER:") && loggedInUser != null) {
                    // Block user logic
                    String[] commandParts = inputLine.split(":");
                    String blockerUsername = commandParts[1];
                    String userToBlock = commandParts[2];

                    synchronized (users) {

                        User userToBlockObj = getUserByUsername(userToBlock);
                        System.out.println(userToBlockObj.getUsername());
                        if (userToBlockObj != null) {
                            loggedInUser.blockUser(userToBlockObj);
                            selfuser.blockUser(userToBlockObj);
                            userToBlockObj.blockUser(selfuser);
                            userToBlockObj.blockUser(loggedInUser);
                            out.println("User " + userToBlock + " blocked successfully.");
                        } else {
                            out.println("Error: User not found.");
                        }
                    }

                }else if (inputLine.startsWith("UNBLOCK_USER:") && loggedInUser != null) {
                    // Block user logic
                    String[] commandParts = inputLine.split(":");
                    String blockerUsername = commandParts[1];
                    String userToBlock = commandParts[2];

                    synchronized (users) {

                        User userToBlockObj = getUserByUsername(userToBlock);
                        System.out.println(userToBlockObj.getUsername());
                        if (userToBlockObj != null) {
                            loggedInUser.unblockUser(userToBlockObj);
                            selfuser.unblockUser(userToBlockObj);
                            userToBlockObj.unblockUser(selfuser);
                            userToBlockObj.unblockUser(loggedInUser);
                            out.println("User " + userToBlock + " unblocked successfully.");
                        } else {
                            out.println("Error: User not found.");
                        }
                    }
                }else if (inputLine.startsWith("SENT_MESSAGE:") && loggedInUser != null) {
                    ArrayList<String> userMessages = readMessagesForUser(loggedInUser.getUsername());
                    ArrayList<String> userMessagesSEd = readMessagesForUserGet(loggedInUser.getUsername());
                    if (userMessages.isEmpty()) {
                        System.out.println("No messages found for user " + loggedInUser.getUsername());
                    } else {
                        loggedInUser.setMessSent(userMessages);
                        selfuser.setMessSent(userMessages);
                        ArrayList<String> deletedMessages = selfuser.getDeletedMessage();
                        ArrayList<String> receivedMessages = selfuser.getSentMessage();

                        // Remove deleted messages from received messages
                        receivedMessages.removeAll(deletedMessages);
                        selfuser.setMessSent(receivedMessages);

                        ArrayList<String> deletedMessagesL = loggedInUser.getDeletedMessage();
                        ArrayList<String> receivedMessagesL = loggedInUser.getSentMessage();

                        // Remove deleted messages from received messages
                        receivedMessagesL.removeAll(deletedMessagesL);
                        loggedInUser.setMessSent(receivedMessages);
                        for (String message : loggedInUser.getSentMessage()) {
                            System.out.println(message);
                        }

                    }
                    if (userMessagesSEd.isEmpty()) {
                        System.out.println("No messages recived  for user " + loggedInUser.getUsername());
                    } else {
                        System.out.println(" for getter ----");
                        loggedInUser.setMessRec(userMessagesSEd);
                        selfuser.setMessRec(userMessagesSEd);
                        for (String message : loggedInUser.getReceivedMessages()) {
                            System.out.println(message);
                        }
                    }

                    ArrayList<String> SENT_MESSAGE = new ArrayList<>();
                    String[] commandPartss = inputLine.split(":");
                    String logUser = commandPartss[1];
                    String vieweeName = commandPartss[2];
                    System.out.println(vieweeName);
                    ArrayList<String> Temp1 = new ArrayList<>();
                    Temp1 = selfuser.getSentMessage();

                    for (String message : Temp1) {
                        System.out.println(message);
                    }

                    for (int i = 0; i < Temp1.size(); i++) {
                        String message = Temp1.get(i);
                        if (!message.contains(vieweeName + ":")) {
                            Temp1.remove(i);
                            i--; // Adjust the index to account for the removed element
                        }
                    }
                    System.out.println("new lines!!!");
                    for (String message : Temp1) {
                        System.out.println(message);
                    }

                    out.println(Temp1.toString());



                }else if (inputLine.startsWith("RECIEVED_MESSAGE:") && loggedInUser != null) {
                    ArrayList<String> userMessages = readMessagesForUser(loggedInUser.getUsername());
                    ArrayList<String> userMessagesSEd = readMessagesForUserGet(loggedInUser.getUsername());
                    if (userMessages.isEmpty()) {
                        System.out.println("No messages found for user " + loggedInUser.getUsername());
                    } else {
                        loggedInUser.setMessSent(userMessages);
                        for (String message : loggedInUser.getSentMessage()) {
                            System.out.println(message);
                        }

                    }
                    if (userMessagesSEd.isEmpty()) {
                        System.out.println("No messages recived  for user " + loggedInUser.getUsername());
                    } else {
                        System.out.println(" for getter ----");
                        selfuser.setMessRec(userMessagesSEd);
                        loggedInUser.setMessRec(userMessagesSEd);
                        for (String message : loggedInUser.getReceivedMessages()) {
                            System.out.println(message);
                        }
                    }


                    ArrayList<String> SENT_MESSAGE = new ArrayList<>();
                    String[] commandPartss = inputLine.split(":");
                    String logUser = commandPartss[1];
                    String vieweeName = commandPartss[2];
                    System.out.println(vieweeName);
                    ArrayList<String> Temp1 = new ArrayList<>();
                    Temp1 = selfuser.getReceivedMessages();

                    for (String message : Temp1) {
                        System.out.println(message);
                    }

                    for (int i = 0; i < Temp1.size(); i++) {
                        String message = Temp1.get(i);
                        if (!message.contains(vieweeName + ":")) {
                            Temp1.remove(i);
                            i--; // Adjust the index to account for the removed element
                        }
                    }
                    System.out.println("new lines!!!");
                    for (String message : Temp1) {
                        System.out.println(message);
                    }

                    out.println(Temp1.toString());
                } else if (inputLine.startsWith("FRIENDADD:") && loggedInUser != null) {
                    String[] commandPartss = inputLine.split(":");
                    String logUser = commandPartss[1];
                    String vieweeName = commandPartss[2];
                    User userFriend = getUserByUsername(vieweeName);
                    selfuser.addFriends(userFriend);
                    loggedInUser.addFriends(userFriend);
                    System.out.println(selfuser.getFriends());
                    System.out.println(loggedInUser.getFriends());
                    out.println(userFriend.getUsername() + " added");

                } else if (inputLine.startsWith("FRIENDREMOVE:") && loggedInUser != null) {
                    String[] commandPartss = inputLine.split(":");
                    String logUser = commandPartss[1];
                    String vieweeName = commandPartss[2];
                    User userFriend = getUserByUsername(vieweeName);
                    selfuser.removeFriends(userFriend);
                    loggedInUser.removeFriends(userFriend);
                    System.out.println(selfuser.getFriends());
                    System.out.println(loggedInUser.getFriends());
                    out.println(userFriend.getUsername() + " removed");


                } else if ((inputLine.startsWith("REMOVE_MESSAGE:") && loggedInUser != null)) {
                    ArrayList<String> userMessages = readMessagesForUser(loggedInUser.getUsername());
                    ArrayList<String> userMessagesSEd = readMessagesForUserGet(loggedInUser.getUsername());
                    if (userMessages.isEmpty()) {
                        System.out.println("No messages found for user " + loggedInUser.getUsername());
                    } else {
                        loggedInUser.setMessSent(userMessages);
                        selfuser.setMessSent(userMessages);
                        ArrayList<String> deletedMessages = selfuser.getDeletedMessage();
                        ArrayList<String> receivedMessages = selfuser.getSentMessage();

                        // Remove deleted messages from received messages
                        receivedMessages.removeAll(deletedMessages);
                        selfuser.setMessSent(receivedMessages);

                        ArrayList<String> deletedMessagesL = loggedInUser.getDeletedMessage();
                        ArrayList<String> receivedMessagesL = loggedInUser.getSentMessage();

                        // Remove deleted messages from received messages
                        receivedMessagesL.removeAll(deletedMessagesL);
                        loggedInUser.setMessSent(receivedMessages);



                        for (String message : loggedInUser.getSentMessage()) {
                            System.out.println(message);
                        }

                    }
                    if (userMessagesSEd.isEmpty()) {
                        System.out.println("No messages recived  for user " + loggedInUser.getUsername());
                    } else {
                        System.out.println(" for getter ----");
                        selfuser.setMessRec(userMessagesSEd);
                        loggedInUser.setMessRec(userMessagesSEd);
                        for (String message : loggedInUser.getReceivedMessages()) {
                            System.out.println(message);
                        }
                    }


                    boolean trueorno = false;
                    String[] commandPartss = inputLine.split(":");
                    String logUser = commandPartss[1];
                    String vieweeName = commandPartss[2];
                    String message = commandPartss[3];
                    ArrayList<String> contained = new ArrayList<>();
                    contained = selfuser.getSentMessage();
                    for(int i = 0; i < contained.size(); i++){
                        if(contained.get(i).contains(vieweeName) && contained.get(i).contains(message)){
                            System.out.println("yay");
                            selfuser.SetDeleted(contained.get(i));
                            loggedInUser.SetDeleted(contained.get(i));
                            trueorno = true;
                        }
                    }

                    System.out.println(vieweeName + " " + message);
                    if(trueorno){
                        out.println(vieweeName + " " + message);
                    }else{
                        out.println("error check if message or user exists");
                    }


                } else {
                    out.println("Error: Unrecognized command or user not logged in.");
                }
            }

            in.close();
            out.close();
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadUsersFromDatabase() {
        String userFileName = "src/userDatabase.txt";
        try (BufferedReader bfr = new BufferedReader(new FileReader(userFileName))) {
            String line;
            while ((line = bfr.readLine()) != null) {
                User user = new User(line);
                users.add(user);
            }
        } catch (IOException e) {
            System.out.println("Error loading users from the database.");
            e.printStackTrace();
        }
    }

    public static synchronized User getUserByUsername(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }
    public static ArrayList<String> readMessagesForUser(String username) {
        ArrayList<String> userMessages = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("src/textlog.txt"))) {
            String line;
            boolean foundUser = false;
            String holdern = "";
            while ((line = br.readLine()) != null) {
                // Check if the line starts with the user's session header
                if (line.contains("****" + username + " ")) {
                    foundUser = true;
                    int holder = line.indexOf("~");
                    holdern = line.substring(holder+1,line.length()-4 );
                    continue; // Skip the header line
                }

                // If user is found and it's not a new session header
                if (foundUser && !line.startsWith("****")) {
                    // Split the line by "~" to separate messages
                    String[] messages = line.split("~");
                    for (String message : messages) {
                        holdern = holdern.replace("*", "");
                        userMessages.add(holdern.trim() + ":" +message);
                    }
                }
                // Check if it's the end of the user's session (next header or end of file)
                if (foundUser && line.startsWith("****")) {
                    break; // End of user's messages
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return userMessages;
    }
    public static ArrayList<String> readMessagesForUserGet(String username) {
        ArrayList<String> userMessages = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("src/textlog.txt"))) {
            String line;
            String holdern = "";
            System.out.println("1");
            System.out.println(username);
            boolean foundUser = false;  // This will track whether we are within the user's session
            while ((line = br.readLine()) != null) {
                // Check for a header line and see if it ends with the username as a receiver
                if (line.contains(username + "****")) {
                    int holder = line.indexOf("~");
                    holdern = line.substring(4,holder-1);
                    foundUser = true;
                    System.out.println("2");
                    continue; // Skip the header line and start collecting the following messages
                }
                // If we find another session header, reset foundUser
                if (foundUser && line.contains("****")) {
                    break; // Stop reading as we've reached another user's session
                }
                // Collect messages if foundUser is true
                if (foundUser && !line.startsWith("****")) {
                    String[] messages = line.split("~");
                    for (String message : messages) {
                        holdern = holdern.replace("*", "");

                        userMessages.add(holdern.trim() + ":" +message);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading messages for user: " + username);
            e.printStackTrace();
        }
        return userMessages;
    }
    public ArrayList<User> getUsers() {

        return this.users;
    }

}