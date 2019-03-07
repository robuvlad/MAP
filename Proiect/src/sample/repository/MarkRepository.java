package sample.repository;

import javafx.util.Pair;
import sample.domain.Mark;
import sample.validation.Validator;

public class MarkRepository extends AbstractRepository<Pair<Integer, Integer>, Mark> {

    public MarkRepository(Validator<Mark> validator){
        super(validator);
    }

}
