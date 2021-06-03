package homework2.Packets;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import homework2.Packets.Message;

public class Packet {
    private static final byte MAGIC_BYTE = 0x13;
    private final byte client;
    private final long packetID;
    Message message;

    public Packet(byte client, long packetID, Message message) {
        this.client = client;
        this.packetID = packetID;
        this.message = message;
    }

    public static byte getMagicByte() {
        return MAGIC_BYTE;
    }

    public byte getClient() {
        return client;
    }

    public long getPacketID() {
        return packetID;
    }

    public Message getMessage() {
        return message;
    }

//    public static byte[] encodePackage(Packet packet) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
//        Message message = packet.getMessage();
//        message.encode();
//        byte[] head = ByteBuffer.allocate(14)
//                .order(ByteOrder.BIG_ENDIAN)
//                .put(MAGIC_BYTE)
//                .put(packet.getClient())
//                .putLong(packet.getPacketID())
//                .putInt(message.fullMessageLength())
//                .array();
//
//        return ByteBuffer.allocate(16 + message.fullMessageLength() + 2)
//                .order(ByteOrder.BIG_ENDIAN)
//                .put(head)
//                .putShort(practice1.CRC16.crc16(head))
//                .put(message.bytesForPacket())
//                .putShort(CRC16.crc16(message.bytesForPacket()))
//                .array();
//    }
    @Override
    public String toString() {
        return "Packet{" +
                "client=" + client +
                ", packet=" + packetID +
                ", message=" + message.toString() +
                '}';
    }
}
