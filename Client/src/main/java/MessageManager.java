import interfaces.ThreadListener;
import model.Conversation;
import model.Message;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.nio.charset.StandardCharsets;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MessageManager {
    private static List<String> serverList = new ArrayList<>();
    private static List<Conversation> conversationList = new ArrayList<>();
    private static final String MASTER_SERVER_IP = "localhost:8999";
    private int bound = 1;

    public void sendMessage(Conversation conversation, Message message, final ThreadListener threadListener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (!message.isDelivered()) {
                    try {
                        ConnectionObject setupConnectionObject = new ConnectionObject().getWorkingConnection();
                        Registry setupRegistry = setupConnectionObject.getRegistry();
                        ClientToMasterCommunication setupChat = setupConnectionObject.getChat();
                        String currentIp = setupConnectionObject.getCurrentIp();

                        ConnectionObject messageConnectionObject = null;
                        messageConnectionObject = new ConnectionObject(setupConnectionObject,
                                conversation.getBoardKey().getNextSpot());
                        Registry messageRegistry = messageConnectionObject.getRegistry();
                        Chat messageChat = (Chat) messageConnectionObject.getChat();

                        int slotLimit = 0;
                        try {
                            slotLimit = messageChat.getLimit();
                            bound = slotLimit;
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }

                        try {
                            Random rnd = new Random();
                            int nextspot = rnd.nextInt(slotLimit);
                            String tag = conversation.getBoardKey().generateRandomString();
                            message.setDelivered(messageChat.sendMessage(conversation.getBoardKey().getNextSpot(),
                                    new String(hash(conversation.getBoardKey().getTag()), StandardCharsets.ISO_8859_1),
                                    conversation.getBoardKey().encrypt(message, slotLimit, nextspot, tag)));
                            if (message.isDelivered()) {
                                conversation.getBoardKey().setTag(tag);
                                conversation.getBoardKey().setNextSpot(nextspot);
                            }
                        } catch (RemoteException | NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeySpecException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException | InvalidAlgorithmParameterException e) {
                            e.printStackTrace();
                        }
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    } catch (NotBoundException e) {
                        e.printStackTrace();
                    }
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                threadListener.threadFinished();
            }
        }).start();
    }

    public void getMessages(final ThreadListener threadListener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        ConnectionObject setupConnectionObject = new ConnectionObject().getWorkingConnection();
                        Registry setupRegistry = setupConnectionObject.getRegistry();
                        ClientToMasterCommunication setupChat = setupConnectionObject.getChat();
                        String currentIp = setupConnectionObject.getCurrentIp();
                        for (int i = 0; i < conversationList.size(); i++) {
                            Conversation conversation = getConversationOnSpot(i);
                            if (conversation != null) {
                                ConnectionObject messageConnectionObject = null;
                                messageConnectionObject = new ConnectionObject(setupConnectionObject,
                                        conversation.getBoardKeyUs().getNextSpot());
                                Registry messageRegistry = messageConnectionObject.getRegistry();
                                Chat messageChat = (Chat) messageConnectionObject.getChat();
                                try {
                                    String text = messageChat.getMessage(conversation.getBoardKeyUs().getNextSpot(),
                                            conversation.getBoardKeyUs().getTag());
                                    if (text != null && !text.equals("")) {
                                        Message message = conversation.getBoardKeyUs().decrypt(text,
                                                conversation.getUserId());

                                        threadListener.newMessage(message, conversation);
                                    }
                                } catch (RemoteException | NoSuchAlgorithmException | InvalidKeyException | NoSuchPaddingException | BadPaddingException | InvalidKeySpecException | IllegalBlockSizeException | InvalidAlgorithmParameterException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    } catch (RemoteException | NotBoundException e) {
                        e.printStackTrace();
                    }

                    try {
                        Thread.sleep(100000000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

    }

    private synchronized Chat getServer() {
        return null;
    }

    private synchronized String getFirstIp() {
        return serverList.get(0);
    }

    private synchronized void removeFirstIp() {
        serverList.remove(0);
    }

    private static byte[] hash(String string) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] passwordByte = string.getBytes(StandardCharsets.ISO_8859_1);
        return digest.digest(passwordByte);

    }

    private synchronized Conversation getConversationOnSpot(int i) {
        if (conversationList.size() > i) {
            return conversationList.get(i);

        }
        return null;
    }

    public synchronized void addConversation(Conversation conversation) {
        conversationList.add(conversation);
    }

    public synchronized void removeConversation(Conversation conversation) {
        conversationList.remove(conversation);
    }

    public synchronized void clearConversations() {
        conversationList = new ArrayList<>();
    }

    public synchronized int getLastBound() {
        return bound;

    }

    private class ConnectionObject {
        private Registry registry;
        ClientToMasterCommunication chat;
        private String currentIp;

        public ConnectionObject(ConnectionObject connectionObject, int nextSpot) throws RemoteException,
                NotBoundException {
            while (this.registry == null || this.chat == null) {
                String ip = connectionObject.chat.getServerWithMailbox(nextSpot);
                try {
                    Thread.sleep(100000000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


                if (ip.equals(connectionObject.currentIp)) {
                    this.registry = connectionObject.registry;
                    this.chat = (Chat) connectionObject.chat;
                } else {
                    this.registry = LocateRegistry.getRegistry(ip.split(":")[0],
                            Integer.parseInt(ip.split(":")[1]));
                    this.chat = (Chat) this.registry.lookup("Chat");
                }
            }
        }

        public ConnectionObject() {

        }

        public Registry getRegistry() {
            return registry;
        }

        public ClientToMasterCommunication getChat() {
            return chat;
        }

        public String getCurrentIp() {
            return currentIp;
        }

        public ConnectionObject getWorkingConnection() {
            registry = null;
            chat = null;
            currentIp = "";
            for (int i = 0; i < serverList.size(); i++) {
                currentIp = getFirstIp();
                try {
                    registry = LocateRegistry.getRegistry(currentIp.split(":")[0],
                            Integer.parseInt(currentIp.split(":")[1]));
                    chat = (Chat) registry.lookup("Chat");
                } catch (RemoteException | NotBoundException e) {
                    e.printStackTrace();
                    removeFirstIp();
                }
                if (chat != null) {
                    i = serverList.size();
                }
            }
            while (registry == null || chat == null) {
                try {
                    registry = LocateRegistry.getRegistry(MASTER_SERVER_IP.split(":")[0],
                            Integer.parseInt(MASTER_SERVER_IP.split(":")[1]));
                    chat = (ClientToMasterCommunication) registry.lookup("ClientToMasterCommunication");
                    currentIp = MASTER_SERVER_IP;
                } catch (RemoteException | NotBoundException e) {
                    e.printStackTrace();
                }

                try {
                    Thread.sleep(100000000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return this;
        }
    }
}
