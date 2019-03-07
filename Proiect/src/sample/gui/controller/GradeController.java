package sample.gui.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
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
import sample.domain.Homework;
import sample.domain.Student;
import sample.domain.StudentHomework;
import sample.observer.ChangeEventType;
import sample.observer.Observer;
import sample.observer.StudHomeEvent;
import sample.service.Service;
import sample.validation.ValidationException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class GradeController implements Observer<StudHomeEvent> {

    private Service service;
    private Stage stage, primaryStage;
    private ObservableList<Student> studentList;
    private ObservableList<StudentHomework> studHomeList;
    private Student selectedStudent;

    @FXML
    private TableView<Student> filterTable;
    @FXML private TableView<StudentHomework> tableViewStudHome;

    @FXML private TableColumn<StudentHomework, String> studentNameColumn;
    @FXML private TableColumn<StudentHomework, String> homeNameColumn;
    @FXML private TableColumn<StudentHomework, Integer> markColumn;

    @FXML private TableColumn<Student, Integer> idColumn;
    @FXML private TableColumn<Student, String> nameColumn;
    @FXML private TableColumn<Student, Integer> groupColumn;
    @FXML private TableColumn<Student, String> emailColumn;

    @FXML private ComboBox<String> comboBox;
    @FXML private ComboBox<String> bottomComboBox;
    @FXML private TextField studentTextField;
    @FXML private TextField bottomTextField;
    @FXML private TextField valueMarkTextField;
    @FXML private TextArea feedbackTextArea;
    @FXML private Button previewButton;
    @FXML private CheckBox absentCheckBox;

    private Popup popup = new Popup();
    @FXML private MenuItem aboutMenuItem;
    @FXML private AnchorPane mainAnchorPane;
    @FXML private ImageView imageView = new ImageView();

    public GradeController() {
        System.out.println("Grade Controller");
    }

    public void setService(Service service, Stage stage, Stage primaryStage){
        this.service = service;
        this.stage = stage;
        this.primaryStage = primaryStage;
        this.service.getMarkService().addObserver(this);

        loadData();
    }

    @Override
    public void update(StudHomeEvent event) {
        try {
            if (event.getChangeEventType() == ChangeEventType.ADD) {
                studHomeList.add(event.getData());
            } else if (event.getChangeEventType() == ChangeEventType.REMOVE) {
                studHomeList.remove(event.getOldData());
            } else if (event.getChangeEventType() == ChangeEventType.UPDATE) {
                for (int i = 0; i < studHomeList.size(); i++) {
                    if (event.getOldData().getId() == studHomeList.get(i).getId()) {
                        studHomeList.set(i, event.getData());
                    }
                }
            }
        }catch (NullPointerException e){
            e.getStackTrace();
        }
    }

    private void loadData(){
        loadDataFilteredTable();
        studentNameColumn = new TableColumn<>("S. Name");
        homeNameColumn = new TableColumn<>("H. Name");
        markColumn = new TableColumn<>("Mark");
        tableViewStudHome.getColumns().addAll(studentNameColumn, homeNameColumn, markColumn);

        studentNameColumn.setCellValueFactory(new PropertyValueFactory<StudentHomework, String>("studentName"));
        homeNameColumn.setCellValueFactory(new PropertyValueFactory<StudentHomework, String>("homeName"));
        markColumn.setCellValueFactory(new PropertyValueFactory<StudentHomework, Integer>("mark"));

        studHomeList = FXCollections.observableList(service.getMarkService().getStudHome());
        tableViewStudHome.setItems(studHomeList);

        fillComboBox();
        fillBottomComboBox();
        setTableViewGradesColumn();
    }

    private void setTableViewGradesColumn(){
        studentNameColumn.prefWidthProperty().bind(tableViewStudHome.widthProperty().divide(3));
        homeNameColumn.prefWidthProperty().bind(tableViewStudHome.widthProperty().divide(3));
        markColumn.prefWidthProperty().bind(tableViewStudHome.widthProperty().divide(3));

        studentNameColumn.setResizable(false);
        homeNameColumn.setResizable(false);
        markColumn.setResizable(false);
    }

    private void loadDataFilteredTable(){
        idColumn = new TableColumn<>("Id");
        nameColumn = new TableColumn<>("Name");
        groupColumn = new TableColumn<>("Group");
        emailColumn = new TableColumn<>("Email");
        filterTable.getColumns().addAll(idColumn, nameColumn, groupColumn, emailColumn);

        idColumn.setCellValueFactory(new PropertyValueFactory<Student, Integer>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<Student, String>("name"));
        groupColumn.setCellValueFactory(new PropertyValueFactory<Student, Integer>("group"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<Student, String>("email"));
        studentList = FXCollections.observableList(StreamSupport.stream(service.getStudentService().findAllStudents()
                .spliterator(), false).collect(Collectors.toList()));

        filterTable.getSelectionModel().selectedItemProperty().addListener((observer, oldData, data) -> selectStudent(data));
        setTableViewFilterColumn();

        List<Student> students = FXCollections.observableList(StreamSupport.stream(service.getStudentService().findAllStudents().spliterator(), false).collect(Collectors.toList()));
        filterTable.setItems(FXCollections.observableList(students));
    }

    private void setTableViewFilterColumn(){
        idColumn.prefWidthProperty().bind(filterTable.widthProperty().divide(4));
        nameColumn.prefWidthProperty().bind(filterTable.widthProperty().divide(4));
        groupColumn.prefWidthProperty().bind(filterTable.widthProperty().divide(4));
        emailColumn.prefWidthProperty().bind(filterTable.widthProperty().divide(4));

        idColumn.setResizable(false);
        nameColumn.setResizable(false);
        groupColumn.setResizable(false);
        emailColumn.setResizable(false);
    }

    private void selectStudent(Student student){
        selectedStudent = student;
        if (selectedStudent != null)
            studentTextField.setText(selectedStudent.getName());
    }

    private void fillComboBox(){
        ArrayList<String> homeNames = new ArrayList<>();
        service.getHomeworkService().findAllHomeworks().forEach(x -> homeNames.add(x.getDescription()));
        comboBox.setItems(FXCollections.observableList(homeNames));

        int i=-1;
        for(Homework h : service.getHomeworkService().findAllHomeworks()){
            i += 1;
            if (h.getDeadline() == service.getMarkService().getHomeworkService().currentWeek()){
                break;
            }
        }
        comboBox.getSelectionModel().select(i);
    }

    private void fillBottomComboBox(){
        List<String> strings = Arrays.asList("S. names", "H. names", "Grades");
        bottomComboBox.setItems(FXCollections.observableList(strings));
        bottomComboBox.getSelectionModel().selectFirst();
    }

    @FXML
    private void textFieldReleased(){
        FilteredList<Student> filteredList = new FilteredList(studentList, p->true);

        filteredList.setPredicate(p -> p.getName().toLowerCase().contains(studentTextField.getText().toLowerCase().trim()));
        filterTable.setItems(filteredList);
    }

    @FXML
    private void textFieldBottomReleased(){
        FilteredList<StudentHomework> filteredList = new FilteredList(studHomeList, p->true);

        switch(bottomComboBox.getSelectionModel().getSelectedItem()){
            case "S. names":
                filteredList.setPredicate(p -> p.getStudentName().toLowerCase().contains(bottomTextField.getText().toLowerCase().trim()));
                break;
            case "H. names":
                filteredList.setPredicate(p -> p.getHomeName().toLowerCase().contains(bottomTextField.getText().toLowerCase().trim()));
                break;
            case "Grades":
                filteredList.setPredicate(p -> String.valueOf(p.getMark()).contains(bottomTextField.getText().toLowerCase().trim()));
                break;
        }

        tableViewStudHome.setItems(filteredList);
    }

    @FXML
    private void fillTextAreaHandler(){
        String description = comboBox.getValue();
        double n = service.getMarkService().getDelay(description);
        String message = "";
        if (n != 0)
            message += "The mark was diminished by " + n + " points due to the delays !";
       feedbackTextArea.setText(message);
    }

    private void alertMethod(String message){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText("Error");
        alert.setContentText(message);
        alert.show();
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(
                getClass().getResource("../css/alert.css").toExternalForm());
    }

    @FXML
    private void previewHandler(){
        try {
            Stage stage = new Stage();
            stage.setTitle("Preview");
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(GradeController.class.getResource("../fxml/preview.fxml"));
            AnchorPane root = loader.load();

            stage.setScene(new Scene(root));
            PreviewController previewController = loader.getController();
            check();
            previewController.setService(service, stage, comboBox.getValue(), absentCheckBox.isSelected(),
                    selectedStudent, valueMarkTextField.getText(), feedbackTextArea.getText());
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        } catch(NullPointerException ex){
            alertMethod("Invalid Student !");
        } catch(NumberFormatException ex){
            alertMethod("Invalid mark !");
        } catch(ValidationException ex){
            alertMethod(ex.getMessage());
        }
    }

    private void check() throws ValidationException{
        if (feedbackTextArea.getText().equals(""))
            throw new ValidationException("Invalid feedback !");
        if (Double.parseDouble(valueMarkTextField.getText()) < 1 || Double.parseDouble(valueMarkTextField.getText()) > 10)
            throw new ValidationException("Invalid mark !");
    }

    @FXML
    private void checkBoxHandler(){
        if (absentCheckBox.isSelected())
            feedbackTextArea.setText("");
    }

    @FXML
    private void back(){
        stage.close();
        primaryStage.show();
    }

    @FXML
    private void closeHandler(){
        stage.close();
    }


    @FXML
    private void openPopUpHandler(){
        Label label = new Label("- you can add a grade for a student;\n\n" +
                "- the student can be chosen from the bottom right table;\n\n" +
                "- the mark should be between 1 and 10;\n\n" +
                "- the student may be absent or not;\n\n" +
                "- the feedback is mandatory;");
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
            mainAnchorPane.setDisable(true);
            mainAnchorPane.setOpacity(.40);
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
