// Generated code from Butter Knife. Do not modify!
package iotdf.iotgateway;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class DevicePanel$$ViewInjector {
  public static void inject(Finder finder, final iotdf.iotgateway.DevicePanel target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131558630, "field 'toolbar'");
    target.toolbar = (android.support.v7.widget.Toolbar) view;
    view = finder.findRequiredView(source, 2131558569, "field 'tabs'");
    target.tabs = (com.astuetz.PagerSlidingTabStrip) view;
    view = finder.findRequiredView(source, 2131558570, "field 'pager'");
    target.pager = (android.support.v4.view.ViewPager) view;
    view = finder.findRequiredView(source, 2131558631, "method 'onClick'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onClick(p0);
        }
      });
    view = finder.findRequiredView(source, 2131558609, "method 'onClick'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onClick(p0);
        }
      });
    view = finder.findRequiredView(source, 2131558549, "method 'onClick'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onClick(p0);
        }
      });
  }

  public static void reset(iotdf.iotgateway.DevicePanel target) {
    target.toolbar = null;
    target.tabs = null;
    target.pager = null;
  }
}
