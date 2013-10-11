package com.node.browser.webviewmanager;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.animation.AccelerateInterpolator;
import android.widget.FrameLayout;

public class HeaderAreaManager {

	FrameLayout mHeader;
	private final long DEFAULT_HEADER_SHOW_HIDEN_DURATION = 100; // 显示、隐藏头部区域的时间戳
	private ObjectAnimator animOnShow;// 显示时的动画
	private ObjectAnimator animOnHiden;// 隐藏时的动画
	private final String TAG_HIDENED = "hidened";
	private final String TAG_SHOWN = "shown";
	private final String TAG_SHOWING = "showing";
	private final String TAG_HIDENING = "hidening";
	private final String TAG_HALF_SHOW = "halfshown";

	private static HeaderAreaManager headerManager;

	private HeaderAreaManager() {
	};

	public static HeaderAreaManager instance() {
		if (headerManager == null) {
			headerManager = new HeaderAreaManager();
		}
		return headerManager;
	}

	public void init(FrameLayout header) {
		this.mHeader = header;
		this.mHeader.setTag(TAG_SHOWN);
	}

	public void hidenByTag() {
		String tag = tag();
		if (!tag.equals(TAG_HALF_SHOW) && !tag.equals(TAG_SHOWN)) {
			return;
		}
		ObjectAnimator animOnHiden = ObjectAnimator.ofFloat(mHeader, "y",
				getY(), -height());
		animOnHiden.addListener(new Animator.AnimatorListener() {
			@Override
			public void onAnimationStart(Animator animation) {
				setTag(TAG_HIDENING);
			}

			@Override
			public void onAnimationRepeat(Animator animation) {
			}

			@Override
			public void onAnimationEnd(Animator animation) {
				setTag(TAG_HIDENED);
			}

			@Override
			public void onAnimationCancel(Animator animation) {
			}
		});
		animOnHiden.setDuration(DEFAULT_HEADER_SHOW_HIDEN_DURATION);
		animOnHiden.setInterpolator(new AccelerateInterpolator());
		animOnHiden.start();
	}

	/**
	 * 只有在hiden状态和半显示状态 才开始动画
	 */
	public void showByTag() {
		String tag = tag();
		if (!tag.equals(TAG_HIDENED) && !tag.equals(TAG_HALF_SHOW)) {
			return;
		}
		ObjectAnimator animOnShow = ObjectAnimator.ofFloat(mHeader, "y",
				getY(), 0);
		animOnShow.addListener(new Animator.AnimatorListener() {
			@Override
			public void onAnimationStart(Animator animation) {
				setTag(TAG_SHOWING);
			}

			@Override
			public void onAnimationRepeat(Animator animation) {
			}

			@Override
			public void onAnimationEnd(Animator animation) {
				setTag(TAG_SHOWN);
			}

			@Override
			public void onAnimationCancel(Animator animation) {
			}
		});
		animOnShow.setDuration(DEFAULT_HEADER_SHOW_HIDEN_DURATION);
		animOnShow.setInterpolator(new AccelerateInterpolator());
		animOnShow.start();

	}

	private float getY() {
		return mHeader.getY();
	}

	public void changeByDynamicWay(int l, int t, int oldl, int oldt) {
		String tag = tag();
		if (tag.equals(TAG_HIDENED) || tag.equals(TAG_HALF_SHOW)
				|| tag.equals(TAG_SHOWN)) {
			// 屏幕向下滑动
			if (t - oldt > 0) {
				if (t >= 4 * height()) {
					hidenByTag();
				}
				if (tag.equals(TAG_HIDENED)) {
					return;
				}
				if (t < height() && t >= 0) {
					setY(-t);
					setTag(TAG_HALF_SHOW);
				}
				if (t >= height() && t <= 2 * height()) {
					setY(-height());
					setTag(TAG_HIDENED);
				}
			}
			// 屏幕向上滑动
			else {
				if (t >= 4 * height()) {
					showByTag();
				}
				if (tag.equals(TAG_SHOWN)) {
					return;
				}
				if (t <= height() && t > 0) {
					setY(-t);
					setTag(TAG_HALF_SHOW);
				}
				if (t <= 0) {
					setY(0);
					setTag(TAG_SHOWN);
				}
			}
		}
	}

	private void setY(float y) {
		mHeader.setY(y);
	}

	private int height() {
		return mHeader.getHeight();
	}

	private String tag() {
		return (String) mHeader.getTag();
	}

	private void setTag(String tag) {
		mHeader.setTag(tag);
	}
}
