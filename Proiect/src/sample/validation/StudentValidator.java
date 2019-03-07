package sample.validation;

import sample.domain.Student;

public class StudentValidator implements Validator<Student> {

    /**
     *
     * @param student - the student that will be validated
     * @throws ValidationException
     * if an attribute is not valid
     */
    @Override
    public void validate(Student student) throws ValidationException {
        String error="";
        if (student.getId() < 0)
            error += "Invalid id !";
        if (student.getName().equals(""))
            error += "Invalid name !";
        if (student.getGroup() < 221 || student.getGroup() > 227)
            error += "Invalid group !";
        if (student.getEmail().equals("") || !student.getEmail().contains("@"))
            error += "Invalid email !";
        if (student.getTeacherName().equals(""))
            error += "Invalid teacher name !";
        if (error.length() > 0)
            throw new ValidationException(error);
    }

}
