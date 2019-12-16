public class Slave {

    private int startBox;
    private String name;
    private Mailbox[] mailboxes;

    public Slave(int size, int startBox) {
        mailboxes = new Mailbox[size];
        this.startBox = startBox;


    }

    public Slave(String name, Slave backup) {
        this.name = name;
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

    public void setMailBox(int i, Object  mailBoxEntry) {
        MailBoxEntry
       // mailboxes[i] = new Mailbox(i + startBox);
    }

}
