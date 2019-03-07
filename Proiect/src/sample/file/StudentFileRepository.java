package sample.file;



import sample.domain.Student;
import sample.repository.StudentRepository;
import sample.validation.ValidationException;
import sample.validation.Validator;

import java.io.*;

public class StudentFileRepository extends StudentRepository {

    private String fileName;

    /**
     * constructor
     * @param fileName
     * @param validator
     */
    public StudentFileRepository(String fileName, Validator<Student> validator){
        super(validator);
        this.fileName = fileName;
        load();
    }

    /**
     * load data from file
     */
    private void load(){
        try(BufferedReader bf = new BufferedReader(new FileReader(fileName))){
            String line;
            while((line = bf.readLine()) != null){
                String[] data = line.split("#");
                Student student = new Student(Integer.parseInt(data[0]),data[1],Integer.parseInt(data[2]),data[3],data[4]);
                super.save(student);
            }
        }catch(IOException ioEx){
            ioEx.getStackTrace();
        }catch(ValidationException ex){
            ex.getStackTrace();
        }
    }

    /**
     * write data to file
     */
    private void write(){
        try(BufferedWriter bf = new BufferedWriter(new FileWriter(fileName))){
            super.findAll().forEach(s -> {
                try {
                    bf.write(s.getId() + "#" + s.getName() + "#" + s.getGroup() + "#" + s.getEmail() + "#" + s.getTeacherName());
                    bf.newLine();
                }catch(IOException ex){
                    ex.getStackTrace();
                }
            });
        }catch(IOException ioEx){
            ioEx.getStackTrace();
        }
    }

    @Override
    public Student save(Student s) throws ValidationException{
        Student student = super.save(s);
        write();
        return student;
    }

    @Override
    public Student delete(Integer id){
        Student student = super.delete(id);
        write();
        return student;
    }

    @Override
    public Student update(Student s){
        Student student = super.update(s);
        write();
        return student;
    }

    @Override
    public Student findOne(Integer id){
        return super.findOne(id);
    }

}
