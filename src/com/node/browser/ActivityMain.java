package com.node.browser;

import java.io.Serializable;
import java.util.ArrayList;

import com.node.browser.activity.dialogs.ActivityECCheckBoxDialog;
import com.node.browser.activity.dialogs.ActivityECDialog;
import com.node.browser.activity.dialogs.DialogUtil;
import com.node.browser.fragment.FragFirstPage;
import com.node.browser.fragment.FragSecondPage;
import com.node.browser.fragment.NFragment;
import com.node.browser.webviewmanager.NWebview;
import com.node.browser.webviewmanager.WebViewManager;
import com.node.log.NLog;
import com.node.util.GlobalUtil;
import com.node.util.PrefUtil;
import com.node.util.UIUtil;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.IntEvaluator;
import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Transformation;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ActivityMain extends FragmentActivity {

	private static final String TAG_DEBUG="NodeBrowser-ActivityMain";
	
	LinearLayout mLLHistoryArea;
	ListView mLVHistory;
	ViewPager mViewPager;
	FrameLayout mContentContainer;
	FrameLayout mHeaderArea; // 顶部输入区域

	// FragmentHolder mFragHolder;
	ArrayList<NFragment> mFrags;
	FragAdapter mAdapter;
	/*
	 * static final String STATE_KEY_FRAGS = "key_frags"; static final String
	 * STATE_KEY_FRAGADAPTER = "key_fragadapter";
	 */

	/*
	 * 顶部输入区域
	 */
	EditText mETUrlInput;// url地址输入框
	LinearLayout mUrlContainer;// url地址容器
	RelativeLayout mUnusedLocationArea;// 占位置的布局 unused_location_search_area
	RelativeLayout mUrlContainerFather;// url地址容器的父容器

	EditText mETSearchInput;// search地址输入框
	LinearLayout mSearchContainer;// search地址容器

	/**
	 * 下部操作区域的所有按钮
	 */
	Button
	/* 回退 */mBtnGoback,
	/* 前进 */mBtnGoForward,
	/* reload or stop load */mBtnStopOrRefresh;

	private final String TAG_STOP = "top";// 停止标签
	private final String TAG_REFRESH = "refresh";

	final long DEFAULT_ANIM_DURATION = 150l;
	final long DEFAULT_ANIM_DURATION_HISTORY_SHOW = 500l;// 显示历史记录时的动画时间
	final long DEFAULT_ANIM_DURATION_HISTORY_HIDEN = 150l;// 隐藏历史记录时的动画时间
	int mSearchAreaWidthSrc = 0;// 搜索框的原始大小

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		GlobalUtil.setFullScreen(this);
		PrefUtil.initPreferenceUtil(this);

		setContentView(R.layout.laout_main);
		initView();
		initAction();
		initData(savedInstanceState);
		// test
		DialogUtil.showSplashDialog(this);
		// test
		DialogUtil.showEnsureCancelDialog(this, "测试标题", "测试内容",
				new ActivityECDialog.ItfEnsureCallback() {
					@Override
					public void onEnsureClick(Activity activity) {
						((ActivityECDialog) activity).dismiss();
					}
				}, new ActivityECDialog.ItfCancelCallback() {
					@Override
					public void onCancelClick(Activity activity) {
						((ActivityECDialog) activity).dismiss();
					}
				});
		// test
		DialogUtil.showECCheckBoxDialog(this, "测试CheckBox", "测试内容",
				"测试的checkbox内容", "key_setting",
				new ActivityECCheckBoxDialog.ItfCancelCallback() {

					@Override
					public void onCancelClick(Activity activity,
							boolean isChecked) {
						((ActivityECCheckBoxDialog) activity).dismiss();
					}
				}, new ActivityECCheckBoxDialog.ItfEnsureCallback() {
					@Override
					public void onEnsureClick(Activity activity,
							boolean isChecked) {
						((ActivityECCheckBoxDialog) activity).dismiss();
					}
				});
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		/*
		 * outState.putSerializable(STATE_KEY_FRAGADAPTER, mAdapter);
		 * outState.putSerializable(STATE_KEY_FRAGS, mFrags);
		 */
		super.onSaveInstanceState(outState);
	}

	private void initData(Bundle state) {
		// 初始化基础Fragment
		initBaseFrags();
		// 初始化Adapter
		mAdapter = new FragAdapter(getSupportFragmentManager());
		// 设置数据源
		mViewPager.setAdapter(mAdapter);
		// 初始化webview管理器
		WebViewManager.instance().initFields(mAdapter, mFrags, mViewPager);
		// test
		WebViewManager.instance().loadingUrlInNewWindow("http://www.baidu.com",
				null, new NWebview.UrlStatusObserver() {
					@Override
					public void onInitNWebview(boolean[] status,
							NWebview webview) {
						updateBottomBtnOperation(status);
					}

					@Override
					public void onUrlStatusChanged(boolean[] status,
							NWebview webview) {
						updateBottomBtnOperation(status);
					}
				});
	}

	NFragment firstPage;// 首页
	NFragment secondPage;

	private void initBaseFrags() {
		mFrags = new ArrayList<NFragment>();
		firstPage = new FragFirstPage();
		secondPage = new FragSecondPage();
		mFrags.add(firstPage);
		mFrags.add(secondPage);
	}

	/**
	 * 初始化底部可操作区域
	 */
	private void initBottomBtnOperation() {
		mBtnGoback.setEnabled(false);
		mBtnGoForward.setEnabled(false);
		mBtnStopOrRefresh.setEnabled(false);
		mBtnStopOrRefresh.setText("刷新");
	}

	/**
	 * 刷新底部可操作区域的状态
	 * 
	 * @param status
	 */
	private void updateBottomBtnOperation(boolean[] status) {
		if (status == null) {
			mBtnGoback.setEnabled(false);
			mBtnGoForward.setEnabled(false);
			mBtnStopOrRefresh.setEnabled(true);
			mBtnStopOrRefresh.setText("停止");
			mBtnStopOrRefresh.setTag(TAG_STOP);
		} else {
			mBtnGoback.setEnabled(status[NWebview.INDEX_CAN_GO_BACK]);
			mBtnGoForward.setEnabled(status[NWebview.INDEX_CAN_GO_FORWARD]);
			mBtnStopOrRefresh.setEnabled(true);
			String tag = "";
			String btnText = "";
			boolean isWaiting = status[NWebview.INDEX_URL_WAITINGFOR_LOAD];
			boolean isLoading = status[NWebview.INDEX_URL_IS_LOADING];
			if (isWaiting || isLoading) {
				tag = TAG_STOP;
				btnText = "停止";
			} else {
				tag = TAG_REFRESH;
				btnText = "刷新";
			}
			mBtnStopOrRefresh.setTag(tag);
			mBtnStopOrRefresh.setText(btnText);
		}
	}

	private void initAction() {
		// EditText url输入框
		mETUrlInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					showUrlHistory();
				}
			}
		});
		// EditText search输入框
		mETSearchInput
				.setOnFocusChangeListener(new View.OnFocusChangeListener() {
					@Override
					public void onFocusChange(View v, boolean hasFocus) {
						if (hasFocus) {
							showSearchItems();
						}
					}
				});
		// 软件盘事件
		mETUrlInput.setOnEditorActionListener(mUrlSearchInputActionListener);
		mETSearchInput.setOnEditorActionListener(mUrlSearchInputActionListener);

		mViewPager
				.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
					@Override
					public void onPageSelected(int index) {
						NWebview webview = WebViewManager.instance()
								.getWebview(index);
						if(webview==null){
							NLog.e(TAG_DEBUG, "webview is null onPageSelected ,index is "+index);
							initBottomBtnOperation();
						}else{
							boolean[] urlStatus=webview.status;
							updateBottomBtnOperation(urlStatus);
						}
					}

					@Override
					public void onPageScrolled(int arg0, float arg1, int arg2) {

					}

					@Override
					public void onPageScrollStateChanged(int arg0) {

					}
				});
		initBottomBtnOperationActions();
	}

	/**
	 * 设置底部操作区域事件
	 */
	private void initBottomBtnOperationActions() {
		View.OnClickListener bottomListener = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				int view_id = v.getId();
				NWebview nWebview = WebViewManager.instance().currentWebview();
				switch (view_id) {
				/* 回退 */
				case R.id.bottom_op_goback:
					nWebview.goBack();
					break;
				/* 前进 */
				case R.id.bottom_op_goforward:
					nWebview.goForward();
					break;
				/* 刷新|停止 */
				case R.id.bottom_op_fresh_or_stop:
					String tag = (String) v.getTag();
					if (tag.equals(TAG_STOP)) {
						nWebview.stopLoading();
					} else if (tag.equals(TAG_REFRESH)) {
						nWebview.reload();
					} else {
						// do nothing
						// can not happen
					}

					break;
				}
			}
		};
		mBtnGoback.setOnClickListener(bottomListener);
		mBtnGoForward.setOnClickListener(bottomListener);
		mBtnStopOrRefresh.setOnClickListener(bottomListener);
	}

	TextView.OnEditorActionListener mUrlSearchInputActionListener = new TextView.OnEditorActionListener() {
		@Override
		public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
			if (v.getText() == null) {
				return false;
			}
			if (v.getText().equals("")) {
				return false;
			}
			if (actionId == NodeConstants.ID_INPUT_ACTION_SEARCHEDITEXT) {
				handleUrlInput(v.getText().toString());
			} else if (actionId == NodeConstants.ID_INPUT_ACTION_URLEDITEXT) {
				doSearchKeywords();
			}
			return true;
		}
	};

	protected void updateUrlArea(WebView webview) {

	}

	/*
	 * url和search相关的页面载入
	 */

	protected void handleUrlInput(String text) {
		if (GlobalUtil.ifEndWithDomain(text, false)) {
			// 如果url的后缀名符合.com .cn .org,则跳转到页面

		} else {

		}
	}

	protected void doSearchKeywords() {

	}

	/*
	 * 动画处理
	 */
	/**
	 * 当搜索输入框聚焦时开始动画
	 */
	private void animOnShowSearchItems() {
		final int width = mSearchContainer.getWidth();
		mSearchAreaWidthSrc = width;
		final int devWidth = (int) GlobalUtil.getDevPixWidth(this);
		Animation anim = new Animation() {
			@Override
			protected void applyTransformation(float interpolatedTime,
					Transformation t) {
				LayoutParams params = mSearchContainer.getLayoutParams();
				params.width = (int) (width + (devWidth - width)
						* interpolatedTime);
				mSearchContainer.requestLayout();
			}
		};
		anim.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				mSearchContainer.clearAnimation();
				mUnusedLocationArea.getLayoutParams().width = LayoutParams.FILL_PARENT;
				mSearchContainer.getLayoutParams().width = LayoutParams.FILL_PARENT;
			}
		});
		// 因为动画跨越的宽度较长因此+50
		anim.setDuration(DEFAULT_ANIM_DURATION + 50);
		anim.setInterpolator(new DecelerateInterpolator());
		anim.setFillAfter(true);

		final int widthUrl = mUrlContainer.getWidth();
		final float curX = mUrlContainer.getX();
		final float tarX = curX - widthUrl;
		ObjectAnimator oa = ObjectAnimator.ofFloat(mUrlContainer, "x", curX,
				tarX);
		oa.setDuration(DEFAULT_ANIM_DURATION - 50);

		mSearchContainer.startAnimation(anim);
		oa.start();
	}

	private void animOnHidenSearchItems() {
		final int width = mSearchAreaWidthSrc;
		final int devWidth = (int) GlobalUtil.getDevPixWidth(this);
		Animation a = new Animation() {
			@Override
			protected void applyTransformation(float interpolatedTime,
					Transformation t) {
				LayoutParams params = mSearchContainer.getLayoutParams();
				params.width = (int) (devWidth - (devWidth - width)
						* interpolatedTime);
				mSearchContainer.requestLayout();
			}
		};
		a.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				mSearchContainer.clearAnimation();
				mSearchContainer.getLayoutParams().width = LayoutParams.WRAP_CONTENT;
				mSearchContainer.clearFocus();
			}
		});
		a.setFillAfter(true);
		a.setDuration(DEFAULT_ANIM_DURATION - 50);

		int widthUrl = mUrlContainer.getWidth();
		float curX = -widthUrl;
		float tarX = 0f;
		ObjectAnimator oa = ObjectAnimator.ofFloat(mUrlContainer, "x", curX,
				tarX);
		oa.setDuration(DEFAULT_ANIM_DURATION + 100);
		oa.setStartDelay(100);
		oa.addListener(new Animator.AnimatorListener() {
			@Override
			public void onAnimationStart(Animator animation) {
				mUnusedLocationArea.getLayoutParams().width = LayoutParams.WRAP_CONTENT;
				mUnusedLocationArea.requestLayout();
			}

			@Override
			public void onAnimationRepeat(Animator animation) {
			}

			@Override
			public void onAnimationEnd(Animator animation) {
			}

			@Override
			public void onAnimationCancel(Animator animation) {
			}
		});

		mSearchContainer.startAnimation(a);
		oa.start();
	}

	/**
	 * 当Url输入框聚焦时开始动画
	 */
	private void animOnUrlFocus() {
		// 初始化Url地址输入栏动画
		final int width = mUrlContainer.getWidth();
		final int devWidth = (int) GlobalUtil.getDevPixWidth(this);
		Animation animation = new Animation() {
			@Override
			protected void applyTransformation(float interpolatedTime,
					Transformation t) {
				LayoutParams params = mUrlContainer.getLayoutParams();
				params.width = (int) (width + (devWidth - width)
						* interpolatedTime);
				mUrlContainer.requestLayout();
			}
		};
		animation.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
				mUnusedLocationArea.setVisibility(View.GONE);
			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				mUrlContainer.clearAnimation();
				LayoutParams params = mUrlContainer.getLayoutParams();
				params.width = LayoutParams.FILL_PARENT;
			}
		});
		animation.setDuration(DEFAULT_ANIM_DURATION - 100);
		animation.setInterpolator(new DecelerateInterpolator());
		animation.setFillAfter(true);

		// 初始化搜索地址输入栏动画
		final float x = mSearchContainer.getX();
		ObjectAnimator oa2 = ObjectAnimator.ofFloat(mSearchContainer, "x",
				devWidth);
		oa2.setDuration(DEFAULT_ANIM_DURATION);
		mUrlContainer.clearAnimation();
		mUrlContainer.startAnimation(animation);
		oa2.start();
	}

	/**
	 * 当Url输入框失去焦点时开始动画
	 */
	private void animOnUrlLostFocus() {
		final int width = mUrlContainer.getMeasuredWidth();
		final int devWidth = (int) GlobalUtil.getDevPixWidth(this);

		Animation anim = new Animation() {
			@Override
			protected void applyTransformation(float interpolatedTime,
					Transformation t) {
				LayoutParams params = mUrlContainer.getLayoutParams();
				params.width = (int) (devWidth - (devWidth - width)
						* interpolatedTime);
				mUrlContainer.requestLayout();
			}
		};
		anim.setDuration(DEFAULT_ANIM_DURATION - 100);
		anim.setFillAfter(true);
		anim.setInterpolator(new DecelerateInterpolator());
		anim.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				mUrlContainer.clearAnimation();
				mUnusedLocationArea.setVisibility(View.VISIBLE);
			}
		});
		final float x = devWidth - mSearchContainer.getWidth();
		ObjectAnimator oa2 = ObjectAnimator.ofFloat(mSearchContainer, "x",
				devWidth, x);
		oa2.setStartDelay(50);
		oa2.setDuration(DEFAULT_ANIM_DURATION);
		mUrlContainer.startAnimation(anim);
		oa2.start();
	}

	/**
	 * 显示历史记录时加载开场动画
	 */
	private void animHistoryOnShow() {
		ObjectAnimator oa = ObjectAnimator.ofFloat(mLLHistoryArea, "alpha", 0f,
				0.5f);
		oa.addListener(new Animator.AnimatorListener() {
			@Override
			public void onAnimationStart(Animator animation) {
				mLLHistoryArea.setVisibility(View.VISIBLE);
			}

			@Override
			public void onAnimationRepeat(Animator animation) {
			}

			@Override
			public void onAnimationEnd(Animator animation) {
			}

			@Override
			public void onAnimationCancel(Animator animation) {
			}
		});
		oa.setDuration(DEFAULT_ANIM_DURATION_HISTORY_SHOW);
		oa.start();
	}

	/**
	 * 隐藏历史记录时加载退场动画
	 */
	private void animHistoryOnHiden() {
		ObjectAnimator oa = ObjectAnimator.ofFloat(mLLHistoryArea, "alpha",
				0.5f, 0f);
		oa.addListener(new Animator.AnimatorListener() {
			@Override
			public void onAnimationStart(Animator animation) {
			}

			@Override
			public void onAnimationRepeat(Animator animation) {
			}

			@Override
			public void onAnimationEnd(Animator animation) {
				mLLHistoryArea.setVisibility(View.GONE);
			}

			@Override
			public void onAnimationCancel(Animator animation) {

			}
		});
		oa.setDuration(DEFAULT_ANIM_DURATION_HISTORY_HIDEN);
		oa.start();
	}

	private void initView() {
		mETUrlInput = (EditText) findViewById(R.id.url_content);
		mUrlContainer = (LinearLayout) findViewById(R.id.header_url_input_container);
		mUrlContainerFather = (RelativeLayout) findViewById(R.id.header_url_container_father);
		mUnusedLocationArea = (RelativeLayout) findViewById(R.id.unused_location_search_area);
		mHeaderArea = (FrameLayout) findViewById(R.id.header_area);

		mETSearchInput = (EditText) findViewById(R.id.search_content);
		mSearchContainer = (LinearLayout) findViewById(R.id.header_search_input_area);

		mLVHistory = (ListView) findViewById(R.id.listview_history);
		mLLHistoryArea = (LinearLayout) findViewById(R.id.url_history_area);
		mViewPager = (ViewPager) findViewById(R.id.viewpager_content_view_container);
		mContentContainer = (FrameLayout) findViewById(R.id.content_container);

		/* 下部可操作按钮 */
		mBtnGoback = (Button) findViewById(R.id.bottom_op_goback);
		mBtnGoForward = (Button) findViewById(R.id.bottom_op_goforward);
		mBtnStopOrRefresh = (Button) findViewById(R.id.bottom_op_fresh_or_stop);
		initBottomBtnOperation();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	// 显示搜索引擎项
	void showSearchItems() {
		// 顶部输入框动画，搜索框向左填满屏幕
		animOnShowSearchItems();
		addStatus(STATUS_SHOWING_SEARCHENGINE);
	}

	// 隐藏搜索引擎项
	void hidenSearchItems() {
		// 顶部输入框动画，搜索框向右回到原始位置
		animOnHidenSearchItems();
		deleteStatus(STATUS_SHOWING_SEARCHENGINE);
	}

	// 显示url历史
	void showUrlHistory() {
		// 开始历史纪录的开场动画
		animHistoryOnShow();
		// 开始Url输入框向右充满屏幕动画
		animOnUrlFocus();
		addStatus(STATUS_SHOWING_URLHISTORY);
	}

	// 隐藏url历史
	void hidenUrlHistory() {
		// 开始历史纪录的退场动画
		animHistoryOnHiden();
		// 开始Url输入框向左返回原型动画
		animOnUrlLostFocus();
		deleteStatus(STATUS_SHOWING_URLHISTORY);
		mUrlContainer.clearFocus();
	}

	void clearOtherShowView() {

	}

	void returnToFirstPage() {

	}

	/*
	 * Fragment相关
	 */

	public class FragAdapter extends FragmentStatePagerAdapter implements
			Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = -7398569788945204186L;

		public FragAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int arg0) {
			return mFrags != null ? mFrags.get(arg0) : null;
		}

		@Override
		public int getCount() {
			return mFrags != null ? mFrags.size() : 0;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			// do nothing
		}

		@Override
		public Object instantiateItem(View container, int position) {
			return super.instantiateItem(container, position);
		}

		@Override
		public int getItemPosition(Object object) {
			// for (Fragment frag : mFrags) {
			// if (object.equals(frag)) {
			// return PagerAdapter.POSITION_UNCHANGED;
			// }
			// }
			// return PagerAdapter.POSITION_NONE;
			return super.getItemPosition(object);
		}
	}

	/**
	 * 刷新
	 */

	/*
	 * 状态管理
	 */
	int status = 0;
	int STATUS_SHOWING_URLHISTORY = 1; // 正在展示url历史
	int STATUS_SHOWING_SEARCHENGINE = 1 << 1; // 正在展示搜索
	int STATUS_SHOWING_FIRSTPAGE = 1 << 2; // 显示首页
	int STATUS_SHOWING_SUBWEBURL = 1 << 3; // 显示子页

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
			return dispatchKeyEvent1(event);
		} else {
			return super.dispatchKeyEvent(event);
		}
	}

	private boolean dispatchKeyEvent1(KeyEvent event) {
		if (containStatus(STATUS_SHOWING_URLHISTORY)) {
			// 隐藏输入历史，返回原状态
			hidenUrlHistory();
		}
		if (containStatus(STATUS_SHOWING_SEARCHENGINE)) {
			// 隐藏搜索引擎项
			hidenSearchItems();
		}
		return true;
	}

	void addStatus(int subStatus) {
		status |= subStatus;
	}

	void deleteStatus(int subStatus) {
		status ^= subStatus;
	}

	boolean containStatus(int subStatus) {
		return ((status & subStatus) == subStatus);
	}

}
