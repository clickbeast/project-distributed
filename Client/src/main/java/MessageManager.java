import interfaces.ThreadListener;
import model.Conversation;
import model.Message;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;

public class MessageManager {
    private static List<String> serverList;
    private static final String MASTER_SERVER_IP = "localhost:9000";

    public void sendMessage(Conversation conversation, Message message, final ThreadListener threadListener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (!message.isDelivered()) {
                    Registry setupRegistry = null;
                    Chat setupChat = null;
                    String currentIp = "";
                    for (int i = 0; i < serverList.size(); i++) {
                        currentIp = getFirstIp();
                        try {
                            setupRegistry = LocateRegistry.getRegistry(currentIp.split(":")[0],
                                    Integer.parseInt(currentIp.split(":")[1]));
                            setupChat = (Chat) setupRegistry.lookup("Chat");
                        } catch (RemoteException | NotBoundException e) {
                            e.printStackTrace();
                            removeFirstIp();
                        }
                    }
                    while (setupRegistry == null || setupChat == null) {
                        try {
                            setupRegistry = LocateRegistry.getRegistry(MASTER_SERVER_IP.split(":")[0],
                                    Integer.parseInt(MASTER_SERVER_IP.split(":")[1]));
                            setupChat = (Chat) setupRegistry.lookup("Chat");
                        } catch (RemoteException | NotBoundException e) {
                            e.printStackTrace();
                        }
                    }
                    Registry messageRegistry = null;
                    Chat messageChat = null;
                    while (messageRegistry == null || messageChat == null) {
                        try {
                            String ip = setupChat.getIpAndPortNumber(conversation.getBoardKey().getNextSpot());
                            if (ip.equals(currentIp)) {
                                messageRegistry = setupRegistry;
                                messageChat = setupChat;
                            } else {
                                messageRegistry = LocateRegistry.getRegistry(ip.split(":")[0],
                                        Integer.parseInt(ip.split(":")[1]));
                                messageChat = (Chat) setupRegistry.lookup("Chat");
                            }
                        } catch (RemoteException | NotBoundException e) {
                            e.printStackTrace();
                        }
                    }
                    int slotLimit = messageChat.getLimit();

                    try {
                        message.setDelivered(messageChat.sendMessage(conversation.getBoardKey().getNextSpot(),
                                conversation.getBoardKey().getTag(),
                                conversation.getBoardKey().encrypt(message, slotLimit)));
                    } catch (RemoteException | NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeySpecException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
                        e.printStackTrace();
                    }
                }

                threadListener.threadFinished();
            }
        }).start();
    }

    private synchronized String getFirstIp() {
        return serverList.get(0);
    }

    private synchronized void removeFirstIp() {
        serverList.remove(0);
    }
}
