package practice3.TCP;

import homework2.Decryptor;
import homework2.Encryptor;
import homework2.Packets.Packet;

import java.io.*;
import java.net.*;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ServerTCP{
    public static final int PORT = 3000;
    private final ServerSocket serverSocket;
    private final ConcurrentMap<Byte, ClientTCPHandler> clientMap;
    private OutputStream out;

    public ServerTCP() throws IOException {
        this.serverSocket = new ServerSocket(PORT);
        clientMap = new ConcurrentHashMap<>();

        new Thread(this::send, "Sender")
                .start();
        new Thread(this::receive, "Receiver")
                .start();
    }


    private void send() {
        while(true){
            try {
                AddressedPacketTCP packet = ServerQueueTCP.QUEUE.poll();
                if(packet != null){
                    byte[] packetBytes = Encryptor.encodePackage((packet.getPacket()));
                    Socket clientSocket = packet.getSocket();

                    out = clientSocket.getOutputStream();
                    out.write(packetBytes);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public void receive() {
        while (true) {
            byte[] packetBytes = new byte[1000];
            try {
                Socket socket = serverSocket.accept();
                InputStream stream = socket.getInputStream();
                int dataLength = stream.read(packetBytes);
                System.out.println(Arrays.toString(Arrays.copyOfRange(packetBytes, 0, dataLength)));
                Packet packet = Decryptor.decodePackage(Arrays.copyOfRange(packetBytes, 0, dataLength));
                clientMap.computeIfAbsent(packet.getClient(), ClientTCPHandler::new)
                        .acceptPacket(packet, socket);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

