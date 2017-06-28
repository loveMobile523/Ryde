package com.ryde.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.TransportMode;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.ryde.R;
import com.ryde.adapter.AddressAdapter;
import com.ryde.constant.Constant;
import com.ryde.custom.CustomFragment;
import com.ryde.handler.BackButtonHandlerInterface;
import com.ryde.handler.OnBackClickListener;
import com.ryde.model.AddressModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class RydeFragment extends CustomFragment implements DirectionCallback {
    public static final String RydeFTAG = "RydeFragment";

    private Button btnSearch;

    private EditText etOriginal;
    private EditText etDestination;

    // ---------------------------------------------------------------------------------------------
    FragmentManager manager;
    private CompareFragment compareFragment;

    MapView mMapView;
    public static GoogleMap googleMap;

    private String serverKey = "AIzaSyDN3CkzIOZNZgM1TNAcgNyooTbu2DNOOls";
    private LatLng camera = new LatLng(37.782437, -122.4281893);
    private LatLng origin = new LatLng(37.7849569, -122.4068855);
    private LatLng destination = new LatLng(37.7814432, -122.4460177);


    double bound = 1.5;
    int cntAddresses = 6;

    // ---------------------------------------------------------------------------------------------
//    int[] addressResource = { R.id.tvPAddress1, R.id.tvPAddress2, R.id.tvPAddress3,
//            R.id.tvPAddress4, R.id.tvPAddress5 };
//    TextView[] tvAddressList = new TextView[cntAddresses];
//    private LinearLayout liLayAddress;

    // ------------

    private ListView lvAddress;
    ArrayList<AddressModel> arrayList;
    List<Address> _addressList;
    boolean _isOrigin = true;
    // ---------------------------------------------------------------------------------------------

    boolean isCheckedOrigin, isCheckedDestination;

    KProgressHUD kpHUD;
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
        View v = inflater.inflate(R.layout.fragment_ryde, null);

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


        mMapView = (MapView) v.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        Constant.CurrentFragment = Constant.RydeFrag;

        initializeMap();
        initWidget(v);
        btnsOnClick();

        mMapView.onResume(); // needed to get the map to display immediately
        return v;
    }

    public void initWidget(View v) {
        isCheckedOrigin = false;
        isCheckedDestination = false;

        kpHUD = KProgressHUD.create(getActivity())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(true)
                .setAnimationSpeed(1)
                .setDimAmount(0.5f);

        // -----------------------------------------------------------------------------------------

        btnSearch = (Button) v.findViewById(R.id.btnSearch);

        etOriginal = (EditText) v.findViewById(R.id.etOriginal);
        etDestination = (EditText) v.findViewById(R.id.etDestination);


        compareFragment = new CompareFragment();
        manager = getActivity().getSupportFragmentManager();

        // -----------------------------------------------------------------------------------------
//        for (int i = 0; i < cntAddresses; i++) {
//            tvAddressList[i] = (TextView) v.findViewById(addressResource[i]);
//        }
//
//        liLayAddress = (LinearLayout) v.findViewById(R.id.liLayAddress);

        // ----------------
        lvAddress = (ListView) v.findViewById(R.id.lvAddress);
        arrayList = new ArrayList<>();
        _addressList = new ArrayList<>();

        lvAddress.setVisibility(View.GONE);
        lvAddress.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        Address address = _addressList.get(position);
                        LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                        Constant.From = Constant.Self;

                        if(_isOrigin){
                            etOriginal.setText(arrayList.get(position).getTitle());

                            googleMap.addMarker(new MarkerOptions().position(latLng).title("").icon(BitmapDescriptorFactory.fromResource(R.drawable.circle15)));

                            isCheckedOrigin = true;
                            origin = latLng;

                            etDestination.requestFocus();
                        } else {
                            etDestination.setText(arrayList.get(position).getTitle());

                            googleMap.addMarker(new MarkerOptions().position(latLng).title("").icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_pin_red50)));

                            isCheckedDestination = true;
                            destination = latLng;
                        }

                        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

                        lvAddress.setVisibility(View.GONE);

                        requestDirection();

            }
        });
    }

    public void btnsOnClick() {
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etOriginal.setText("");
                etDestination.setText("");
                FragmentTransaction ft0 = manager.beginTransaction().replace(R.id.content_frame, compareFragment, CompareFragment.CompareFTAG);
//                FragmentTransaction ft0 = manager.beginTransaction().add(R.id.content_frame, compareFragment, CompareFragment.CompareFTAG);
                ft0.commit();

            }
        });

        etOriginal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if(s.toString().length() == 0) {
                    lvAddress.setVisibility(View.GONE);
                    return;
                }

                if(Constant.From.equals(Constant.Self)){
                    Constant.From = Constant.RydeFrag;
                    return;
                }

                String location = s.toString();

                List<Address> addressList = null;

                if (location != null || !location.equals("")) {

                    if(Constant.CurrentPosition != null) {
                        Geocoder geocoder = new Geocoder(getActivity());

                        double latitude = Constant.CurrentPosition.latitude;
                        double longitude = Constant.CurrentPosition.longitude;

                        try {
                            addressList = geocoder.getFromLocationName(location, cntAddresses, latitude - bound, longitude - bound, latitude + bound, longitude + bound);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        if(addressList != null && addressList.size() != 0) {
                            List<String> addressListString = new ArrayList<>();
                            addressListString = getLocationAddressList(addressList);

//                            liLayAddress.setVisibility(View.VISIBLE);
//                            displayPopup(addressListString, addressList, true);

                            // -------------
                            lvAddress.setVisibility(View.VISIBLE);

                            _addressList = addressList;
                            _isOrigin = true;
                            displayPopup(addressListString, addressList, true);
                        } else {
                            lvAddress.setVisibility(View.GONE);
                        }
                    }

                }
            }
        });

        etDestination.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if(s.toString().length() == 0) {
                    lvAddress.setVisibility(View.GONE);
                    return;
                }

                if(Constant.From.equals(Constant.Self)){
                    Constant.From = Constant.RydeFrag;
                    return;
                }

                String location = s.toString();

                List<Address> addressList = null;

                if (location != null || !location.equals("") ) {
                    if(Constant.CurrentPosition != null) {
                        Geocoder geocoder = new Geocoder(getActivity());

                        double latitude = Constant.CurrentPosition.latitude;
                        double longitude = Constant.CurrentPosition.longitude;

                        try {
                            addressList = geocoder.getFromLocationName(location, cntAddresses, latitude - bound, longitude - bound, latitude + bound, longitude + bound);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        if(addressList != null && addressList.size() != 0) {
                            List<String> addressListString = new ArrayList<>();
                            addressListString = getLocationAddressList(addressList);

//                            liLayAddress.setVisibility(View.VISIBLE);
//                            displayPopup(addressListString, addressList, false);

                            // --------------
                            lvAddress.setVisibility(View.VISIBLE);

                            _addressList = addressList;
                            _isOrigin = false;

                            displayPopup(addressListString, addressList, false);
                        } else {
                            lvAddress.setVisibility(View.GONE);
                        }
                    }
                }

            }
        });
    }

    public void displayPopup(final List<String> addressListString, final List<Address> addressList, final boolean isOrigin){

        AddressModel addressModel = new AddressModel();
        arrayList.clear();

        int len = addressListString.size();

        for (int i = 0; i < len; i++) {

//            tvAddressList[i].setVisibility(View.VISIBLE);
//            tvAddressList[i].setText(addressListString.get(i));
//
//            final int finalI = i;
//            tvAddressList[i].setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Address address = addressList.get(finalI);
//                    LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
//                    Constant.From = Constant.Self;
//
//                    if(isOrigin){
//                        etOriginal.setText(addressListString.get(finalI));
//
//                        googleMap.addMarker(new MarkerOptions().position(latLng).title("").icon(BitmapDescriptorFactory.fromResource(R.drawable.circle15)));
//
//                        isCheckedOrigin = true;
//                        origin = latLng;
//                    } else {
//                        etDestination.setText(addressListString.get(finalI));
//
//                        googleMap.addMarker(new MarkerOptions().position(latLng).title("").icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_pin_red50)));
//
//                        isCheckedDestination = true;
//                        destination = latLng;
//                    }
//
//                    googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
//
//                    liLayAddress.setVisibility(View.GONE);
//
//                    requestDirection();
//                }
//            });


            // ----------
            addressModel = new AddressModel(addressListString.get(i));
            arrayList.add(addressModel);
        }

        lvAddress.setAdapter(new AddressAdapter(getContext(), arrayList));

//        for (int i = len; i < cntAddresses; i++) {
//
//            tvAddressList[i].setVisibility(View.GONE);
//        }

    }

    public List<String> getLocationAddressList(List<Address> addresses){

        List<String> returnList = new ArrayList<>();
        String addressString;

        String address; // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        String city;
        String state;
        String country;
        String postalCode;
        String knownName; // Only if available else return NULL
        String subLocality;

        String shareAddress = "";

        int len = addresses.size();
        for (int i = 0; i < len; i++) {
            Address geoAddress = addresses.get(i);

            addressString = geoAddress.getFeatureName();

            address = geoAddress.getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            city = geoAddress.getLocality();
            state = geoAddress.getAdminArea();
            country = geoAddress.getCountryName();
            postalCode = geoAddress.getPostalCode();
            knownName = geoAddress.getFeatureName(); // Only if available else return NULL
            subLocality = geoAddress.getSubLocality();

            shareAddress = "";
            if(address != null){                    shareAddress = shareAddress + address + " ";                }
            if(subLocality != null){                shareAddress = shareAddress + subLocality + " ";            }
            if(city != null){                       shareAddress = shareAddress + city + " ";                   }
            if(state != null){                      shareAddress = shareAddress + state + " ";                  }
            if(postalCode != null){                 shareAddress = shareAddress + postalCode + " ";             }
            else {                                  shareAddress = shareAddress + " ";                          }

            returnList.add(shareAddress);
        }
        return returnList;
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
                googleMap.setMyLocationEnabled(true);
                googleMap.setOnMyLocationChangeListener(myLocationChangeListener);

                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(Constant.CurrentPosition, Constant.ZoomLevel));
            }
        });
    }

    private GoogleMap.OnMyLocationChangeListener myLocationChangeListener = new GoogleMap.OnMyLocationChangeListener() {
        @Override
        public void onMyLocationChange(Location location) {

            LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());

            if (googleMap != null) {
//                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(Constant.CurrentPosition, Constant.ZoomLevel));
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

        etOriginal.setText("");
        etDestination.setText("");

         // -----------------------------------------------------------------------------------------
        if (googleMap != null) {
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(Constant.CurrentPosition, Constant.ZoomLevel));
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


    public void requestDirection() {
        if(isCheckedDestination && isCheckedOrigin) {
            kpHUD.show();

            GoogleDirection.withServerKey(serverKey)
                    .from(origin)
                    .to(destination)
                    .transportMode(TransportMode.DRIVING)
                    .execute(this);
        }
    }

    @Override
    public void onDirectionSuccess(Direction direction, String rawBody) {
        kpHUD.dismiss();

        if (direction.isOK()) {
            googleMap.addMarker(new MarkerOptions().position(origin).icon(BitmapDescriptorFactory.fromResource(R.drawable.circle15)));
            googleMap.addMarker(new MarkerOptions().position(destination).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_pin_red50)));

            ArrayList<LatLng> directionPositionList = direction.getRouteList().get(0).getLegList().get(0).getDirectionPoint();
            googleMap.addPolyline(DirectionConverter.createPolyline(getActivity(), directionPositionList, 5, Color.RED));

            Constant.Origin = origin;
            Constant.Destination = destination;

            double longitude = (origin.longitude + destination.longitude) / 2;
            double latitude = (origin.latitude + destination.latitude) / 2;

            Constant.Camera = new LatLng(latitude, longitude);
            Constant.directionPositionList = directionPositionList;

//            Constant.distance = getDistance(origin.latitude, origin.longitude, destination.latitude, destination.longitude);


            Location locationA = new Location("point A");
            Location locationB = new Location("point B");
            Constant.distance = 0;

            int len = directionPositionList.size();

            for (int i = 1; i < len; i++) {
                locationA.setLatitude(directionPositionList.get(i - 1).latitude);
                locationA.setLongitude(directionPositionList.get(i - 1).longitude);

                locationB.setLatitude(directionPositionList.get(i).latitude);
                locationB.setLongitude(directionPositionList.get(i).longitude);

                Constant.distance += locationA.distanceTo(locationB) / 1000;
//                Constant.distance += getDistance(directionPositionList.get(i - 1).latitude, directionPositionList.get(i - 1).longitude,
//                        directionPositionList.get(i).latitude, directionPositionList.get(i).longitude);

            }
        }
    }

    @Override
    public void onDirectionFailure(Throwable t) {
        kpHUD.dismiss();
//        Snackbar.make(btnSearch, t.getMessage(), Snackbar.LENGTH_SHORT).show();
    }

    private double getDistance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    // ---------------------------------------------------------------------------------------------
}
