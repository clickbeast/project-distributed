import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Arrays;
import java.util.Objects;

public class Slave {

    private int startBox;
    private String name;
    int size;
    private ObservableList<Mailbox> mailboxes;

    public Slave(int size, int startBox, String name) {
        this.size = size;
        mailboxes = FXCollections.observableArrayList();
        for (int i = 0; i < size; i++) {
            mailboxes.add(new Mailbox(startBox + i));
        }
        this.startBox = startBox;
        this.name = name;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public void addMessage(MailBoxEntry mailBoxEntry) {
        Message m = new Message(mailBoxEntry.getTag(), mailBoxEntry.getMessage());
        if (!mailboxes.get(mailBoxEntry.getBoxNumber() - startBox).containsMessage(m)) {
            mailboxes.get(mailBoxEntry.getBoxNumber() - startBox).addMessage(m);
        }
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

    @Override
    public String toString() {
        return "Slave{" +
                "startBox=" + startBox +
                ", name='" + name + '\'' +
                ", size=" + size +
                ", mailboxes=" + mailboxes +
                '}';
    }

    public int getStartBox() {
        return startBox;
    }

    public void setStartBox(int startBox) {
        this.startBox = startBox;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public ObservableList<Mailbox> getMailboxes() {
        return mailboxes;
    }

    public void setMailboxes(ObservableList<Mailbox> mailboxes) {
        this.mailboxes = mailboxes;
    }
}
