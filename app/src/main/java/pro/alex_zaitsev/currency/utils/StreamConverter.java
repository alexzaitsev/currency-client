package pro.alex_zaitsev.currency.utils;

import java.io.InputStream;

/**
 * Created by rocknow on 10.03.2015.
 */
public class StreamConverter {

    public static String toString(InputStream in) {
        java.util.Scanner s = new java.util.Scanner(in).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
}
