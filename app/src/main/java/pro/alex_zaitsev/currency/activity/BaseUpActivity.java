package pro.alex_zaitsev.currency.activity;

import android.os.Bundle;
import android.view.MenuItem;

import pro.alex_zaitsev.currency.presenter.core.ActivityPresenter;

/**
 * Created by rocknow on 21.03.2015.
 */
public abstract class BaseUpActivity<P extends ActivityPresenter> extends BaseActivity<P> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
