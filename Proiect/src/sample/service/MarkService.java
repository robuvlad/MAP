package sample.service;

import javafx.collections.FXCollections;
import javafx.util.Pair;
import sample.domain.Homework;
import sample.domain.Mark;
import sample.domain.Student;
import sample.domain.StudentHomework;
import sample.observer.*;
import sample.repository.HomeworkRepository;
import sample.repository.MarkRepository;
import sample.repository.Repository;
import sample.repository.StudentRepository;
import sample.validation.ValidationException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class MarkService implements Observable<StudHomeEvent> {

    private Repository<Pair<Integer, Integer>, Mark> repoMark;
    private StudentService studentService;
    private HomeworkService homeworkService;
    private ArrayList<Observer<StudHomeEvent>> listMark;


    public MarkService(MarkRepository repoMark, StudentService studentService, HomeworkService homeworkService){
        this.repoMark = repoMark;
        this.studentService = studentService;
        this.homeworkService = homeworkService;
        listMark = new ArrayList<>();
    }


    @Override
    public void addObserver(Observer<StudHomeEvent> observer) {
        listMark.add(observer);
    }

    @Override
    public void removeObserver(Observer<StudHomeEvent> observer) {
        listMark.remove(observer);
    }

    @Override
    public void notifyObserver(StudHomeEvent event) {
        listMark.forEach(x -> x.update(event));
    }


    public Repository<Pair<Integer, Integer>, Mark> getRepoMark() {
        return repoMark;
    }

    public HomeworkService getHomeworkService() {
        return homeworkService;
    }

    /**
     *
     * @param description - description of homework
     * @return number of points that should be diminished
     */
    public double getDelay(String description){
        int deadline=0;
        for(Homework h : homeworkService.findAllHomeworks()){
            if (h.getDescription().equals(description))
                deadline = h.getDeadline();
        }
        double  n=0;
        if (homeworkService.currentWeek() - deadline == 1)
            n = 2.5;
        if (homeworkService.currentWeek() - deadline == 2)
            n = 5;
        return n;
    }


    /**
     * change mark's value if the current week is greater than deadline
     * decreased by 2.5p for each week
     * can be decreased twice
     * @param mark
     * @return
     */
    public double changeValue(Mark mark, Boolean isAbsent){
        Homework homework = homeworkService.findOneHomework(mark.getHomeworkId());
        int numberOfWeeks = homeworkService.currentWeek() - homework.getDeadline();
        if (numberOfWeeks > 2)
            return -10;
        double newValue = mark.getValue();
        if (isAbsent)
            return newValue;
        if (numberOfWeeks == 1)
            newValue -= 2.5;
        else if (numberOfWeeks == 2)
            newValue -= 5;
        return newValue;
    }



    /**
     *
     * @param mark
     * @param isAbsent
     * @return
     * @throws ValidationException
     */
    public Mark saveMark(Mark mark, Boolean isAbsent) throws ValidationException {
        Student student = studentService.findOneStudent(mark.getStudentId());
        Homework homework = homeworkService.findOneHomework(mark.getHomeworkId());
        checkMark(mark, student, homework, isAbsent);
        Mark mark2 = new Mark(mark.getStudentId(), mark.getHomeworkId(), changeValue(mark, isAbsent), mark.getFeedback());
        try {
            repoMark.save(mark2);
            notifyThis(mark2);
            return mark2;
        }catch(IllegalArgumentException ex){
            ex.getStackTrace();
        }
        return null;
    }

    public void checkMark(Mark mark, Student student, Homework homework, Boolean isAbsent) throws ValidationException{
        if (mark.getValue() < 1 || mark.getValue() > 10)
            throw new ValidationException("Invalid mark !");
        if (student == null || homework == null)
            throw new ValidationException("Student or homework id invalid !");
        if (repoMark.findOne(new Pair(student.getId(), homework.getId())) != null)
            throw new ValidationException("This already exists !");
        if (changeValue(mark, isAbsent) == -10)
            throw new ValidationException("Homework is too old !");
    }

    /**
     *
     * @return all marks entities
     */
    public Iterable<Mark> findAllMarks(){
        return repoMark.findAll();
    }

    /**
     * notify the observer with new data
     * @param mark
     */
    private void notifyThis(Mark mark){
        Student student = studentService.findOneStudent(mark.getStudentId());
        Homework homework = homeworkService.findOneHomework(mark.getHomeworkId());
        StudentHomework studentHomework = new StudentHomework(student.getId(), student.getName(), homework.getId(),
                homework.getDescription(), mark.getValue());
        notifyObserver(new StudHomeEvent(null, studentHomework, ChangeEventType.ADD));
    }

    public List<StudentHomework> getStudHome(){
        List<Mark> markList = FXCollections.observableList(StreamSupport.stream(findAllMarks().spliterator(), false).collect(Collectors.toList()));
        List<StudentHomework> finalList = new ArrayList<>();
        for(Mark mark : markList){
            Student student = studentService.findOneStudent(mark.getStudentId());
            Homework homework = homeworkService.findOneHomework(mark.getHomeworkId());
            StudentHomework studentHomework = new StudentHomework(student.getId(), student.getName(), homework.getId(),
                    homework.getDescription(), mark.getValue());
            finalList.add(studentHomework);
        }
        return finalList;
    }

//    /**
//     *
//     * @return the current week
//     */
//    public int currentWeek(){
//        int currentWeek;
//        Calendar calendar = Calendar.getInstance();
//        Date date = new Date();
//        calendar.setTime(date);
//        if (calendar.get(Calendar.WEEK_OF_YEAR) == 52 || calendar.get(Calendar.WEEK_OF_YEAR) == 1)
//            currentWeek = 51;
//        else if (calendar.get(Calendar.WEEK_OF_YEAR) == 2)
//            currentWeek = 52;
//        else if (calendar.get(Calendar.WEEK_OF_YEAR) == 3)
//            currentWeek = 53;
//        else
//            currentWeek = calendar.get(Calendar.WEEK_OF_YEAR);
//
//        try {
//            calendar.setTime(new SimpleDateFormat("dd/MM/yyyy").parse("30/09/2018"));
//        }catch(ParseException ex){
//            ex.getStackTrace();
//        }
//        int firstWeek = calendar.get(Calendar.WEEK_OF_YEAR);
//        int result = currentWeek - firstWeek;
//        return result;
//    }

    public Mark deleteMark(Pair<Integer, Integer> pair){
        try {
            Mark mark = repoMark.delete(pair);
            return mark;
        }catch(IllegalArgumentException ex){
            ex.getStackTrace();
            return null;
        }
    }

}
