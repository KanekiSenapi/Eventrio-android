package pl.aogiri.eventrio.notifi;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import pl.aogiri.eventrio.R;
import pl.aogiri.eventrio.ServiceGenerator;
import pl.aogiri.eventrio.user.UserInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotifiAdapter extends RecyclerView.Adapter<NotifiAdapter.ViewHolder> {
    private final String TAG = "NotfiAdapter.java";
    private List<Notifi> mDataset;
    private UserInterface service;
    private static Context CONTEXT;


    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        private LinearLayout thisNotfi;
        private TextView notfiTitle;
        private TextView notfiWhen;
        private TextView notfiImage;
        private ImageView notfiVisited;

        public ViewHolder(final View vi) {
            super(vi);
            cv = vi.findViewById(R.id.recycleComments);
            thisNotfi = vi.findViewById(R.id.thisNotfi);
            notfiTitle = vi.findViewById(R.id.notfiTitle);
            notfiWhen = vi.findViewById(R.id.notfiWhen);
            notfiImage = vi.findViewById(R.id.notfiImage);
            notfiVisited = vi.findViewById(R.id.notfiVisited);
            CONTEXT = vi.getContext();

            }

    }

    public NotifiAdapter(List<Notifi> myDataset) {
        mDataset = myDataset;
    }

    @Override
    public NotifiAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                        int viewType) {


        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_notfi, parent, false);
        NotifiAdapter.ViewHolder pvh = new ViewHolder(v);
        return pvh;


    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Notifi notifi = mDataset.get(position);
        if(notifi.isShowed())
            holder.notfiVisited.setVisibility(View.GONE);
        holder.notfiImage.setText(notifi.getCategory());
        holder.notfiTitle.setText(Html.fromHtml(notifi.getTitle(), Html.FROM_HTML_MODE_LEGACY));
        holder.notfiWhen.setText(notifi.getDate());

        holder.thisNotfi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG,notifi.getId() + " tet");
                if(!notifi.isShowed())
                    setNotifiSeen(notifi.getId(),holder.notfiVisited);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    private void setNotifiSeen(Integer id,final ImageView visited){
        service = ServiceGenerator.createService(UserInterface.class);
        Call<Void> call = service.setNotifiSeen(id);
        Log.e(TAG,call.request().toString());

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.e(TAG,response.raw().toString());
                if(response.code()==202){
                    visited.setVisibility(View.GONE);
                }
                else if(response.code()==204)
                    Toast.makeText(CONTEXT, "Bad notifi... try again", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e(TAG,t.getMessage());
            }
        });
    }
}
