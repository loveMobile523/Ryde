package com.ryde.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.akexorcist.googledirection.util.DirectionConverter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ryde.R;
import com.ryde.constant.Constant;
import com.ryde.custom.CustomFragment;
import com.ryde.handler.OnBackClickListener;

/**
 * Created by dsndw3 on 07.06.2017.
 */

public class DetailInfoFragment extends CustomFragment {
    public static final String DetailInfoFTAG = "DetailInfoFragment";

    private TextView tvCompareFare;
    private ImageView imgCompanyLogo;

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
        View v = inflater.inflate(R.layout.fragment_detail_info, null);

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


        Constant.CurrentFragment = Constant.DetailFrag;

        mMapView = (MapView) v.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        initializeMap();
        initWidget(v);

        mMapView.onResume(); // needed to get the map to display immediately
        return v;
    }

    public void initWidget(View v){
        tvCompareFare = (TextView) v.findViewById(R.id.tvCompareFare);
        imgCompanyLogo = (ImageView) v.findViewById(R.id.imgCompanyLogo);

        tvCompareFare.setText(Constant.compareFare1);
        imgCompanyLogo.setImageResource(Constant.companyLogoIndex[Constant.selectedCompanyLogoIndex]);
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
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
