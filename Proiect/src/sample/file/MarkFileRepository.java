package sample.file;


import javafx.util.Pair;
import sample.domain.Mark;
import sample.repository.MarkRepository;
import sample.validation.ValidationException;
import sample.validation.Validator;

import java.io.*;

public class MarkFileRepository extends MarkRepository {

    private String fileName;

    /**
     * constructor
     * @param fileName
     * @param validator
     */
    public MarkFileRepository(String fileName, Validator<Mark> validator){
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
                Mark mark = new Mark(Integer.parseInt(data[0]),Integer.parseInt(data[1]),Double.parseDouble(data[2]), data[3]);
                super.save(mark);
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
                    bf.write(s.getStudentId() + "#" + s.getHomeworkId() + "#" + s.getValue() + "#" + s.getDate());
                    bf.newLine();
                } catch(IOException ex){
                    ex.getStackTrace();
                }
            });
        }catch(IOException ioEx){
            ioEx.getStackTrace();
        }
    }

    @Override
    public Mark save(Mark m) throws ValidationException {
        Mark mark = super.save(m);
        write();
        return mark;
    }

    @Override
    public Mark delete(Pair id){
        Mark mark = super.delete(id);
        write();
        return mark;
    }

    @Override
    public Mark update(Mark s){
        Mark mark = super.update(s);
        write();
        return mark;
    }

    @Override
    public Mark findOne(Pair id){
        return super.findOne(id);
    }

}
