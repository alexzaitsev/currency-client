package pro.alex_zaitsev.currency.activity;

import android.os.Bundle;
import android.text.TextUtils;

import com.bluejamesbond.text.DocumentView;

import java.io.InputStream;

import butterknife.ButterKnife;
import butterknife.Bind;
import pro.alex_zaitsev.currency.R;
import pro.alex_zaitsev.currency.utils.StreamConverter;

/**
 * Created by rocknow on 28.02.2015.
 */
public class TermsActivity extends BaseUpActivity {

    @Bind(R.id.txt_terms_of_usage)
    DocumentView txtTermsOfUsage;

    @Override
    protected void setPresenter() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms);
        ButterKnife.bind(this);
        try {
            InputStream in = getResources().openRawResource(R.raw.terms);
            String text = StreamConverter.toString(in);
            if (TextUtils.isEmpty(text)) {
                text = getString(R.string.error);
            } else {
                String appName = getString(R.string.app_name);
                text = String.format(text, appName, appName, appName);
            }
            txtTermsOfUsage.setText(text);
        } catch (Exception e) {
            txtTermsOfUsage.setText(getString(R.string.error));
        }
    }
}
