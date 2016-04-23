package pro.alex_zaitsev.currency.adapter;

import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rocknow on 01.03.2015.
 */

public abstract class BaseObjectAdapter<T> extends BaseAdapter {

    public List<T> items = new ArrayList<>();

    public BaseObjectAdapter(List<T> items) {
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public T getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }
}