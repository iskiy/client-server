package practice3.UPD;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ServerQueue {
    public static final Queue<AddressedPacket> QUEUE = new ConcurrentLinkedQueue<>();
}
