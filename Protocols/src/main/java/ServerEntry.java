import java.io.Serializable;

public class ServerEntry implements Serializable {
    int startMailbox, endMailbox;
    String ip;
    int portNumber;

    public ServerEntry(int startMailbox, int endMailbox, String ip, int portNumber) {
        this.startMailbox = startMailbox;
        this.endMailbox = endMailbox;
        this.ip = ip;
        this.portNumber = portNumber;
    }

    public int getStartMailbox() {
        return startMailbox;
    }

    public void setStartMailbox(int startMailbox) {
        this.startMailbox = startMailbox;
    }

    public int getEndMailbox() {
        return endMailbox;
    }

    public void setEndMailbox(int endMailbox) {
        this.endMailbox = endMailbox;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPortNumber() {
        return portNumber;
    }

    public void setPortNumber(int portNumber) {
        this.portNumber = portNumber;
    }

    public String address() {
        return ip + ":" + portNumber;
    }

    public String chatAddress() {
        return ip + ":" + (portNumber + 1);
    }

    public boolean contains(int mailbox) {
        if (startMailbox <= mailbox && endMailbox >= mailbox)
            return true;
        return false;
    }
}
