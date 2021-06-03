package homework2;

import homework2.Packets.CRC16;
import homework2.Packets.Message;
import homework2.Packets.Packet;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.concurrent.BlockingQueue;

public class Encryptor implements Runnable{
    private BlockingQueue<Packet> answerQueue;
    private BlockingQueue<byte[]> encryptQueue;

    public Encryptor(BlockingQueue<Packet> answerQueue, BlockingQueue<byte[]> encryptQueue) {
        this.answerQueue = answerQueue;
        this.encryptQueue = encryptQueue;
    }

    @Override
    public void run() {
        try{
            while (true) {
                if(!Server.alive) {
                    System.out.println("CLOSE Encryptor");
                    return;
                }
                encryptQueue.put(encodePackage(answerQueue.take()));
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static byte[] encodePackage(Packet packet){
        Message message = packet.getMessage();
        message.encode();
        byte[] head = ByteBuffer.allocate(14)
                .order(ByteOrder.BIG_ENDIAN)
                .put(Packet.getMagicByte())
                .put(packet.getClient())
                .putLong(packet.getPacketID())
                .putInt(message.fullMessageLength())
                .array();

        return ByteBuffer.allocate(16 + message.fullMessageLength() + 2)
                .order(ByteOrder.BIG_ENDIAN)
                .put(head)
                .putShort(CRC16.crc16(head))
                .put(message.bytesForPacket())
                .putShort(CRC16.crc16(message.bytesForPacket()))
                .array();
    }
}
