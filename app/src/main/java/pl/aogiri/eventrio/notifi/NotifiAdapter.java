package pl.aogiri.eventrio.notifi;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import pl.aogiri.eventrio.R;

public class NotifiAdapter extends RecyclerView.Adapter<NotifiAdapter.ViewHolder> {
    private List<Notifi> mDataset;


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        CardView cv;

        private TextView notfiTitle;
        private TextView notfiWhen;
        private TextView notfiImage;
        private ImageView notfiVisited;

        public ViewHolder(final View vi) {
            super(vi);
            cv = vi.findViewById(R.id.recycleComments);
            notfiTitle = vi.findViewById(R.id.notfiTitle);
            notfiWhen = vi.findViewById(R.id.notfiWhen);
            notfiImage = vi.findViewById(R.id.notfiImage);
            notfiVisited = vi.findViewById(R.id.notfiVisited);

            }

    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public NotifiAdapter(List<Notifi> myDataset) {

        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public NotifiAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                        int viewType) {


        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_notfi, parent, false);
        NotifiAdapter.ViewHolder pvh = new ViewHolder(v);
        return pvh;


    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Notifi notifi = mDataset.get(position);
        if(notifi.isShowed())
            holder.notfiVisited.setVisibility(View.GONE);
        holder.notfiImage.setText(notifi.getCategory());
        holder.notfiTitle.setText(Html.fromHtml(notifi.getTitle(), Html.FROM_HTML_MODE_LEGACY));
        holder.notfiWhen.setText(notifi.getDate());

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
