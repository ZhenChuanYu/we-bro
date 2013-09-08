package com.node.browser.activity.dialogs;

import com.node.browser.R;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 1.包含显示标题、内容和具有取消、确认操作的基本对话框<br>
 * 2.在DialogUtil中使用
 * {@code DialogUtil#showEnsureCancelDialog(Activity, String, String, ItfEnsureCallback, ItfCancelCallback)}
 * <br>
 * 3.ECDialog 为EnsureCancelDialog的简称<br>
 * 
 * @author zhenchuan
 * 
 */
public class ActivityECDialog extends Activity {

	static String EXTRA_KEY_TITLE = "key_title";
	static String EXTRA_KEY_CONTENT = "key_content";
	TextView mTitle;
	TextView mContent;
	String mStrTitle;
	String mStrContent;
	Button mBtnEnsure;
	Button mBtnCancel;
	View mContainer;// 顶层容器
	static ItfCancelCallback mCancelCallback;
	static ItfEnsureCallback mEnsureCallback;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_ensure_cancel_tip);
		handleIntent();
		initView();
		initAction();
		startShowAnim();
	}

	/**
	 * 渐变消失效果
	 */
	public void dismiss() {
		startHidenAnim();
	}

	private void handleIntent() {
		Intent intent = getIntent();
		mStrTitle = intent.getStringExtra(EXTRA_KEY_TITLE);
		mStrContent = intent.getStringExtra(EXTRA_KEY_CONTENT);
	}

	private void initAction() {
		mBtnEnsure.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mEnsureCallback != null) {
					mEnsureCallback.onEnsureClick(ActivityECDialog.this);
				}
			}
		});
		mBtnCancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mCancelCallback != null) {
					mCancelCallback.onCancelClick(ActivityECDialog.this);
				}
			}
		});
	}

	private void initView() {
		mTitle = (TextView) findViewById(R.id.dialog_title);
		mContent = (TextView) findViewById(R.id.dialog_content);
		mBtnEnsure = (Button) findViewById(R.id.dialog_btn_ensure);
		mBtnCancel = (Button) findViewById(R.id.dialog_btn_cancel);
		mContainer = findViewById(R.id.dialog_ensure_cancel_container);
		mTitle.setText(mStrTitle);
		mContent.setText(mStrContent);
	}

	final long DURATION_DEFAULT = 500l;

	private void startShowAnim() {
		ObjectAnimator oa = ObjectAnimator.ofFloat(mContainer, "alpha", 0f, 1f);
		oa.setDuration(DURATION_DEFAULT);
		oa.start();
	}

	private void startHidenAnim() {
		ObjectAnimator oa = ObjectAnimator.ofFloat(mContainer, "alpha", 1f, 0f);
		oa.setDuration(DURATION_DEFAULT);
		oa.addListener(new Animator.AnimatorListener() {
			@Override
			public void onAnimationStart(Animator animation) {
			}

			@Override
			public void onAnimationRepeat(Animator animation) {
			}

			@Override
			public void onAnimationEnd(Animator animation) {
				mContainer.setVisibility(View.INVISIBLE);
				finish();
			}

			@Override
			public void onAnimationCancel(Animator animation) {
			}
		});
		oa.start();
	}

	static void setEnsureCallback(ItfEnsureCallback callback) {
		mEnsureCallback = callback;
	}

	static void setCancelCallback(ItfCancelCallback callback) {
		mCancelCallback = callback;
	}

	public static interface ItfCancelCallback {
		void onCancelClick(Activity activity);
	}

	public static interface ItfEnsureCallback {
		void onEnsureClick(Activity activity);
	}
}
