package fr.dtrx.androidcore.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.method.ScrollingMovementMethod;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

@SuppressWarnings({"unused", "WeakerAccess"})
@SuppressLint("ClickableViewAccessibility")
public class ViewUtils {

    public static int getTotalHeightofListView(ListView listView) {
        ListAdapter mAdapter = listView.getAdapter();

        int totalHeight = 0;

        for (int i = 0; i < mAdapter.getCount(); i++) {
            View mView = mAdapter.getView(i, null, listView);

            mView.measure(
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
            );

            totalHeight += mView.getMeasuredHeight();
        }

        return totalHeight + (listView.getDividerHeight() * (mAdapter.getCount() - 1));
    }

    public static float dpToPx(Context context, float dip) {
        return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dip,
                context.getResources().getDisplayMetrics()
        );
    }

    /**
     * Enabling the scroll in a View
     *
     * @param view      The view
     * @param condition The condition
     */
    public static void enableScroll(View view, boolean condition) {
        if (view instanceof TextView) {
            TextView textView = (TextView) view;
            textView.setMovementMethod(!condition ? null : new ScrollingMovementMethod());
        }

        view.setOnTouchListener(!condition ? null : (View.OnTouchListener) (v, event) -> {
            v.getParent().requestDisallowInterceptTouchEvent(true);
            if ((event.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_UP) {
                v.getParent().requestDisallowInterceptTouchEvent(false);
            }
            return false;
        });
    }

}
