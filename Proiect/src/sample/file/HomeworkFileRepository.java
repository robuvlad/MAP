package sample.file;



import sample.domain.Homework;
import sample.repository.HomeworkRepository;
import sample.validation.ValidationException;
import sample.validation.Validator;

import java.io.*;

public class HomeworkFileRepository extends HomeworkRepository {

    private String fileName;

    /**
     * constructor
     * @param fileName
     * @param validator
     */
    public HomeworkFileRepository(String fileName, Validator<Homework> validator){
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
                Homework home = new Homework(Integer.parseInt(data[0]), data[1], Integer.parseInt(data[2]), Integer.parseInt(data[3]));
                super.save(home);
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
        super.findAll().forEach(h -> {
            try {
                bf.write(h.getId() + "#" + h.getDescription() + "#" + h.getDeadline() + "#" + h.getReceived());
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
    public Homework save(Homework h) throws ValidationException{
        Homework homework = super.save(h);
        write();
        return homework;
    }

    @Override
    public Homework delete(Integer id){
        Homework homework = super.delete(id);
        write();
        return homework;
    }

    @Override
    public Homework update(Homework h){
        Homework homework = super.update(h);
        write();
        return homework;
    }

    @Override
    public Homework findOne(Integer id){
        return super.findOne(id);
    }

}
