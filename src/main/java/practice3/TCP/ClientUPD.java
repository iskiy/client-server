package practice3.TCP;

import homework2.Decryptor;
import homework2.Encryptor;
import homework2.Packets.Packet;
import practice3.UPD.Client;
import practice3.UPD.ServerUPD;

import java.io.IOException;
import java.net.*;
import java.util.Arrays;

public class ClientUPD implements Client {
    private final DatagramSocket socket;
    private final byte clientID;
    public ClientUPD(byte clientID) throws SocketException {
        this.clientID = clientID;
        this.socket = new DatagramSocket();
    }

    @Override
    public void send(Packet packet) throws UnknownHostException {
        byte[] bytes = Encryptor.encodePackage(packet);
        DatagramPacket datagramPacket = new DatagramPacket(bytes,bytes.length, InetAddress.getByName(null), ServerUPD.PORT);

        try {
            socket.send(datagramPacket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Packet receive() {
        DatagramPacket packet = new DatagramPacket(new byte[1000],1000);
        try {
            socket.receive(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Decryptor.decodePackage(Arrays.copyOfRange(packet.getData(), 0, packet.getLength()));
    }

    @Override
    public boolean isConnectionAvailable() {
        return false;
    }

}
