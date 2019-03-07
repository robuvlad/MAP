package sample.gui.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.util.Pair;
import sample.domain.Homework;
import sample.domain.Mark;
import sample.domain.Student;
import sample.observer.ChangeEventType;
import sample.observer.HomeworkEvent;
import sample.observer.Observable;
import sample.observer.Observer;
import sample.service.Service;
import sample.validation.ValidationException;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class HomeworkController implements Observer<HomeworkEvent> {

    private Service service;
    private Stage stage;
    private Stage primaryStage;
    private ObservableList<Homework> list;

    @FXML
    private TableView<Homework> tableView;
    @FXML private TableColumn<Homework, Integer> idColumn;
    @FXML private TableColumn<Homework, String> descColumn;
    @FXML private TableColumn<Homework, Integer> deadlineColumn;
    @FXML private TableColumn<Homework, Integer> receivedColumn;

    @FXML private Button addButton;
    @FXML private Button updateButton;
    @FXML private Button removeButton;

    private Popup popup = new Popup();
    @FXML private MenuItem aboutMenuItem;
    @FXML private AnchorPane mainAnchorPane;
    @FXML private ImageView imageView = new ImageView();

    public HomeworkController() {
        System.out.println("Homework C");
    }


    public void setService(Service service, Stage stage, Stage primaryStage){
        this.service = service;
        this.stage = stage;
        this.primaryStage = primaryStage;
        this.service.getHomeworkService().addObserver(this);

        addButton.setDisable(true);
        updateButton.setDisable(true);
        removeButton.setDisable(true);
        loadData();
    }

    @Override
    public void update(HomeworkEvent event) {
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
        descColumn = new TableColumn<>("Description");
        deadlineColumn = new TableColumn<>("Deadline Week");
        receivedColumn = new TableColumn<>("Received Week");
        tableView.getColumns().addAll(idColumn, descColumn, deadlineColumn, receivedColumn);

        idColumn.setCellValueFactory(new PropertyValueFactory<Homework, Integer>("id"));
        descColumn.setCellValueFactory(new PropertyValueFactory<Homework, String>("description"));
        deadlineColumn.setCellValueFactory(new PropertyValueFactory<Homework, Integer>("deadline"));
        receivedColumn.setCellValueFactory(new PropertyValueFactory<Homework, Integer>("received"));

        list = FXCollections.observableList(StreamSupport.stream(service.getHomeworkService().findAllHomeworks().spliterator(), false).collect(Collectors.toList()));
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
        idColumn.prefWidthProperty().bind(tableView.widthProperty().divide(4));
        descColumn.prefWidthProperty().bind(tableView.widthProperty().divide(4));
        deadlineColumn.prefWidthProperty().bind(tableView.widthProperty().divide(4));
        receivedColumn.prefWidthProperty().bind(tableView.widthProperty().divide(4));

        idColumn.setResizable(false);
        descColumn.setResizable(false);
        deadlineColumn.setResizable(false);
        receivedColumn.setResizable(false);
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
    private void addHomeworkHandler(){
        windowAddUpdate(new Homework(-1,"",-1,-1));
    }

    @FXML
    private void updateHomeworkHandler(){
        Homework homework = tableView.getSelectionModel().getSelectedItem();
        if (homework == null){
            alert();
        } else {
            windowAddUpdate(homework);
        }
    }

    @FXML
    private void removeHomeworkHandler(){
        Homework homework = tableView.getSelectionModel().getSelectedItem();
        if (homework == null){
            alert();
        } else {
            try {
                service.getHomeworkService().delete(homework.getId());
                List<Mark> markList = FXCollections.observableList(StreamSupport.stream(service.getMarkService().findAllMarks().spliterator(), false).collect(Collectors.toList()));
                markList.forEach(x -> {
                    Pair<Integer, Integer> pair = new Pair<>(x.getStudentId(), homework.getId());
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

    private void windowAddUpdate(Homework homework){
        try{
            Stage stage = new Stage();
            stage.setTitle("Edit Homework");
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(StudentController.class.getResource("../fxml/editHomework.fxml"));
            AnchorPane root = loader.load();

            Scene scene = new Scene(root);
            stage.setScene(scene);
            EditHomework editHomework = loader.getController();
            editHomework.setService(service, homework, stage);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void openPopUpHandler(){
        Label label = new Label("- you can add, update or remove a homework from the list;\n\n" +
                "- in order to update or remove a homework, just select it \n" +
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
