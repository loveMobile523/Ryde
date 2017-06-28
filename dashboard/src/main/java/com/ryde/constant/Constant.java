package com.ryde.constant;

import com.google.android.gms.maps.model.LatLng;
import com.ryde.R;
import com.ryde.model.CompanyModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dsndw3 on 07.06.2017.
 */

public class Constant {

    // test location : KL sentral and second location klia
    public static final String MyShared = "MyShared";


    public static final String Default = "Default";

    /**
     *
     */
    public static final int cntCompany = 6;

    public static final String[] CheckedCompanyListShared = { "company1", "company2", "company3", "company4", "company5", "company6" };
    public static boolean[] checkedCompanyValues = new boolean[cntCompany];


    // check box
    public static final String isAgreeChecked = "isAgreeChecked";
    public static final String isPersonChecked = "isPersonChecked";


    // request permission tag
    public static final int Permission_ACCESS_FINE_LOCATION = 2;

    public static LatLng CurrentPosition = new LatLng(3.1462596, 101.673855);
    public static float ZoomLevel = 9.0f;

    public static String From = "From";
    public static String Self = "Self";
    public static String CurrentFragment = "CurrentFragment";

    public static String RydeFrag = "RydeFrag";
    public static String CompareFrag = "CompareFrag";
    public static String DetailFrag = "DetailFrag";

    /**
     *
     */
    public static LatLng Camera;
    public static LatLng Origin;
    public static LatLng Destination;

    public static double distance;


    public static ArrayList<LatLng> directionPositionList;

    public static List<CompanyModel> companyModelList;
    public static List<CompanyModel> checkedCompanyModelList;


    /**
     *
     */
    // $2 , $2.1, $2.15, $2.2, $2.30, $2.26, $2.31
    public static final String[] companyName = { "Blue cab", "Public taxi", "Gold Cab",
                    "Sunlight", "Uber", "Grab"};
    public static final int[] companyLogoIndex = { R.drawable.ic_1pblue_cab, R.drawable.ic_2ppublic_cab, R.drawable.ic_3pgold_cap,
                    R.drawable.ic_4psunlight, R.drawable.ic_5puber, R.drawable.ic_6pgrab};
    public static final float[] companyFare = { 2.0f, 1.25f, 1.5f, 1f, 0.60f, 1.10f };
    public static final float[] companyFarePlus = { 6f, 3f, 4f, 3f, 0.95f, 2f };

    public static final String[] companyPickupTime = { "7.30m", "3.50m", "2.15m", "9.45m", "8.30m", "2.50m" };

    /**
     *
     */
    public static String compareFare1;
    public static int selectedCompanyLogoIndex;

}
