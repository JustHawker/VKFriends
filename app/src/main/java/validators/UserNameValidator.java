package validators;

import android.support.design.widget.TextInputLayout;
import android.util.Pair;

import java.util.List;
import constants.ValidationConstants;
public class UserNameValidator {
    private String text;
    public UserNameValidator(String text){
        this.text = text;
    }
    public void setText(String text){
        this.text = text;
    }
    public int isValid() {
        if (text.isEmpty())
            return ValidationConstants.EMPTY_STRING;
        if (!text.equals(text.replaceAll("\\s+", "")))
            return ValidationConstants.SPACES;
        return ValidationConstants.SUCCESS;
    }
}
