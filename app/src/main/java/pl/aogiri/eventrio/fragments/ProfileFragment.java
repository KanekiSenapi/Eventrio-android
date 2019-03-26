package pl.aogiri.eventrio.fragments;

import android.content.Context;
import android.content.Intent;
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

import pl.aogiri.eventrio.OnSwipeTouchListener;
import pl.aogiri.eventrio.R;
import pl.aogiri.eventrio.ServiceGenerator;
import pl.aogiri.eventrio.activity.NewEvent;
import pl.aogiri.eventrio.event.EventAdapter;
import pl.aogiri.eventrio.notifi.Notifi;
import pl.aogiri.eventrio.notifi.NotifiAdapter;
import pl.aogiri.eventrio.user.User;
import pl.aogiri.eventrio.user.UserInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//TODO Create API + get full data to this

public class ProfileFragment extends Fragment {
    private static final String TAG = "ProfileFragment.java";

    private LinearLayout containerMain;

    private TextView profileName;
    private TextView profileStatus;
    private ImageView profileImage;
    private Button profileSettings;
    SharedPreferences sharedPref;
    private LinearLayout recycleBox;

    private TextView[][] tabs = new TextView[3][3];
    private LinearLayout[] tabs2 = new LinearLayout[3];

    private ImageView profileBack;
    private ImageView newEvent;


    private RecyclerView[] recyclerViews = new RecyclerView[3];

    private RecyclerView recyclerView;
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
        service = ServiceGenerator.createService(UserInterface.class, getString(R.string.api_login), getString(R.string.api_password));
        super.onViewCreated(view, savedInstanceState);

        containerMain = view.findViewById(R.id.containerMain);
        profileImage = view.findViewById(R.id.profileImage);
        profileName = view.findViewById(R.id.profileName);
        profileStatus = view.findViewById(R.id.profileStatus);
        profileSettings = view.findViewById(R.id.profileSettings);
        profileBack = view.findViewById(R.id.profileBack);
        newEvent = view.findViewById(R.id.newEvent);
        recycleBox = view.findViewById(R.id.recycleBox);

        tabs[0][0] = view.findViewById(R.id.notfi);
        tabs[1][0] = view.findViewById(R.id.event);
        tabs[2][0] = view.findViewById(R.id.friend);

        tabs[0][1] = view.findViewById(R.id.numberNotfi);
        tabs[1][1] = view.findViewById(R.id.numberEvents);
        tabs[2][1] = view.findViewById(R.id.numberFriends);

        tabs2[0] = view.findViewById(R.id.notfisClick);
        tabs2[1] = view.findViewById(R.id.eventsClick);
        tabs2[2] = view.findViewById(R.id.friendsClick);

        recyclerView = view.findViewById(R.id.recycleNotifis);
        recyclerViews[1] = view.findViewById(R.id.recycleEvents);
        recyclerViews[2] = view.findViewById(R.id.recycleFriends);


        profileBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
//        for (RecyclerView recyclerView1 : recyclerViews) {
//            recyclerView1.setLayoutManager(new LinearLayoutManager(view.getContext()));
//        }

        for(int i = 0 ; i < tabs2.length ; i++){
            tabs2[i].setOnClickListener(new TabListener(i) {
                public void onClick(View v) {
                    Log.e(TAG,"clicked" + actual_card + id);
                    if(actual_card!=id){
                        setDisable(actual_card);
                        tabs[id][0].setTextColor(getResources().getColor(R.color._white,null));
                        setAdapter(id);
                    }
                }
            });
        }


        //TODO create animation slide maybe create 3 containers preloaded
        recyclerView.setOnTouchListener(new OnSwipeTouchListener(getActivity()){

            @Override
            public void onSwipeRight() {//To right
                if(actual_card>0){
                    setDisable(actual_card);
                    tabs[actual_card-1][0].setTextColor(getResources().getColor(R.color._white,null));
                    setAdapter(actual_card-1);

//                    final ValueAnimator animator = ValueAnimator.ofFloat(0.0f, 1f);
//                    animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//                        @Override
//                        public void onAnimationUpdate(ValueAnimator animation) {
//                            float value = (float) animation.getAnimatedValue();
//
//                            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) recycleProfile[].getLayoutParams();
//                            params.weight = value;
//                            detailsMax.setLayoutParams(params);
//                        }
//
//                    });
//                    animator.start();
                }
            }

            @Override
            public void onSwipeLeft() {//To left
                if(actual_card<2){
                    setDisable(actual_card);
                    tabs[actual_card+1][0].setTextColor(getResources().getColor(R.color._white,null));
                    setAdapter(actual_card+1);
                }
            }

            @Override
            public void onSwipeBottom() {
                super.onSwipeBottom();
            }

            @Override
            public void onSwipeTop() {
                super.onSwipeTop();
            }
        });

        containerMain.setOnTouchListener(new OnSwipeTouchListener(getActivity()){
            @Override
            public void onSwipeLeft() {
                getActivity().onBackPressed();
            }
        });

        newEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), NewEvent.class);
                startActivity(i);
            }
        });

        setProfile(view);




    }

    private void setDisable(int id){
        tabs[id][0].setTextColor(getResources().getColor(R.color.trans_white, null));
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

    public void setAdapter(int i){
        switch (i){
            case 0:
                Log.e(TAG,"NOTIGIG");
                setNotifis();
                break;
            case 1:
                setEvents();
                break;
            case 2:
                setFriends();
                break;
        }
    }

    private void setNotifis(){
        mAdapter = new NotifiAdapter(user.getNotifis());
        recyclerView.setAdapter(mAdapter);
        actual_card=0;

    }

    private void setEvents(){
        mAdapter = new EventAdapter(user.getEvents());
        recyclerView.setAdapter(mAdapter);
        actual_card=1;
    }

    private void setFriends(){
        recyclerView.setAdapter(new NotifiAdapter(new ArrayList<Notifi>()));
        Toast.makeText(getContext(), "Soon", Toast.LENGTH_SHORT).show();
        actual_card=2;
    }

    private void getProfile(String id){
        Call<User> call = service.getUser(id);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                Log.e(TAG, response.raw().toString());
                if(response.body()==null){
                    Toast.makeText(getContext(), "error" + response.raw(), Toast.LENGTH_LONG).show();
                }else {
                    user = response.body();


                    tabs[0][1].setText(numberRepairNineNine(user.getNotifis().size()));
                    tabs[1][1].setText(numberRepairNineNine(user.getEvents().size()));
                    tabs[2][1].setText("0");

                    //TODO add number of friends

                    setNotifis();
                }

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
            }

            private String numberRepairNineNine(int number){
                if(number>99)
                    return "99+";
                else
                    return String.valueOf(number);
            }
        });
    }

    public class TabListener implements View.OnClickListener {
        int id;

        private TabListener(int id) {
            this.id = id;
        }

        @Override
        public void onClick(View v) {
        }
    }
}
