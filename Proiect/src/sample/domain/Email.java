package sample.domain;

public class Email {

    private String receiver, subject, body;

    public Email(String receiver, String subject, String body) {
        this.receiver = receiver;
        this.subject = subject;
        this.body = body;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "Email{" +
                "receiver='" + receiver + '\'' +
                ", subject='" + subject + '\'' +
                ", body='" + body + '\'' +
                '}';
    }
}
