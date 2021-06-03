package homework2.Packets;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class Message {
    private int cType;
    private int bUserID;
    private byte[] message;

    public static final int INFO_LENGTH = 8;

    public Message(int cType, int bUserID, byte[] message) {
        this.cType = cType;
        this.bUserID = bUserID;
        this.message = message;
    }

    public int getCType() {
        return cType;
    }

    public int getBUserID() {
        return bUserID;
    }

    public byte[] getMessage() {
        return message;
    }

    public int fullMessageLength(){
        return INFO_LENGTH + message.length;
    }

    public byte[] bytesForPacket(){
        return ByteBuffer.allocate(fullMessageLength())
                .putInt(cType)
                .putInt(bUserID)
                .put(message)
                .array();
    }

    public void encode(){
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            SecretKey secretKey = new SecretKeySpec("thisisa128bitkey".getBytes(StandardCharsets.UTF_8), "AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            message = cipher.doFinal(message);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
    }

    public void decode(){
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            SecretKey secretKey = new SecretKeySpec("thisisa128bitkey".getBytes(StandardCharsets.UTF_8), "AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            message = cipher.doFinal(message);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
    }
}
