package com.node.browser.activity.dialogs;

import com.node.browser.R;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ActivitySplash extends Activity {
	ImageView mLogo;
	TextView mLogoName;
	LinearLayout mBackground;

	Handler handler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_splash);
		setFinishOnTouchOutside(false);
		handler = new Handler();
		initView();
		showComponent();
	}

	private void showComponent() {
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				mBackground.setVisibility(View.VISIBLE);
				startAnim();
			}
		}, 300);
	}

	private void hidenComponent() {
		ObjectAnimator oa = ObjectAnimator.ofFloat(mBackground, "alpha", 1, 0);
		oa.setDuration(DEFAULT_BACKGROUND_HIDEN_DURATION);
		oa.setInterpolator(new AccelerateDecelerateInterpolator());
		oa.addListener(new Animator.AnimatorListener() {
			@Override
			public void onAnimationStart(Animator animation) {
			}

			@Override
			public void onAnimationRepeat(Animator animation) {
			}

			@Override
			public void onAnimationEnd(Animator animation) {
				mBackground.setVisibility(View.GONE);
				finish();
			}
			@Override
			public void onAnimationCancel(Animator animation) {
			}
		});
		oa.start();
	}

	long DEFAULT_DURATION = 1000l;
	long DEFAULT_BACKGROUND_SHOW_DURATION = 1000l;
	long DEFAULT_BACKGROUND_HIDEN_DURATION = 1000l;
	long DEFAULT_SHOWTIME = 2000l;

	private void startAnim() {
		ObjectAnimator oa1 = ObjectAnimator.ofFloat(mBackground, "alpha", 0.5f,
				1f);
		oa1.setDuration(DEFAULT_BACKGROUND_SHOW_DURATION);
		oa1.setInterpolator(new AccelerateInterpolator());
		oa1.addListener(new Animator.AnimatorListener() {
			@Override
			public void onAnimationStart(Animator animation) {
			}

			@Override
			public void onAnimationRepeat(Animator animation) {
			}

			@Override
			public void onAnimationEnd(Animator animation) {
				ObjectAnimator oa = ObjectAnimator.ofFloat(mLogoName, "alpha",
						0, 1);
				oa.setDuration(DEFAULT_DURATION);
				oa.setInterpolator(new AccelerateDecelerateInterpolator());
				oa.addListener(new Animator.AnimatorListener() {
					@Override
					public void onAnimationStart(Animator animation) {
					}

					@Override
					public void onAnimationRepeat(Animator animation) {
					}

					@Override
					public void onAnimationEnd(Animator animation) {
						handler.postDelayed(new Runnable() {
							@Override
							public void run() {
								hidenComponent();
							}
						}, DEFAULT_SHOWTIME);
					}

					@Override
					public void onAnimationCancel(Animator animation) {
					}
				});
				oa.start();
			}
			public void onAnimationCancel(Animator animation) {
			}
		});
		oa1.start();
	}

	private void initView() {
		mLogo = (ImageView) findViewById(R.id.splash_node_logo);
		mLogoName = (TextView) findViewById(R.id.splash_node_logo_name);
		mBackground = (LinearLayout) findViewById(R.id.splash_dialog_bg_area);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}
}
