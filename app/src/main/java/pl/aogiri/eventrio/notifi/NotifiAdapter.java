package pl.aogiri.eventrio.notifi;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import pl.aogiri.eventrio.R;

public class NotifiAdapter extends RecyclerView.Adapter<NotifiAdapter.ViewHolder> {
    private final String TAG = "NotfiAdapter.java";
    private List<Notifi> mDataset;


    public static class ViewHolder extends RecyclerView.ViewHolder {
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
        Notifi notifi = mDataset.get(position);
        if(notifi.isShowed())
            holder.notfiVisited.setVisibility(View.GONE);
        holder.notfiImage.setText(notifi.getCategory());
        holder.notfiTitle.setText(Html.fromHtml(notifi.getTitle(), Html.FROM_HTML_MODE_LEGACY));
        holder.notfiWhen.setText(notifi.getDate());
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
