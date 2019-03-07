package sample.domain;

import javafx.util.Pair;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.Calendar;
import java.util.Locale;

public class Mark implements HasId<Pair<Integer, Integer>> {

    private int studentId;
    private int homeworkId;
    private double value;
    private LocalDate date;
    private Pair<Integer, Integer> pair;
    private String feedback;

    /**
     * constructor
     * @param studentId
     * @param homeworkId
     * @param value
     */
    public Mark(int studentId, int homeworkId, double value, String feedback){
        this.date = LocalDate.now();
        this.studentId = studentId;
        this.homeworkId = homeworkId;
        this.value = value;
        pair = new Pair<>(studentId, homeworkId);
        this.feedback = feedback;
    }

    public Mark(int studentId, int homeworkId, double value, String localDate, String feedback){
        this.studentId = studentId;
        this.homeworkId = homeworkId;
        this.value = value;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        this.date = LocalDate.parse(localDate, formatter);
        pair = new Pair<>(studentId, homeworkId);
        this.feedback = feedback;
    }

    @Override
    public Pair<Integer, Integer> getId(){
        return this.pair;
    }

    @Override
    public void setId(Pair<Integer, Integer> p){
        this.pair = p;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public void setHomeworkId(int homeworkId) {
        this.homeworkId = homeworkId;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getStudentId() {
        return studentId;
    }

    public int getHomeworkId() {
        return homeworkId;
    }

    public double getValue() {
        return value;
    }

    public String getDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedString = date.format(formatter);
        return formattedString;
    }

    public LocalDate getDateD(){
        return date;
    }

//    public int getNumberOfWeeks(){
//        WeekFields weekFields = WeekFields.of(Locale.getDefault());
//        int weekNumber = date.get(weekFields.weekOfWeekBasedYear());
//        Calendar calendar = Calendar.getInstance();
//        try {
//            calendar.setTime(new SimpleDateFormat("dd/MM/yyyy").parse("30/09/2018"));
//        }catch(ParseException ex){
//            ex.getStackTrace();
//        }
//        int firstWeek = calendar.get(Calendar.WEEK_OF_YEAR);
//        return weekNumber - firstWeek;
//    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    //    @Override
//    public String toString() {
//        return "Mark{" +
//                "studentId=" + studentId +
//                ", homeworkId=" + homeworkId +
//                ", value=" + value +
//                ", date=" + date +
//                '}';
//    }


}
