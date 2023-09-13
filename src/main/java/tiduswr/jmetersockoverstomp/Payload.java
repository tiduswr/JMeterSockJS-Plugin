package tiduswr.jmetersockoverstomp;

public class Payload {
    private Long id;
    private String message;
    private String sender;
    private Boolean read;
    private String status;

    public Payload(){}

    public Payload(Long id, String message, String sender, Boolean read, String status) {
        this.id = id;
        this.message = message;
        this.sender = sender;
        this.read = read;
        this.status = status;
    }

    // Getter para id
    public Long getId() {
        return id;
    }

    // Setter para id
    public void setId(Long id) {
        this.id = id;
    }

    // Getter para message
    public String getMessage() {
        return message;
    }

    // Setter para message
    public void setMessage(String message) {
        this.message = message;
    }

    // Getter para sender
    public String getSender() {
        return sender;
    }

    // Setter para sender
    public void setSender(String sender) {
        this.sender = sender;
    }

    // Getter para read
    public Boolean getRead() {
        return read;
    }

    // Setter para read
    public void setRead(Boolean read) {
        this.read = read;
    }

    // Getter para status
    public String getStatus() {
        return status;
    }

    // Setter para status
    public void setStatus(String status) {
        this.status = status;
    }
}

