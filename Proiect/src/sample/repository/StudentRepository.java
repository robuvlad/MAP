package sample.repository;

import sample.domain.Student;
import sample.validation.Validator;

public class StudentRepository extends AbstractRepository<Integer, Student> {

    /**
     *
     * @param validator
     */
    public StudentRepository(Validator<Student> validator){
        super(validator);
    }

}
