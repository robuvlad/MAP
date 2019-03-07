package sample.gui.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sample.domain.Student;
import sample.service.Service;
import sample.validation.ValidationException;

public class EditStudent {

    private Service service;
    private Stage stage;
    private Student student;

    @FXML private TextField idText;
    @FXML private TextField nameText;
    @FXML private TextField groupText;
    @FXML private TextField emailText;
    @FXML private TextField teacherText;

    @FXML private Button addButton;

    public EditStudent() {
        System.out.println("Edit Student");
    }

    public void setService(Service service, Student student, Stage stage){
        this.service = service;
        this.student = student;
        this.stage = stage;
        fillWithData();
    }

    private void fillWithData(){
        if (student.getId() == -1){
            addButton.setText("Add");
        } else {
            addButton.setText("Update");
            idText.setText(student.getId().toString());
            idText.setEditable(false);
            nameText.setText(student.getName());
            groupText.setText(String.valueOf(student.getGroup()));
            emailText.setText(student.getEmail());
            teacherText.setText(student.getTeacherName());
        }
    }

    @FXML
    private void editStudentHandler() {
        try {
            if (student.getId() == -1) {
                service.getStudentService().save(new Student(Integer.parseInt(idText.getText()), nameText.getText(), Integer.parseInt(groupText.getText()),
                        emailText.getText(), teacherText.getText()));
            } else {
                service.getStudentService().update(new Student(Integer.parseInt(idText.getText()), nameText.getText(), Integer.parseInt(groupText.getText()),
                        emailText.getText(), teacherText.getText()));
                stage.close();
            }
            clearAllHandler();
        } catch(ValidationException e){
            alertMethod(e);
        } catch(Exception ex){
            alertMethod(ex);
        }
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
        if (student.getId() == -1)
            idText.setText("");
        nameText.setText("");
        groupText.setText("");
        emailText.setText("");
        teacherText.setText("");
    }

    @FXML
    private void cancelHandler(){
        stage.close();
    }


}

