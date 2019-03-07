package sample.service;

import sample.domain.Student;
import sample.observer.ChangeEventType;
import sample.observer.Observable;
import sample.observer.Observer;
import sample.observer.StudentEvent;
import sample.repository.Repository;
import sample.repository.StudentRepository;
import sample.validation.ValidationException;

import java.util.ArrayList;

public class StudentService implements Observable<StudentEvent>{

    private Repository<Integer, Student> studentRepository;
    private ArrayList<Observer<StudentEvent>> list;

    public StudentService(StudentRepository studentRepository){
        this.studentRepository = studentRepository;
        list = new ArrayList<>();
    }

    public ArrayList<Observer<StudentEvent>> getList() {
        return list;
    }

    @Override
    public void addObserver(Observer<StudentEvent> observer) {
        list.add(observer);
    }

    @Override
    public void removeObserver(Observer<StudentEvent> observer) {
        list.remove(observer);
    }

    @Override
    public void notifyObserver(StudentEvent event) {
        list.forEach(x -> x.update(event));
    }

    public Repository<Integer, Student> getStudentRepository() {
        return studentRepository;
    }

    /**
     *
     * @param id
     * @return the entity with the specified id
     * or null - if there is no entity with the given id
     */
    public Student findOneStudent(int id){
        try {
            return studentRepository.findOne(id);
        } catch(IllegalArgumentException ex){
            ex.getStackTrace();
            return null;
        }
    }

    /**
     *
     * @param student
     * @return null- if the given entity is saved
     * otherwise returns the entity (id already exists)
     * @throws ValidationException
     * if the entitty is not valid
     */
    public Student save(Student student) throws ValidationException {
        try {
            Student s = studentRepository.save(student);
            notifyObserver(new StudentEvent(null, student, ChangeEventType.ADD));
            return s;
        }catch(IllegalArgumentException ex){
            ex.getStackTrace();
        }
        return null;
    }

    /**
     *
     * @param id
     * @return the removed entity or null
     * if there is no entity with the given id
     */
    public Student delete(int id) throws ValidationException{
        try {
            Student student = studentRepository.delete(id);
            if (student == null)
                throw new ValidationException("This entity does not exist !");
            notifyObserver(new StudentEvent(student, student, ChangeEventType.REMOVE));
            return student;
        }catch(IllegalArgumentException ex){
            ex.getStackTrace();
            return null;
        }
    }

    /**
     *
     * @param student
     * @return null - if the entity is updated,
     * otherwise returns the entity - (e.g id does not exist).
     */
    public Student update(Student student) throws ValidationException{
        try {
            Student s = studentRepository.update(student);
            Student oldStudent = studentRepository.findOne(student.getId());
            if (oldStudent == null)
                throw new ValidationException("This entity does not exist !");
            notifyObserver(new StudentEvent(oldStudent,student,ChangeEventType.UPDATE));
            return s;
        }catch(IllegalArgumentException ex){
            ex.getStackTrace();
            return null;
        }
    }

    /**
     *
     * @return all entities
     */
    public Iterable<Student> findAllStudents(){
        return studentRepository.findAll();
    }


}
