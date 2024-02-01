package mate.academy.mainspringproject.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;
import mate.academy.mainspringproject.exception.RegistrationException;

public class FieldMatchValidator implements ConstraintValidator<FieldMatch, Object> {
    private String firstFieldName;
    private String secondFieldName;

    @Override
    public void initialize(FieldMatch constraintAnnotation) {
        firstFieldName = constraintAnnotation.first();
        secondFieldName = constraintAnnotation.second();
    }

    @Override
    public boolean isValid(Object object, ConstraintValidatorContext context) {
        try {
            Field firstField = object.getClass().getDeclaredField(firstFieldName);
            Field secondField = object.getClass().getDeclaredField(secondFieldName);

            firstField.setAccessible(true);
            secondField.setAccessible(true);

            Object firstFieldValue = firstField.get(object);
            Object secondFieldValue = secondField.get(object);

            if (firstFieldValue == null || !firstFieldValue.equals(secondFieldValue)) {
                return false;
            }
        } catch (Exception e) {
            throw new RegistrationException("The contents of field " + firstFieldName
                    + " and field " + secondFieldName + " do not match");
        }
        return true;
    }
}
