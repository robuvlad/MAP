package sample.database;

import javafx.util.Pair;
import sample.domain.Homework;
import sample.domain.Mark;
import sample.domain.Student;
import sample.repository.MarkRepository;
import sample.validation.ValidationException;
import sample.validation.Validator;

import java.sql.*;
import java.time.format.DateTimeFormatter;

public class DatabaseMarkRepository extends MarkRepository {

    private static final String DB_NAME = "project.db";
    private static final String CONNECTION_STRING = "jdbc:sqlite:D:\\FACULTATE\\SEM 3\\MAP\\ProiectFinalMAP\\Proiect\\" + DB_NAME;

    private static final String TABLE_NAME = "Marks";
    private static final String TABLE_COLUMN_ID_STUDENT = "_idStudent";
    private static final String TABLE_COLUMN_ID_HOMEWORK = "_idHomework";
    private static final String TABLE_COLUMN_VALUE = "_value";
    private static final String TABLE_COLUMN_DATE = "_date";
    private static final String TABLE_COLUMN_FEEDBACK = "_feedback";

    private Connection connection;

    public DatabaseMarkRepository(Validator<Mark> validator) {
        super(validator);
        open();
        load();
    }

    private void open(){
        try{
            connection = DriverManager.getConnection(CONNECTION_STRING);
            Statement statement = connection.createStatement();
            statement.execute("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                    TABLE_COLUMN_ID_STUDENT + " INTEGER, " +
                    TABLE_COLUMN_ID_HOMEWORK + " INTEGER, " +
                    TABLE_COLUMN_VALUE + " DOUBLE, " +
                    TABLE_COLUMN_DATE + " TEXT, " +
                    TABLE_COLUMN_FEEDBACK + " TEXT, " +
                    "PRIMARY KEY (" + TABLE_COLUMN_ID_STUDENT + ", " + TABLE_COLUMN_ID_HOMEWORK + "), " +
                    "FOREIGN KEY (" + TABLE_COLUMN_ID_STUDENT + ") REFERENCES " + "Students (_id), " +
                    "FOREIGN KEY (" + TABLE_COLUMN_ID_HOMEWORK + ") REFERENCES " + "Homeworks (_id)" + ")");
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }

    private void load(){
        try(Statement statement = connection.createStatement();
            ResultSet results = statement.executeQuery("SELECT * FROM " + TABLE_NAME)){
            while(results.next()){
                Mark mark = new Mark(results.getInt(TABLE_COLUMN_ID_STUDENT), results.getInt(TABLE_COLUMN_ID_HOMEWORK),
                        results.getDouble(TABLE_COLUMN_VALUE), results.getString(TABLE_COLUMN_DATE),
                        results.getString(TABLE_COLUMN_FEEDBACK));
                super.save(mark);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }catch(ValidationException ex){
            System.out.println(ex.getMessage());
        }
    }

//    private void write(){
//        try(Statement statement = connection.createStatement()){
//            statement.execute("DELETE FROM " + TABLE_NAME);
//            super.findAll().forEach(x -> {
//                try {
//                    statement.execute("INSERT INTO " + TABLE_NAME + "(_idStudent, _idHomework, _value, _date, _feedback) VALUES ("
//                            + x.getStudentId() +
//                            ", " + x.getHomeworkId() +
//                            ", " + x.getValue() +
//                            ", '" + x.getDate() +
//                            "', '" + x.getFeedback() + "')");
//                }catch(SQLException e){
//                    System.out.println(e.getMessage());
//                }
//            });
//        }catch(SQLException e){
//            System.out.println(e.getMessage());
//        }
//    }

    private void writeAdd(Mark x){
        try(Statement statement = connection.createStatement()){
            statement.execute("INSERT INTO " + TABLE_NAME + " VALUES (" +
                    x.getStudentId() + ", " +
                    x.getHomeworkId() + ", " +
                    x.getValue() + ", '" +
                    x.getDate() + "', '" +
                    x.getFeedback() + "')");
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }

    private void writeUpdate(Mark mark){
        try(Statement statement = connection.createStatement()){
            statement.execute("UPDATE " + TABLE_NAME + " SET " +
                    TABLE_COLUMN_VALUE + "=" + mark.getValue() + ", " +
                    TABLE_COLUMN_DATE+ "='" + mark.getDate() + "', " +
                    TABLE_COLUMN_FEEDBACK+ "='" + mark.getFeedback() + "', " +
                    "' WHERE " + TABLE_COLUMN_ID_STUDENT+ "=" + mark.getStudentId() + " AND " + TABLE_COLUMN_ID_HOMEWORK + "=" + mark.getHomeworkId());
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }

    private void writeDelete(Pair<Integer, Integer> pair){
        try(Statement statement = connection.createStatement()){
            statement.execute("DELETE FROM " + TABLE_NAME + " WHERE " + TABLE_COLUMN_ID_STUDENT+ "=" + pair.getKey() +
                    " AND " + TABLE_COLUMN_ID_HOMEWORK + "=" + pair.getValue());
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }


    @Override
    public Mark save(Mark entity) throws ValidationException {
        Mark mark = super.save(entity);
        writeAdd(entity);
        return mark;
    }

    @Override
    public Mark delete(Pair<Integer, Integer> integerIntegerPair) {
        Mark mark = super.delete(integerIntegerPair);
        writeDelete(integerIntegerPair);
        return mark;
    }

    @Override
    public Mark update(Mark entity) {
        Mark mark = super.update(entity);
        writeUpdate(entity);
        return mark;
    }
}
