package sample.domain;

public class Homework implements HasId<Integer> {

    private int id, deadline, received;
    private String description;

    /**
     *
     * @param id
     * @param description
     * @param deadline
     * @param received
     */
    public Homework(int id, String description, int deadline, int received){
        this.id = id;
        this.description = description;
        this.deadline = deadline;
        this.received = received;
    }

    /**
     * get id
     * @return
     */
    @Override
    public Integer getId() {
        return id;
    }

    /**
     * set id
     * @param id
     */
    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * set deadline
     * @param deadline
     */
    public void setDeadline(int deadline) {
        this.deadline = deadline;
    }

    /**
     * set received
     * @param received
     */
    public void setReceived(int received) {
        this.received = received;
    }

    /**
     * set description
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * get deadline
     * @return
     */
    public int getDeadline() {
        return deadline;
    }

    /**
     * get received
     * @return
     */
    public int getReceived() {
        return received;
    }

    /**
     * get description
     * @return
     */
    public String getDescription() {
        return description;
    }

    /**
     * override toString
     * @return homework
     */
    @Override
    public String toString() {
        return "Homework{" +
                "id=" + id +
                ", deadline=" + deadline +
                ", received=" + received +
                ", description='" + description + '\'' +
                '}';
    }
}
