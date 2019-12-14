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
import java.util.ArrayList;
import java.util.List;

public class MessageManager {
    private static List<String> serverList = new ArrayList<>();
    private static List<Conversation> conversationList = new ArrayList<>();
    private static final String MASTER_SERVER_IP = "localhost:5000";

    public void sendMessage(Conversation conversation, Message message, final ThreadListener threadListener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (!message.isDelivered()) {
                    ConnectionObject setupConnectionObject = new ConnectionObject().getWorkingConnection();
                    Registry setupRegistry = setupConnectionObject.getRegistry();
                    Chat setupChat = setupConnectionObject.getChat();
                    String currentIp = setupConnectionObject.getCurrentIp();

                    ConnectionObject messageConnectionObject = new ConnectionObject(setupConnectionObject,
                            conversation.getBoardKey().getNextSpot());
                    Registry messageRegistry = messageConnectionObject.getRegistry();
                    Chat messageChat = messageConnectionObject.getChat();

                    int slotLimit = 0;
                    try {
                        slotLimit = messageChat.getLimit();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }

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

    public void getMessages(final ThreadListener threadListener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    ConnectionObject setupConnectionObject = new ConnectionObject().getWorkingConnection();
                    Registry setupRegistry = setupConnectionObject.getRegistry();
                    Chat setupChat = setupConnectionObject.getChat();
                    String currentIp = setupConnectionObject.getCurrentIp();
                    for (int i = 0; i < conversationList.size(); i++) {
                        Conversation conversation = getConversationOnSpot(i);
                        if (conversation != null) {
                            ConnectionObject messageConnectionObject = new ConnectionObject(setupConnectionObject,
                                    conversation.getBoardKeyUs().getNextSpot());
                            Registry messageRegistry = messageConnectionObject.getRegistry();
                            Chat messageChat = messageConnectionObject.getChat();
                            try {
                                String text = messageChat.getMessage(conversation.getBoardKeyUs().getNextSpot(),
                                        conversation.getBoardKeyUs().getTag());
                                if (text != null && !text.equals("")) {
                                    Message message = new Message(text, conversation.getUserId(),
                                            System.currentTimeMillis(), false, true, false);

                                    threadListener.newMessage(message, conversation);
                                }
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    try {
                        Thread.sleep(100);
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

    private class ConnectionObject {
        private Registry registry;
        private Chat chat;
        private String currentIp;

        public ConnectionObject(ConnectionObject connectionObject, int nextSpot) {
            while (this.registry == null || this.chat == null) {
                try {
                    String ip = connectionObject.chat.getIpAndPortNumber(nextSpot);
                    if (ip.equals(connectionObject.currentIp)) {
                        this.registry = connectionObject.registry;
                        this.chat = connectionObject.chat;
                    } else {
                        this.registry = LocateRegistry.getRegistry(ip.split(":")[0],
                                Integer.parseInt(ip.split(":")[1]));
                        this.chat = (Chat) connectionObject.registry.lookup("Chat");
                    }
                } catch (RemoteException | NotBoundException e) {
                    e.printStackTrace();
                }
            }
        }

        public ConnectionObject() {

        }

        public Registry getRegistry() {
            return registry;
        }

        public Chat getChat() {
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
                    chat = (Chat) registry.lookup("Chat");
                    currentIp = MASTER_SERVER_IP;
                } catch (RemoteException | NotBoundException e) {
                    e.printStackTrace();
                }
            }
            return this;
        }
    }
}
