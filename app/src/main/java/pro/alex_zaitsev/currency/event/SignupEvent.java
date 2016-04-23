package pro.alex_zaitsev.currency.event;

import com.parse.ParseException;
import com.parse.ParseUser;

/**
 * Created by rocknow on 08.03.2015.
 */
public class SignupEvent extends ParseEvent {

    private ParseUser user;

    public SignupEvent(ParseException parseException) {
        super(parseException);
    }

    public SignupEvent(ParseUser user) {
        super(null);
        this.user = user;
    }

    @Override
    public boolean isSuccess() {
        return super.isSuccess() && user != null;
    }

    public ParseUser getUser() {
        return user;
    }
}
