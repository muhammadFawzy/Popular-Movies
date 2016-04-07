package app.popularmovies.mfawzy.com;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


         if (savedInstanceState == null) {

        getSupportFragmentManager().beginTransaction().add(R.id.details_container, new DetailFragment(
                getIntent().getParcelableExtra("movie"), getIntent().getExtras().getBoolean("local"))).commit();

         }
    }
}
