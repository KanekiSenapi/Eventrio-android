package pl.aogiri.eventrio.comment;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import pl.aogiri.eventrio.R;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    private List<Comment> mDataset;


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        CardView cv;

        private ImageView commentImage;
        private TextView commentUser;
        private TextView commentContent;

        public ViewHolder(final View vi) {
            super(vi);
            cv = vi.findViewById(R.id.recycleComments);

            commentImage = vi.findViewById(R.id.commentImage);
            commentUser = vi.findViewById(R.id.commentUser);
            commentContent = vi.findViewById(R.id.commentContent);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO

                }
            });

        }

    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public CommentAdapter(List<Comment> myDataset) {

        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public CommentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                        int viewType) {


        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_coment, parent, false);
        CommentAdapter.ViewHolder pvh = new ViewHolder(v);
        return pvh;


    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Comment comment = mDataset.get(position);
//        holder.commentImage.setImageResource(R.drawable.testevent);
        holder.commentUser.setText(comment.getUser().getPseudonym());
        holder.commentContent.setText(comment.getContent());

           }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
