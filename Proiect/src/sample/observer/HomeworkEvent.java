package sample.observer;

import sample.domain.Homework;

public class HomeworkEvent extends AbstractEntityEvent<Homework> {

    public HomeworkEvent(Homework oldEntity, Homework entity, ChangeEventType changeEventType) {
        super(oldEntity, entity, changeEventType);
    }
}
