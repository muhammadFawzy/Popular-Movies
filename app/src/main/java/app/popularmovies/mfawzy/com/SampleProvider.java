package app.popularmovies.mfawzy.com;

import android.net.Uri;

import edu.mit.mobile.android.content.GenericDBHelper;
import edu.mit.mobile.android.content.ProviderUtils;
import edu.mit.mobile.android.content.QuerystringWrapper;
import edu.mit.mobile.android.content.SimpleContentProvider;
import edu.mit.mobile.android.content.dbhelper.SearchDBHelper;

/**
 * Created by mohamed on 28/03/16.
 */
public class SampleProvider extends SimpleContentProvider {

    public static final String AUTHORITY = "app.popularmovies.mfawzy.com.sampleprovider";
    public static final String SEARCH_PATH = null;
    public static final Uri SEARCH = ProviderUtils.toContentUri(AUTHORITY,
            getSearchPath(SEARCH_PATH));

    private static final int DB_VERSION = 1;

    public SampleProvider() {
        super(AUTHORITY, DB_VERSION);

        final GenericDBHelper movieHelper = new GenericDBHelper(MovieLocalData.class);

        final QuerystringWrapper queryWrapper = new QuerystringWrapper(movieHelper);

        addDirAndItemUri(queryWrapper, MovieLocalData.PATH);


        final SearchDBHelper searchHelper = new SearchDBHelper();

        searchHelper.registerDBHelper(movieHelper, MovieLocalData.CONTENT_URI, MovieLocalData.COL_ID,
                MovieLocalData.COL_TITLE, MovieLocalData.COL_POSTER,
                MovieLocalData.COL_BACK_POSTER, MovieLocalData.COL_OVERVIEW,
                MovieLocalData.COL_DATE, MovieLocalData.COL_REATING);

        addSearchUri(searchHelper, SEARCH_PATH);
    }
}
