package pro.alex_zaitsev.currency.event;

import com.parse.ParseException;

/**
 * Created by rocknow on 21.03.2015.
 */
public class MyAdvertCountEvent extends ParseEvent {

    private int count;

    public MyAdvertCountEvent(ParseException parseException) {
        super(parseException);
    }

    public MyAdvertCountEvent(int count) {
        super(null);
        this.count = count;
    }

    public int getCount() {
        return count;
    }
}
