package fr.dtrx.androidcore.utils;

import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;

@SuppressWarnings("unused")
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

}
