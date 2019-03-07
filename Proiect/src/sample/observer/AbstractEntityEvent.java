package sample.observer;

public abstract class AbstractEntityEvent<E> implements Event {

    private E entity, oldEntity;
    private ChangeEventType changeEventType;

    public AbstractEntityEvent(E oldEntity, E entity, ChangeEventType changeEventType) {
        this.entity = entity;
        this.oldEntity = oldEntity;
        this.changeEventType = changeEventType;
    }

    public void setEntity(E entity) {
        this.entity = entity;
    }

    public void setOldEntity(E oldEntity) {
        this.oldEntity = oldEntity;
    }

    public void setChangeEventType(ChangeEventType changeEventType) {
        this.changeEventType = changeEventType;
    }

    public E getData() {
        return entity;
    }

    public E getOldData() {
        return oldEntity;
    }

    public ChangeEventType getChangeEventType() {
        return changeEventType;
    }
}
