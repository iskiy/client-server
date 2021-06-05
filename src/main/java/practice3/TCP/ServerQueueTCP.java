package practice3.TCP;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ServerQueueTCP {
    public static final Queue<AddressedPacketTCP> QUEUE = new ConcurrentLinkedQueue<>();
}
