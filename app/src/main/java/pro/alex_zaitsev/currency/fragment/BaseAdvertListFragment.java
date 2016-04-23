package pro.alex_zaitsev.currency.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;

import com.nhaarman.listviewanimations.appearance.ViewAnimator;
import com.nhaarman.listviewanimations.appearance.simple.SwingBottomInAnimationAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import pro.alex_zaitsev.currency.R;
import pro.alex_zaitsev.currency.adapter.AdvertAdapter;
import pro.alex_zaitsev.currency.event.AdvertDeletedEvent;
import pro.alex_zaitsev.currency.event.AdvertSavedEvent;
import pro.alex_zaitsev.currency.interfaces.callback.BaseAdvertListFragmentCallback;
import pro.alex_zaitsev.currency.object.model.Advert;
import pro.alex_zaitsev.currency.utils.Toaster;

/**
 * Created by rocknow on 16.03.2015.
 */
public abstract class BaseAdvertListFragment<C extends BaseAdvertListFragmentCallback>
        extends BaseListFragment<C>
        implements AdapterView.OnItemClickListener {

    private static final int INITIAL_DELAY_MILLIS = 300;
    private AdvertAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        listView.setAdapter(getAdapter());
        listView.setOnItemClickListener(this);
        return view;
    }

    private BaseAdapter getAdapter() {
        adapter = new AdvertAdapter(getActivity(), new ArrayList<Advert>());
        SwingBottomInAnimationAdapter swingBottomInAnimationAdapter = new SwingBottomInAnimationAdapter(adapter);
        swingBottomInAnimationAdapter.setAbsListView(listView);
        ViewAnimator viewAnimator = swingBottomInAnimationAdapter.getViewAnimator();
        if (viewAnimator != null) {
            viewAnimator.setInitialDelayMillis(INITIAL_DELAY_MILLIS);
        }
        return swingBottomInAnimationAdapter;
    }

    public void notifyDataSetChanged() {
        sort();
        ((BaseAdapter)listView.getAdapter()).notifyDataSetChanged();
    }

    @Override
    protected void initEmptyView(LayoutInflater inflater) {

    }

    @Override
    protected int getCurrentItemsCount() {
        return adapter == null ? 0 : adapter.getCount();
    }

    @Override
    protected void onLoadStarted() {
        callback.getAdverts();
    }

    protected void onGetEvent(List<Advert> advertList) {
        if (advertList != null) {
            onLoadCompleted();
            adapter.setItems(advertList);
            notifyDataSetChanged();
        } else {
            Toaster.makeToastLong(getActivity(), R.string.list_not_loaded);
        }
        checkEmptyView();
    }

    public void onAdvertSavedEvent(AdvertSavedEvent event) {
        if (!isAdded()) {
            return;
        }
        if (event.isSuccess()) {
            adapter.getItems().add(event.getAdvert());
            notifyDataSetChanged();
            checkEmptyView();
        } else {
            Toaster.makeToastLong(getActivity(), R.string.advert_not_posted);
        }
    }

    public void onAdvertDeletedEvent(AdvertDeletedEvent event) {
        if (!isAdded()) {
            return;
        }
        if (event.isSuccess()) {
            adapter.getItems().remove(event.getAdvert());
            notifyDataSetChanged();
            checkEmptyView();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        callback.onAdvertClick(adapter.getItem(i));
    }

    protected void sort() {
        Comparator<Advert> comparator = getComparator();
        if (comparator != null) {
            Collections.sort(adapter.getItems(), comparator);
        }
    }

    /**
     * This method should return current comparator. Note that it can be called several times
     * so it is a good approach to store current comparator in object and return it instead of
     * creating new instance every time
     * @return current advert comparator
     */
    protected abstract Comparator<Advert> getComparator();
}
