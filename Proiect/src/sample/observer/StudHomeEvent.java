package sample.observer;


import sample.domain.StudentHomework;

public class StudHomeEvent implements Event {

    private StudentHomework data, oldData;
    private ChangeEventType changeEventType;

    public StudHomeEvent(StudentHomework oldData, StudentHomework data, ChangeEventType changeEventType) {
        this.data = data;
        this.oldData = oldData;
        this.changeEventType = changeEventType;
    }

    public StudentHomework getData() {
        return data;
    }

    public StudentHomework getOldData() {
        return oldData;
    }

    public ChangeEventType getChangeEventType() {
        return changeEventType;
    }

    public void setData(StudentHomework data) {
        this.data = data;
    }

    public void setOldData(StudentHomework oldData) {
        this.oldData = oldData;
    }

    public void setChangeEventType(ChangeEventType changeEventType) {
        this.changeEventType = changeEventType;
    }
}
