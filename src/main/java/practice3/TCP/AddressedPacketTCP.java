package practice3.TCP;

import homework2.Packets.Packet;

import java.net.Socket;
import java.net.SocketAddress;

public class AddressedPacketTCP {
    private final Packet packet;
    private final Socket socket;

    public AddressedPacketTCP(Packet packet, Socket socket) {
        this.packet = packet;
        this.socket = socket;
    }

    public Packet getPacket() {
        return packet;
    }

    public Socket getSocket() {
        return socket;
    }
}
