package com.example.aakash.materialdesign;

import android.location.Location;
import android.location.LocationListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.wearable.MessageApi;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import activity.MessagesFragment;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,LocationListener,GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    private GoogleMap mMap;
    protected GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker currLocationMarker;
    static double  lat =0, lng=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera

        mMap.setMyLocationEnabled(true);

        buildGoogleApiClient();

/*
        MarkerOptions mp = new MarkerOptions();

        mp.position(new LatLng(location.getLatitude(), location.getLongitude()));

        mp.title("my position");

        mMap.addMarker(mp);

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(location.getLatitude(), location.getLongitude()), 16));*/



       /*Log.i("maps", lat+":"+lng);

        LatLng current = new LatLng(lat, lng);
        mMap.addMarker(new MarkerOptions().position(current).title("Marker in current position"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(current));*/
    }
    @Override
    public void onLocationChanged(Location location) {

        mMap.clear();

        MarkerOptions mp = new MarkerOptions();

        mp.position(new LatLng(location.getLatitude(), location.getLongitude()));

        mp.title("my position");

        mMap.addMarker(mp);

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(location.getLatitude(), location.getLongitude()), 16));
            lat=location.getLatitude();
            lng=location.getLongitude();
        sendLocation();
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    private void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {
       /* mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        Log.i("maps","maps:mLastLocation:"+mLastLocation);
        if (mLastLocation != null) {
            lat = mLastLocation.getLatitude();
            lng = mLastLocation.getLongitude();
        }*/
        Log.i("maps","on connected");
        Toast.makeText(this, "onConnected", Toast.LENGTH_SHORT).show();
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            //place marker at current position
            //mGoogleMap.clear();
            lat=mLastLocation.getLatitude();
            lng=mLastLocation.getLongitude();

            LatLng latLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title("Current Position");
           // markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()), 16));
            currLocationMarker = mMap.addMarker(markerOptions);
           // mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        }

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(5000); //5 seconds
        mLocationRequest.setFastestInterval(3000); //3 seconds
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        //mLocationRequest.setSmallestDisplacement(0.1F); //1/10 meter

        //LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        sendLocation();
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, new com.google.android.gms.location.LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

            }
        });
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    public static String getClients() {
        final String[] body = new String[1];
        new AsyncTask<Void, Void, String>() {

            @Override
            protected String doInBackground(Void... voids) {
                Log.i("maps", "doinbackr=ground");
                HttpClient client = new DefaultHttpClient();
                URI website = null;
                try {
                    website = new URI("http://192.168.48.70:8081/getAllClients" );
                    Log.i("maps", "location" + website);
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
                HttpGet request = new HttpGet();

                request.setURI(website);


                HttpResponse response = null;
                try {
                    response = client.execute(request);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                int code = response.getStatusLine().getStatusCode();
                Log.i("maps", "code:" + code);
                try {
                    HttpEntity entity=response.getEntity();

                    body[0] =EntityUtils.toString(entity);
                    Log.i("values of the body",body[0]);

                } catch (IOException e) {
                    e.printStackTrace();
                }

               return body[0];
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                MessagesFragment.changeText(s);
            }
        }.execute(null, null, null);

    return  body[0];
    }

    public static void sendLocation() {
        /*final double lat=latitude;
        final double lon = longitude;*/
        Log.i("maps", "in sendLocation");


                new AsyncTask<Void, Void, Void>() {

                    @Override
                    protected Void doInBackground(Void... voids) {
                        Log.i("maps", "doinbackr=ground");
                        List<NameValuePair> params = new LinkedList<NameValuePair>();
                        HttpClient client = new DefaultHttpClient();
                        URI website = null;
                        try {
                            if (lat != 0.0 && lng != 0.0) {
                                params.add(new BasicNameValuePair("userid", String.valueOf((Math.round(Math.random() * 10000)))));
                                params.add(new BasicNameValuePair("latitude", String.valueOf(lat)));
                                params.add(new BasicNameValuePair("longitude", String.valueOf(lng)));
                            }
                            String paramUrl = URLEncodedUtils.format(params, "utf-8");
                            website = new URI("http://192.168.48.70:8081/saveLocation?" + paramUrl);
                            Log.i("maps", "location" + website);
                        } catch (URISyntaxException e) {
                            e.printStackTrace();
                        }
                        HttpGet request = new HttpGet();

                        request.setURI(website);


                        HttpResponse response = null;
                        try {
                            response = client.execute(request);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        int code = response.getStatusLine().getStatusCode();
                        Log.i("maps", "code:" + code);

                        return null;
                    }
                }.execute(null, null, null);

            }




    }



