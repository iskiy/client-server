package homework2;

import homework2.Packets.Packet;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Server {
    private final static int NUM_OF_THREADS = 4;
    private final static int BOUND = 8;
    private static BlockingQueue<byte[]> receiverQueue = new LinkedBlockingQueue<>(BOUND);
    private static BlockingQueue<Packet> decryptQueue = new LinkedBlockingQueue<>(BOUND);
    private static BlockingQueue<Packet> answerQueue = new LinkedBlockingQueue<>(BOUND);
    private static  BlockingQueue<byte[]> encryptQueue = new LinkedBlockingQueue<>(BOUND);
    public static Boolean alive = true;

    public static void start() {
        for (int i = 0; i < NUM_OF_THREADS; i++) {
            new Thread(new Receiver(receiverQueue)).start();
        }

        for (int i = 0; i < NUM_OF_THREADS; i++) {
            new Thread(new Decryptor(receiverQueue,decryptQueue)).start();
        }
        for (int i = 0; i < NUM_OF_THREADS; i++) {
            new Thread(new Processor(decryptQueue, answerQueue)).start();
        }
        for (int i = 0; i < NUM_OF_THREADS; i++) {
            new Thread(new Encryptor(answerQueue, encryptQueue)).start();
        }
        for (int i = 0; i < NUM_OF_THREADS; i++) {
            new Thread(new Sender(encryptQueue)).start();
        }

    }
}
