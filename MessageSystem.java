import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Objects;

public class MessageSystem implements MessageSystemInterface { // class and interface made by Cole
    private User sender;
    private User receiver;
    private ArrayList<String> messages;

    public MessageSystem(String userLine, String messageLine) {
        String[] users = userLine.split(" ~ "); // splits up users
        String senderUsername = users[0].substring(4); // removes 4 asterisks
        String receiverUsername = users[1].substring(0, users[1].length() - 4);

        String database = "src/userDatabase.txt";
        try { // sets users in receiver and sender
            BufferedReader bfr = new BufferedReader(new FileReader(database));
            String line;
            int foundUsernames = 0;
            while ((line = bfr.readLine()) != null) {
                String username = line.split("~")[0];
                if (Objects.equals(username, senderUsername)) {
                    foundUsernames++;
                    this.sender = new User(line);
                } else if (Objects.equals(username, receiverUsername)) {
                    foundUsernames++;
                    this.receiver = new User(line);
                }
                if (foundUsernames == 2) { // stops reading the file when both usernames are found
                    break;
                }
            }
            bfr.close();
        } catch (Exception e) {
            System.out.println("you should never get here because of IO");
            e.printStackTrace();
        }
        String[] messages = messageLine.split("~"); // sets messages
        ArrayList<String> messageOutput = new ArrayList<>();
        for (String mess : messages) {
            messageOutput.add(mess);
        }
        this.messages = messageOutput;
    }

    @Override
    public User getSender() {
        return this.sender;
    }

    @Override
    public void setSender(User sender) {
        this.sender = sender;
    }

    @Override
    public User getReceiver() {
        return this.receiver;
    }

    @Override
    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    @Override
    public ArrayList<String> getMessages() {
        return this.messages;
    }

    @Override
    public void setMessages(ArrayList<String> messages) {
        this.messages = messages;
    }
}
