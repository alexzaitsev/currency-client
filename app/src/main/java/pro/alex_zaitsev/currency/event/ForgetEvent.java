package pro.alex_zaitsev.currency.event;

import com.parse.ParseException;
import com.parse.ParseUser;

/**
 * Created by rocknow on 08.03.2015.
 */
public class ForgetEvent extends ParseEvent {

    public ForgetEvent(ParseException parseException) {
        super(parseException);
    }
}
