package sample.gui.controller;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import sample.service.Service;

import javax.swing.text.View;
import java.io.IOException;

public class MainController {

    private Service service;
    private Stage primaryStage;
    @FXML private AnchorPane mainAnchorPane;

    public MainController() {
        System.out.println("Main C");
    }

    public void setController(Service service, Stage primaryStage){
        this.service = service;
        this.primaryStage = primaryStage;
    }

    @FXML
    private void openStudents(){
        try {
            Stage stage = new Stage();
            stage.setTitle("Student");
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainController.class.getResource("../fxml/student.fxml"));
            AnchorPane root = loader.load();

            Scene scene = new Scene(root);
            stage.setScene(scene);
            StudentController controller = loader.getController();
            controller.setService(service, stage, primaryStage);
            stage.show();

            primaryStage.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    private void openHomeworks(){
        try{
            Stage stage = new Stage();
            stage.setTitle("Homework");
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainController.class.getResource("../fxml/homework.fxml"));
            AnchorPane root = loader.load();

            Scene scene = new Scene(root);
            stage.setScene(scene);
            HomeworkController homeworkController = loader.getController();
            homeworkController.setService(service, stage, primaryStage);
            stage.show();
            primaryStage.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }


    @FXML
    private void openGrades(){
        try {
            Stage stage = new Stage();
            stage.setTitle("Grade");
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainController.class.getResource("../fxml/grade.fxml"));
            AnchorPane root = loader.load();

            stage.setScene(new Scene(root));
            GradeController gradeController = loader.getController();
            gradeController.setService(service, stage, primaryStage);
            stage.show();

            primaryStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void openFilters(){
        try{
            Stage stage2 = new Stage();
            stage2.setTitle("Filters");
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(GradeController.class.getResource("../fxml/filter.fxml"));
            AnchorPane root = loader.load();

            stage2.setScene(new Scene(root));
            FilterController previewController = loader.getController();
            previewController.setService(service, stage2, primaryStage);
            stage2.show();
            primaryStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void openReports(){
        try{
            Stage stage2 = new Stage();
            stage2.setTitle("Reports");
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(ReportController.class.getResource("../fxml/report.fxml"));
            AnchorPane root = loader.load();

            stage2.setScene(new Scene(root));
            ReportController reportController = loader.getController();
            reportController.setService(service, stage2, primaryStage);
            stage2.show();
            primaryStage.close();
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    private void cancelHandler(){
        primaryStage.close();
    }

}
