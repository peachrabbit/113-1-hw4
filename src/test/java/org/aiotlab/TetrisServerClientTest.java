package org.aiotlab;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class TetrisServerClientTest {

    private static TetrisMTServer server;
    private int server_port = 10612;

    @BeforeAll
    public static void beforeTest() {
        server = new TetrisMTServer();
        server.startServer();
    }

    @Test
    public void testConnection() {
        try {
            TetrisTestTCPClient client = new TetrisTestTCPClient();
            client.connect("localhost", 10612);
            client.disconnect();
        }
        catch (Exception exp) {
            System.out.println(exp.getMessage());
            fail();
        }
    }

    @Test
    public void testCommandStart() {
        try {
            TetrisTestTCPClient client = new TetrisTestTCPClient();
            client.connect();

            client.start();
            Boolean isGameOver = client.isGameOver();
            assertFalse(isGameOver, "After starting a new game, the isGameOver should be false");

            int linesRemoved = client.getLinesRemoved();
            assertEquals(linesRemoved, 0, "After starting a new game, the number of lines should be 0");

            client.disconnect();
        }
        catch (Exception exp) {
            System.out.println(exp.getMessage());
            fail();
        }
    }

    @Test
    public void testReceivedImage() {
        try {
            TetrisTestTCPClient client = new TetrisTestTCPClient();
            client.connect();

            client.sendRequest("start");
            BufferedImage img = client.getScreenshot();
            assertNotNull(img, "Check parsePacket() to see if the received image is saved.");
            saveScreenshot("ServerScreenshot.png", client.getScreenshot());
        } catch (Exception exp) {
            System.out.println(exp.getMessage());
            fail();
        }
    }

    @Test
    public void testRemoveLines() {
        try {
            TetrisTestTCPClient client = new TetrisTestTCPClient();
            client.connect();

            client.sendRequest("start");
            // Because of request-response pattern, server will first generate a random piece.
            // So we need to drop squares at least three times to guarantee removing lines
            for (int i=0; i<3; i++) {
                for (int x = -6; x <= 4; x += 2) {
                    // Create a new square Tetrominoe
                    // Due to request-response pattern, new_piece will cause server to call drop_down first
                    client.sendRequest("new_piece 5");
                    client.move(x);
                }
            }

            saveScreenshot("SquareDropTest.png", client.getScreenshot());

            int linesRemoved = client.getLinesRemoved();
            System.out.println("Total lines removed: " + linesRemoved);
            // We can only be sure that lines removed should be more than 1
            assertTrue(linesRemoved > 2, "The removed lines of square drop test should be larger that 2. Check SquareDropTest.png");

            client.disconnect();
        }
        catch (Exception exp) {
            System.out.println(exp.getMessage());
            fail();
        }
    }

    private void saveScreenshot(String filename, BufferedImage bImg) {
        try {
            File outputfile = new File(filename);
            ImageIO.write(bImg, "PNG", outputfile);
        }
        catch (IOException exp) {
            System.out.println(exp.getMessage());
        }
    }
}