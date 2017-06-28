package com.ryde.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.ryde.R;
import com.ryde.custom.CustomFragment;


public class RateUsFragment extends CustomFragment implements BaseSliderView.OnSliderClickListener {

    /** The vertical tab container. */
    private View vTabv;

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
        View v = inflater.inflate(R.layout.fragment_rate_us, null);

        open = AnimationUtils.loadAnimation(getActivity(),
                android.R.anim.fade_in);
        close = AnimationUtils.loadAnimation(getActivity(),
                android.R.anim.fade_out);
        close.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation)            {            }

            @Override
            public void onAnimationRepeat(Animation animation)            {            }

            @Override
            public void onAnimationEnd(Animation animation)
            {
                vTabv.setVisibility(View.GONE);
            }
        });


        return v;
    }


    /* (non-Javadoc)
     * @see com.whatshere.custom.CustomFragment#onClick(android.view.View)
     */
    @Override
    public void onClick(View v)
    {
        super.onClick(v);
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

    }
}
