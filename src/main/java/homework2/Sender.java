package homework2;

import java.util.concurrent.BlockingQueue;

public class Sender implements Runnable{
    private BlockingQueue<byte[]> encodedQueue;

    public Sender(BlockingQueue<byte[]> encodedQueue) {
        this.encodedQueue = encodedQueue;
    }


    @Override
    public void run() {
        try{
            while (true) {
                if(!Server.alive) {
                    System.out.println("CLOSE SENDER");
                    return;
                }
                encodedQueue.take();
                printAnswer();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void printAnswer(){
        System.out.println("OK");
    }
}
