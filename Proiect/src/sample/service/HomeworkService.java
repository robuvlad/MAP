package sample.service;

import javafx.beans.value.ObservableValue;
import sample.domain.Email;
import sample.domain.Homework;
import sample.domain.Student;
import sample.observer.*;
import sample.repository.HomeworkRepository;
import sample.repository.Repository;
import sample.validation.ValidationException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class HomeworkService implements Observable<HomeworkEvent> {

    private Repository<Integer, Homework> repoHomework;
    private ArrayList<Observer<HomeworkEvent>> list;

    public HomeworkService(HomeworkRepository repoHomework){
        this.repoHomework = repoHomework;
        list = new ArrayList<>();
    }

    public Repository<Integer, Homework> getRepoHomework() {
        return repoHomework;
    }

    public ArrayList<Observer<HomeworkEvent>> getList() {
        return list;
    }

    @Override
    public void addObserver(Observer<HomeworkEvent> observer) {
        list.add(observer);
    }

    @Override
    public void removeObserver(Observer<HomeworkEvent> observer) {
        list.remove(observer);
    }

    @Override
    public void notifyObserver(HomeworkEvent event) {
        list.forEach(x -> x.update(event));
    }

    /**
     *
     * @param homework
     * @return null- if the given entity is saved
     * otherwise returns the entity (id already exists)
     * @throws ValidationException
     * if the entitty is not valid
     */
    public Homework save(Homework homework) throws ValidationException {
        try {
            Homework h = repoHomework.save(homework);
            notifyObserver(new HomeworkEvent(null, homework, ChangeEventType.ADD));
            return h;
        }catch(IllegalArgumentException ex){
            ex.getStackTrace();
        }
        return null;
    }

    /**
     *
     * @return all entities
     */
    public Iterable<Homework> findAllHomeworks(){
        return repoHomework.findAll();
    }

    /**
     *
     * @param id
     * @return the entity with the specified id
     * or null - if there is no entity with the given id
     */
    public Homework findOneHomework(int id){
        try {
            return repoHomework.findOne(id);
        } catch(IllegalArgumentException ex){
            ex.getStackTrace();
            return null;
        }
    }

    public Homework delete(int id) throws ValidationException{
        try {
            Homework homework = repoHomework.delete(id);
            if (homework == null)
                throw new ValidationException("This entity does not exist !");
            notifyObserver(new HomeworkEvent(homework, homework, ChangeEventType.REMOVE));
            return homework;
        }catch(IllegalArgumentException ex){
            ex.getStackTrace();
            return null;
        }
    }

    public Homework update(Homework homework) throws ValidationException{
        try {
            Homework h = repoHomework.update(homework);
            Homework oldHomework = repoHomework.findOne(homework.getId());
            if (oldHomework == null)
                throw new ValidationException("This entity does not exist !");
            notifyObserver(new HomeworkEvent(oldHomework,homework,ChangeEventType.UPDATE));
            return h;
        }catch(IllegalArgumentException ex){
            ex.getStackTrace();
            return null;
        }
    }



    /**
     *
     * @param id - the id of the Homework to be returned
     * @return false if the entity is null or currentDate greater than deadline
     * or true - otherwise
     */
    public boolean extensionDeadline(int id){
        Homework homework = repoHomework.findOne(id);
        if (homework == null)
            return false;
        int result = currentWeek();
        if (result <= homework.getDeadline()){
            repoHomework.update(new Homework(homework.getId(), homework.getDescription(),
                    homework.getDeadline() + 1, homework.getReceived()));
            return true;
        }
        return false;
    }


    /**
     *
     * @return the current week
     */
    public int currentWeek(){
        int currentWeek;
        Calendar calendar = Calendar.getInstance();
        Date date = new Date();
        calendar.setTime(date);
        if (calendar.get(Calendar.WEEK_OF_YEAR) == 52 || calendar.get(Calendar.WEEK_OF_YEAR) == 1)
            currentWeek = 51;
        else if (calendar.get(Calendar.WEEK_OF_YEAR) == 2)
            currentWeek = 52;
        else if (calendar.get(Calendar.WEEK_OF_YEAR) == 3)
            currentWeek = 53;
        else
            currentWeek = calendar.get(Calendar.WEEK_OF_YEAR);

        try {
            calendar.setTime(new SimpleDateFormat("dd/MM/yyyy").parse("30/09/2018"));
        }catch(ParseException ex){
            ex.getStackTrace();
        }
        int firstWeek = calendar.get(Calendar.WEEK_OF_YEAR);
        int result = currentWeek - firstWeek;
        return result;
    }

}
