import java.util.HashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class BulletinBoard {
    /**
     * Iedere cel in het messageBoard houdt een set (hier een
     * HashMap) bij van entries, elke entry bestaat uit een
     * tag (waarmee de ontvanger het bericht kan vinden) en
     * een value (deze value bevat het bericht en de index+tag
     * van het volgende bericht).
     * De key in deze HashMaps is de tag en de waarde is de value.
     */
    private HashMap<String, String>[] messageBoard;

    /**
     * Ieder mailbox mag door meerdere threads gelezen worden
     * en mag door maximum 1 thread gewrite worden; Daarom
     * houden we een ReadWriteLock bij per cell in de mailbox
     */
    private ReadWriteLock[] readWriteLocks;

    public BulletinBoard(int numberOfCells, int capacityPerCell){
        messageBoard = new HashMap[numberOfCells];
        readWriteLocks = new ReadWriteLock[numberOfCells];

        for(int i=0;i<numberOfCells;i++) {
            messageBoard[i] = new HashMap<String, String>(capacityPerCell);
            readWriteLocks[i] = new ReentrantReadWriteLock();
        }
    }

    public void add(int index, String tag, String value){
        readWriteLocks[index].writeLock().lock();
        messageBoard[index].put(tag, value);
        readWriteLocks[index].writeLock().unlock();
    }

    public String get(int index, String preImageTag){
        String tag = globalHashFunction(preImageTag);
        String content = null;

        readWriteLocks[index].readLock().lock();
        content = messageBoard[index].get(tag);
        readWriteLocks[index].readLock().unlock();

        return content;
    }

    //TODO: implement
    public static String globalHashFunction(String s){
        return null;
    }
}
