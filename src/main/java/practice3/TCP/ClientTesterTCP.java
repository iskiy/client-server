package practice3.TCP;

import homework2.Packets.Message;
import homework2.Packets.Packet;

import java.io.IOException;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.Random;

public class ClientTesterTCP {
    public static void main(String[] args) throws IOException {
        for(byte i = 0; i < 8; i++){
            client(i);
        }
    }


    private static void client(byte id){
        new Thread(()->{
            try{
                Thread.sleep(new Random().nextInt(200));
                ClientTCP client = new ClientTCP(id);
                client.send(new Packet(id, 1, new Message(1, 1,
                        "Hello world".getBytes(StandardCharsets.UTF_8))));
                System.out.println(client.receive());
            } catch (InterruptedException | SocketException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        })
            .start();
    }
}
