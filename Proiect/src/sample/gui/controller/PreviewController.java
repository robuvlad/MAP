package sample.gui.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import sample.domain.Homework;
import sample.domain.Mark;
import sample.domain.Student;
import sample.service.Service;
import sample.validation.ValidationException;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


public class PreviewController {

    private Service service;
    private Stage stage;
    private Student student;
    private Homework homework;
    private double valueMark;
    private String feedback;
    private Boolean checkBox;

    @FXML private Label homeworkLabel;
    @FXML private Label absentLabel;
    @FXML private Label studentLabel;
    @FXML private Label markLabel;
    @FXML private TextFlow feedbackTextFlow;

    private final String subjectAdd = "MAP - add new grade";
    private final String bodyAdd = "Hello ! \n\n Watch out ! You got a new grade. Please, check out and let me now if" +
            " everything is fine ." +
            "\n\n Regards, \n Robu Vlad";

    public PreviewController() {
        System.out.println("Preview Controller");
    }

    public void setService(Service service, Stage stage, String homework, Boolean checkBox, Student student, String valueMark, String feedback){
        this.service = service;
        this.checkBox = checkBox;
        this.stage = stage;
        this.student = student;
        service.getHomeworkService().findAllHomeworks().forEach(x -> {if (x.getDescription().equals(homework)) this.homework = x;});
        this.valueMark = (checkBox == false) ? (Double.parseDouble(valueMark) - service.getMarkService().getDelay(homework)) : Double.parseDouble(valueMark);
        this.feedback = feedback;
        setLabels();
    }

    private void setLabels(){
        homeworkLabel.setText(homework.getDescription());
        absentLabel.setText((checkBox == false) ? "False" : "True");
        studentLabel.setText(student.getName());
        markLabel.setText(String.valueOf(valueMark));
        Text text = new Text(feedback);
        feedbackTextFlow.getChildren().add(text);

        homeworkLabel.setAlignment(Pos.CENTER);
        absentLabel.setAlignment(Pos.CENTER);
        studentLabel.setAlignment(Pos.CENTER);
        markLabel.setAlignment(Pos.CENTER);
    }

    @FXML
    private void okHandler(){
        double newValue = (checkBox == false) ? (valueMark + service.getMarkService().getDelay(homework.getDescription())) : valueMark;
        try {
            service.getMarkService().saveMark(new Mark(student.getId(), homework.getId(), newValue, feedback), checkBox);

            List<Student> students = Arrays.asList(student);
            service.sendEmail(students, subjectAdd, bodyAdd);

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText("Confirmed !");
            alert.setContentText("Mark added successfully !");
            alert.show();
            stage.close();
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(
                    getClass().getResource("../css/alert.css").toExternalForm());
        } catch (ValidationException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Error");
            alert.setContentText(e.getMessage());
            alert.show();
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(
                    getClass().getResource("../css/alert.css").toExternalForm());
        }
    }

    @FXML
    private void cancelHandler(){
        stage.close();
    }


}
