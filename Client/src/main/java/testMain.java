import model.BoardKey;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class testMain {
    public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeySpecException {
        BoardKey boardKey = new BoardKey("abc", "abc123", 123123);
        System.out.println(boardKey.getKey().equals("abc"));
        boardKey.generateNextKey();
        System.out.println(boardKey.getKey().equals("9134ffcb2bc22ceae22bc2a86ae18f35a99cb04efbf11e7bfcf2367bd6eec176b8eb86760d7f9ebb609f345741017172b7053e2b0d4bf779a9028f1d6c6fbf4180cffdbd8e975c9ed1748b1c88ddbadda25595be25d1a96205a37677155439433095fbde3c3992cbfdd5ad80559adf073cc02b20db0fc05d38e2c62e471e9fe0"));
        boardKey.generateNextKey();

        System.out.println(boardKey.getKey().equals("66ca479db2acdb104c86e226df73bd8ab8ce68dfd4d6a32a52ffc08a1fa115a9ddc976c761917653ea51d7a431e44d53dfa71816a720595c20a6cf39b9b536408817e2ce514e5750d3354834e44c7498ce30b7c3a0aeecffcd67346765ee3090088c6cc65e2abedad99de6f92dec3117469037bf19f827b2d689d5eb30511e7d"));
        boardKey.generateNextKey();

        System.out.println(boardKey.getKey().equals("6ecdf0459b0c9fc35e7bf6e7595fd18e5dca9a8d375ebfa4e06d1d98ebdf3f1f9cfbe69a3bbb04417a8b0b1d5f43937a4e6a1a8de0d7357fd0f10215fe54e16cbd592fe2356b21d507b927dbeee10ebbd883ced028a2ccac9038384dd3360a3c5d9d998b15146894d799b788202c3d1728565525d9ffc0f6a1ffb40e1affb1ec"));
        boardKey.generateNextKey();

        System.out.println(boardKey.getKey().equals("5fe1b61c96a699f6bad42137a26324b5c09acb158b8f0a822151d5db34c0af8c23faf037ebee5960d05b608783174e711d7a45288fd0f90d75f202d016fdc9a2b506ac41b9f818bb5d9b386eaadde5d59506a63bee5719c92873098f58c980f94c3fc828075549b1f68657c70fcd6cd426426d18c1418b2f9e6dd6fc1ba0ce6a"));

    }
}
