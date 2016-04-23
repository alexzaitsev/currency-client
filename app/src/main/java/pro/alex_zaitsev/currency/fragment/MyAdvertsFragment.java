package pro.alex_zaitsev.currency.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import java.util.Comparator;

import pro.alex_zaitsev.currency.R;
import pro.alex_zaitsev.currency.event.MyAdvertGetEvent;
import pro.alex_zaitsev.currency.interfaces.callback.MyAdvertsFragmentCallback;
import pro.alex_zaitsev.currency.object.comparator.AdvertEnabledComparator;
import pro.alex_zaitsev.currency.object.model.Advert;

/**
 * Created by rocknow on 14.03.2015.
 */
public class MyAdvertsFragment extends BaseAdvertListFragment<MyAdvertsFragmentCallback> {

    private AdvertEnabledComparator comparator = new AdvertEnabledComparator();
    Button btnCreateAdvert;

    @Override
    protected void initEmptyView(LayoutInflater inflater) {
        inflater.inflate(R.layout.layout_empty_my_adverts, emptyView, true);
        btnCreateAdvert = (Button) emptyView.findViewById(R.id.btn_create_advert);
        btnCreateAdvert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.onNewAdvertClick();
            }
        });
    }

    @Override
    protected Comparator<Advert> getComparator() {
        return comparator;
    }

    public void onMyAdvertGetEvent(MyAdvertGetEvent event) {
        if (!isAdded()) {
            return;
        }
        onGetEvent(event.getAdvertList());
    }
}
