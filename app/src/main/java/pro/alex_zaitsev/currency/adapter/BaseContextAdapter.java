package pro.alex_zaitsev.currency.adapter;

import android.content.Context;
import android.view.LayoutInflater;

import java.util.List;

/**
 * Created by rocknow on 01.03.2015.
 */
public abstract class BaseContextAdapter<T> extends BaseObjectAdapter<T> {

    protected Context context;
    protected LayoutInflater inflater;

    protected BaseContextAdapter(Context context, List<T> list) {
        super(list);
        this.context = context;
        inflater = LayoutInflater.from(context);
    }
}
