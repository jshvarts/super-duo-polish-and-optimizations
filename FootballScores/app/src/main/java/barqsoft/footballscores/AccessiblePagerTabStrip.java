package barqsoft.footballscores;

import android.content.Context;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.widget.TextView;

/**
 * Accessible version of PagerTabStrip.
 */
public class AccessiblePagerTabStrip extends PagerTabStrip {
    public AccessiblePagerTabStrip(Context context) {
        super(context);
    }

    public AccessiblePagerTabStrip(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onRequestSendAccessibilityEvent(View child, AccessibilityEvent event) {
        final String textViewTitle = ((TextView) child).getText().toString();
        final ViewPager viewPager = (ViewPager) this.getParent();
        final int itemIndex = viewPager.getCurrentItem();
        String title = viewPager.getAdapter().getPageTitle(itemIndex).toString();

        if (textViewTitle.equalsIgnoreCase(title)) {
            child.setContentDescription("Tab " + textViewTitle + "selected.");
        } else {
            child.setContentDescription("Tab " + textViewTitle + "not selected.");
        }

        return super.onRequestSendAccessibilityEvent(child, event);
    }
}
