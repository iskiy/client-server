package practice1;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.*;

class PacketTest {

    @ParameterizedTest
    @CsvSource({
            "10, 222, 1, 2, hello world",
            "4, 321, 7622, 4, tra rewtra tra",
            "7, 4357, 432, 4, dsalkd",
            "5, 43631, 6546, 43, sdskfasofp fapsok sad "

    })
    void shouldEncodePackage(byte client, long packet, int cType, int bUserID, String text) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        Message message = new Message(cType, bUserID, text.getBytes(StandardCharsets.UTF_8));
        Packet input = new Packet(client, packet, message);
        byte[] encodedInput = Packet.encodePackage(input);

        Packet decoded = Packet.decodePackage(encodedInput);

        assertEquals(client, decoded.getClient());
        assertEquals(packet, decoded.getPacketID());
        assertEquals(cType, decoded.getMessage().getCType());
        assertEquals(bUserID, decoded.getMessage().getBUserID());
        assertEquals(text, new String(decoded.getMessage().getMessage(), StandardCharsets.UTF_8));
    }
}