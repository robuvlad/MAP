package sample.repository;

import sample.domain.HasId;
import sample.validation.ValidationException;
import sample.validation.Validator;

import java.util.LinkedHashMap;
import java.util.Map;

public abstract class AbstractRepository<ID, E extends HasId<ID>> implements Repository<ID, E> {

    public Map<ID, E> map;
    Validator<E> validator;

    public AbstractRepository(Validator validator){
        map = new LinkedHashMap<>();
        this.validator = validator;
    }

    @Override
    public E findOne(ID id){
        if (id == null)
            throw new IllegalArgumentException("Id is null !");
        if (map.get(id) == null)
            return null;
        return map.get(id);
    }

    @Override
    public Iterable<E> findAll(){
        return map.values();
    }

    @Override
    public E save(E entity) throws ValidationException {
        if (entity == null)
            throw new IllegalArgumentException("Entity is null !");
        validator.validate(entity);
        if (map.get(entity.getId()) == null)
            return map.put(entity.getId(), entity);
        else
            throw new ValidationException("Entity already exists !");
    }

    @Override
    public E delete(ID id){
        if (id == null)
            throw new IllegalArgumentException("Id is null !");
        if (map.get(id) == null)
            return null;
        return map.remove(id);
    }

    @Override
    public E update(E entity){
        if (entity == null)
            throw new IllegalArgumentException("Entity is null !");
        try{
            validator.validate(entity);
            if (findOne(entity.getId()) == null)
                return null;
            return map.replace(entity.getId(), entity);
        } catch(ValidationException e){
            return null;
        }
    }

}
