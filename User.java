import java.io.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ArrayList;
import java.util.Objects;

public class User implements UserInterface {
    private String username;
    private String password;
    private int age;
    private String location;
    private String name;
    private final String fileName = "src/userDatabase.txt";
    private ArrayList<String> sentMessages;
    private ArrayList<String> receivedMessages;
    private ArrayList<String> blockedUsers;
    private ArrayList<User> friends;
    private ArrayList<String> deletedMessage;

    public User(String username, String password, int age, String location, String name) {
        try {
            BufferedReader bfr = new BufferedReader(new FileReader(fileName));
            String line;
            while ((line = bfr.readLine()) != null) {
                String[] lineParts = line.split("~");
                if (Objects.equals(lineParts[0], username)) {
                    System.out.println(lineParts[0]);
                    throw new IllegalArgumentException("Username already Taken");
                }
            }
            bfr.close();
        } catch (IOException io) {
            io.printStackTrace();
        }

        this.age = age;
        this.username = username;
        this.password = password;
        this.location = location;
        this.name = name;

        // Initialize blockedUsers ArrayList
        this.blockedUsers = new ArrayList<>();

        this.sentMessages = new ArrayList<>();  // ADDED THESE TO ALLOW FOR MESSAGING
        this.receivedMessages = new ArrayList<>();
        this.friends = new ArrayList<>();
        this.deletedMessage = new ArrayList<>();

        try {
            FileWriter writer = new FileWriter(fileName, true);
            BufferedWriter bufferedWriter = new BufferedWriter(writer);

            // Writing user data to the file
            bufferedWriter.write(this.username + "~" +
                    this.password + "~" +
                    this.age + "~" +
                    this.location + "~" +
                    this.name + "\n");

            bufferedWriter.close();
            System.out.println("User data has been written to " + fileName);
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file.");
            e.printStackTrace();
        }
    }

    public User(User user) {


        this.age = user.getAge();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.location = user.getLocation();
        this.name = user.getName();


        this.sentMessages = new ArrayList<>();  // ADDED THESE TO ALLOW FOR MESSAGING
        this.blockedUsers = new ArrayList<>();
        this.receivedMessages = new ArrayList<>();
        this.friends = new ArrayList<>();
        this.deletedMessage = new ArrayList<>();


    }

    public User(String userData) {
        String[] userParts = userData.split("~");
        this.username = userParts[0];
        this.password = userParts[1];
        this.age = Integer.parseInt(userParts[2]);
        this.location = userParts[3];
        this.name = userParts[4];
        this.sentMessages = new ArrayList<>();  // ADDED THESE TO ALLOW FOR MESSAGING
        this.blockedUsers = new ArrayList<>();
        this.receivedMessages = new ArrayList<>();
        this.friends = new ArrayList<>();
        this.deletedMessage = new ArrayList<>();
    }

    public String getName() {
        return this.name;
    }

    public String getLocation() {
        return location;
    }

    public int getAge() {
        return age;
    }


    public void setMessRec(String mess) {

        this.sentMessages.add(mess);
    }
    public void setMessRec(ArrayList mess) {

        this.sentMessages = mess;
    }

    public ArrayList<String> getReceivedMessages() {

        return this.sentMessages;
    }
    public void setMessSent(String mess) {

        this.receivedMessages.add(mess);
    }
    public void setMessSent(ArrayList mess) {

        this.receivedMessages = mess;
    }


    public ArrayList<String> getSentMessage() {

        return receivedMessages;
    }
    public void SetDeleted(String input){
        this.deletedMessage.add(input);
    }
    public ArrayList<String> getDeletedMessage(){
        return this.deletedMessage;
    }


    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
    public void blockUser(User user) {
        String username = user.getUsername();
        blockedUsers.add(username);
    }
    public ArrayList<User> getFriends (){
        return this.friends;
    }
    public void addFriends(User user){
        this.friends.add(user);
    }
    public void removeFriends(User user){
        for(int i = 0; i <this.friends.size(); i++){
            if(friends.get(i).compare(user)){
                friends.remove(i);
            }
        }
    }
    public boolean compare(User user){
        if((this.age == user.getAge()) && (this.username.equals(user.getUsername())) &&  (this.name.equals(user.getName())) && (this.location.equals(user.getLocation()))){
            return true;
        }
        return false;
    }

    public void unblockUser(User user) {
        String username = user.getUsername();
        blockedUsers.remove(username);
    }

    public boolean isBlocked(String username) {
        return blockedUsers.contains(username);
    }

}