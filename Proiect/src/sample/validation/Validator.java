package sample.validation;

public interface Validator<E> {

    /**
     *@param entity - the entity which is validated
     *validate Entity
     *@throws ValidationException
     */
    void validate(E entity) throws ValidationException;

}
