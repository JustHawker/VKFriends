package com.example.vkfriends;

import org.junit.Test;
import org.mockito.Mock;

import constants.ValidationConstants;
import validators.UserNameValidator;

import static org.junit.Assert.*;

public class UserNameValidatorTest {

    @Test
    public void validationIsCorrect() {
        UserNameValidator validator = new UserNameValidator("text");
        assertEquals(ValidationConstants.SUCCESS,validator.isValid());
    }

    @Test
    public void validationIsSpacesError(){
        UserNameValidator validator = new UserNameValidator("text"+" ");
        assertEquals(ValidationConstants.SPACES,validator.isValid());
    }
    @Test
    public void validationIsEmptyError(){
        UserNameValidator validator = new UserNameValidator("");
        assertEquals(ValidationConstants.EMPTY_STRING,validator.isValid());
    }
}