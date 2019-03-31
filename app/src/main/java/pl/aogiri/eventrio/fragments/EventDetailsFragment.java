
package pl.aogiri.eventrio.fragments;

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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import pl.aogiri.eventrio.OnSwipeTouchListener;
import pl.aogiri.eventrio.R;
import pl.aogiri.eventrio.ServiceGenerator;
import pl.aogiri.eventrio.comment.Comment;
import pl.aogiri.eventrio.comment.CommentAdapter;
import pl.aogiri.eventrio.event.Event;
import pl.aogiri.eventrio.event.EventInterface;
import pl.aogiri.eventrio.tag.Tag;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class EventDetailsFragment extends Fragment {

    private static final String TAG = "EventDetailsFragment";

    private static String ID;
    private static ProgressBar LOAD;

    private TextView eventName;
    private TextView eventTime;
    private LinearLayout details;
    private LinearLayout detailsMax;
    private ImageView eventImage;
    private LinearLayout containter;
    private TextView eventDescription;
    private ImageView eventOrganizer;
    private LinearLayout containterTags;
    private TextView noComment;
    private RecyclerView recycleComments;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private TextView eventAddress;

    private EventInterface service;

    private boolean full = false;

    private OnFragmentInteractionListener mListener;

    public EventDetailsFragment() {
    }


    public static EventDetailsFragment newInstance(String id, ProgressBar load) {
        EventDetailsFragment fragment = new EventDetailsFragment();
        ID = id;
        LOAD = load;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        service = ServiceGenerator.createService(EventInterface.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_event_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        eventName = view.findViewById(R.id.eventName);
        eventTime = view.findViewById(R.id.eventTime);
        eventImage = view.findViewById(R.id.eventImage);
        eventAddress = view.findViewById(R.id.eventAddress);
        eventOrganizer = view.findViewById(R.id.eventOrganizer);
        containter = view.findViewById(R.id.container);
        details = view.findViewById(R.id.details);
        detailsMax = view.findViewById(R.id.detailsmax);
        eventDescription = view.findViewById(R.id.eventDescription);
        containterTags = view.findViewById(R.id.containerTags);
        noComment = view.findViewById(R.id.noComment);
        recycleComments = view.findViewById(R.id.recycleComments);

        mLayoutManager = new LinearLayoutManager(view.getContext());
        recycleComments.setLayoutManager(mLayoutManager);

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


                }else{
                    full=false;
                    exit();
                }
            }
        });



            Call<Event> eventCall = service.getEvent(ID);

            eventCall.enqueue(new Callback<Event>() {
                @Override
                public void onResponse(Call<Event> call, Response<Event> response) {
                    if(response.code()==204){
                        Toast.makeText(getContext(), "Error 204... try again", Toast.LENGTH_SHORT).show();
                    }
                    else {

                        Event event = response.body();

                        eventName.setText(event.getName());
                        eventTime.setText(event.getDateBeg());
                        eventAddress.setText(event.getAddress());
                        eventDescription.setText(event.getDescription());
                        List<Tag> tags = event.getTags();
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        layoutParams.setMargins(10, 0, 10, 0);

                        for (int i = 0; i < tags.size(); i++) {
                            TextView tmp = new TextView(getContext(), null, 0, R.style.tagigosz);
                            tmp.setText(tags.get(i).getName());
                            tmp.setBackgroundResource(R.drawable.button_tag);

                            containterTags.addView(tmp, layoutParams);
                        }

                        Glide.with(view).load(event.getOrganizer().getPicture()).apply(RequestOptions.circleCropTransform()).into(eventOrganizer);
//                    eventOrganizer.(event.getOrganizer().getPseudonym());
                        List<Comment> comments = event.getComments();
                        mAdapter = new CommentAdapter(comments);
                        recycleComments.setAdapter(mAdapter);
                        if(comments.size()==0)
                            noComment.setVisibility(View.VISIBLE);

                        LOAD.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailure(Call<Event> call, Throwable t) {

                }
            });


    }

    public void onButtonPressed(Uri uri) {
        exit();
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

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    private void exit(){
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_up, R.anim.slide_down);
        transaction.remove(EventDetailsFragment.this).commit();
    }

}

