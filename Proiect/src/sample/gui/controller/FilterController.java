package sample.gui.controller;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Pair;
import sample.domain.Homework;
import sample.domain.StudentHomework;
import sample.gui.pdf.PdfGroup;
import sample.gui.pdf.PdfHomework;
import sample.observer.Observable;
import sample.service.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class FilterController {

    private Service service;
    private Stage stage, primaryStage;

    @FXML private TableView filterTableView;

    @FXML private ComboBox<String> homeComboBox;
    @FXML private TextField nameTextField;
    @FXML private ComboBox<String> dateChoiceBox;
    @FXML private ComboBox<Integer> groupChoiceBox;

    public FilterController() {
        System.out.println("Filter Controller");
    }

    public void setService(Service service, Stage stage, Stage primaryStage) {
        this.service = service;
        this.stage = stage;
        this.primaryStage = primaryStage;

        fillComboBox();
        fillDateChoiceBox();
        fillGroupChoiceBox();

        marksCertainHomeHandler();
    }

    private void fillComboBox(){
        ArrayList<String> homeNames = new ArrayList<>();
        service.getHomeworkService().findAllHomeworks().forEach(x -> homeNames.add(x.getDescription()));
        homeComboBox.setItems(FXCollections.observableList(homeNames));

        int i=-1;
        for(Homework h : service.getHomeworkService().findAllHomeworks()){
            i += 1;
            if (h.getDeadline() == service.getMarkService().getHomeworkService().currentWeek()){
                break;
            }
        }
        homeComboBox.getSelectionModel().select(i);
    }

    private void fillDateChoiceBox(){
        List<String> dateList = Arrays.asList("October", "November", "December", "January");
        dateChoiceBox.setItems(FXCollections.observableList(dateList));
        dateChoiceBox.getSelectionModel().selectFirst();
    }

    private void fillGroupChoiceBox(){
        List<Integer> groupList = Arrays.asList(221, 222, 223, 224, 225, 226, 227);
        groupChoiceBox.setItems(FXCollections.observableList(groupList));
        groupChoiceBox.getSelectionModel().selectFirst();
    }

    @FXML
    private void marksCertainHomeHandler(){
        filterTableView.getColumns().clear();
        TableColumn<Pair<String, Double>, String> nameColumn = new TableColumn<>("Student Name");
        TableColumn<Pair<String, Double>, Double> markColumn = new TableColumn<>("Mark");
        filterTableView.getColumns().addAll(nameColumn, markColumn);

        nameColumn.setCellValueFactory(new PropertyValueFactory<Pair<String, Double>, String>("key"));
        markColumn.setCellValueFactory(new PropertyValueFactory<Pair<String, Double>, Double>("value"));

        List<Pair<String, Double>> doubleList = service.marksCertainHome(homeComboBox.getValue());
        filterTableView.setItems(FXCollections.observableList(doubleList));

        nameColumn.prefWidthProperty().bind(filterTableView.widthProperty().divide(2));
        markColumn.prefWidthProperty().bind(filterTableView.widthProperty().divide(2));
        nameColumn.setResizable(false);
        markColumn.setResizable(false);
    }


    @FXML
    private void marksCertainStudentHandler(){
        try {
            filterTableView.getColumns().clear();
            TableColumn<StudentHomework, String> nameSColumn = new TableColumn<>("S. Name");
            TableColumn<StudentHomework, String> nameHColumn = new TableColumn<>("H. Name");
            TableColumn<StudentHomework, Double> valueColumn= new TableColumn<>("Mark");

            filterTableView.getColumns().addAll(nameSColumn, nameHColumn, valueColumn);

            nameSColumn.setCellValueFactory(new PropertyValueFactory<StudentHomework, String>("studentName"));
            nameHColumn.setCellValueFactory(new PropertyValueFactory<StudentHomework, String>("homeName"));
            valueColumn.setCellValueFactory(new PropertyValueFactory<StudentHomework, Double>("mark"));

            List<StudentHomework> pairList = service.marksCertainStudent(nameTextField.getText());
            filterTableView.setItems(FXCollections.observableList(pairList));

            nameSColumn.prefWidthProperty().bind(filterTableView.widthProperty().divide(3));
            nameHColumn.prefWidthProperty().bind(filterTableView.widthProperty().divide(3));
            valueColumn.prefWidthProperty().bind(filterTableView.widthProperty().divide(3));
            nameSColumn.setResizable(false);
            nameHColumn.setResizable(false);
            valueColumn.setResizable(false);
        } catch(NumberFormatException ex){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Error");
            alert.setContentText("Invalid data !");
            alert.show();
        }
    }

    @FXML
    private void marksCertainDate(){
        filterTableView.getColumns().clear();
        TableColumn<Pair<String, List<Double>>, Integer> idColumn = new TableColumn<>("Student Id");
        TableColumn<Pair<String, List<Double>>, List<Double>> markColumn = new TableColumn<>("Mark");
        filterTableView.getColumns().addAll(idColumn, markColumn);

        idColumn.setCellValueFactory(new PropertyValueFactory<Pair<String, List<Double>>, Integer>("key"));
        markColumn.setCellValueFactory(new PropertyValueFactory<Pair<String, List<Double>>, List<Double>>("value"));

        List<Pair<String, List<Double>>> myList = service.marksCertainDate(dateChoiceBox.getValue());
        filterTableView.setItems(FXCollections.observableList(myList));

        idColumn.prefWidthProperty().bind(filterTableView.widthProperty().divide(2));
        markColumn.prefWidthProperty().bind(filterTableView.widthProperty().divide(2));
        idColumn.setResizable(false);
        markColumn.setResizable(false);
    }

    @FXML
    private void filterByGroupHandler(){
        filterTableView.getColumns().clear();
        TableColumn<Pair<String, Double>, Integer> idColumn = new TableColumn<>("Student Name");
        TableColumn<Pair<String, Double>, Double> markColumn = new TableColumn<>("Mark");
        filterTableView.getColumns().addAll(idColumn, markColumn);


        idColumn.setCellValueFactory(new PropertyValueFactory<Pair<String, Double>, Integer>("key"));
        markColumn.setCellValueFactory(new PropertyValueFactory<Pair<String, Double>, Double>("value"));

        List<Pair<String, Double>> myList = service.filterByGroup(groupChoiceBox.getValue(), homeComboBox.getValue());
        filterTableView.setItems(FXCollections.observableList(myList));

        idColumn.prefWidthProperty().bind(filterTableView.widthProperty().divide(2));
        markColumn.prefWidthProperty().bind(filterTableView.widthProperty().divide(2));
        idColumn.setResizable(false);
        markColumn.setResizable(false);
    }

    @FXML
    private void cancelHandler(){
        stage.close();
    }

    @FXML
    private void back(){
        stage.close();
        primaryStage.show();
    }

}
