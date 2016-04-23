package pro.alex_zaitsev.currency.fragment;

import android.view.LayoutInflater;
import android.view.View;

import java.util.Comparator;
import java.util.List;

import pro.alex_zaitsev.currency.R;
import pro.alex_zaitsev.currency.interfaces.callback.BaseAdvertListFragmentCallback;
import pro.alex_zaitsev.currency.object.comparator.AdvertSourceComparator;
import pro.alex_zaitsev.currency.object.model.Advert;

/**
 * Created by rocknow on 01.03.2015.
 */
public class AdvertListFragment extends BaseAdvertListFragment<BaseAdvertListFragmentCallback> {

    private AdvertSourceComparator comparator = new AdvertSourceComparator();

    @Override
    protected void initEmptyView(LayoutInflater inflater) {
        inflater.inflate(R.layout.layout_empty_advert_list, emptyView, true);
        emptyView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startLoading();
                }
            });
    }

    @Override
    protected Comparator<Advert> getComparator() {
        return comparator;
    }

    public void onAdvertGetEvent(List<Advert> advertList) {
        if (!isAdded()) {
            return;
        }
        onGetEvent(advertList);
    }

    public void onSortChanged() {
        if (!isAdded()) {
            return;
        }
        notifyDataSetChanged();
    }
}
