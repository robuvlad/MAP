package sample.gui.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sample.domain.Homework;
import sample.domain.Student;
import sample.service.Service;
import sample.validation.ValidationException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class EditHomework {


    private Service service;
    private Stage stage;
    private Homework homework;

    @FXML private TextField idText;
    @FXML private TextField descText;
    @FXML private TextField deadlineText;
    @FXML private TextField receivedText;

    @FXML private Button addButton;

    private final String subjectAdd = "MAP - add new homework";
    private final String bodyAdd = "Hello ! \n\n Watch out ! A new homework has been added. Read it very carefully and write an email if you do not " +
            "understand it or something is wrong. \n\n Regards, \n Robu Vlad";
    private final String subjectUpdate = "MAP - update homework";
    private final String bodyUpdate = "Hello ! \n\n Watch out ! A homework has been updated. Read it very carefully and write an email if you do not " +
            "understand it or something is wrong. \n\n Regards, \n Robu Vlad";

    public EditHomework() {
        System.out.println("Edit Homework");
    }

    public void setService(Service service, Homework homework, Stage stage){
        this.service = service;
        this.homework = homework;
        this.stage = stage;
        fillWithData();
    }

    private void fillWithData(){
        if (homework.getId() == -1){
            addButton.setText("Add");
        } else {
            addButton.setText("Update");
            idText.setText(homework.getId().toString());
            idText.setEditable(false);
            descText.setText(homework.getDescription());
            deadlineText.setText(String.valueOf(homework.getDeadline()));
            receivedText.setText(String.valueOf(homework.getReceived()));
        }
    }

    @FXML
    private void editHomeworkHandler() {
        List<Student> students = FXCollections.observableList(StreamSupport.stream(service.getStudentService().findAllStudents().spliterator(), false).collect(Collectors.toList()));
        try {
            if (homework.getId() == -1) {
                service.getHomeworkService().save(new Homework(Integer.parseInt(idText.getText()), descText.getText(), Integer.parseInt(deadlineText.getText()),
                        Integer.parseInt(receivedText.getText())));

                service.sendEmail(students, subjectAdd, bodyAdd);
                stage.close();
            } else {
                service.getHomeworkService().update(new Homework(Integer.parseInt(idText.getText()), descText.getText(), Integer.parseInt(deadlineText.getText()),
                        Integer.parseInt(receivedText.getText())));

                service.sendEmail(students, subjectUpdate, bodyUpdate);
                stage.close();
            }
            clearAllHandler();
            alert();
        } catch(ValidationException e){
            alertMethod(e);
        } catch(Exception ex){
            alertMethod(ex);
        }
    }

    private void alert(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Done !");
        alert.setContentText("The emails were sent successfully !");
        alert.show();
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(
                getClass().getResource("../css/alert.css").toExternalForm());
    }

    private void alertMethod(Exception e){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText("Error");
        alert.setContentText(e.getMessage());
        alert.show();
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(
                getClass().getResource("../css/alert.css").toExternalForm());
    }

    @FXML
    private void clearAllHandler(){
        if (homework.getId() == -1)
            idText.setText("");
        descText.setText("");
        deadlineText.setText("");
        receivedText.setText("");
    }

    @FXML
    private void cancelHandler(){
        stage.close();
    }

}
