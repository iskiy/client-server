package homework2;

import homework2.Packets.CRC16;
import homework2.Packets.Message;
import homework2.Packets.Packet;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.concurrent.BlockingQueue;

public class Decryptor implements Runnable {
    private BlockingQueue<byte[]> receiverQueue;
    private BlockingQueue<Packet> decryptQueue;

    public Decryptor(BlockingQueue<byte[]> receiverQueue, BlockingQueue<Packet> decryptQueue) {
        this.receiverQueue = receiverQueue;
        this.decryptQueue = decryptQueue;
    }

    @Override
    public void run() {
        try{
            while (true) {
                if (!Server.alive) {
                    System.out.println("CLOSE Decryptor");
                    return;
                }
                decryptQueue.put(decodePackage(receiverQueue.take()));
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static Packet decodePackage(byte[] bytes){
        ByteBuffer bb = ByteBuffer.wrap(bytes).order(ByteOrder.BIG_ENDIAN);

        if(bb.get() != Packet.getMagicByte()){
            throw new IllegalArgumentException("Magic byte");
        }

        byte client = bb.get();
        System.out.println("client: " + client);
        long packet = bb.getLong();
        System.out.println("packetID: " + packet);
        int messageLength = bb.getInt();
        System.out.println("length: " + messageLength);
        short crc16Head = bb.getShort();
        System.out.println("crc16 head: " + crc16Head);

        byte[] head = ByteBuffer.allocate(14)
                .order(ByteOrder.BIG_ENDIAN)
                .put(Packet.getMagicByte())
                .put(client)
                .putLong(packet)
                .putInt(messageLength)
                .array();

        if(CRC16.crc16(head) != crc16Head){
            throw new IllegalArgumentException("CRC16 head");
        }

        int cType = bb.getInt();
        int bUserID = bb.getInt();


        byte[] checkMessage = Arrays.copyOfRange(bytes, 16, 16 + messageLength);
        short crc16Message = bb.getShort(16 + messageLength);
        if(CRC16.crc16(checkMessage) != crc16Message){
            throw new IllegalArgumentException("CRC16 message");
        }

        byte[] text = Arrays.copyOfRange(bytes, 24, 24 + messageLength - Message.INFO_LENGTH);
        Message message = new Message(cType, bUserID, text);
        message.decode();
        return new Packet(client, packet, message);
    }
}
