package pro.alex_zaitsev.currency.event;

import com.parse.ParseException;

/**
 * Created by rocknow on 01.03.2015.
 */
public abstract class ParseEvent extends ApiEvent {

    protected ParseException parseException;

    public ParseEvent(ParseException parseException) {
        this.parseException = parseException;
    }

    public boolean isSuccess() {
        return parseException == null;
    }

    public ParseException getParseException() {
        return parseException;
    }
}
