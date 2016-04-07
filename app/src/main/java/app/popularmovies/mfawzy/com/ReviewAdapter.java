package app.popularmovies.mfawzy.com;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by mohamed on 18/03/16.
 */
public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.MyReviewHolder> {

    ArrayList<Review> reviews;
    Context context;

    public ReviewAdapter(Context context) {
        this.context = context;
        reviews = new ArrayList<>();

    }

    @Override
    public MyReviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View root = inflater.inflate(R.layout.review_row, parent, false);
        return new MyReviewHolder(root);
    }

    @Override
    public void onBindViewHolder(MyReviewHolder holder, int position) {

        holder.author.setText(reviews.get(position).getAuthor());
        holder.content.setText(reviews.get(position).getContent());


    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }


    public void add(Review review) {

        reviews.add(review);
        notifyDataSetChanged();


    }

    public void clear() {

        reviews.clear();
    }

    public class MyReviewHolder extends RecyclerView.ViewHolder {
        TextView author, content;


        public MyReviewHolder(View itemView) {
            super(itemView);
            author = (TextView) itemView.findViewById(R.id.tv_author);
            content = (TextView) itemView.findViewById(R.id.tv_content);
        }
    }
}
