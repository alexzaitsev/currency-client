package pro.alex_zaitsev.currency.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.Build;
import android.view.View;

/**
 * Created by rocknow on 14.03.2015.
 */
public class AnimatedViewSwitcher {

    private View primaryView;
    private View secondaryView;

    public AnimatedViewSwitcher(View primaryView, View secondaryView) {
        this.primaryView = primaryView;
        this.secondaryView = secondaryView;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showPrimary(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = primaryView.getContext().getResources().
                    getInteger(android.R.integer.config_shortAnimTime);

            primaryView.setVisibility(show ? View.VISIBLE : View.GONE);
            primaryView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    primaryView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });

            secondaryView.setVisibility(show ? View.GONE : View.VISIBLE);
            secondaryView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    secondaryView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            secondaryView.setVisibility(show ? View.GONE : View.VISIBLE);
            primaryView.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }
}
