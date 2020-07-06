package fr.dtrx.androidcore.basics;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import fr.dtrx.androidcore.R;
import fr.dtrx.androidcore.utils.KeyboardUtils;
import fr.dtrx.androidcore.utils.NetworkUtils;

@SuppressWarnings("unused")
public abstract class BaseActivity<T extends ViewDataBinding> extends AppCompatActivity {

    protected T binding;

    protected Bundle bundle;
    protected Toolbar toolbar;
    protected TextView toolbarTitle;

    private NotificationReceiver notificationReceiver = new NotificationReceiver();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.bundle = getIntent().getExtras();

        initializeData();
        initializeView();
        initializeToolbar();
        initializeListeners();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (notificationReceiver != null) {
            unregisterReceiver(notificationReceiver);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    public abstract int layoutResId();

    public MenuDisplay displayHomeButton() {
        return MenuDisplay.NOTHING;
    }

    public boolean isActivityFullscreen() {
        return false;
    }

    /**
     * Data initialization
     */
    public void initializeData() {
    }

    /**
     * Process the custom toolbar initialization
     */
    public void initializeToolbar() {
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayShowTitleEnabled(false);
            }
        }

        switch (displayHomeButton()) {
            case BACK: {
                enableBackButton();
                break;
            }
            case CLOSE: {
                enableCloseButton();
                break;
            }
            case MENU: {
                enableMenuButton();
                break;
            }
            default: {
                disableButtons();
                break;
            }
        }
    }

    /**
     * Views initialization
     */
    public void initializeView() {
        if (isActivityFullscreen()) {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        binding = DataBindingUtil.setContentView(this, layoutResId());
        toolbar = findViewById(R.id.toolbar);
        toolbarTitle = findViewById(R.id.toolbar_title);

        if (notificationReceiver != null) {
            IntentFilter filter = new IntentFilter(NotificationReceiver.TAG);
            registerReceiver(notificationReceiver, filter);
        }
    }

    /**
     * Listeners initialization
     */
    public void initializeListeners() {
        if (notificationReceiver != null) {
            notificationReceiver.setNotificationListener(getNotificationListener());
        }
    }

    /**
     * Post listeners initialization
     */
    public void postListenersInitialized() {
    }

    public NotificationReceiver.NotificationListener getNotificationListener() {
        return null;
    }

    private void enableBackButton() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void enableCloseButton() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        }
    }

    private void enableMenuButton() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);
        }
    }

    private void disableButtons() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
    }

    @Override
    public void setTitle(int titleId) {
        if (toolbarTitle != null) {
            toolbarTitle.setText(titleId);
        }
        else {
            super.setTitle(titleId);
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        if (toolbarTitle != null) {
            toolbarTitle.setText(title);
        }
        else {
            super.setTitle(title);
        }
    }

    protected void hideKeyboard() {
        KeyboardUtils.hideKeyboard(this);
    }

    protected boolean isConnected() {
        return NetworkUtils.isConnected(this);
    }

    protected boolean hasToolbar() {
        return toolbar != null;
    }

    public enum MenuDisplay {
        NOTHING,
        BACK,
        CLOSE,
        MENU
    }

    public static class NotificationReceiver extends BroadcastReceiver {
        public static String TAG = "notification_receiver";
        public static String EXTRA_TITLE = "title";
        public static String EXTRA_DESCRIPTION = "description";

        public NotificationListener notificationListener;

        public void setNotificationListener(NotificationListener notificationListener) {
            this.notificationListener = notificationListener;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            if (notificationListener != null) {
                notificationListener.onNotificationReceive(context, intent);
            }
        }

        public interface NotificationListener {
            void onNotificationReceive(Context context, Intent intent);
        }
    }

}
