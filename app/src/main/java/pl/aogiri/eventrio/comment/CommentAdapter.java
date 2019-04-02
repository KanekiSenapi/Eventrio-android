package pl.aogiri.eventrio.comment;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import pl.aogiri.eventrio.R;
import pl.aogiri.eventrio.user.User;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    private static final String TAG = "CommentAdapter.java" ;
    private List<Comment> mDataset;
    private static RequestManager requestManager;


    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cv;

        private ImageView commentImage;
        private TextView commentUser;
        private TextView commentContent;

        public ViewHolder(final View vi) {
            super(vi);
            requestManager = Glide.with(vi);
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

    public CommentAdapter(List<Comment> myDataset) {
        mDataset = myDataset;
    }

    @Override
    public CommentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                        int viewType) {


        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_coment, parent, false);
        CommentAdapter.ViewHolder pvh = new ViewHolder(v);
        return pvh;


    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Comment comment = mDataset.get(position);
        User user = comment.getCommentator();
        requestManager.load(user.getPicture()).apply(RequestOptions.circleCropTransform()).into(holder.commentImage);
        holder.commentUser.setText(comment.getCommentator().getPseudonym());
        holder.commentContent.setText(comment.getContent());
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

}
