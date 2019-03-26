package pl.aogiri.eventrio;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.maps.android.heatmaps.Gradient;
import com.google.maps.android.heatmaps.HeatmapTileProvider;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pl.aogiri.eventrio.event.Event;
import pl.aogiri.eventrio.event.EventInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsActivity extends FragmentActivity implements
        GoogleMap.OnCameraMoveStartedListener,
        GoogleMap.OnCameraMoveListener,
        GoogleMap.OnCameraMoveCanceledListener,
        GoogleMap.OnCameraIdleListener,
        OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener{
    private static String TAG = "MapsActivity";
    private  List<Event> events = new ArrayList<>();

    private ImageView imageButton;
    private FrameLayout profileContainer;

    // The entry points to the Places API.
    private GeoDataClient mGeoDataClient;
    private PlaceDetectionClient mPlaceDetectionClient;

    // The entry point to the Fused Location Provider.
    private FusedLocationProviderClient mFusedLocationProviderClient;

    private HeatmapTileProvider mProvider;

    private int DEFAULT_ZOOM = 16;

    private Location mLastKnownLocation;

    private EventInterface service;

    private int thumbHeight = 125;
    private int thumbWidth = 125;
    private int thumRadius = 125;

    private double thumbScale = 1;


    private GoogleMap mMap;

    private boolean heatmap=false;

    private Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        profileContainer = findViewById(R.id.profileContainer);
        imageButton = findViewById(R.id.imageButton);
        fragment = new EventDetailsFragment();


        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new ProfileFragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.slide_from_left, R.anim.slide_to_left,R.anim.slide_from_left, R.anim.slide_to_left);
                transaction
                        .add(R.id.profileContainer, fragment)
                        .addToBackStack("fragment2")
                        .commit();
            }
        });

        service = ServiceGenerator.createService(EventInterface.class, getString(R.string.api_login),getString(R.string.api_password));

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                            .findFragmentById(R.id.map);
                    mapFragment.getMapAsync(MapsActivity.this);

        }



    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        mMap.setOnCameraIdleListener(this);
        mMap.setOnCameraMoveStartedListener(this);
        mMap.setOnCameraMoveListener(this);
        mMap.setOnCameraMoveCanceledListener(this);
        mMap.setOnMarkerClickListener(this);

        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }else{
            mGeoDataClient = Places.getGeoDataClient(this);
            mPlaceDetectionClient = Places.getPlaceDetectionClient(this );
            mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
            mMap.setMyLocationEnabled(true);
            getDeviceLocation();
        }
            }

    @Override
    public void onCameraMoveStarted(int reason) {
    }

    @Override
    public void onCameraMove() {
    }

    @Override
    public void onCameraMoveCanceled() {
    }

    @Override
    public void onCameraIdle() {
        LatLngBounds bounds = mMap.getProjection().getVisibleRegion().latLngBounds;
        fetchData(bounds);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Bundle bundle = new Bundle();
        bundle.putString("id",marker.getTitle());
        fragment.setArguments(bundle);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_up, R.anim.slide_down,R.anim.slide_up, R.anim.slide_down);
        if(fragment.isAdded()){
            transaction.remove(fragment);
        }
        transaction
                .add(R.id.container, fragment)
                .addToBackStack("fragment")
                .commit();
        return true;
    }




    public void createMark(String title, LatLng position, String image){
        heatmap = false;

        mMap.addMarker(
                new MarkerOptions()
                           .title(title)
                           .position(position)
                           .snippet("This is my stop!")
                           .icon(BitmapDescriptorFactory.fromBitmap(resize(getDrawable(R.mipmap.placeholder))))
            );
    }


    private void getDeviceLocation() {
        try {
                Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(Task<Location> task) {
                        if (task.isSuccessful()) {
                            mLastKnownLocation = task.getResult();
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(mLastKnownLocation.getLatitude(),
                                            mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                        } else {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
        } catch(SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    void fetchData(LatLngBounds bounds){
        double E = bounds.northeast.latitude;
        double W = bounds.southwest.latitude;
        double S = bounds.northeast.longitude;
        double N = bounds.southwest.longitude;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentDateAndTime = sdf.format(new Date());
        Call<List<Event>> eventCall = service.listEventsBox(N,S,W,E, currentDateAndTime);

        eventCall.enqueue(new Callback<List<Event>>() {
            @Override
            public void onResponse(Call<List<Event>> call, Response<List<Event>> response) {
                events.clear();
                Log.e(TAG,response.raw().toString());
                if(response.code()==204)
                {
                    return;
                }

                double zoom = mMap.getCameraPosition().zoom;
                if(zoom != DEFAULT_ZOOM){
                    thumbScale = zoom / DEFAULT_ZOOM;
                    if(zoom<13){
                        addHeatMap(response.body());
                        return;
                    }
                }

                mMap.clear();
                for(int i = 0 ; i < response.body().size() ; i++){
                    Event event = response.body().get(i);
                    String name = event.getId();
                    LatLng latLng = new LatLng(event.getLat(),event.getLng());
                    createMark(name,latLng,null);
                    events.add(event);
                }
            }

            @Override
            public void onFailure(Call<List<Event>> call, Throwable t) {
                Log.e(TAG,"fuck response " + t.getMessage());

            }
        });
    }
//    private Bitmap getCircularBitmap(int radius, Bitmap bitmap) {
//        Bitmap.Config conf = Bitmap.Config.ARGB_8888;
//        Bitmap bmp = Bitmap.createBitmap(radius, radius, conf);
//        Canvas canvas = new Canvas(bmp);
//
//        // creates a centered bitmap of the desired size
//        bitmap = ThumbnailUtils.extractThumbnail(bitmap, radius, radius, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
//        BitmapShader shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
//        Paint paint = new Paint();
//        paint.setAntiAlias(true);
//
//        paint.setShader(shader);
//        RectF rect = new RectF(0, 0, radius, radius);
//        canvas.drawRoundRect(rect, radius, radius, paint);
//
//        return bmp;
//    }

    private Bitmap resize(Drawable image) {
        Bitmap b = ((BitmapDrawable)image).getBitmap();
        Bitmap bitmapResized = Bitmap.createScaledBitmap(b, thumbWidth, thumbHeight, false);
        return bitmapResized;
    }

    private void addHeatMap(List<Event> date) {
        if(date.isEmpty()) return;
        int[] colors =  {
            Color.rgb(146,255,192),
            Color.rgb(0,38,97)
        };
        float[] startPoints = {
                0.2f, 1f
        };
        Gradient gradient = new Gradient(colors, startPoints);

        if(!heatmap) mMap.clear();
        heatmap=true;

        List<LatLng> list = null;
        list = eventsToLatLngList(date);


        mProvider = new HeatmapTileProvider.Builder()
                .data(list)
                .gradient(gradient)
                .build();
        mMap.addTileOverlay(new TileOverlayOptions().tileProvider(mProvider));
    }

    private ArrayList<LatLng> eventsToLatLngList(List<Event> events) {
        ArrayList<LatLng> list = new ArrayList<LatLng>();
        for (int i = 0; i < events.size(); i++) {
            Event event = events.get(i);
            double lat = event.getLat();
            double lng = event.getLng();
            list.add(new LatLng(lat, lng));
        }

        return list;
    }

}
