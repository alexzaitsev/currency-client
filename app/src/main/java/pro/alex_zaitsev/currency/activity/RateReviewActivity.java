package pro.alex_zaitsev.currency.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;

import butterknife.ButterKnife;
import butterknife.Bind;
import pro.alex_zaitsev.currency.App;
import pro.alex_zaitsev.currency.R;
import pro.alex_zaitsev.currency.presenter.RateReviewActivityPresenter;
import pro.alex_zaitsev.currency.utils.Toaster;

/**
 * Created by rocknow on 15.03.2015.
 */
public class RateReviewActivity extends BaseUpActivity<RateReviewActivityPresenter> {

    @Bind(R.id.rating)
    RatingBar ratingBar;
    @Bind(R.id.layout_feedback)
    View layoutFeedback;
    @Bind(R.id.edit_feedback)
    EditText editFeedback;

    @Override
    protected void setPresenter() {
        presenter = new RateReviewActivityPresenter(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_review);
        ButterKnife.bind(this);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean b) {
                App.getAnalytics().onRate((int) rating);
                if (presenter.isRatingGood((int) rating)) {
                    presenter.goMarket();
                    finish();
                } else {
                    showFeedBackForm(true);
                }
                ratingBar.setEnabled(false);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.refresh_done, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            App.getAnalytics().onRateRefresh();
            hideKeyboard();
            ratingBar.setRating(0);
            ratingBar.setEnabled(true);
            showFeedBackForm(false);
            return true;
        } else if (id == R.id.action_done) {
            App.getAnalytics().onRateDone();
            int rating = (int) ratingBar.getRating();
            if (rating == 0) {
                // user is lazy :(
                Toaster.makeToastShort(this, R.string.toast_warn_rating);
                showFeedBackForm(false);
            } else {
                sendFeedback();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showFeedBackForm(boolean visible) {
        layoutFeedback.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    private void sendFeedback() {
        int rating = (int) ratingBar.getRating();
        String feedback = editFeedback.getText().toString();
        presenter.sendEmail(rating, feedback);
    }
}
