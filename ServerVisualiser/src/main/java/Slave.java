import java.util.Arrays;
import java.util.Objects;

public class Slave {

    private int startBox;
    private String name;
    private Mailbox[] mailboxes;

    public Slave(int size, int startBox) {
        mailboxes = new Mailbox[size];
        this.startBox = startBox;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Mailbox[] getMailboxes() {
        return mailboxes;
    }

    public void setMailboxes(Mailbox[] mailboxes) {
        this.mailboxes = mailboxes;
    }

    public void addMessage(MailBoxEntry mailBoxEntry) {
        mailboxes[mailBoxEntry.getBoxNumber() - startBox].addMessage(new Message(mailBoxEntry.getTag(),
                mailBoxEntry.getMessage()));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Slave slave = (Slave) o;
        return Objects.equals(name, slave.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    public void checkBounds(ServerEntry serverEntry) {
        if (mailboxes.length != serverEntry.endMailbox - serverEntry.startMailbox) {
            mailboxes=new Mailbox[serverEntry.endMailbox - serverEntry.startMailbox];
        }
    }

    @Override
    public String toString() {
        return "Slave{" +
                "startBox=" + startBox +
                ", name='" + name + '\'' +
                ", mailboxes=" + Arrays.toString(mailboxes) +
                '}';
    }
}
