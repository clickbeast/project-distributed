import java.util.HashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class BulletinBoard implements Chat {
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

    public boolean sendMessage(int boxNumber, String tag, String message){
        try{
            readWriteLocks[boxNumber].writeLock().lock();
            messageBoard[boxNumber].put(tag, message);
            readWriteLocks[boxNumber].writeLock().unlock();
        }
        catch(Exception e){
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public String get(int boxNumber, String tag){
        tag = globalHashFunction(tag);
        String content = null;

        readWriteLocks[boxNumber].readLock().lock();
        content = messageBoard[boxNumber].get(tag);
        readWriteLocks[boxNumber].readLock().unlock();

        return content;
    }

    //TODO: implement
    public static String globalHashFunction(String s){
        return null;
    }
}
