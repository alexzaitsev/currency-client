package pro.alex_zaitsev.currency.fragment;

import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.Bind;
import pro.alex_zaitsev.currency.R;
import pro.alex_zaitsev.currency.interfaces.callback.LogoutFragmentCallback;

/**
 * Created by rocknow on 12.03.2015.
 */
public class LogoutFragment extends BaseFragment<LogoutFragmentCallback> {

    @Bind(R.id.txt_signed_as_email)
    TextView txtSignedAsEmail;
    @Bind(R.id.layout_my_adverts)
    View myAdverts;
    @Bind(R.id.txt_my_adverts_count)
    TextView txtMyAdvertsCount;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_logout, container, false);
        ButterKnife.bind(this, view);

        callback.getMyAdvertsCount();

        String userEmail = callback.getUserEmail();
        SpannableString emailText = new SpannableString(userEmail);
        emailText.setSpan(new UnderlineSpan(), 0, emailText.length(), 0);
        txtSignedAsEmail.setText(emailText);

        myAdverts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.onMyAdvertsClick();
            }
        });

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.sign_out, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_sign_out) {
            callback.signOut();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void setMyAdvertsCount(int count) {
        if (!isAdded()) {
            return;
        }
        txtMyAdvertsCount.setText(count + "");
    }
}
