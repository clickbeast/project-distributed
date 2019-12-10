import java.util.LinkedList;

public class Server {
    private static final int CELLS_PER_BOARD = 512;
    private static final int CAPACITY_PER_CELL = 5;
    /**
     * twee bulletin boards, dit is voor als
     * er moet worden overgeschakeld naar een
     * groter board.
     * Dit is de werkwijze in het paper maar kan
     * mss beter?
     */
    private static BulletinBoard boardA, boardB;
    /**
     * Een lijst van threads, een per user
     * waarmee de server is aan het praten
     */
    private static LinkedList<CommunicationThread> communicationThreads;

    /**
     * Deze thread ontvangt nieuwe inkomende connecties.
     * De server maakt dan een nieuwe CommunicationThread
     * waarmee hij voor de rest van de tijd communiceert
     * met deze client.
     */
    private static CommunicationThread communicationThread;

    public static void main(String[] args) {
        boardA = new BulletinBoard();
        boardA.sendMessage(0, "abc", "123123");
        System.out.println(boardA.getMessage(0, "abc"));
    }
}
