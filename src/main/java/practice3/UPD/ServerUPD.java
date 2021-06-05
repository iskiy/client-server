package practice3.UPD;

import homework2.Decryptor;
import homework2.Encryptor;
import homework2.Packets.Packet;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ServerUPD{
    public static final int PORT = 3000;
    private final DatagramSocket socket;
    private final ConcurrentMap<Byte, ClientProcessor> clientMap;

    public ServerUPD() throws SocketException {
        this.socket = new DatagramSocket(PORT);
        clientMap = new ConcurrentHashMap<>();

        new Thread(this::send, "Sender")
                .start();
        new Thread(this::receive, "Receiver")
                .start();
    }


    private void send() {
        while(true){
            try {
                AddressedPacket packet = ServerQueue.QUEUE.poll();
                if(packet != null){
                    byte[] packetBytes = Encryptor.encodePackage((packet.getPacket()));
                    DatagramPacket datagramPacket = new DatagramPacket(packetBytes, packetBytes.length, packet.getSocketAddress());
                    socket.send(datagramPacket);
                }
            } catch (IOException e) {
                    e.printStackTrace();
            }
        }
    }


    public void receive() {
        while (true) {
            DatagramPacket datagramPacket = new DatagramPacket(new byte[1000], 1000);
            try {
                socket.receive(datagramPacket);
            } catch (IOException e) {
                e.printStackTrace();
            }

            Packet packet = Decryptor.decodePackage(Arrays.copyOfRange(datagramPacket.getData(), 0, datagramPacket.getLength()));
            clientMap.computeIfAbsent(packet.getClient(), ClientProcessor::new)
                    .acceptPacket(packet, datagramPacket.getSocketAddress());
        }
    }
}
