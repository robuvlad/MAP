package sample.repository;

import sample.domain.Homework;
import sample.validation.Validator;

public class HomeworkRepository extends AbstractRepository<Integer, Homework> {

    /**
     *
     * @param validator
     */
    public HomeworkRepository(Validator<Homework> validator){
        super(validator);
    }

}
