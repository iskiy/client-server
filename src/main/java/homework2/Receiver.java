package homework2;

import java.util.concurrent.BlockingQueue;

public class Receiver implements Runnable{
        private BlockingQueue<byte[]> queue;

        public Receiver( BlockingQueue<byte[]> receiverQueue) {
                this.queue = receiverQueue;
        }

        @Override
        public void run() {
                try{
                        for(int i = 0; i < MessageGenerator.getPacketsAmount() + 1; ++i){
                                if(i == MessageGenerator.getPacketsAmount()){
                                        queue.put(MessageGenerator.generateDeath());
                                }
                                queue.put(MessageGenerator.generatePacket());
                                if (!Server.alive) {
                                        System.out.println("CLOSE RECEIVER");
                                        return;
                                }
                        }
                } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                }
        }
//
//        private void generatePackets() {
//                for(int i = 0; i < MessageGenerator.getPacketsAmount(); ++i){
//                try {
//                        queue.put(MessageGenerator.generatePacket());
//                } catch (InterruptedException e) {
//                        e.printStackTrace();
//                }
//        }
//        }
}
