package app.popularmovies.mfawzy.com;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

/**
 * Created by mohamed on 12/03/16.
 */
public class DetailFragment extends Fragment {
    final String ApiKey = "1892e04f560af13ddb5daa9989b3b1ef";
    TrailerAdapter trailerAdapter;
    ReviewAdapter reviewAdapter;
    RecyclerView recyclerReview;
    Button add_fav;
    MovieLocalData localMovieData;
    boolean local;

    Movie movie;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        trailerAdapter = new TrailerAdapter(getActivity());
        localMovieData = new MovieLocalData(getActivity());
        reviewAdapter = new ReviewAdapter(getActivity());
        setRetainInstance(true);
    }

    public DetailFragment() {

    }


    public DetailFragment(Parcelable movie, boolean local) {
        this.movie = (Movie) movie;
        this.local = local;


    }

    @Override
    public void onStart() {
        super.onStart();
        if (local == false)
            refresh();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_detail, container, false);
        setHasOptionsMenu(true);

        TextView title = (TextView) root.findViewById(R.id.movie_title);
        title.setText(movie.getTitle());

        TextView date = (TextView) root.findViewById(R.id.tv_date);
        date.setText(movie.getDate());

        TextView rate = (TextView) root.findViewById(R.id.tv_rate);
        rate.setText(movie.getRating() + "/10");

        TextView overview = (TextView) root.findViewById(R.id.tv_overview);
        overview.setText(movie.getOverview());

        ImageView back_poster = (ImageView) root.findViewById(R.id.back_poster);
        if (local == false)
            Picasso.with(getActivity()).load("http://image.tmdb.org/t/p/w185/" + movie.getImage2()).into(back_poster);
        else
            Picasso.with(getActivity()).load(new File(Environment.getExternalStorageDirectory().getPath() + movie.getImage2())).into(back_poster);


        RecyclerView recyclerTrailer = (RecyclerView) root.findViewById(R.id.recycler_trailers);
        recyclerReview = (RecyclerView) root.findViewById(R.id.recycler_reviews);


        recyclerTrailer.setLayoutManager(new MyLinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerTrailer.setItemAnimator(new DefaultItemAnimator());


        recyclerReview.setLayoutManager(new MyLinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerReview.setItemAnimator(new DefaultItemAnimator());


        recyclerTrailer.setAdapter(trailerAdapter);
        recyclerReview.setAdapter(reviewAdapter);

        add_fav = (Button) root.findViewById(R.id.btn_fav);
        ArrayList<Movie> localMovies = localMovieData.getData();

        for (int i = 0; i < localMovies.size(); i++) {
            if (movie.getId() == localMovies.get(i).getId()) {
                add_fav.setEnabled(false);
                break;
            }
        }
        add_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (add_fav.isEnabled()) {

                    Picasso.with(getActivity()).load("http://image.tmdb.org/t/p/w342/" + movie.getImage()).into(target);
                    Picasso.with(getActivity()).load("http://image.tmdb.org/t/p/w185/" + movie.getImage2()).into(target2);

                    localMovieData.insertData(
                            movie.getId(), movie.getTitle(), movie.getImage(),
                            movie.getImage2(), movie.getDate(), movie.getOverview(),
                            movie.getRating());

                    Toast.makeText(getActivity(), "Added to Favourite!", Toast.LENGTH_SHORT).show();
                    add_fav.setEnabled(false);
                }

            }
        });


        return root;

    }

    android.support.v7.widget.ShareActionProvider mShareActionProvider;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.detais_menu, menu);
        MenuItem item = menu.findItem(R.id.action_share);
        mShareActionProvider = (android.support.v7.widget.ShareActionProvider) MenuItemCompat.getActionProvider(item);


    }
//
//    private Intent getShareIntent() {
//
//        Intent shareIntent = new Intent(Intent.ACTION_SEND);
//        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
//        shareIntent.setType("text/plain");
//        shareIntent.putExtra(Intent.EXTRA_TEXT,
//                mForecastStr + FORECAST_SHARE_HASHTAG);
//
//
//        return shareIntent;
//    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        return super.onOptionsItemSelected(item);
    }

    private Target target = new Target() {


        @Override
        public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
            new Thread(new Runnable() {

                @Override
                public void run() {

                    File file = new File(Environment.getExternalStorageDirectory().getPath() + movie.getImage());
                    try {
                        file.createNewFile();
                        FileOutputStream ostream = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, ostream);
                        ostream.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }).start();
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {
        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {
            if (placeHolderDrawable != null) {
            }
        }
    };
    private Target target2 = new Target() {


        @Override
        public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
            new Thread(new Runnable() {

                @Override
                public void run() {

                    File file = new File(Environment.getExternalStorageDirectory().getPath() + movie.getImage2());
                    try {
                        file.createNewFile();
                        FileOutputStream ostream = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, ostream);
                        ostream.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }).start();
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {
        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {
            if (placeHolderDrawable != null) {
            }
        }
    };

    public void refresh() {
        String url1 = "http://api.themoviedb.org/3/movie/" + movie.getId() + "/videos?api_key=" + ApiKey;
        String url2 = "http://api.themoviedb.org/3/movie/" + movie.getId() + "/reviews?api_key=" + ApiKey;
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        StringRequest Request1 = new StringRequest(Request.Method.GET, url1,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        readJsonTrailers(response);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        StringRequest Request2 = new StringRequest(Request.Method.GET, url2,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        readJsonReviews(response);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        queue.add(Request1);
        queue.add(Request2);


    }

    private void readJsonReviews(String response) {
        if (response.length() != 0 && response != null) {
            reviewAdapter.clear();

            try {
                JSONObject jsonstr = new JSONObject(response);
                JSONArray results = jsonstr.getJSONArray("results");

                for (int i = 0; i < results.length(); i++) {
                    JSONObject obj = results.getJSONObject(i);
                    Review review = new Review();

                    review.setId(obj.getString("id"));
                    review.setAuthor(obj.getString("author"));
                    review.setContent(obj.getString("content"));
                    reviewAdapter.add(review);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


    }

    private void readJsonTrailers(String response) {
        if (response.length() != 0 && response != null) {
            trailerAdapter.clear();


            try {
                JSONObject jsonstr = new JSONObject(response);
                JSONArray results = jsonstr.getJSONArray("results");

                for (int i = 0; i < results.length(); i++) {
                    JSONObject obj = results.getJSONObject(i);
                    Trailer trailer = new Trailer();

                    trailer.setId(obj.getString("id"));
                    trailer.setKey(obj.getString("key"));
                    trailer.setName(obj.getString("name"));
                    trailer.setSite(obj.getString("site"));
                    trailer.setType(obj.getString("type"));
                    trailerAdapter.add(trailer);
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_TEXT,
                            "http://m.youtube.com/watch?v=" + trailerAdapter.trailers.get(0).getKey());
                    mShareActionProvider.setShareIntent(shareIntent);


                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public class MyLinearLayoutManager extends LinearLayoutManager {

        public MyLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
            super(context, orientation, reverseLayout);
        }

        private int[] mMeasuredDimension = new int[2];

        @Override
        public void onMeasure(RecyclerView.Recycler recycler, RecyclerView.State state,
                              int widthSpec, int heightSpec) {
            final int widthMode = View.MeasureSpec.getMode(widthSpec);
            final int heightMode = View.MeasureSpec.getMode(heightSpec);
            final int widthSize = View.MeasureSpec.getSize(widthSpec);
            final int heightSize = View.MeasureSpec.getSize(heightSpec);
            int width = 0;
            int height = 0;
            for (int i = 0; i < getItemCount(); i++) {


                if (getOrientation() == HORIZONTAL) {

                    measureScrapChild(recycler, i,
                            View.MeasureSpec.makeMeasureSpec(i, View.MeasureSpec.UNSPECIFIED),
                            heightSpec,
                            mMeasuredDimension);

                    width = width + mMeasuredDimension[0];
                    if (i == 0) {
                        height = mMeasuredDimension[1];
                    }
                } else {
                    measureScrapChild(recycler, i,
                            widthSpec,
                            View.MeasureSpec.makeMeasureSpec(i, View.MeasureSpec.UNSPECIFIED),
                            mMeasuredDimension);
                    height = height + mMeasuredDimension[1];
                    if (i == 0) {
                        width = mMeasuredDimension[0];
                    }
                }
            }
            switch (widthMode) {
                case View.MeasureSpec.EXACTLY:
                    width = widthSize;
                case View.MeasureSpec.AT_MOST:
                case View.MeasureSpec.UNSPECIFIED:
            }

            switch (heightMode) {
                case View.MeasureSpec.EXACTLY:
                    height = heightSize;
                case View.MeasureSpec.AT_MOST:
                case View.MeasureSpec.UNSPECIFIED:
            }

            setMeasuredDimension(width, height);
        }

        private void measureScrapChild(RecyclerView.Recycler recycler, int position, int widthSpec,
                                       int heightSpec, int[] measuredDimension) {
            View view = recycler.getViewForPosition(position);
            recycler.bindViewToPosition(view, position);
            if (view != null) {
                RecyclerView.LayoutParams p = (RecyclerView.LayoutParams) view.getLayoutParams();
                int childWidthSpec = ViewGroup.getChildMeasureSpec(widthSpec,
                        getPaddingLeft() + getPaddingRight(), p.width);
                int childHeightSpec = ViewGroup.getChildMeasureSpec(heightSpec,
                        getPaddingTop() + getPaddingBottom(), p.height);
                view.measure(childWidthSpec, childHeightSpec);
                measuredDimension[0] = view.getMeasuredWidth() + p.leftMargin + p.rightMargin;
                measuredDimension[1] = view.getMeasuredHeight() + p.bottomMargin + p.topMargin;
                recycler.recycleView(view);
            }
        }
    }


}
