package com.ryde.ui;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.ryde.R;
import com.ryde.custom.CustomFragment;

// refer a friend
@SuppressLint("InflateParams")
public class TellFriendFragment extends CustomFragment{
	private SharedPreferences.Editor editor;


	/** The open and close animations. */
	private Animation open, close;
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View v = inflater.inflate(R.layout.fragment_tell_a_friend, null);

		open = AnimationUtils.loadAnimation(getActivity(),
				android.R.anim.fade_in);
		close = AnimationUtils.loadAnimation(getActivity(),
				android.R.anim.fade_out);
		close.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {			}

			@Override
			public void onAnimationRepeat(Animation animation) {			}

			@Override
			public void onAnimationEnd(Animation animation) {			}
		});

		initWidget(v);

		onClickBtnNext();
		onClickOptionItems();


		editTextChangeMethods();
		return v;
	}

	private void initWidget(View v){

	}

	private void onClickBtnNext(){

	}

	private void initStatue(){

	}

	private void onClickOptionItems(){

	}

	private void showAlert(String content){
		final Dialog dialog =  new Dialog(getContext());
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.popup_alert);

		TextView tvNumberSolar = (TextView)dialog.findViewById(R.id.tvNumberSolar);
		tvNumberSolar.setText(content);

		dialog.setCancelable(false);

		dialog.show();

		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				dialog.dismiss();
			}
		},1500);
	}

	private boolean checkPreviewPageInformation(){
		boolean judge = false;


		return judge;
	}

	private boolean checkPreviewPageStatusSelected(){
		boolean judge = false;

		return judge;
	}

	private void editTextChangeMethods(){

	}

	/* (non-Javadoc)
	 * @see com.whatshere.custom.CustomFragment#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v)
	{
		super.onClick(v);
	}




}
