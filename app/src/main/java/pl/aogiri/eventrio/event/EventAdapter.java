package pl.aogiri.eventrio.event;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import pl.aogiri.eventrio.R;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder>{
    private List<Event> mDataset;


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        CardView cv;

        private TextView title;
        private TextView subtext;
        private TextView subtext2;
        private ImageView image;

        public ViewHolder(final View vi) {
            super(vi);
            cv = vi.findViewById(R.id.cardEvent);
            title = vi.findViewById(R.id.title);
            subtext = vi.findViewById(R.id.subtext);
            subtext2 = vi.findViewById(R.id.subtext2);
            image = vi.findViewById(R.id.image);

        }

    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public EventAdapter(List<Event> myDataset) {

        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public EventAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {


        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_event, parent, false);
        EventAdapter.ViewHolder pvh = new EventAdapter.ViewHolder(v);
        return pvh;


    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final EventAdapter.ViewHolder holder, int position) {
        Event event = mDataset.get(position);

        holder.title.setText(event.getName());
        holder.subtext.setText(event.getDescriptionSub());
        holder.subtext2.setText(event.getAddress());
        Glide.with(holder.itemView).load(event.getImage()).apply(RequestOptions.circleCropTransform()).into(holder.image);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
