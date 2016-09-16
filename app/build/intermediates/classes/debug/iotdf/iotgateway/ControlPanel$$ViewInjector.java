// Generated code from Butter Knife. Do not modify!
package iotdf.iotgateway;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class ControlPanel$$ViewInjector {
  public static void inject(Finder finder, final iotdf.iotgateway.ControlPanel target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131558553, "field 'switchCompat'");
    target.switchCompat = (android.support.v7.widget.SwitchCompat) view;
    view = finder.findRequiredView(source, 2131558562, "field 'Tv_time'");
    target.Tv_time = (android.widget.TextView) view;
    view = finder.findRequiredView(source, 2131558555, "field 'slider'");
    target.slider = (com.rey.material.widget.Slider) view;
    view = finder.findRequiredView(source, 2131558551, "method 'onclick'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onclick(p0);
        }
      });
    view = finder.findRequiredView(source, 2131558556, "method 'onclick'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onclick(p0);
        }
      });
    view = finder.findRequiredView(source, 2131558557, "method 'onclick'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onclick(p0);
        }
      });
    view = finder.findRequiredView(source, 2131558558, "method 'onclick'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onclick(p0);
        }
      });
    view = finder.findRequiredView(source, 2131558559, "method 'onclick'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onclick(p0);
        }
      });
  }

  public static void reset(iotdf.iotgateway.ControlPanel target) {
    target.switchCompat = null;
    target.Tv_time = null;
    target.slider = null;
  }
}
