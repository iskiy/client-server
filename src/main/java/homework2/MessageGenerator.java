package homework2;


import homework2.Packets.Message;
import homework2.Packets.Packet;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.ThreadLocalRandom;


/* *
* Fake message generator
* */
public class MessageGenerator {
    private static final String[] COMMANDS = new String[]{
                                                "Quantity of products",
                                                "Remove amount of products",
                                                "Enroll amount of products",
                                                "Add group of products",
                                                "Add good name to product",
                                                "Set price for product"

    };

    private static final int CLIENTS_AMOUNT = 4;
    private static final int USERS_AMOUNT = 50;
    private static final int PACKETS_AMOUNT = 200;
    private final static Integer deadCommand = Integer.MAX_VALUE;


    public MessageGenerator() {
    }

    public static int getPacketsAmount() {
        return PACKETS_AMOUNT;
    }

    public static byte[]  generatePacket(){
        int commandID = ThreadLocalRandom.current().nextInt(COMMANDS.length);
        int userID = ThreadLocalRandom.current().nextInt(USERS_AMOUNT);
        int packetID = ThreadLocalRandom.current().nextInt(PACKETS_AMOUNT);
        Integer clientID = ThreadLocalRandom.current().nextInt(CLIENTS_AMOUNT);
        Message message = new Message(commandID, userID, COMMANDS[commandID].getBytes(StandardCharsets.UTF_8));
        Packet packet =  new Packet(clientID.byteValue(), packetID, message);
        return  Encryptor.encodePackage(packet);
    }

    public static byte[] generateDeath(){
        int commandID = deadCommand;
        Message message = new Message(commandID, 1, "DEATH".getBytes(StandardCharsets.UTF_8));
            Packet packet =  new Packet((byte) 1, 1, message);
        return Encryptor.encodePackage(packet);
    }

    public static Integer getDeadCommand() {
        return deadCommand;
    }


}