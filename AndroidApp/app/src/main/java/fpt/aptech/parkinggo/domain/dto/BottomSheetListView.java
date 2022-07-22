package fpt.aptech.parkinggo.domain.dto;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

public class BottomSheetListView extends ListView {
    public BottomSheetListView (Context context, AttributeSet p_attrs) {
        super (context, p_attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (canScrollVertically(BottomSheetListView.this)) {
            getParent().requestDisallowInterceptTouchEvent(true);
        }
        return super.onTouchEvent(ev);
    }

    public boolean canScrollVertically (BottomSheetListView view) {
        boolean canScroll = false;
        if (view !=null && view.getChildCount ()> 0) {
            boolean isOnTop = view.getFirstVisiblePosition() != 0 || view.getChildAt(0).getTop() != 0;
            boolean isAllItemsVisible = isOnTop && view.getLastVisiblePosition() == view.getChildCount();

            if (isOnTop || isAllItemsVisible) {
                canScroll = true;
            }
        }

        return  canScroll;
    }
}
