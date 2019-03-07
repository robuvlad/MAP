package sample.database;

import sample.domain.Homework;
import sample.domain.Student;
import sample.repository.HomeworkRepository;
import sample.validation.ValidationException;
import sample.validation.Validator;

import java.sql.*;

public class DatabaseHomeworkRepository extends HomeworkRepository {

    private static final String DB_NAME = "project.db";
    private static final String CONNECTION_STRING = "jdbc:sqlite:D:\\FACULTATE\\SEM 3\\MAP\\ProiectFinalMAP\\Proiect\\" + DB_NAME;

    private static final String TABLE_NAME = "Homeworks";
    private static final String TABLE_COLUMN_ID = "_id";
    private static final String TABLE_COLUMN_DESCRIPTION = "_description";
    private static final String TABLE_COLUMN_DEADLINE = "_deadline";
    private static final String TABLE_COLUMN_RECEIVED = "_received";

    private Connection connection;


    public DatabaseHomeworkRepository(Validator<Homework> validator) {
        super(validator);
        open();
        load();
    }

    private void open(){
        try{
            connection = DriverManager.getConnection(CONNECTION_STRING);
            Statement statement = connection.createStatement();
            statement.execute("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                    TABLE_COLUMN_ID + " INTEGER PRIMARY KEY, " +
                    TABLE_COLUMN_DESCRIPTION + " TEXT, " +
                    TABLE_COLUMN_DEADLINE + " INTEGER, " +
                    TABLE_COLUMN_RECEIVED + " INTEGER)");
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }

    private void load(){
        try(Statement statement = connection.createStatement();
            ResultSet results = statement.executeQuery("SELECT * FROM " + TABLE_NAME)){
            while(results.next()){
                Homework homework = new Homework(results.getInt(TABLE_COLUMN_ID), results.getString(TABLE_COLUMN_DESCRIPTION),
                        results.getInt(TABLE_COLUMN_DEADLINE), results.getInt(TABLE_COLUMN_RECEIVED));
                super.save(homework);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }catch(ValidationException ex){
            System.out.println(ex.getMessage());
        }
    }

    private void writeAdd(Homework x){
        try(Statement statement = connection.createStatement()){
            statement.execute("INSERT INTO " + TABLE_NAME + " VALUES (" +
                    x.getId() + ", '" +
                    x.getDescription() + "', " +
                    x.getDeadline() + ", " +
                    x.getReceived() + ")");
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }

    private void writeUpdate(Homework homework){
        try(Statement statement = connection.createStatement()){
            statement.execute("UPDATE " + TABLE_NAME + " SET " +
                    TABLE_COLUMN_DESCRIPTION + "='" + homework.getDescription() + "', " +
                    TABLE_COLUMN_DEADLINE + "=" + homework.getDeadline() + ", " +
                    TABLE_COLUMN_RECEIVED + "=" + homework.getReceived() +
                    " WHERE " + TABLE_COLUMN_ID + "=" + homework.getId());
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }

    private void writeDelete(int id){
        try(Statement statement = connection.createStatement()){
            statement.execute("DELETE FROM " + TABLE_NAME + " WHERE " + TABLE_COLUMN_ID + "=" + id);
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public Homework save(Homework entity) throws ValidationException {
        Homework homework = super.save(entity);
        writeAdd(entity);
        return homework;
    }

    @Override
    public Homework delete(Integer integer) {
        Homework homework = super.delete(integer);
        writeDelete(integer);
        return homework;
    }

    @Override
    public Homework update(Homework entity) {
        Homework homework = super.update(entity);
        writeUpdate(entity);
        return homework;
    }
}
