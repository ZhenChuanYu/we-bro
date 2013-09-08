package com.node.browser.activity.dialogs;

import com.node.browser.R;
import com.node.util.PrefUtil;
import com.node.util.PrefUtil.PrefFactory;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

/**
 * 1.包含一个checkbox选择框 2.包含显示标题、内容和具有取消、确认操作的基本对话框<br>
 * 3.在DialogUtil中使用
 * {@code DialogUtil#showECCheckBoxDialog(Activity, String, String, String, String, ItfCancelCallback, ItfEnsureCallback)
 * <br>
 * 4.ECCheckBoxDialog 为EnsureCancelCheckBoxDialog的简称<br>
 * 
 * @author zhenchuan
 * 
 */
public class ActivityECCheckBoxDialog extends Activity {

	static String EXTRA_KEY_TITLE = "key_title";
	static String EXTRA_KEY_CONTENT = "key_content";
	static String EXTRA_KEY_CHECKBOXTEXT = "key_checkbox_text";
	static String EXTRA_KEY_PREFERENCE_KEY = "key_preference_key";
	TextView mTitle;// 标题控件
	TextView mContent;// 内容控件
	CheckBox mCheckbox;// 复选框
	String mStrTitle;// 标题
	String mStrContent;// 内容
	String mCheckboxText;// 复选框内容
	String mPreferenceKey;// 复选框对应的preference键
	Button mBtnEnsure;// 确认按钮
	Button mBtnCancel;// 取消按钮
	View mContainer;// 顶层容器
	static ItfCancelCallback mCancelCallback;// 取消回调接口
	static ItfEnsureCallback mEnsureCallback;// 确认回调接口

	PrefUtil mPrefUtil;// 偏好设置工具

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mPrefUtil = PrefFactory.createDefault();
		setContentView(R.layout.dialog_ensure_cancel_checkbox_tip);
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
		mCheckboxText = intent.getStringExtra(EXTRA_KEY_CHECKBOXTEXT);
		mPreferenceKey = intent.getStringExtra(EXTRA_KEY_PREFERENCE_KEY);
	}

	private void initAction() {
		mBtnEnsure.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// 持久化checkbox值
				boolean value = mCheckbox.isChecked();
				setPreferenceValue(value);
				if (mEnsureCallback != null) {
					mEnsureCallback.onEnsureClick(
							ActivityECCheckBoxDialog.this, value);
				}
			}
		});
		mBtnCancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// 持久化checkbox值
				boolean value = mCheckbox.isChecked();
				setPreferenceValue(value);
				if (mCancelCallback != null) {
					mCancelCallback.onCancelClick(
							ActivityECCheckBoxDialog.this, value);
				}
			}
		});
	}

	private void initView() {
		mTitle = (TextView) findViewById(R.id.dialog_title);
		mContent = (TextView) findViewById(R.id.dialog_content);
		mBtnEnsure = (Button) findViewById(R.id.dialog_btn_ensure);
		mBtnCancel = (Button) findViewById(R.id.dialog_btn_cancel);
		mCheckbox = (CheckBox) findViewById(R.id.dialog_checkbox);
		mContainer = findViewById(R.id.dialog_ensure_cancel_container);
		mTitle.setText(mStrTitle);
		mContent.setText(mStrContent);
		mCheckbox.setText(mCheckboxText);
		mCheckbox.setChecked(getPreferenceValue());
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

	@SuppressWarnings({ "static-access" })
	private boolean getPreferenceValue() {
		// 默认checkbox均为true
		return mPrefUtil.getBoolean(mPreferenceKey, true);
	}

	@SuppressWarnings("static-access")
	private void setPreferenceValue(boolean value) {
		mPrefUtil.putBoolean(mPreferenceKey, value);
	}

	static void setEnsureCallback(ItfEnsureCallback callback) {
		mEnsureCallback = callback;
	}

	static void setCancelCallback(ItfCancelCallback callback) {
		mCancelCallback = callback;
	}

	public static interface ItfCancelCallback {
		void onCancelClick(Activity activity, boolean isChecked);
	}

	public static interface ItfEnsureCallback {
		void onEnsureClick(Activity activity, boolean isChecked);
	}
}
