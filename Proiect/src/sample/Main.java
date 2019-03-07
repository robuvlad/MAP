package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import sample.database.DatabaseHomeworkRepository;
import sample.database.DatabaseMarkRepository;
import sample.database.DatabaseStudentRepository;
import sample.domain.Homework;
import sample.domain.Mark;
import sample.domain.Student;
import sample.file.HomeworkFileRepository;
import sample.file.MarkFileRepository;
import sample.file.StudentFileRepository;
import sample.gui.controller.MainController;
import sample.repository.HomeworkRepository;
import sample.repository.MarkRepository;
import sample.repository.StudentRepository;
import sample.service.*;
import sample.validation.HomeworkValidator;
import sample.validation.MarkValidator;
import sample.validation.StudentValidator;
import sample.validation.Validator;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        Validator<Student> studentValidator = new StudentValidator();
        Validator<Homework> homeworkValidator = new HomeworkValidator();
        Validator<Mark> markValidator = new MarkValidator();
//        StudentRepository studentRepository = new StudentFileRepository("students.txt", studentValidator);
//        HomeworkRepository homeworkRepository = new HomeworkFileRepository("homeworks.txt", homeworkValidator);
//        MarkRepository markRepository = new MarkFileRepository("catalog.txt", markValidator);
        StudentRepository studentRepository = new DatabaseStudentRepository(studentValidator);
        HomeworkRepository homeworkRepository = new DatabaseHomeworkRepository(homeworkValidator);
        MarkRepository markRepository = new DatabaseMarkRepository(markValidator);
        StudentService studentService = new StudentService(studentRepository);
        HomeworkService homeworkService = new HomeworkService(homeworkRepository);
        MarkService markService = new MarkService(markRepository, studentService, homeworkService);
        Service service = new Service(studentService, homeworkService, markService);

        Parent root;
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("gui/fxml/main.fxml"));
        root = loader.load();
        MainController mainController = loader.getController();
        mainController.setController(service, primaryStage);

        primaryStage.setTitle("App");
        Scene scene = new Scene(root);
        //scene.getStylesheets().add("https://fonts.googleapis.com/css?family=Aclonica");
        //scene.getStylesheets().add("https://fonts.googleapis.com/css?family=Rokkitt");
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
