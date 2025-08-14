

import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Objects;

public interface MessageSystemInterface { // made by Cole
    User getSender();
    void setSender(User sender);
    User getReceiver();
    void setReceiver(User receiver);
    ArrayList<String> getMessages();
    void setMessages(ArrayList<String> messages);
}





