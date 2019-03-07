package sample.service;


import javafx.collections.FXCollections;
import javafx.util.Pair;
import sample.domain.*;
import sample.observer.*;
import sample.observer.Observable;
import sample.observer.Observer;
import sample.repository.HomeworkRepository;
import sample.repository.MarkRepository;
import sample.repository.StudentRepository;
import sample.validation.ValidationException;


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;

public class Service {

//    private StudentRepository repoStudent;
//    private HomeworkRepository repoHomework;
//    private MarkRepository repoMark;
    //private ArrayList<Observer<StudentEvent>> list;

    private StudentService studentService;
    private HomeworkService homeworkService;
    private MarkService markService;

    /**
     * constructor
     *
     * @param studentService
     * @param homeworkService
     * @param markService
     */
    public Service(StudentService studentService, HomeworkService homeworkService, MarkService markService) {
        this.studentService = studentService;
        this.homeworkService = homeworkService;
        this.markService = markService;
    }

    public StudentService getStudentService() {
        return studentService;
    }

    public HomeworkService getHomeworkService() {
        return homeworkService;
    }

    public MarkService getMarkService() {
        return markService;
    }

    public void sendEmail(List<Student> students, String subject, String body){
        List<Email> emails = new ArrayList<>();
        for(Student student : students){
            emails.add(new Email(student.getEmail(), subject, body));
        }

        ExecutorService executors = Executors.newFixedThreadPool(5);

        for(Email email : emails){
            executors.submit(new EmailService(email, subject, body));
        }

        executors.shutdown();
    }


    public List<Pair<String, Double>> marksCertainHome(String description) {
        List<Mark> markList = FXCollections.observableList(StreamSupport.stream(markService.findAllMarks().spliterator(), false).collect(Collectors.toList()));
        Map<String, Double> map = markList.stream().filter(x -> homeworkService.findOneHomework(x.getHomeworkId()).getDescription().equals(description))
                .collect(Collectors.toMap(x -> studentService.findOneStudent(x.getStudentId()).getName(), Mark::getValue));
        return map.entrySet().stream().map(x -> new Pair<String, Double>(x.getKey(), x.getValue())).collect(Collectors.toList());
    }

    public List<StudentHomework> marksCertainStudent(String name) {
        List<StudentHomework> finalList = new ArrayList<>();
        List<Mark> markList = FXCollections.observableList(StreamSupport.stream(markService.findAllMarks().spliterator(), false).collect(Collectors.toList()));
        List<Mark> secondList = markList.stream()
                .filter(x -> studentService.findOneStudent(x.getStudentId()).getName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
        secondList.forEach(x -> {
            Student st = studentService.findOneStudent(x.getStudentId());
            Homework hw = homeworkService.findOneHomework(x.getHomeworkId());
            finalList.add(new StudentHomework(st.getId(), st.getName(), hw.getId(), hw.getDescription(), x.getValue()));
        });
        return finalList;
    }

    public List<Pair<String, List<Double>>> marksCertainDate(String month) {
        List<Mark> markList = FXCollections.observableList(StreamSupport.stream(markService.findAllMarks().spliterator(), false).collect(Collectors.toList()));
        Map<String, List<Double>> map = markList.stream()
                .filter(x -> x.getDateD().getMonth().toString().equals(month.toUpperCase()))
                .collect(Collectors.groupingBy(x -> studentService.findOneStudent(x.getStudentId()).getName(),
                        mapping(Mark::getValue, toList())));
        return map.entrySet().stream().map(e -> new Pair<String, List<Double>>(e.getKey(), e.getValue())).collect(Collectors.toList());
    }

    public List<Pair<String, Double>> filterByGroup(Integer group, String homework) {
        List<Mark> marks = FXCollections.observableList(StreamSupport.stream(markService.findAllMarks().spliterator(), false).collect(Collectors.toList()));
        List<Mark> newMarks = marks.stream().filter(x -> studentService.findOneStudent(x.getStudentId()).getGroup() == group)
                .filter(x -> homeworkService.getRepoHomework().findOne(x.getHomeworkId()).getDescription().toString().equals(homework))
                .collect(Collectors.toList());

        Map<String, Double> map = newMarks.stream().collect(Collectors.toMap(x -> studentService.findOneStudent(x.getStudentId()).getName(), Mark::getValue));
        return map.entrySet().stream().map(x -> new Pair<String, Double>(x.getKey(), x.getValue())).collect(Collectors.toList());
    }

    public List<Pair<String, Double>> reportToughHomework() {
        List<Mark> marks = FXCollections.observableList(StreamSupport.stream(markService.findAllMarks().spliterator(), false).collect(Collectors.toList()));
        Map<String, List<Double>> map = marks.stream()
                .collect(Collectors.groupingBy(x -> homeworkService.findOneHomework(x.getHomeworkId()).getDescription(),
                        mapping(Mark::getValue, toList())));
        Map<String, Double> map2 = map.entrySet().stream()
                .collect(Collectors.toMap(x -> x.getKey(), y -> {
                    List<Double> l = y.getValue();
                    int count = l.size();
                    double m = l.stream().reduce(0d, (a, b) -> a + b);
                    double finalD = Math.round(m / count * 100);
                    return finalD / 100;
                }));
        List<Pair<String, Double>> allPairs = map2.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .map(x -> new Pair<String, Double>(x.getKey(), x.getValue()))
                .collect(Collectors.toList());
//        Pair<String, Double> finalPair = allPairs.stream().findFirst().get();
        return allPairs;
    }

    public List<Pair<String, Double>> reportPassedStudents(Boolean passed) {
        List<Mark> marks = FXCollections.observableList(StreamSupport.stream(markService.findAllMarks().spliterator(), false).collect(Collectors.toList()));
        List<Homework> homeworks = FXCollections.observableList(StreamSupport.stream(homeworkService.findAllHomeworks().spliterator(), false).collect(Collectors.toList()));
        Map<String, List<Double>> map = marks.stream()
                .collect(Collectors.groupingBy(x -> studentService.findOneStudent(x.getStudentId()).getName(),
                        mapping(Mark::getValue, toList())));
        Map<String, Double> map2 = map.entrySet().stream()
                .collect(Collectors.toMap(x -> x.getKey(), y -> {
                    List<Double> l = y.getValue();
                    int count = homeworks.size();
                    double m = l.stream().reduce(0d, (a, b) -> a + b);
                    double finalD = Math.round(m / count * 100);
                    return finalD / 100;
                }));
        List<Pair<String, Double>> allPairs = map2.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .map(x -> new Pair<String, Double>(x.getKey(), x.getValue()))
                .collect(Collectors.toList());
        List<Pair<String, Double>> finalPairs = (passed == true) ?
                allPairs.stream().filter(x -> x.getValue() >= 5).collect(Collectors.toList()) :
                allPairs.stream().filter(x -> x.getValue() < 5).collect(Collectors.toList());
        return finalPairs;
    }

    public List<Pair<Integer, Double>> reportPerGroup(){
        List<Mark> marks = FXCollections.observableList(StreamSupport.stream(markService.findAllMarks().spliterator(), false).collect(Collectors.toList()));
        List<Student> students = FXCollections.observableList(StreamSupport.stream(studentService.findAllStudents().spliterator(), false).collect(Collectors.toList()));
        List<Homework> homeworks = FXCollections.observableList(StreamSupport.stream(homeworkService.findAllHomeworks().spliterator(), false).collect(Collectors.toList()));

        Map<Integer, Long> mapOfStudent = students.stream()
                .collect(Collectors.groupingBy(Student::getGroup, counting()));

        Map<Integer, List<Double>> map = marks.stream()
                .collect(Collectors.groupingBy(x -> studentService.findOneStudent(x.getStudentId()).getGroup(),
                        mapping(Mark::getValue, toList())));
        Map<Integer, Double> map2 = map.entrySet().stream()
                .collect(Collectors.toMap(x -> x.getKey(), y -> {
                    List<Double> l = y.getValue();
                    int count = homeworks.size();
                    long howManyStudents = mapOfStudent.get(y.getKey());
                    double m = l.stream().reduce(0d, (a,b) -> a+b);
                    double finalD = Math.round(m / (count * howManyStudents) * 100);
                    return finalD / 100;
                }));
        return map2.entrySet().stream().sorted(Map.Entry.comparingByKey())
                .map(x -> new Pair<Integer, Double>(x.getKey(), x.getValue()))
                .collect(Collectors.toList());
    }

    public List<Pair<String, Long>> reportCountStudents(){
        List<Student> students = FXCollections.observableList(StreamSupport.stream(studentService.findAllStudents().spliterator(), false).collect(Collectors.toList()));
        Map<String, Long> map = students.stream()
                .collect(Collectors.groupingBy(Student::getTeacherName, counting()));
        return map.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .map(x -> new Pair<String, Long>(x.getKey(), x.getValue())).collect(Collectors.toList());
    }

}
