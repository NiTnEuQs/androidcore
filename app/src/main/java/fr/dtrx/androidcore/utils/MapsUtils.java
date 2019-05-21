package fr.dtrx.androidcore.utils;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsUtils {

    /**
     * String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
     * String city = addresses.get(0).getLocality();
     * String state = addresses.get(0).getAdminArea();
     * String country = addresses.get(0).getCountryName();
     * String postalCode = addresses.get(0).getPostalCode();
     * String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL
     *
     * @param context Context
     * @param latLng  Lattitue and Longitude
     * @return Addresses list
     */
    @SuppressWarnings("unused")
    public static List<Address> getAddressesFromLatLng(Context context, LatLng latLng) {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(context, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

            return addresses;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * @param context Context
     * @param latLng  Lattitue and Longitude
     * @return Addresses list
     */
    @SuppressWarnings("unused")
    public static String getAddressFromLatLng(Context context, LatLng latLng) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());

        try {
            List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);

            if (addresses != null && addresses.size() > 0) {
                return addresses.get(0).getAddressLine(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * @param context Context
     * @param address Address
     * @return Lattitude and Longitude
     */
    @SuppressWarnings("unused")
    public static LatLng getLatLngFromAddress(Context context, String address) {
        Geocoder geocoder = new Geocoder(context);
        List<Address> addresses;
        try {
            addresses = geocoder.getFromLocationName(address, 1);
            if (addresses.size() > 0) {
                double latitude = addresses.get(0).getLatitude();
                double longitude = addresses.get(0).getLongitude();

                return new LatLng(latitude, longitude);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


}
