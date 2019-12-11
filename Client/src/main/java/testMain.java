import model.BoardKey;

import java.security.NoSuchAlgorithmException;

public class testMain {
    public static void main(String[] args) throws NoSuchAlgorithmException {
        BoardKey boardKey = new BoardKey("abc", "abc123", 123123);
        System.out.println(boardKey.getKey());
        boardKey.generateNextKey();
        //TODO: RUn this, check if it gives same results for everyone
        if (boardKey.getKey().equals("d3d9caad85bf9bda3edb4f9a15952dfa")) {
            System.out.println(true);
        }
        boardKey.generateNextKey();
        if (boardKey.getKey().equals("4dd18370dc4b8832141036d6dc35e7f9")) {
            System.out.println(true);
        }
        boardKey.generateNextKey();
        if (boardKey.getKey().equals("c5d881dceeed6ce61d3b4f21cded4726")) {
            System.out.println(true);
        }
    }
}
