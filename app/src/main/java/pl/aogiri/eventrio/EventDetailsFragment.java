
package pl.aogiri.eventrio;

import android.animation.ValueAnimator;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import pl.aogiri.eventrio.comment.Comment;
import pl.aogiri.eventrio.comment.CommentAdapter;
import pl.aogiri.eventrio.event.Event;
import pl.aogiri.eventrio.event.EventInterface;
import pl.aogiri.eventrio.tag.Tag;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EventDetailsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EventDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventDetailsFragment extends Fragment {

    private static final String TAG = "EventDetailsFragment";

    private TextView eventName;
    private TextView eventTime;
    private LinearLayout details;
    private LinearLayout detailsMax;
    private ImageView eventImage;
    private LinearLayout containter;
    private TextView eventDescription;
    private LinearLayout containterTags;
    private RecyclerView recycleComments;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private TextView eventAddress;
    private RelativeLayout toBack;
    private RelativeLayout containerForSwipe;

    private EventInterface service;

    private boolean full = false;

    private OnFragmentInteractionListener mListener;

    public EventDetailsFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static EventDetailsFragment newInstance(String param1, String param2) {
        EventDetailsFragment fragment = new EventDetailsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "Created");
        //Create interface for connection
        service = ServiceGenerator.createService(EventInterface.class, "admin", "password");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_event_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        eventName = view.findViewById(R.id.eventName);
        eventTime = view.findViewById(R.id.eventTime);
        eventImage = view.findViewById(R.id.eventImage);
        eventAddress = view.findViewById(R.id.eventAddress);
        containter = view.findViewById(R.id.container);
        details = view.findViewById(R.id.details);
        toBack = view.findViewById(R.id.toBack);
        containerForSwipe = view.findViewById(R.id.containerForSwipe);
        detailsMax = view.findViewById(R.id.detailsmax);
        eventDescription = view.findViewById(R.id.eventDescription);
        containterTags = view.findViewById(R.id.containerTags);
        recycleComments = view.findViewById(R.id.recycleComments);

        mLayoutManager = new LinearLayoutManager(view.getContext());
        recycleComments.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)


        eventImage.setImageResource(R.drawable.testevent);



        details.setOnTouchListener(new OnSwipeTouchListener(getActivity()){
            public void onSwipeTop() {
                Log.e(TAG, " Swipe top");
                if(!full){
                    full = true;

                    final ValueAnimator animator = ValueAnimator.ofFloat(0.0f, 0.5f);
                    animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            float value = (float) animation.getAnimatedValue();

                            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) detailsMax.getLayoutParams();
                            params.weight = value;
                            detailsMax.setLayoutParams(params);
                        }

                    });


                    //Load animation
                    Animation slide_down = AnimationUtils.loadAnimation(getContext(),
                            R.anim.slide_top_down);
                    slide_down.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                            eventImage.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {
                        }
                    });

                    eventImage.startAnimation(slide_down);
                    animator.start();
                    //toBack.setBackgroundColor(0xFFf2f3f8);






                }
            }
            public void onSwipeBottom() {
                Log.e(TAG, " Swipe bottom");
                if(full){
                    full = false;

                    final ValueAnimator animator2 = ValueAnimator.ofFloat(0.5f, 0.0f);
                    animator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            float value = (float) animation.getAnimatedValue();

                            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) detailsMax.getLayoutParams();
                            params.weight = value;
                            detailsMax.setLayoutParams(params);
                        }
                    });

                    //Load animation
                    Animation slide_down = AnimationUtils.loadAnimation(getContext(),
                            R.anim.slide_top_up);
                    slide_down.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                            eventImage.setVisibility(View.GONE);
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {
                        }
                    });

                    eventImage.startAnimation(slide_down);
                    animator2.start();
                    //toBack.setBackgroundColor(0x64000000);


                }else{
                    full=false;
                    exit();
                }
            }
        });

//        toBack.setOnTouchListener(new OnSwipeTouchListener(getActivity()){
//            @Override
//            public void onClick() {
//                full=false;
//                exit();
//
//            }
//        });


        Bundle bundle = this.getArguments();

        if (bundle != null) {
            String i = bundle.getString("id", null);
            Call<Event> eventCall = service.getEvent(i);

            eventCall.enqueue(new Callback<Event>() {
                @Override
                public void onResponse(Call<Event> call, Response<Event> response) {
                    Event event = response.body();

                    eventName.setText(event.getName());
                    eventTime.setText(event.getDateBeg());
                    eventAddress.setText(event.getAddress());
                    eventDescription.setText(event.getDescription());
                    List<Tag> tags = event.getTags();
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    layoutParams.setMargins(10,0,10,0);

                    for(int i = 0 ; i < tags.size() ; i++){
                        TextView tmp = new TextView(getContext(),null, 0 , R.style.tagigosz);
                        tmp.setText(tags.get(i).getName());
                        tmp.setBackgroundResource(R.drawable.button_tag);

                        containterTags.addView(tmp,layoutParams);
                    }
                    List<Comment> comments = event.getComments();
                    mAdapter = new CommentAdapter(event.getComments());
                    recycleComments.setAdapter(mAdapter);


                }

                @Override
                public void onFailure(Call<Event> call, Throwable t) {

                }
            });
        }
//       view.findViewById(R.id.toBack).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                exit();
//            }
//        });


    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void exit(){
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_up, R.anim.slide_down);
        transaction.remove(EventDetailsFragment.this).commit();
    }

}
