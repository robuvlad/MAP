package sample.gui.controller;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.Glow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Pair;
import sample.gui.pdf.*;
import sample.service.Service;

import javax.activation.MimeType;
import javax.print.Doc;
import javax.swing.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReportController {

    private Service service;
    private Stage stage, primaryStage;

    @FXML private TableView tableView;
    @FXML private PieChart pieChart;

    private Tooltip container;

    public ReportController(){
        System.out.println("Report Controller");
    }

    public void setService(Service service, Stage stage, Stage primaryStage){
        this.service = service;
        this.stage = stage;
        this.primaryStage = primaryStage;

        reportHomeworkHandler();
    }

    @FXML
    private void reportHomeworkHandler(){
        tableView.getColumns().clear();
        TableColumn<Pair<String, Double>, Integer> homeColumn = new TableColumn<>("Homework Name");
        TableColumn<Pair<String, Double>, Double> averageColumn = new TableColumn<>("Average");
        tableView.getColumns().addAll(homeColumn, averageColumn );

        homeColumn.setCellValueFactory(new PropertyValueFactory<Pair<String, Double>, Integer>("key"));
        averageColumn .setCellValueFactory(new PropertyValueFactory<Pair<String, Double>, Double>("value"));

        List<Pair<String, Double>> myList = service.reportToughHomework();
        tableView.setItems(FXCollections.observableList(myList));

        homeColumn.prefWidthProperty().bind(tableView.widthProperty().divide(2));
        averageColumn.prefWidthProperty().bind(tableView.widthProperty().divide(2));
        homeColumn.setResizable(false);
        averageColumn.setResizable(false);

        showPieChartDouble(myList);
    }

    private File setFileChooser(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialFileName("File");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Files", "*.pdf");
        fileChooser.getExtensionFilters().add(extFilter);
        File fileToSave = fileChooser.showSaveDialog(stage);
        return fileToSave;
    }

    @FXML
    private void generateHomeworkHandler() {
        List<Pair<String, Double>> myList = service.reportToughHomework();
        PdfHomework pdfHomework = new PdfHomework();
        File file = setFileChooser();

        if (file != null) {
            try {
                FileOutputStream fos = new FileOutputStream(file);
                pdfHomework.set(myList, fos);
                alert();
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Unable to save file : " + e.getMessage(), ButtonType.CLOSE);
                alert.showAndWait();
                return;
            }
        }
    }


    @FXML
    private void reportPerGroupHandler(){
        tableView.getColumns().clear();
        TableColumn<Pair<Integer, Double>, Integer> groupColumn = new TableColumn<>("Group");
        TableColumn<Pair<Integer, Double>, Double> averageColumn = new TableColumn<>("Average");
        tableView.getColumns().addAll(groupColumn, averageColumn );


        groupColumn.setCellValueFactory(new PropertyValueFactory<Pair<Integer, Double>, Integer>("key"));
        averageColumn .setCellValueFactory(new PropertyValueFactory<Pair<Integer, Double>, Double>("value"));

        List<Pair<Integer, Double>> myList = service.reportPerGroup();
        tableView.setItems(FXCollections.observableList(myList));

        groupColumn.prefWidthProperty().bind(tableView.widthProperty().divide(2));
        averageColumn.prefWidthProperty().bind(tableView.widthProperty().divide(2));
        groupColumn.setResizable(false);
        averageColumn.setResizable(false);

        showPieChartInteger(myList);
    }

    @FXML
    private void generateGroupHandler(){
        List<Pair<Integer, Double>> myList = service.reportPerGroup();
        PdfGroup pdfGroup = new PdfGroup();
        File file = setFileChooser();

        if (file != null) {
            try {
                FileOutputStream fos = new FileOutputStream(file);
                pdfGroup.set(myList, fos);
                alert();
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Unable to save file : " + e.getMessage(), ButtonType.CLOSE);
                alert.showAndWait();
                return;
            }
        }
    }

    @FXML
    private void reportPassedStudents(){
        tableView.getColumns().clear();
        TableColumn<Pair<String, Double>, Integer> stNameColumn = new TableColumn<>("Student Name");
        TableColumn<Pair<String, Double>, Double> averageColumn = new TableColumn<>("Average");
        tableView.getColumns().addAll(stNameColumn, averageColumn );


        stNameColumn.setCellValueFactory(new PropertyValueFactory<Pair<String, Double>, Integer>("key"));
        averageColumn .setCellValueFactory(new PropertyValueFactory<Pair<String, Double>, Double>("value"));

        List<Pair<String, Double>> myList = service.reportPassedStudents(true);
        tableView.setItems(FXCollections.observableList(myList));

        stNameColumn.prefWidthProperty().bind(tableView.widthProperty().divide(2));
        averageColumn.prefWidthProperty().bind(tableView.widthProperty().divide(2));
        stNameColumn.setResizable(false);
        averageColumn.setResizable(false);

        showPieChartDouble(myList);
    }

    @FXML
    private void generatePassedHandler(){
        List<Pair<String, Double>> myList = service.reportPassedStudents(true);
        PdfPassedStudents pdf = new PdfPassedStudents();
        File file = setFileChooser();

        if (file != null) {
            try {
                FileOutputStream fos = new FileOutputStream(file);
                pdf.set(myList, fos);
                alert();
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Unable to save file : " + e.getMessage(), ButtonType.CLOSE);
                alert.showAndWait();
                return;
            }
        }
    }

    @FXML
    private void reportNotPassedStudents(){
        tableView.getColumns().clear();
        TableColumn<Pair<String, Double>, Integer> stNameColumn = new TableColumn<>("Student Name");
        TableColumn<Pair<String, Double>, Double> averageColumn = new TableColumn<>("Average");
        tableView.getColumns().addAll(stNameColumn, averageColumn );


        stNameColumn.setCellValueFactory(new PropertyValueFactory<Pair<String, Double>, Integer>("key"));
        averageColumn .setCellValueFactory(new PropertyValueFactory<Pair<String, Double>, Double>("value"));

        List<Pair<String, Double>> myList = service.reportPassedStudents(false);
        tableView.setItems(FXCollections.observableList(myList));

        stNameColumn.prefWidthProperty().bind(tableView.widthProperty().divide(2));
        averageColumn.prefWidthProperty().bind(tableView.widthProperty().divide(2));
        stNameColumn.setResizable(false);
        averageColumn.setResizable(false);

        showPieChartDouble(myList);
    }

    @FXML
    private void generateNotPassedStudentsHandler(){
        List<Pair<String, Double>> myList = service.reportPassedStudents(false);
        PdfNotPassedStudents pdf = new PdfNotPassedStudents();
        File file = setFileChooser();

        if (file != null) {
            try {
                FileOutputStream fos = new FileOutputStream(file);
                pdf.set(myList, fos);
                alert();
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Unable to save file : " + e.getMessage(), ButtonType.CLOSE);
                alert.showAndWait();
                return;
            }
        }
    }

    @FXML
    private void reportCountStudents(){
        tableView.getColumns().clear();
        TableColumn<Pair<String, Long>, String> nameColumn = new TableColumn<>("Teacher Name");
        TableColumn<Pair<String, Long>, Long> countColumn = new TableColumn<>("Nr. of Students");
        tableView.getColumns().addAll(nameColumn, countColumn);


        nameColumn.setCellValueFactory(new PropertyValueFactory<Pair<String, Long>, String>("key"));
        countColumn.setCellValueFactory(new PropertyValueFactory<Pair<String, Long>, Long>("value"));

        List<Pair<String, Long>> myList = service.reportCountStudents();
        tableView.setItems(FXCollections.observableList(myList));

        nameColumn.prefWidthProperty().bind(tableView.widthProperty().divide(2));
        countColumn.prefWidthProperty().bind(tableView.widthProperty().divide(2));
        nameColumn.setResizable(false);
        countColumn.setResizable(false);

        showPieChartLong(myList);
    }

    @FXML
    private void generateCountStudentsHandler(){
        List<Pair<String, Long>> myList = service.reportCountStudents();
        PdfTeachers pdf = new PdfTeachers();
        File file = setFileChooser();

        if (file != null) {
            try {
                FileOutputStream fos = new FileOutputStream(file);
                pdf.set(myList, fos);
                alert();
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Unable to save file : " + e.getMessage(), ButtonType.CLOSE);
                alert.showAndWait();
                return;
            }
        }
    }

    private void showPieChartDouble(List<Pair<String, Double>> l){
        pieChart.getData().clear();

        l.forEach(x -> {
            PieChart.Data data = new PieChart.Data(x.getKey(), x.getValue());
            pieChart.getData().add(data);
        });
        pieChart.setLegendVisible(false);

        final Label caption = new Label("");
        caption.setTextFill(Color.DARKORANGE);
        caption.setStyle("-fx-font: 24 arial;");
        container = new Tooltip();
        container.setGraphic(caption);

        for(final PieChart.Data data : pieChart.getData()){
            data.getNode().addEventHandler(MouseEvent.MOUSE_MOVED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    caption.setText(String.valueOf(data.getPieValue()));
                    container.show(stage, event.getScreenX() - 30, event.getScreenY() - 30);
                }
            });
        }
    }

    private void showPieChartInteger(List<Pair<Integer, Double>> listOfPairs){
        pieChart.getData().clear();
        listOfPairs.forEach(x -> {
            PieChart.Data data = new PieChart.Data(x.getKey().toString(), x.getValue());
            pieChart.getData().addAll(data);
        });
        pieChart.setLegendVisible(false);

        final Label caption = new Label("");
        caption.setTextFill(Color.DARKORANGE);
        caption.setStyle("-fx-font: 24 arial;");
        container = new Tooltip();
        container.setGraphic(caption);

        for(final PieChart.Data data : pieChart.getData()){
            data.getNode().addEventHandler(MouseEvent.MOUSE_MOVED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    caption.setText(String.valueOf(data.getPieValue()));
                    container.show(stage, event.getScreenX() - 30, event.getScreenY() - 30);
                }
            });
        }
    }

    private void showPieChartLong(List<Pair<String, Long>> listOfPairs){
        pieChart.getData().clear();
        listOfPairs.forEach(x -> {
            PieChart.Data data = new PieChart.Data(x.getKey().toString(), x.getValue());
            pieChart.getData().addAll(data);
        });
        pieChart.setLegendVisible(false);

        final Label caption = new Label("");
        caption.setTextFill(Color.DARKORANGE);
        caption.setStyle("-fx-font: 24 arial;");
        container = new Tooltip();
        container.setGraphic(caption);

        for(final PieChart.Data data : pieChart.getData()){
            data.getNode().addEventHandler(MouseEvent.MOUSE_MOVED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    caption.setText(String.valueOf(data.getPieValue()));
                    container.show(stage, event.getScreenX() - 30, event.getScreenY() - 30);
                }
            });
        }
    }


    @FXML
    private void onMouseEnteredHandler(){
        container.hide();
    }

    @FXML
    private void cancelHandler(){
        this.stage.close();
    }

    @FXML
    private void back(){
        this.stage.close();
        this.primaryStage.show();
    }

    private void alert(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Done !");
        alert.setContentText("PDF generated successfully !");
        alert.show();
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(
                getClass().getResource("../css/alert.css").toExternalForm());
    }

}
