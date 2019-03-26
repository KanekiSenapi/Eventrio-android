package pl.aogiri.eventrio;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Base64;
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
import com.google.android.gms.maps.model.CameraPosition;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        profileContainer = findViewById(R.id.profileContainer);
        imageButton = findViewById(R.id.imageButton);

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


        //Create interface for connection
        service = ServiceGenerator.createService(EventInterface.class, "admin", "f57624a2-fc4c-4d94-8264-c69cf8be676d");

        //Get data from api
        Call<List<Event>> callEvents = service.listEvents();

        callEvents.enqueue(new Callback<List<Event>>() {
            @Override
            public void onResponse(Call<List<Event>> call, Response<List<Event>> response) {
                if (response.isSuccessful() ) {
                    Log.e(TAG, response.body().toString());
                    Log.e(TAG,"done");
                    events = response.body();
                    // Obtain the SupportMapFragment and get notified when the map is ready to be used.
                    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                            .findFragmentById(R.id.map);
                    mapFragment.getMapAsync(MapsActivity.this);
                } else {
                    Log.e(TAG, "ops");
                    Log.e(TAG,response.raw().toString());
                }

            }

            @Override
            public void onFailure(Call<List<Event>> call, Throwable t) {
                Log.e(TAG, t.getMessage());
            }
        });


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
            //TODO
            // Construct a GeoDataClient.
            mGeoDataClient = Places.getGeoDataClient(this);

            // Construct a PlaceDetectionClient.
            mPlaceDetectionClient = Places.getPlaceDetectionClient(this );

            // Construct a FusedLocationProviderClient.
            mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
            //
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
        Log.e(TAG, marker.getTitle());
        Fragment fragment = new EventDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putString("id",marker.getTitle());
        fragment.setArguments(bundle);
//        fragment.setEnterTransition(android.R.transition.slide_bottom);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_up, R.anim.slide_down,R.anim.slide_up, R.anim.slide_down);
        transaction
                .add(R.id.container, fragment)
                .addToBackStack("fragment")
                .commit();

        return true;
    }

    public void toLocation(LatLng current){
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(current)
                .zoom(17)
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }



    public void createMark(String title, LatLng position, String image){
        heatmap = false;
        if(image != null) {
        byte[] bytes = Base64.decode(image, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);


            mMap.addMarker(
                    new MarkerOptions()
                            .title(title)
                            .position(position)
                            .icon(BitmapDescriptorFactory.fromBitmap(
                                    getCircularBitmap((int)(thumRadius*thumbScale),
                                            ThumbnailUtils.extractThumbnail(Bitmap.createBitmap(bitmap),(int)(thumbHeight*thumbScale), (int)(thumbWidth*thumbScale), ThumbnailUtils.OPTIONS_RECYCLE_INPUT)
                                    )
                                )
                            )
            );
        }else {
            mMap.addMarker(
                    new MarkerOptions()
                            .title(title)
                            .position(position)
                            .snippet("This is my stop!")
                            .icon(BitmapDescriptorFactory.fromBitmap(resize(getDrawable(R.drawable.default_icon))))
            );
        }

        resize(getDrawable(R.drawable.default_icon));
    }


    private void getDeviceLocation() {
        try {
                Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            mLastKnownLocation = task.getResult();
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(mLastKnownLocation.getLatitude(),
                                            mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                        } else {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
//                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
        } catch(SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    void fetchData(LatLngBounds bounds){
        double N = bounds.northeast.latitude;
        double E = bounds.northeast.longitude;
        double S = bounds.southwest.latitude;
        double W = bounds.southwest.longitude;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.0'Z'");
        String currentDateandTime = sdf.format(new Date());
        Call<List<Event>> eventCall = service.listEventsBox(N,E,S,W,currentDateandTime);

        eventCall.enqueue(new Callback<List<Event>>() {
            @Override
            public void onResponse(Call<List<Event>> call, Response<List<Event>> response) {
                Log.e(TAG,"get response");
//                mMap.clear();
                events.clear();
                double zoom = mMap.getCameraPosition().zoom;
                Log.e(TAG, String.valueOf(zoom));
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
//                    byte[] image = event.getImage();
                    LatLng latLng = new LatLng(event.getLat(),event.getLng());
                    createMark(name,latLng, event.getImage());
                    events.add(event);
                }
            }

            @Override
            public void onFailure(Call<List<Event>> call, Throwable t) {
                Log.e(TAG,"fuck response " + t.getMessage());

            }
        });
    }
    private Bitmap getCircularBitmap(int radius, Bitmap bitmap) {
        Bitmap.Config conf = Bitmap.Config.ARGB_8888;
        Bitmap bmp = Bitmap.createBitmap(radius, radius, conf);
        Canvas canvas = new Canvas(bmp);

        // creates a centered bitmap of the desired size
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, radius, radius, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        BitmapShader shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        Paint paint = new Paint();
        paint.setAntiAlias(true);

        paint.setShader(shader);
        RectF rect = new RectF(0, 0, radius, radius);
        canvas.drawRoundRect(rect, radius, radius, paint);

        return bmp;
    }

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
        Log.e(TAG, " Hear map");
        List<LatLng> list = null;
        list = eventsToLatLngList(date);


        mProvider = new HeatmapTileProvider.Builder()
                .data(list)
                .gradient(gradient)
                .build();
        // Add a tile overlay to the map, using the heat map tile provider.
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
