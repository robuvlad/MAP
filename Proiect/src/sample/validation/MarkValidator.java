package sample.validation;

import sample.domain.Mark;

public class MarkValidator implements Validator<Mark> {

    @Override
    public void validate(Mark mark) throws ValidationException{
        String error = "";
        if (mark.getStudentId() < 1)
            error += "Invalid student id !";
        if (mark.getHomeworkId() < 1)
            error += "Invalid homework id !";
        if (mark.getValue() < 1 || mark.getValue() > 10)
            error += "Invalid value !";
        if (!error.equals(""))
            throw new ValidationException(error);
    }

}
