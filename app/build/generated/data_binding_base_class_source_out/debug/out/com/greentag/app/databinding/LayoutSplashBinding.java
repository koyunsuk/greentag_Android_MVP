// Generated by view binder compiler. Do not edit!
package com.greentag.app.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.greentag.app.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class LayoutSplashBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final Button buttonBenefit;

  @NonNull
  public final Button buttonReset;

  @NonNull
  public final Button buttonReturn;

  @NonNull
  public final ProgressBar progressBar;

  @NonNull
  public final LinearLayout splashLayout;

  @NonNull
  public final TextView subtitleText;

  @NonNull
  public final TextView textViewCo2;

  @NonNull
  public final TextView textViewSummary;

  @NonNull
  public final TextView textViewTree;

  @NonNull
  public final TextView titleText;

  private LayoutSplashBinding(@NonNull LinearLayout rootView, @NonNull Button buttonBenefit,
      @NonNull Button buttonReset, @NonNull Button buttonReturn, @NonNull ProgressBar progressBar,
      @NonNull LinearLayout splashLayout, @NonNull TextView subtitleText,
      @NonNull TextView textViewCo2, @NonNull TextView textViewSummary,
      @NonNull TextView textViewTree, @NonNull TextView titleText) {
    this.rootView = rootView;
    this.buttonBenefit = buttonBenefit;
    this.buttonReset = buttonReset;
    this.buttonReturn = buttonReturn;
    this.progressBar = progressBar;
    this.splashLayout = splashLayout;
    this.subtitleText = subtitleText;
    this.textViewCo2 = textViewCo2;
    this.textViewSummary = textViewSummary;
    this.textViewTree = textViewTree;
    this.titleText = titleText;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static LayoutSplashBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static LayoutSplashBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.layout_splash, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static LayoutSplashBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.buttonBenefit;
      Button buttonBenefit = ViewBindings.findChildViewById(rootView, id);
      if (buttonBenefit == null) {
        break missingId;
      }

      id = R.id.buttonReset;
      Button buttonReset = ViewBindings.findChildViewById(rootView, id);
      if (buttonReset == null) {
        break missingId;
      }

      id = R.id.buttonReturn;
      Button buttonReturn = ViewBindings.findChildViewById(rootView, id);
      if (buttonReturn == null) {
        break missingId;
      }

      id = R.id.progressBar;
      ProgressBar progressBar = ViewBindings.findChildViewById(rootView, id);
      if (progressBar == null) {
        break missingId;
      }

      LinearLayout splashLayout = (LinearLayout) rootView;

      id = R.id.subtitleText;
      TextView subtitleText = ViewBindings.findChildViewById(rootView, id);
      if (subtitleText == null) {
        break missingId;
      }

      id = R.id.textViewCo2;
      TextView textViewCo2 = ViewBindings.findChildViewById(rootView, id);
      if (textViewCo2 == null) {
        break missingId;
      }

      id = R.id.textViewSummary;
      TextView textViewSummary = ViewBindings.findChildViewById(rootView, id);
      if (textViewSummary == null) {
        break missingId;
      }

      id = R.id.textViewTree;
      TextView textViewTree = ViewBindings.findChildViewById(rootView, id);
      if (textViewTree == null) {
        break missingId;
      }

      id = R.id.titleText;
      TextView titleText = ViewBindings.findChildViewById(rootView, id);
      if (titleText == null) {
        break missingId;
      }

      return new LayoutSplashBinding((LinearLayout) rootView, buttonBenefit, buttonReset,
          buttonReturn, progressBar, splashLayout, subtitleText, textViewCo2, textViewSummary,
          textViewTree, titleText);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
