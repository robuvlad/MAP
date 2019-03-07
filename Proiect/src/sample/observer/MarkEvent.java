package sample.observer;


import sample.domain.Mark;

public class MarkEvent implements Event {

    private Mark data, oldData;
    private ChangeEventType changeEventType;

    public MarkEvent(Mark oldData, Mark data, ChangeEventType changeEventType) {
        this.data = data;
        this.oldData = oldData;
        this.changeEventType = changeEventType;
    }

    public void setData(Mark data) {
        this.data = data;
    }

    public void setOldData(Mark oldData) {
        this.oldData = oldData;
    }

    public void setChangeEventType(ChangeEventType changeEventType) {
        this.changeEventType = changeEventType;
    }

    public Mark getData() {
        return data;
    }

    public Mark getOldData() {
        return oldData;
    }

    public ChangeEventType getChangeEventType() {
        return changeEventType;
    }
}
