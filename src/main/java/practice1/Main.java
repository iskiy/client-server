package practice1;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class Main {

    private static final byte MAGIC_BYTE = 0x13;

    public static void main(String[] args) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
         Message message = new Message(1, 1, "Hello world".getBytes(StandardCharsets.UTF_8));
        Packet input = new Packet((byte) 1, 111, message);
        byte[] encodedInput = Packet.encodePackage(input);
        Packet packet = Packet.decodePackage(encodedInput);
        System.out.println(new String(packet.message.getMessage(), StandardCharsets.UTF_8));;
    }
}
