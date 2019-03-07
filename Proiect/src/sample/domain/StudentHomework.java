package sample.domain;

import javafx.util.Pair;

public class StudentHomework implements HasId<Pair>{

    private int studentId, homeworkId;
    private double mark;
    private String studentName, homeName;
    private Pair pair;

    public StudentHomework(int studentId, String studentName, int homeworkId, String homeName, double mark){
        this.studentId = studentId;
        this.homeworkId = homeworkId;
        this.mark = mark;
        this.studentName = studentName;
        this.homeName = homeName;
        pair = new Pair(studentId, homeworkId);
    }

    @Override
    public Pair getId() {
        return this.pair;
    }

    @Override
    public void setId(Pair pair) {
        this.pair = pair;
    }

    public int getStudentId() {
        return studentId;
    }

    public int getHomeworkId() {
        return homeworkId;
    }

    public double getMark() {
        return mark;
    }

    public String getStudentName() {
        return studentName;
    }

    public String getHomeName() {
        return homeName;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public void setHomeworkId(int homeworkId) {
        this.homeworkId = homeworkId;
    }

    public void setMark(int mark) {
        this.mark = mark;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public void setHomeName(String homeName) {
        this.homeName = homeName;
    }
}
