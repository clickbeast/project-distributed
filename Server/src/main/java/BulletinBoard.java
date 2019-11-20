import java.util.HashMap;

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

    public BulletinBoard(int numberOfCells, int capacityPerCell){
        messageBoard = new HashMap[numberOfCells];

        for(int i=0;i<numberOfCells;i++)
            messageBoard[i] = new HashMap<String, String>(capacityPerCell);
    }

    public void add(int index, String tag, String value){
        messageBoard[index].put(tag, value);
    }

    public String get(int index, String preImageTag){
        String tag = globalHashFunction(preImageTag);

        return messageBoard[index].get(tag);
    }

    //TODO: implement
    public static String globalHashFunction(String s){
        return null;
    }
}
