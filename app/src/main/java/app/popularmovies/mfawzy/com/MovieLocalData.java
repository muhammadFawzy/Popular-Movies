package app.popularmovies.mfawzy.com;

import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.net.Uri;

import java.util.ArrayList;

import edu.mit.mobile.android.content.ContentItem;
import edu.mit.mobile.android.content.ProviderUtils;
import edu.mit.mobile.android.content.column.DBColumn;
import edu.mit.mobile.android.content.column.DoubleColumn;
import edu.mit.mobile.android.content.column.IntegerColumn;
import edu.mit.mobile.android.content.column.TextColumn;

/**
 * Created by mohamed on 28/03/16.
 */
public class MovieLocalData implements ContentItem {

    Context context;

    public MovieLocalData(Context context) {
        this.context = context;
    }

    @DBColumn(type = IntegerColumn.class)
    public static final String COL_ID = "id";

    @DBColumn(type = TextColumn.class)
    public static final String COL_TITLE = "title";

    @DBColumn(type = TextColumn.class)
    public static final String COL_POSTER = "poster_image";

    @DBColumn(type = TextColumn.class)
    public static final String COL_BACK_POSTER = "back_image";

    @DBColumn(type = TextColumn.class)
    public static final String COL_OVERVIEW = "overview";

    @DBColumn(type = TextColumn.class)
    public static final String COL_REATING = "rating";

    @DBColumn(type = DoubleColumn.class)
    public static final String COL_DATE = "date";


    public static final String PATH = "movie";
    public static final Uri CONTENT_URI = ProviderUtils.toContentUri(
            SampleProvider.AUTHORITY, PATH);

    public static final String CONTENT_TYPE_ITEM = "vnd.android.cursor.item/vnd.com.mfawzy.popularmovies.app.sampleprovider.movielocaldata";

    public static final String CONTENT_TYPE_DIR = "vnd.android.cursor.dir/vnd.com.mfawzy.popularmovies.app.sampleprovider.movielocaldata";

    public ArrayList<Movie> getData() {

        ArrayList<Movie> movies = new ArrayList<>();

        String[] PROJECTION = {COL_ID, COL_TITLE, COL_OVERVIEW, COL_POSTER, COL_BACK_POSTER, COL_DATE, COL_REATING};

        CursorLoader cursorloader = new CursorLoader(context, CONTENT_URI, PROJECTION, null, null, null);
        Cursor cursor = cursorloader.loadInBackground();


        while (cursor.moveToNext()) {

            Movie movie = new Movie();
            movie.setId(cursor.getInt(cursor.getColumnIndex(COL_ID)));
            movie.setTitle(cursor.getString(cursor.getColumnIndex(COL_TITLE)));
            movie.setImage(cursor.getString(cursor.getColumnIndex(COL_POSTER)));
            movie.setImage2(cursor.getString(cursor.getColumnIndex(COL_BACK_POSTER)));
            movie.setDate(cursor.getString(cursor.getColumnIndex(COL_DATE)));
            movie.setOverview(cursor.getString(cursor.getColumnIndex(COL_OVERVIEW)));
            movie.setRating(cursor.getDouble(cursor.getColumnIndex(COL_REATING)));
            movies.add(movie);

        }


        return movies;

    }

    public void insertData(int ID, String Title, String image, String image2, String date, String overview, Double rating) {


        ContentValues values = new ContentValues();
        values.put(COL_ID, ID);
        values.put(COL_TITLE, Title);
        values.put(COL_POSTER, image);
        values.put(COL_BACK_POSTER, image2);
        values.put(COL_DATE, date);
        values.put(COL_OVERVIEW, overview);
        values.put(COL_REATING, rating);
        Uri newUri = context.getContentResolver().insert(CONTENT_URI, values);


    }


}
