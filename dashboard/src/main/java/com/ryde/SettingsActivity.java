package com.ryde;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.widget.CheckBox;
import android.widget.ImageButton;

import com.ryde.constant.Constant;


public class SettingsActivity extends Activity {
    private SharedPreferences.Editor editor;
    private SharedPreferences prefs;

    private CheckBox[] checkBoxes = new CheckBox[Constant.cntCompany];
    private int[] checkBoxIndex = { R.id.cbCompany1, R.id.cbCompany2, R.id.cbCompany3,
            R.id.cbCompany4, R.id.cbCompany5, R.id.cbCompany6 };

    private ImageButton btnBack;

    /** The vertical tab container. */
    private View vTabv;

    /** The open and close animations. */
    private Animation open, close;

    /* (non-Javadoc)
     * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
     */


    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_settings);

        getActionBar().hide();

        editor = getSharedPreferences(Constant.MyShared, MODE_PRIVATE).edit();
        prefs = getSharedPreferences(Constant.MyShared, MODE_PRIVATE);

        initWidget();
    }

    public void initWidget () {
        for (int i = 0; i < Constant.cntCompany; i++) {
            checkBoxes[i] = (CheckBox) findViewById(checkBoxIndex[i]);
            checkBoxes[i].setChecked(Constant.companyModelList.get(i).isChecked());

            final int finalI = i;
            checkBoxes[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(((CheckBox)v).isChecked()) {
                        Constant.companyModelList.get(finalI).setChecked(true);
                        editor.putBoolean(Constant.CheckedCompanyListShared[finalI], true);
                    } else {
                        Constant.companyModelList.get(finalI).setChecked(false);
                        editor.putBoolean(Constant.CheckedCompanyListShared[finalI], false);
                    }
                    editor.commit();
                }
            });
        }

        btnBack = (ImageButton) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /* (non-Javadoc)
     * @see com.whatshere.custom.CustomFragment#onClick(android.view.View)
     */
//    @Override
//    public void onClick(View v)
//    {
//        super.onClick(v);
//    }

}
