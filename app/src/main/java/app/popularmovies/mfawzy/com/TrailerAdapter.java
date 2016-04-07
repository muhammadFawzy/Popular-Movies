package app.popularmovies.mfawzy.com;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by mohamed on 17/03/16.
 */
public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.MyTrailerHolder> {

    Context context;
    ArrayList<Trailer> trailers;

    public TrailerAdapter(Context context) {
        this.context = context;
        trailers = new ArrayList<Trailer>();

    }

    @Override
    public MyTrailerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View root = inflater.inflate(R.layout.trailer_row, parent, false);
        return new MyTrailerHolder(root);
    }

    @Override
    public void onBindViewHolder(MyTrailerHolder holder, int position) {

        holder.tv_tariler.setText("Trailer" + (position + 1));


    }

    @Override
    public int getItemCount() {
        return trailers.size();
    }


    public void add(Trailer trailer) {
        trailers.add(trailer);
        notifyDataSetChanged();


    }
    public void clear(){
        trailers.clear();
    }

    public class MyTrailerHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView playTrailer;
        TextView tv_tariler;


        public MyTrailerHolder(View itemView) {
            super(itemView);
            playTrailer = (ImageView) itemView.findViewById(R.id.iv_play);
            playTrailer.setOnClickListener(this);
            tv_tariler = (TextView) itemView.findViewById(R.id.tv_trailer);
        }

        @Override
        public void onClick(View view) {
            String videoId = trailers.get(getPosition()).getKey();
            Intent videoClient = new Intent(Intent.ACTION_VIEW);
            videoClient.setData(Uri.parse("http://m.youtube.com/watch?v=" + videoId));
            context.startActivity(videoClient);


        }
    }
}
