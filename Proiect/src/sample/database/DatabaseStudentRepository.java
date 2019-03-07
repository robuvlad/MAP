package sample.database;

import sample.domain.Student;
import sample.repository.StudentRepository;
import sample.validation.ValidationException;
import sample.validation.Validator;

import java.sql.*;

public class DatabaseStudentRepository extends StudentRepository {

    private static final String DB_NAME = "project.db";
    private static final String CONNECTION_STRING = "jdbc:sqlite:D:\\FACULTATE\\SEM 3\\MAP\\ProiectFinalMAP\\Proiect\\" + DB_NAME;

    private static final String TABLE_NAME = "Students";
    private static final String TABLE_COLUMN_ID = "_id";
    private static final String TABLE_COLUMN_NAME = "_name";
    private static final String TABLE_COLUMN_GROUP = "_group";
    private static final String TABLE_COLUMN_EMAIL = "_email";
    private static final String TABLE_COLUMN_TEACHER = "_teacher";

    private Connection connection;

    public DatabaseStudentRepository(Validator<Student> validator) {
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
                        TABLE_COLUMN_NAME + " TEXT, " +
                        TABLE_COLUMN_GROUP + " INTEGER, " +
                        TABLE_COLUMN_EMAIL + " TEXT, " +
                        TABLE_COLUMN_TEACHER + " TEXT)");
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }

    private void load(){
        try(Statement statement = connection.createStatement();
            ResultSet results = statement.executeQuery("SELECT * FROM " + TABLE_NAME)){
            while(results.next()){
                Student student = new Student(results.getInt(TABLE_COLUMN_ID), results.getString(TABLE_COLUMN_NAME),
                        results.getInt(TABLE_COLUMN_GROUP), results.getString(TABLE_COLUMN_EMAIL),
                        results.getString(TABLE_COLUMN_TEACHER));
                super.save(student);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }catch(ValidationException ex){
            System.out.println(ex.getMessage());
        }
    }

    private void writeAdd(Student x){
        try(Statement statement = connection.createStatement()){
            statement.execute("INSERT INTO " + TABLE_NAME + " VALUES (" + x.getId() + ", '" + x.getName() + "', " +
                    x.getGroup() + ", '" + x.getEmail() + "', '" + x.getTeacherName() + "')");
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }

    private void writeUpdate(Student student){
        try(Statement statement = connection.createStatement()){
            statement.execute("UPDATE " + TABLE_NAME + " SET " +
                    TABLE_COLUMN_NAME + "='" + student.getName() + "', " +
                    TABLE_COLUMN_GROUP + "=" + student.getGroup() + ", " +
                    TABLE_COLUMN_EMAIL + "='" + student.getEmail() + "', " +
                    TABLE_COLUMN_TEACHER + "='" + student.getTeacherName() +
                    "' WHERE " + TABLE_COLUMN_ID + "=" + student.getId());
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
    public Student save(Student entity) throws ValidationException {
        Student student = super.save(entity);
        writeAdd(entity);
        return student;
    }

    @Override
    public Student delete(Integer integer) {
        Student student = super.delete(integer);
        writeDelete(integer);
        return student;
    }

    @Override
    public Student update(Student entity) {
        Student student = super.update(entity);
        writeUpdate(entity);
        return student;
    }
}
