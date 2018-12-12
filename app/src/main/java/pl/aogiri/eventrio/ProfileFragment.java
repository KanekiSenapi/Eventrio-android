package pl.aogiri.eventrio;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import pl.aogiri.eventrio.notifi.NotifiAdapter;
import pl.aogiri.eventrio.user.User;
import pl.aogiri.eventrio.user.UserInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {
    private static final String TAG = "ProfileFragment.java";

    private TextView profileName;
    private TextView profileStatus;
    private ImageView profileImage;
    private Button profileSettings;
    SharedPreferences sharedPref;

    private TextView numberNotfi;

    private ImageView profileBack;

    private RecyclerView recycleProfile;



    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private OnFragmentInteractionListener mListener;

    private UserInterface service;
    private User user;

    public ProfileFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        service = ServiceGenerator.createService(UserInterface.class, getString(R.string.apiUser), getString(R.string.apiPassword));

        super.onViewCreated(view, savedInstanceState);
        profileImage = view.findViewById(R.id.profileImage);
        profileName = view.findViewById(R.id.profileName);
        profileStatus = view.findViewById(R.id.profileStatus);
        profileSettings = view.findViewById(R.id.profileSettings);
        recycleProfile = view.findViewById(R.id.recycleProfile);
        profileBack = view.findViewById(R.id.profileBack);
        numberNotfi = view.findViewById(R.id.numberNotfi);

        profileBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        mLayoutManager = new LinearLayoutManager(view.getContext());
        recycleProfile.setLayoutManager(mLayoutManager);

        setProfile(view);




    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;

    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void setProfile(View v){
        sharedPref = getContext().getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        profileName.setText(sharedPref.getString(getString(R.string.userData_username), "none"));
        getProfile(sharedPref.getString(getString(R.string.userData_userid), "0"));
        String url = sharedPref.getString(getString(R.string.userData_picture),"null");
        Glide.with(v).load(url).apply(RequestOptions.circleCropTransform()).into(profileImage);
//        List<Notifi> notifis = new ArrayList<>();
//        notifis.add(new Notifi("5","<b>Juliś</b> sent you message", "Just now!",false,"M"));
//        notifis.add(new Notifi("4","<b>Juliś</b> comment your event", "3m",false,"C"));
//        notifis.add(new Notifi("3","<b>Juliś</b> invited you to event", "4m",true,"I"));
//        notifis.add(new Notifi("2","<b>Juliś</b> created new event", "5m",true,"E"));
//        notifis.add(new Notifi("1","<b>Juliś</b> is following you", "10m",true,"F"));

    }

    private void setNotifis(){
        mAdapter = new NotifiAdapter(user.getNotifis());
        recycleProfile.setAdapter(mAdapter);
    }

    private void getProfile(String id){
        Log.e(TAG,id);
        Call<User> call = service.getUser(id);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                Log.e(TAG, response.raw().toString());
                if(response.body()==null){
                    Toast.makeText(getContext(), "error" + response.raw(), Toast.LENGTH_LONG).show();
                }else {
                    user = response.body();
                    numberNotfi.setText(String.valueOf(user.getNotifis().size()));
                    setNotifis();
                }

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
            }
        });
    }
}
