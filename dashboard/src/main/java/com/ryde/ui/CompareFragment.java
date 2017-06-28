package com.ryde.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.akexorcist.googledirection.util.DirectionConverter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ryde.R;
import com.ryde.constant.Constant;
import com.ryde.custom.CustomFragment;

import java.util.ArrayList;

/**
 * Created by dsndw3 on 07.06.2017.
 */

public class CompareFragment extends CustomFragment {
    public static final String CompareFTAG = "CompareFragment";


    private TextView[] tvCompanyFares = new TextView[Constant.cntCompany];
    private int[] tvCompanyFareIndex = { R.id.tvFare1, R.id.tvFare2, R.id.tvFare3,
            R.id.tvFare4, R.id.tvFare5, R.id.tvFare6 };

    private TextView[] tvCompanyPickUps = new TextView[Constant.cntCompany];
    private int[] tvCompanyPickUpIndex = { R.id.tvPickup1, R.id.tvPickup2, R.id.tvPickup3,
            R.id.tvPickup4, R.id.tvPickup5, R.id.tvPickup6 };

    private ImageView[] imgCompanyLogos = new ImageView[Constant.cntCompany];
    private int[] imgCompanyLogosIndex = { R.id.imgLogo1, R.id.imgLogo2, R.id.imgLogo3,
            R.id.imgLogo4, R.id.imgLogo5, R.id.imgLogo6 };

    private LinearLayout[] liLayCompanies = new LinearLayout[Constant.cntCompany];
    private int[] liLayCompaniesIndex = { R.id.liLayCompany1, R.id.liLayCompany2, R.id.liLayCompany3,
            R.id.liLayCompany4, R.id.liLayCompany5, R.id.liLayCompany6 };

    private EditText etCompareFare1;

    // ---------------------------------------------------------------------------------------------
    FragmentManager manager;
    private DetailInfoFragment detailInfoFragment;
    private RydeFragment rydeFragment;

    private Button btnTap1;

    MapView mMapView;
    public static GoogleMap googleMap;

    private float initZoomLevel = 12;

    // ---------------------------------------------------------------------------------------------

    public static int width, height;

    /** The vertical tab container. */
    private View vTabv;

    /** The open and close animations. */
    private Animation open, close;

    /* (non-Javadoc)
     * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
     */
    @SuppressLint("InflateParams")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_compare, null);

        open = AnimationUtils.loadAnimation(getActivity(),
                android.R.anim.fade_in);
        close = AnimationUtils.loadAnimation(getActivity(),
                android.R.anim.fade_out);
        close.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation)
            {
            }

            @Override
            public void onAnimationRepeat(Animation animation)
            {
            }

            @Override
            public void onAnimationEnd(Animation animation)
            {
                vTabv.setVisibility(View.GONE);
            }
        });


        Constant.CurrentFragment = Constant.CompareFrag;

        mMapView = (MapView) v.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        initializeMap();
        initWidget(v);
        btnsOnClick();

        mMapView.onResume(); // needed to get the map to display immediately
        return v;
    }

    public void initWidget(View v) {
        btnTap1 = (Button) v.findViewById(R.id.btnTap1);
        etCompareFare1 = (EditText) v.findViewById(R.id.etCompareFare1);

        Constant.checkedCompanyModelList = new ArrayList<>();

        for (int i = 0; i < Constant.cntCompany; i++ ) {
            tvCompanyFares[i] = (TextView) v.findViewById(tvCompanyFareIndex[i]);
            tvCompanyPickUps[i] = (TextView) v.findViewById(tvCompanyPickUpIndex[i]);
            imgCompanyLogos[i] = (ImageView) v.findViewById(imgCompanyLogosIndex[i]);
            liLayCompanies[i] = (LinearLayout) v.findViewById(liLayCompaniesIndex[i]);
        }

        initialize();

//        etCompareFare1.setText("0");
    }

    public void initialize() {
        Constant.checkedCompanyModelList.clear();

        for (int i = 0; i < Constant.cntCompany; i++ ) {
            tvCompanyFares[i].setText("");
            tvCompanyPickUps[i].setText("");
            imgCompanyLogos[i].setImageResource(R.drawable.blank);

            if(Constant.companyModelList.get(i).isChecked()) {
                Constant.checkedCompanyModelList.add(Constant.companyModelList.get(i));
            }
        }

        int len = Constant.checkedCompanyModelList.size();

        for (int i = 0; i < len; i++){
            imgCompanyLogos[i].setImageResource(Constant.companyLogoIndex[Constant.checkedCompanyModelList.get(i).getLogoIndex()]);

            String distance = String.valueOf(Constant.distance * Constant.checkedCompanyModelList.get(i).getFare() + Constant.checkedCompanyModelList.get(i).getFarePlus());
            if (distance.length() > 5) {
                distance = distance.substring(0, 5);
            }

            distance = "RM" + distance;
            tvCompanyFares[i].setText(distance);

            // ------
            tvCompanyPickUps[i].setText(Constant.checkedCompanyModelList.get(i).getTime());

            final int finalI = i;
            liLayCompanies[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentTransaction ft0 = manager.beginTransaction().replace(R.id.content_frame, detailInfoFragment, DetailInfoFragment.DetailInfoFTAG);
                    ft0.commit();

                    Constant.compareFare1 = etCompareFare1.getText().toString();
                    Constant.selectedCompanyLogoIndex = Constant.checkedCompanyModelList.get(finalI).getLogoIndex();
                }
            });
        }

        detailInfoFragment = new DetailInfoFragment();
        rydeFragment = new RydeFragment();
        manager = getActivity().getSupportFragmentManager();
    }

    public void btnsOnClick() {
        btnTap1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    public void initializeMap() {
        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;

                // For showing a move to my location button
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: permission check
                    ActivityCompat.requestPermissions( getActivity(),
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            Constant.Permission_ACCESS_FINE_LOCATION);
                    return;
                }
//                googleMap.setMyLocationEnabled(true);
                googleMap.setOnMyLocationChangeListener(myLocationChangeListener);

                if(Constant.Camera != null && Constant.Origin != null) {
                    googleMap.addMarker(new MarkerOptions().position(Constant.Origin).icon(BitmapDescriptorFactory.fromResource(R.drawable.circle15)));
                    googleMap.addMarker(new MarkerOptions().position(Constant.Destination).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_pin_red50)));
                    googleMap.addPolyline(DirectionConverter.createPolyline(getActivity(), Constant.directionPositionList, 5, Color.RED));
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(Constant.Camera, Constant.ZoomLevel));
                }

            }
        });
    }

    private GoogleMap.OnMyLocationChangeListener myLocationChangeListener = new GoogleMap.OnMyLocationChangeListener() {
        @Override
        public void onMyLocationChange(Location location) {

            LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());

            if (googleMap != null) {

                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: permission check
                    ActivityCompat.requestPermissions( getActivity(),
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            Constant.Permission_ACCESS_FINE_LOCATION);
                    return;
                }
                googleMap.setMyLocationEnabled(false);
            }


        }
    };

    @Override
    public void onResume() {
        super.onResume();

        initialize();

        mMapView.onResume();

        // -----------------------------------------------------------------------------------------
        if (googleMap != null) {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: permission check
                ActivityCompat.requestPermissions( getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        Constant.Permission_ACCESS_FINE_LOCATION);
                return;
            }
//            googleMap.setMyLocationEnabled(true);
        }
    }

    @Override
    public void onPause() {
//        placesDataSource.close();
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    public void onStart() {
        super.onStart();
        mMapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mMapView.onStop();
    }

    /* (non-Javadoc)
     * @see com.whatshere.custom.CustomFragment#onClick(android.view.View)
     */
    @Override
    public void onClick(View v)
    {
        super.onClick(v);
    }

    // ---------------------------------------------------------------------------------------------

}
