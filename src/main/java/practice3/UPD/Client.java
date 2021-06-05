package practice3.UPD;

import homework2.Packets.Packet;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.net.UnknownHostException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public interface Client {

    void send(Packet packet) throws IOException;

    Packet receive() throws IOException;

    boolean isConnectionAvailable();
}
