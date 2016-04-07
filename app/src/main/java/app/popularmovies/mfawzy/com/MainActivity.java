package app.popularmovies.mfawzy.com;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView Grid_movies;
    final String ApiKey = "1892e04f560af13ddb5daa9989b3b1ef";
    RecyclerAdapter adapter;
    StaggeredGridLayoutManager gridLayout;
    boolean local = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Grid_movies = (RecyclerView) findViewById(R.id.grid_movies);
        adapter = new RecyclerAdapter();
        if ((getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK) >=
                Configuration.SCREENLAYOUT_SIZE_LARGE)

            gridLayout = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        else
            gridLayout = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        Grid_movies.setLayoutManager(gridLayout);
        Grid_movies.setAdapter(adapter);
        refresh("popular");


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.most_popular) {
            refresh("popular");

        } else if (item.getItemId() == R.id.top_rated) {
            refresh("top_rated");

        } else if (item.getItemId() == R.id.favourite) {
            refresh("favourite");

        }
        return super.onOptionsItemSelected(item);
    }

    public void refresh(String sort) {
        if (!sort.equalsIgnoreCase("favourite")) {
            local = false;
            String url = "http://api.themoviedb.org/3/movie/" + sort + "?api_key=" + ApiKey;
            RequestQueue queue = Volley.newRequestQueue(this);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            readJson(response);

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(MainActivity.this, "Error Connection", Toast.LENGTH_SHORT).show();

                }
            });

            queue.add(stringRequest);
        } else {
            adapter.clear();
            adapter.setMovies(new MovieLocalData(MainActivity.this).getData());
            local = true;

        }


    }

    private void readJson(String response) {
        if (response.length() != 0 && response != null) {

            adapter.clear();


            JSONObject fullJson = null;
            try {
                fullJson = new JSONObject(response);
                JSONArray results = fullJson.getJSONArray("results");
                for (int i = 0; i < results.length(); i++) {
                    Movie movie = new Movie();
                    JSONObject obj = results.getJSONObject(i);
                    movie.setImage(obj.getString("poster_path"));
                    movie.setOverview(obj.getString("overview"));
                    movie.setTitle(obj.getString("original_title"));
                    movie.setDate(obj.getString("release_date"));
                    movie.setImage2(obj.getString("backdrop_path"));
                    movie.setId(obj.getInt("id"));
                    movie.setRating(obj.getDouble("vote_average"));
                    adapter.add(movie);
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyHolder> {

        ArrayList<Movie> movies;
        LayoutInflater inflater;


        public RecyclerAdapter() {

            movies = new ArrayList<>();
        }

        @Override
        public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View root = inflater.inflate(R.layout.grid_movie_layout, parent, false);


            return new MyHolder(root);
        }

        @Override
        public void onBindViewHolder(MyHolder holder, int position) {
            if (local == false)
                Picasso.with(MainActivity.this).load("http://image.tmdb.org/t/p/w342/" + movies.get(position).getImage()).into(holder.movie_poster);
            else
                Picasso.with(MainActivity.this).load(new File(Environment.getExternalStorageDirectory().getPath() + movies.get(position).getImage())).into(holder.movie_poster);


        }

        @Override
        public int getItemCount() {
            return movies.size();
        }

        public void add(Movie movie) {
            movies.add(movie);
            notifyDataSetChanged();


        }

        public void clear() {

            movies.clear();


        }

        public void setMovies(ArrayList<Movie> movies) {

            this.movies.addAll(movies);
            notifyDataSetChanged();

        }


        public class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            ImageView movie_poster;


            public MyHolder(View itemView) {
                super(itemView);
                movie_poster = (ImageView) itemView.findViewById(R.id.grid_item_image);
                movie_poster.setOnClickListener(this);

            }


            @Override
            public void onClick(View view) {
                Movie movie = movies.get(getPosition());
                if ((getResources().getConfiguration().screenLayout &
                        Configuration.SCREENLAYOUT_SIZE_MASK) >=
                        Configuration.SCREENLAYOUT_SIZE_LARGE) {

                    getSupportFragmentManager().beginTransaction().replace(R.id.details_container_tab, new DetailFragment(movie, local)).commit();

                } else {
                    Intent intent = new Intent(MainActivity.this, DetailActivity.class);

                    intent.putExtra("movie", (Parcelable) movie);
                    intent.putExtra("local", local);

                    startActivity(intent);
                }


            }
        }
    }


}

