package practice3.TCP;

import java.io.IOException;

public class ServerTesterTCP {
    public static void main(String[] args) throws IOException {
        ServerTCP server = new ServerTCP();
        while(true){
            server.receive();
        }
    }
}
