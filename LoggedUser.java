import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class LoggedUser extends User {
    public LoggedUser(User user) {
        super(user);
    }

    public boolean sendMessage(User user, String message) {
        String holder = getUsername() + "-" + message;
        user.setMessSent(holder);
        return true;
    }
    private static String lastReceiver = null; // Static variable to keep track of the last receiver



    public void sendMessageLog(User receiver, String messageContent) {
        String senderUsername = this.getUsername();
        String receiverUsername = receiver.getUsername(); // Assuming User has a getName() method.

        // Determine if this message starts a new session
        boolean isNewSession = !receiverUsername.equals(lastReceiver);

        try (FileWriter fw = new FileWriter("src/textlog.txt", true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {

            // If it's a new session, print the "sender - receiver".
            if (isNewSession) {
                if (lastReceiver != null) {
                    out.println(); // Optionally add a newline for separation before a new header
                }
                out.printf("****" + "%s ~ %s" + "**** \n", senderUsername, receiverUsername);
                lastReceiver = receiverUsername; // Update the lastReceiver to the current one
            }

            // Append the message content, separated by ~
            if (!isNewSession) {
                out.print("~"); // Add a tilde to separate messages within a session
            }
            out.print(messageContent);

        } catch (IOException e) {
            System.err.println("An error occurred while trying to log the message to the file.");
        }

        // Add here your logic for the actual message sending mechanism
    }
}