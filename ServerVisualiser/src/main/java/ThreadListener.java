import model.Mailbox;

public interface ThreadListener {
 Mailbox onUpdate(Mailbox m,int box);

}
