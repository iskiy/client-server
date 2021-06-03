package homework2;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ServerTest {

    @Test
    public void DeathThreadsTest() throws InterruptedException {
        int startAmount = Thread.activeCount();
        Server.start();
        Server.alive = false;
        Thread.sleep(1000);
        int currAmount = Thread.activeCount();
        assert(startAmount == currAmount);
    }



}