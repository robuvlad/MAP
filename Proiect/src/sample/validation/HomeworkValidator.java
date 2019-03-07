package sample.validation;

import sample.domain.Homework;

public class HomeworkValidator implements Validator<Homework> {

    /**
     *
     * @param homework - the homework that will be validated
     * @throws ValidationException
     * if an attribute is not valid
     */
    @Override
    public void validate(Homework homework) throws ValidationException {
        String error="";
        if (homework.getId() < 0)
            error += "Invalid id !";
        if (homework.getDescription().equals(""))
            error += "Invalid description !";
        if (homework.getDeadline() < 1 || homework.getDeadline() > 14)
            error += "Invalid deadline week!";
        if (homework.getReceived() < 1 || homework.getReceived() > 14)
            error += "Invalid received week!";
        if (homework.getReceived() >= homework.getDeadline())
            error += "Invalid deadline and received weeks !";
        if (error.length() > 0)
            throw new ValidationException(error);
    }

}
