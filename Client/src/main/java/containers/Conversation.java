package containers;


/**
 * Describes a conversation
 */
public class Conversation {

    private String mostRecentMessage;
    private String id;


    //Subject to change
    private String partner;


    public Conversation(String mostRecentMessage, String id, String partner) {
        this.mostRecentMessage = mostRecentMessage;
        this.id = id;
        this.partner = partner;
    }


}
