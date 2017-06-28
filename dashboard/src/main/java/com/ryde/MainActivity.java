package com.ryde;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ryde.adapter.LeftNavAdapter;
import com.ryde.constant.Constant;
import com.ryde.custom.CustomActivity;
import com.ryde.custom.CustomFragment;
import com.ryde.handler.BackButtonHandlerInterface;
import com.ryde.handler.OnBackClickListener;
import com.ryde.model.Data;
import com.ryde.ui.CompareFragment;
import com.ryde.ui.DetailInfoFragment;
import com.ryde.ui.RateUsFragment;
import com.ryde.ui.FeedbackFragment;
import com.ryde.ui.RydeFragment;
import com.ryde.ui.TellFriendFragment;
import com.ryde.ui.PaymentFragment;
import com.ryde.ui.AboutUsFragment;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * The Activity MainActivity will launched after the  and it is the
 * Home/Base activity of the app which holds all the Fragments and also show the
 * Sliding Navigation drawer. You can write your code for displaying actual
 * items on Drawer layout.
 */
public class MainActivity extends CustomActivity  {
	FragmentManager manager;
	private DetailInfoFragment detailInfoFragment;
	private CompareFragment compareFragment;
	private RydeFragment rydeFragment;

	private String[] leftMenuTitle = {"Setting",
			"Payment",
			"Feedback",
			"Tell a friend",
			"Rate us",
			"About us"
	};

	private String[] topMenuTitle = {"Setting",
			"Payment",
			"Feedback",
			"Tell a friend",
			"Rate us",
			"About us"
	};

	private ImageView imageView1;

	private LeftNavAdapter adp;

	/** The drawer layout. */
	private DrawerLayout drawerLayout;

	/** ListView for left side drawer. */
	private ListView drawerLeft;

	/** The drawer toggle. */
	private ActionBarDrawerToggle drawerToggle;

	/** The lbl title. */
	private TextView lblTitle;

	/* (non-Javadoc)
	 * @see com.newsfeeder.custom.CustomActivity#onCreate(android.os.Bundle)
	 */
	@SuppressLint("InflateParams")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		View c = getLayoutInflater().inflate(R.layout.notify, null);
		lblTitle = (TextView) c.findViewById(R.id.title);

		getActionBar().setCustomView(c);
		getActionBar().setDisplayShowCustomEnabled(true);

		setupDrawer();
		setupContainer(9);

		imageView1 = (ImageView) findViewById(R.id.imageView1);
		imageView1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				drawerLayout.closeDrawers();
				setupContainer(9);
			}
		});

		manager = getSupportFragmentManager();
		detailInfoFragment = new DetailInfoFragment();
		compareFragment = new CompareFragment();
		rydeFragment = new RydeFragment();
	}

	/* (non-Javadoc)
	 * @see com.ryde.custom.CustomActivity#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		super.onClick(v);
		/*if (v.getId() == R.id.feed)
			startActivity(new Intent(this, SalesFeed.class));
		else */
		if (v == lblTitle) {
			if (drawerLayout.isDrawerOpen(drawerLeft))
				drawerLayout.closeDrawers();
			else {
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
				drawerLayout.openDrawer(drawerLeft);
			}
		}
	}

	/**
	 * Setup the drawer layout. This method also includes the method calls for
	 * setting up the Left & Right side drawers.
	 */
	private void setupDrawer() {
		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		drawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);
		drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
				R.drawable.ic_drawer, R.string.drawer_open,
				R.string.drawer_close) {
			@Override
			public void onDrawerClosed(View view) {
			}

			@Override
			public void onDrawerOpened(View drawerView) {
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(lblTitle.getWindowToken(), 0);
			}
		};
		drawerLayout.setDrawerListener(drawerToggle);
		drawerLayout.closeDrawers();

		setupLeftNavDrawer();
	}

	/**
	 * Setup the left navigation drawer/slider. You can add your logic to load
	 * the contents to be displayed on the left side drawer. It will also setup
	 * the Header and Footer contents of left drawer. This method also apply the
	 * Theme for components of Left drawer.
	 */
	@SuppressLint("InflateParams")
	private void setupLeftNavDrawer() {
		drawerLeft = (ListView) findViewById(R.id.left_drawer);

		View header = getLayoutInflater().inflate(R.layout.left_nav_header, null);

		drawerLeft.addHeaderView(header);

		final ArrayList<Data> al = new ArrayList<Data>();
		al.add(new Data(new String[]{leftMenuTitle[0]}, new int[]{R.string.awesome_font_profile, 0}));
		al.add(new Data(new String[]{leftMenuTitle[1]}, new int[]{0, 0}));
		al.add(new Data(new String[]{leftMenuTitle[2]}, new int[]{0, 0}));
		al.add(new Data(new String[]{leftMenuTitle[3]}, new int[]{0, 0}));
		al.add(new Data(new String[]{leftMenuTitle[4]}, new int[]{0, 0}));
		al.add(new Data(new String[]{leftMenuTitle[5]}, new int[]{0, 0}));

//		al.add(new Data(new String[] { "Help" }, new int[] {
//				R.drawable.ic_nav6, R.drawable.ic_nav6_sel }));

		adp = new LeftNavAdapter(this, al);
		drawerLeft.setAdapter(adp);
		drawerLeft.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> l, View arg1, int pos, long arg3) {
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(arg1.getWindowToken(), 0);
				drawerLayout.closeDrawers();
				setupContainer(pos);
			}
		});

	}

	/**
	 * Setup the container fragment for drawer layout. This method will setup
	 * the grid view display of main contents. You can customize this method as
	 * per your need to display specific content.
	 *
	 * @param pos
	 *            the new up container
	 */
	private void setupContainer(int pos) {
		CustomFragment f = null;
		String title = "hot";  // getString(R.string.app_name);

//		Toast.makeText(MainActivity.this, String.valueOf(pos), Toast.LENGTH_SHORT).show();
		if (pos == 1) {
			startActivity(new Intent(MainActivity.this, SettingsActivity.class));
			return;
		}
		if (pos == 2) {
			f = new PaymentFragment();
			title = topMenuTitle[1];
		}
		if (pos == 3) {
			f = new FeedbackFragment();
			title = topMenuTitle[2];
		}
		if (pos == 4) {
			f = new TellFriendFragment();
			title = topMenuTitle[3];
		}
		if (pos == 5) {
			f = new RateUsFragment();
			title = topMenuTitle[4];
		}
		if (pos == 6) {
			f = new AboutUsFragment();
			title = topMenuTitle[5];
		}
		if (pos == 9) {
			f = new RydeFragment();
			title = "Ryde";
		}


		if (f == null)
			return;

		lblTitle.setText(title);
		adp.setSelection(pos - 1);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.content_frame, f).commit();
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onPostCreate(android.os.Bundle)
	 */
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		drawerToggle.syncState();
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onConfigurationChanged(android.content.res.Configuration)
	 */
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggle
		drawerToggle.onConfigurationChanged(newConfig);
	}

	/* (non-Javadoc)
	 * @see com.whatshere.custom.CustomActivity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(lblTitle.getWindowToken(), 0);

		if (drawerToggle.onOptionsItemSelected(item)) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (data != null) {
//			Toast.makeText(getApplicationContext(), "Okay", Toast.LENGTH_SHORT).show();
//			if(requestCode == 17){ // todo Selected number

			String number = data.getStringExtra("number");
			Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", number, null));
			startActivity(intent);
//			}
		}
	}

	private void startAutoCall(String callNumber) {
		String callNumberString = "tel:" + callNumber;
		Intent intentCall = new Intent(Intent.ACTION_CALL);
		intentCall.setData(Uri.parse(callNumberString));
		if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
			// TODO: Consider calling
			return;
		}
		startActivity(intentCall);
	}

	@Override
	public void onRequestPermissionsResult(int requestCode,
										   String permissions[], int[] grantResults) {

	}

	// ---------------------------------------------------------------------------------------------
	/**
	 *
	 */
	@Override
	public void onBackPressed() {


		if (Constant.CurrentFragment.equals(Constant.CompareFrag)) {
			Constant.From = Constant.CompareFrag;
			FragmentTransaction ft0 = manager.beginTransaction().replace(R.id.content_frame, rydeFragment, RydeFragment.RydeFTAG);
			ft0.commit();
			return;
		}

		if (Constant.CurrentFragment.equals(Constant.DetailFrag)) {
			FragmentTransaction ft0 = manager.beginTransaction().replace(R.id.content_frame, compareFragment, CompareFragment.CompareFTAG);
			ft0.commit();
			return;
		}

		super.onBackPressed();

	}
}
