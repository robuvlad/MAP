package sample.observer;

import sample.domain.Student;

public class StudentEvent extends AbstractEntityEvent<Student>{


    public StudentEvent(Student oldEntity, Student entity, ChangeEventType changeEventType) {
        super(oldEntity, entity, changeEventType);
    }
}
