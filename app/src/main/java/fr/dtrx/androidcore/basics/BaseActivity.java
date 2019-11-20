package fr.dtrx.androidcore.basics;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import fr.dtrx.androidcore.R;
import fr.dtrx.androidcore.utils.KeyboardUtils;
import fr.dtrx.androidcore.utils.NetworkUtils;

@SuppressWarnings("unused")
public abstract class BaseActivity extends AppCompatActivity {

    protected Bundle bundle;

    protected Toolbar toolbar;

    private ViewDataBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.bundle = getIntent().getExtras();

        initializeData();
        initializeView();
        initializeToolbar();
        initializeListeners();
    }

    @SuppressWarnings("unchecked")
    protected <T extends ViewDataBinding> T getBinding() {
        return (T) binding;
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
    }

    /**
     * Listeners initialization
     */
    public void initializeListeners() {
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

}
