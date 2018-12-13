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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import pl.aogiri.eventrio.event.Event;
import pl.aogiri.eventrio.event.EventAdapter;
import pl.aogiri.eventrio.notifi.NotifiAdapter;
import pl.aogiri.eventrio.user.User;
import pl.aogiri.eventrio.user.UserInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//TODO Create API + get full data to this

public class ProfileFragment extends Fragment {
    private static final String TAG = "ProfileFragment.java";

    private TextView profileName;
    private TextView profileStatus;
    private ImageView profileImage;
    private Button profileSettings;
    SharedPreferences sharedPref;

    private TextView numberNotfi;
    private TextView numberEvents;
    private TextView numberFriends;
    private TextView notfi;
    private TextView event;
    private TextView friend;

    private ImageView profileBack;

    private RecyclerView recycleProfile;

    private LinearLayout notfisClick;
    private LinearLayout eventsClick;
    private LinearLayout friendsClick;



    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private OnFragmentInteractionListener mListener;

    private UserInterface service;
    private User user;

    private int actual_card = 0;

    public ProfileFragment() {

    }

    // TODO: Rename and change types and number of parameters
//    public static ProfileFragment newInstance(String param1, String param2) {
//        ProfileFragment fragment = new ProfileFragment();
//        return fragment;
//    }

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
        numberEvents = view.findViewById(R.id.numberEvents);
        numberFriends = view.findViewById(R.id.numberFriends);

        notfi = view.findViewById(R.id.notfi);
        event = view.findViewById(R.id.event);
        friend = view.findViewById(R.id.friend);

        notfisClick = view.findViewById(R.id.notfisClick);
        eventsClick = view.findViewById(R.id.eventsClick);
        friendsClick = view.findViewById(R.id.friendsClick);


        profileBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        mLayoutManager = new LinearLayoutManager(view.getContext());
        recycleProfile.setLayoutManager(mLayoutManager);

        notfisClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(actual_card!=1) {
                    setDisable("notfisClick");
                    setNotifis();
                }
            }
        });

        eventsClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(actual_card!=2) {
                    setDisable("eventsClick");
                    setEvents();
                }
            }
        });

        friendsClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(actual_card!=3) {
//                setDisable("friendsClick");
                    setFriends();
                }
            }
        });

        setProfile(view);




    }

    private void setDisable(String tmp){
        switch (actual_card){
            case 1:
                notfi.setTextColor(getResources().getColor(R.color.trans_white, null));
                break;
            case 2:
                event.setTextColor(getResources().getColor(R.color.trans_white, null));
                break;
            case 3:
                friend.setTextColor(getResources().getColor(R.color.trans_white, null));
                break;
        }
        if(tmp.equals("notfisClick")) {
            notfi.setTextColor(getResources().getColor(R.color._white, null));
        }
        else if(tmp.equals("eventsClick")){
            event.setTextColor(getResources().getColor(R.color._white,null));
        }
        else if(tmp.equals("friendsClick")){
            friend.setTextColor(getResources().getColor(R.color._white,null));
        }
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

    }

    private void setNotifis(){
        mAdapter = new NotifiAdapter(user.getNotifis());
        recycleProfile.setAdapter(mAdapter);
        actual_card=1;
    }

    private void setEvents(){
        mAdapter = new EventAdapter(user.getEvents());
        recycleProfile.setAdapter(mAdapter);
        actual_card=2;
    }

    private void setFriends(){
        Toast.makeText(getContext(), "Soon", Toast.LENGTH_SHORT).show();
        //actual_card=3;
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
                    //TEST
                    List<Event> events = new ArrayList<>();
                    Event event = new Event();
                    event.setName("Event I");
                    event.setAddress("ul. Ta i ta, 32-011 Kraków");
                    event.setDescription("Party hard in old russian style... Just get arras and vodka! Drink and hug everyone! Have fun at party at 365 days per year. I hope u will come with me to this great time!");
                    events.add(event);
                    user.setEvents(events);
                    //TEST

                    int tmp = user.getNotifis().size();
                    if(tmp>99)
                        numberNotfi.setText("99+");
                    else
                        numberNotfi.setText(String.valueOf(tmp));

                    //TODO add number of events and friends

                    setNotifis();
                }

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
            }
        });
    }
}
