import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Objects;

public class Runlocaltestphase2 { // Phase 2 Testing all written by Cole
    static String database = "src/userDatabase.txt";

    public static void main(String[] args) {
        for (int i = 1; i <= 10; i++) {
            try {
                PrintWriter pw = new PrintWriter(new FileWriter(database)); // rewrites the file each time
                pw.write("alice22~851~22~chicago~alice\n" +
                        "sidr415~1234~23~detroit~sid\n" +
                        "larry22~0~22~chic~larry\n");
                pw.flush();
                pw.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            switch (i) { // determines which test to run
                case 1:
                    if (testOne()) {
                        System.out.println("Test One Success!");
                    } else {
                        System.out.println("Test One Failure :(");
                    }
                    break;
                case 2:
                    if (testTwo()) {
                        System.out.println("Test Two Success!");
                    } else {
                        System.out.println("Test Two Failure :(");
                    }
                    break;
                case 3:
                    if (testThree()) {
                        System.out.println("Test Three Success!");
                    } else {
                        System.out.println("Test Three Failure :(");
                    }
                    break;
                case 4:
                    if (testFour()) {
                        System.out.println("Test Four Success!");
                    } else {
                        System.out.println("Test Four Failure :(");
                    }
                    break;
                case 5:
                    if (testFive()) {
                        System.out.println("Test Five Success!");
                    } else {
                        System.out.println("Test Five Failure :(");
                    }
                    break;
                case 6:
                    if (testSix()) {
                        System.out.println("Test Six Success!");
                    } else {
                        System.out.println("Test Six Failure :(");
                    }
                    break;
                case 7:
                    if (testSeven()) {
                        System.out.println("Test Seven Success!");
                    } else {
                        System.out.println("Test Seven Failure :(");
                    }
                    break;
                case 8:
                    if (testEight()) {
                        System.out.println("Test Eight Success!");
                    } else {
                        System.out.println("Test Eight Failure :(");
                    }
                    break;
                case 9:
                    if (testNine()) {
                        System.out.println("Test Nine Success!");
                    } else {
                        System.out.println("Test Nine Failure :(");
                    }
                    break;
                case 10:
                    if (testTen()) {
                        System.out.println("Test Ten Success!");
                    } else {
                        System.out.println("Test Ten Failure :(");
                    }
                    break;
            }
        }
    }

    public static boolean testOne() { // User constructor works with string parameter
        String username = "username1";
        String password = "pAsSwOrD123";
        int age = 1931;
        String location = "Location";
        String name = "Real Name";
        String[] expectedOutput = {"alice22~851~22~chicago~alice", "sidr415~1234~23~detroit~sid",
                "larry22~0~22~chic~larry", "username1~pAsSwOrD123~1931~Location~Real Name"};
        User user = new User(username, password, age, location, name);
        try {
            BufferedReader bfr = new BufferedReader(new FileReader(database));
            String line;
            int index = 0;
            while ((line = bfr.readLine()) != null) {
                if (!line.equals(expectedOutput[index])) {
                    return false;
                }
                index++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public static boolean testTwo() {// User Constructor fails
        String username = "alice22"; // username already taken
        String password = "pAsSwOrD123";
        int age = 1931;
        String location = "Location";
        String name = "Real Name";
        String[] expectedOutput = {"alice22~851~22~chicago~alice", "sidr415~1234~23~detroit~sid",
                "larry22~0~22~chic~larry", "username1~pAsSwOrD123~1931~Location~Real Name"};

        try {
            User user = new User(username, password, age, location, name);
        } catch (IllegalArgumentException iae) {
            return true;
        }
        return false;
    }

    public static boolean testThree() { // checks User function with string data
        User user = new User("username1~pAsSwOrD123~1931~Location~Real Name");
        String[] expectedOutput = {"alice22~851~22~chicago~alice", "sidr415~1234~23~detroit~sid",
                "larry22~0~22~chic~larry", "username1~pAsSwOrD123~1931~Location~Real Name"};
        try {
            BufferedReader bfr = new BufferedReader(new FileReader(database));
            String line;
            int index = 0;
            while ((line = bfr.readLine()) != null) {
                if (!line.equals(expectedOutput[index])) {
                    return false;
                }
                index++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public static boolean testFour() { // Logged User constructor works with User
        String username = "username1";
        String password = "pAsSwOrD123";
        int age = 1931;
        String location = "Location";
        String name = "Real Name";
        User user = new User(username, password, age, location, name);
        LoggedUser lUser = new LoggedUser(user);
        return lUser.getAge() == 1931; // if age of logged user is 1931, then the logged User was made
    }

    public static boolean testFive() { // Tests get Functions in User
        String username = "username1";
        String password = "pAsSwOrD123";
        int age = 1931;
        String location = "Location";
        String name = "Real Name";
        User user = new User(username, password, age, location, name);
        if (Objects.equals(username, user.getUsername()) && Objects.equals(password, user.getPassword()) &&
                Objects.equals(location, user.getLocation()) && Objects.equals(name, user.getName()) &&
                age == user.getAge()) { // checks all the get functions
            return true;
        }
        return false;
    }

    public static boolean testSix() { // tests set messages in User
        User user = new User("username1~pAsSwOrD123~1931~Location~Real Name");
        user.setMessSent("Message Test");
        user.setMessSent("number two");
        return Objects.equals(user.getSentMessage().get(0), "Message Test") &&
                Objects.equals(user.getSentMessage().get(1), "number two");
    }

    public static boolean testSeven() { // Tests load users from database from Server.java
        Server server = new Server();
        Server.loadUsersFromDatabase();
        User[] expectedOutput = {new User("alice22~851~22~chicago~alice"),
                new User("sidr415~1234~23~detroit~sid"), new User("larry22~0~22~chic~larry")};
        for (int i = 0; i < server.getUsers().size(); i++) {
            if (!Objects.equals(server.getUsers().get(i).getUsername(), expectedOutput[i].getUsername())) {
                return false; // if they have the same username then they are the same
            }
        }
        return true;
    }

    public static boolean testEight() { // tests get user by username in server
        User user = Server.getUserByUsername("alice22");
        return Objects.equals(user.getUsername(), "alice22");
    }

    public static boolean testNine() { // checks if a username that doesn't exist is found in getUserByUsername
        User user = Server.getUserByUsername("fakeUsername");
        return user == null;
    }

    public static boolean testTen() { // readMessagesForUser working
        try {
            PrintWriter pw = new PrintWriter("src/textlog.txt");
            pw.write("****alice22 - larry****\n" + // initializes text log
                    "hi larry\n" +
                    "nice day\n" +
                    "\n" +
                    "****alice22 - sid****\n" +
                    "hi sid\n" +
                    "bad day\n" +
                    "\n" +
                    "****alice22 - larry****\n" +
                    "so?");
            pw.flush();
            pw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        ///prints for comformatinon alice22 messages are intilized to her
        ArrayList<String> messages = Server.readMessagesForUser("alice22");
        for (String mess : messages) {
            System.out.println(mess);
        }
        return true;
    }
}