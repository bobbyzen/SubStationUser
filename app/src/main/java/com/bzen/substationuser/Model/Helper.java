package com.bzen.substationuser.Model;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class Helper {
    private static Helper INSTANCE;
    private static Context context;

    private Helper(Context context) {
        this.context = context;
    }

    public static Helper getINSTANCE(Context context) {
        if (INSTANCE == null) {
            if (INSTANCE == null) {
                INSTANCE = new Helper(context);
            }
        }
        return INSTANCE;
    }

    public String PositionToAddress(double longitude, double latitude){
        Geocoder geocoder;
        List<Address> addresses = null;
        geocoder = new Geocoder(this.context, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

        } catch (IOException e) {
            e.printStackTrace();
        }
        String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        String city = addresses.get(0).getLocality();
        String state = addresses.get(0).getAdminArea();
        String country = addresses.get(0).getCountryName();
        String postalCode = addresses.get(0).getPostalCode();
        String knownName = addresses.get(0).getFeatureName();
        return address;
    }
}
