package practice3.UPD;

import java.io.IOException;

public class ServerTesterUPD {
    public static void main(String[] args) throws IOException {
        ServerUPD server = new ServerUPD();
        while(true){
            server.receive();
        }
    }
}
