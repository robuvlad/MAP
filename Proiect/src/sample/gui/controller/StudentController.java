package sample.gui.controller;


import javafx.animation.FadeTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.Pair;
import sample.domain.Mark;
import sample.domain.Student;
import sample.observer.ChangeEventType;
import sample.observer.Observer;
import sample.observer.StudentEvent;
import sample.service.Service;
import sample.validation.ValidationException;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class StudentController implements Observer<StudentEvent> {

    private Service service;
    private Stage stage;
    private Stage primaryStage;
    private ObservableList<Student> list;

    @FXML private TableView<Student> tableView;
    @FXML private TableColumn<Student, Integer> idColumn;
    @FXML private TableColumn<Student, String> nameColumn;
    @FXML private TableColumn<Student, Integer> groupColumn;
    @FXML private TableColumn<Student, String> emailColumn;
    @FXML private TableColumn<Student, String> teacherColumn;

    @FXML private Button addButton;
    @FXML private Button updateButton;
    @FXML private Button removeButton;

    @FXML private Popup popup = new Popup();
    @FXML private MenuItem aboutMenuItem;
    @FXML private AnchorPane mainAnchorPane;
    @FXML private ImageView imageView = new ImageView();

    public StudentController() {
        System.out.println("Student C");
    }

    public void setService(Service service, Stage stage, Stage primaryStage){
        this.service = service;
        this.stage = stage;
        this.primaryStage = primaryStage;
        this.service.getStudentService().addObserver(this);

        addButton.setDisable(true);
        updateButton.setDisable(true);
        removeButton.setDisable(true);

        loadData();
    }

    @Override
    public void update(StudentEvent event) {
        if(event.getChangeEventType() == ChangeEventType.ADD){
            list.add(event.getData());
        }else if(event.getChangeEventType()==ChangeEventType.REMOVE){
            list.remove(event.getOldData());
        }else if(event.getChangeEventType() == ChangeEventType.UPDATE){
            for(int i=0; i<list.size(); i++){
                if (event.getOldData().getId() == list.get(i).getId()){
                    list.set(i, event.getData());
                }
            }
        }
    }

    private void loadData(){
        idColumn = new TableColumn<>("Id");
        nameColumn = new TableColumn<>("Name");
        groupColumn = new TableColumn<>("Group");
        emailColumn= new TableColumn<>("Email");
        teacherColumn= new TableColumn<>("Teacher Name");
        tableView.getColumns().addAll(idColumn, nameColumn, groupColumn, emailColumn, teacherColumn);

        idColumn.setCellValueFactory(new PropertyValueFactory<Student, Integer>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<Student, String>("name"));
        groupColumn.setCellValueFactory(new PropertyValueFactory<Student, Integer>("group"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<Student, String>("email"));
        teacherColumn.setCellValueFactory(new PropertyValueFactory<Student, String>("teacherName"));


        list = FXCollections.observableList(StreamSupport.stream(service.getStudentService().findAllStudents().spliterator(), false).collect(Collectors.toList()));
        tableView.setItems(list);
        setTableViewColumn();
        setButtonDisable();
    }

    private void setButtonDisable(){
        addButton.setDisable(false);
        updateButton.setDisable(false);
        removeButton.setDisable(false);
    }

    private void setTableViewColumn(){
        idColumn.prefWidthProperty().bind(tableView.widthProperty().divide(6));
        nameColumn.prefWidthProperty().bind(tableView.widthProperty().divide(5));
        groupColumn.prefWidthProperty().bind(tableView.widthProperty().divide(6));
        emailColumn.prefWidthProperty().bind(tableView.widthProperty().divide(4));
        teacherColumn.prefWidthProperty().bind(tableView.widthProperty().divide(4));

        idColumn.setResizable(false);
        nameColumn.setResizable(false);
        groupColumn.setResizable(false);
        emailColumn.setResizable(false);
        teacherColumn.setResizable(false);
    }

    @FXML
    private void close(){
        stage.close();
    }

    @FXML
    private void back(){
        primaryStage.show();
        stage.close();
    }

    @FXML
    private void addStudentHandler(){
        windowAddUpdate(new Student(-1,"",-1,"",""));
    }

    @FXML
    private void updateStudentHandler(){
        Student student = tableView.getSelectionModel().getSelectedItem();
        if (student == null){
            alert();
        } else {
            windowAddUpdate(student);
        }
    }

    @FXML
    private void removeStudentHandler(){
        Student student = tableView.getSelectionModel().getSelectedItem();
        if (student == null){
            alert();
        } else {
            try {
                service.getStudentService().delete(student.getId());
                List<Mark> markList = FXCollections.observableList(StreamSupport.stream(service.getMarkService().findAllMarks().spliterator(), false).collect(Collectors.toList()));
                markList.forEach(x -> {
                    Pair<Integer, Integer> pair = new Pair<>(student.getId(), x.getHomeworkId());
                    service.getMarkService().deleteMark(pair);
                });
            } catch (ValidationException e) {
                Alert alert2 = new Alert(Alert.AlertType.ERROR);
                alert2.setContentText(e.getMessage());
                alert2.show();
                DialogPane dialogPane = alert2.getDialogPane();
                dialogPane.getStylesheets().add(
                        getClass().getResource("../css/alert.css").toExternalForm());
            }
        }
    }

    private void alert(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText("No selected entities !");
        alert.show();
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(
                getClass().getResource("../css/alert.css").toExternalForm());
    }

    private void windowAddUpdate(Student student){
        try{
            Stage stage = new Stage();
            stage.setTitle("Edit Student");
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(StudentController.class.getResource("../fxml/editStudent.fxml"));
            AnchorPane root = loader.load();

            Scene scene = new Scene(root);
            stage.setScene(scene);
            EditStudent editStudent = loader.getController();
            editStudent.setService(service, student, stage);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void openPopUpHandler(){
        Label label = new Label("- you can add, update or remove a student from the list;\n\n" +
                "- in order to update or remove a student, just select it \n" +
                "and then you can update or remove it;\n\n" + "- if you want to add a new student, you can \n" +
                "press 'Add' and complete the information;");
        label.setStyle("-fx-font-size: 13px;-fx-font-weight: bold;-fx-text-fill: black;-fx-font-family: \"Rokkitt\";");
        Circle circle = new Circle(25, 25, 200, Color.ALICEBLUE);
        Image image = new Image(StudentController.class.getResourceAsStream("../images/questionMark.png"));
        imageView.setImage(image);
        imageView.setFitHeight(250);
        imageView.setFitWidth(150);
        imageView.setOpacity(0.6);

        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(circle, imageView, label);

        popup.getContent().add(stackPane);

        if (!popup.isShowing()) {
            popup.show(stage);
            mainAnchorPane.setOpacity(.40);
            mainAnchorPane.setDisable(true);
        }

        popup.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                mainAnchorPane.setDisable(false);
                mainAnchorPane.setOpacity(1);
                popup.hide();
            }
        });
    }

}
