package pro.alex_zaitsev.currency.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.Bind;
import pro.alex_zaitsev.currency.App;
import pro.alex_zaitsev.currency.R;
import pro.alex_zaitsev.currency.interfaces.callback.LifeTimeFragmentCallback;
import pro.alex_zaitsev.currency.utils.InAppPurchase;

/**
 * Created by rocknow on 05.03.2015.
 */
public class LifeTimeFragment extends BaseFragment<LifeTimeFragmentCallback> {

    @Bind(R.id.btn_is_colored)
    Button btnIsColored;
    @Bind(R.id.btn_is_top)
    Button btnIsTop;
    @Bind(R.id.txt_my_adverts_info)
    TextView txtMyAdvertsInfo;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_life_time, container, false);
        ButterKnife.bind(this, view);
        txtMyAdvertsInfo.setVisibility(callback.isUserLoggedIn() ? View.VISIBLE : View.GONE);
        btnIsColored.setOnClickListener(buyClickListener);
        btnIsTop.setOnClickListener(buyClickListener);
        return view;
    }

    private View.OnClickListener buyClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            callback.buy(getSku(view));
        }
    };

    private String getSku(View view) {
        switch (view.getId()) {
            case R.id.btn_is_colored:
                App.getAnalytics().onBuyColorClick();
                return InAppPurchase.SKU_IS_COLORED;
            case R.id.btn_is_top:
                App.getAnalytics().onBuyTopClick();
                return InAppPurchase.SKU_IS_TOP;
        }
        return null;
    }

    public void onBought(String productId) {
        if (!TextUtils.isEmpty(productId)) {
            int icon = R.drawable.ic_action_accept;
            switch (productId) {
                case InAppPurchase.SKU_IS_TOP:
                    btnIsColored.setCompoundDrawablesWithIntrinsicBounds(0, 0, icon, 0);
                    btnIsTop.setCompoundDrawablesWithIntrinsicBounds(0, 0, icon, 0);
                    break;
                case InAppPurchase.SKU_IS_COLORED:
                    btnIsColored.setCompoundDrawablesWithIntrinsicBounds(0, 0, icon, 0);
                    break;
            }
        }
    }
}
