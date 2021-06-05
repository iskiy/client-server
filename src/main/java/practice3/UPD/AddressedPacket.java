package practice3.UPD;

import homework2.Packets.Packet;

import java.net.SocketAddress;

public class AddressedPacket {
    private final Packet packet;
    private final SocketAddress socketAddress;

    public AddressedPacket(final Packet packet,final  SocketAddress socketAddress) {
        this.packet = packet;
        this.socketAddress = socketAddress;
    }

    public Packet getPacket() {
        return packet;
    }

    public SocketAddress getSocketAddress() {
        return socketAddress;
    }
}
