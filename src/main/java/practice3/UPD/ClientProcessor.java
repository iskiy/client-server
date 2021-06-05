package practice3.UPD;

import homework2.Packets.Message;
import homework2.Packets.Packet;

import java.net.SocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ClientProcessor extends Thread{
    private final byte clientID;
    private final Queue<Packet> queue = new ConcurrentLinkedQueue<>();
    private SocketAddress socketAddress;

    public ClientProcessor(final byte clientID){
        this.clientID = clientID;
        start();
    }

    public void acceptPacket(Packet requestPacket, SocketAddress socketAddress){
        this.socketAddress = socketAddress;
        queue.add(requestPacket);
    }

    @Override
    public void run(){
        while(true){
            Packet packet = queue.poll();
            if(packet != null){
                System.out.println(String.format("[client %s] Processing packet %s",
                        clientID, new String(packet.getMessage().getMessage())));
                Packet responsePacket = new Packet(clientID, 1L,
                        new Message(1, 1, "accepted".getBytes(StandardCharsets.UTF_8)));
                ServerQueue.QUEUE.add(new AddressedPacket(responsePacket, socketAddress));
            }
        }
    }
}
