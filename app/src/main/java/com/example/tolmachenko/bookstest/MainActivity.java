package com.example.tolmachenko.bookstest;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.widget.SearchView;

import com.example.tolmachenko.bookstest.model.Book;
import com.example.tolmachenko.bookstest.model.CustomResponse;
import com.example.tolmachenko.bookstest.model.Item;
import com.example.tolmachenko.bookstest.util.BooksAdapter;
import com.example.tolmachenko.bookstest.util.Constants;
import com.example.tolmachenko.bookstest.util.EndlessRecyclerViewScrollListener;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private final String TAG = this.getClass().getSimpleName();

    private SearchView searchView;
    private RecyclerView recyclerView;
    private BooksAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private String lastQuery;
    private SwipeRefreshLayout refreshLayout;
    private boolean orientationChange = false;

    List<Book> receivedBooks = new ArrayList<>();

    private int startIndex = 0;
    private final int maxResults = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Realm.init(this);

        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        layoutManager = new StaggeredGridLayoutManager(getColumnsNum(), StaggeredGridLayoutManager.VERTICAL);
        adapter = new BooksAdapter(MainActivity.this, receivedBooks);

        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(setupScrollListener());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        searchView = createSearchView(menu);
        searchView.clearFocus();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                lastQuery = query;
                receivedBooks.clear();
                deleteAllFromRealm();

                try {
                    if (!TextUtils.isEmpty(lastQuery)) {
                        getBooks(URLEncoder.encode(lastQuery, Constants.ENCODING), startIndex, maxResults);
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        refreshLayout.setOnRefreshListener(setupRefreshListener());

        if (!TextUtils.isEmpty(lastQuery)) {
            searchView.setQuery(lastQuery, false);
        }

        return true;
    }

    private Retrofit buildRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(Constants.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    private BooksApiInterface createService() {
        return buildRetrofit().create(BooksApiInterface.class);
    }

    private void getBooks(final String query, int startIndex, int maxResults) {

        Call<CustomResponse> call = createService().getBooks(query, startIndex, maxResults, BuildConfig.apiKey);
        call.enqueue(new Callback<CustomResponse>() {
            @Override
            public void onResponse(Call<CustomResponse> call, Response<CustomResponse> response) {
                Log.d(TAG, "RESPONSE CODE:" + response.code() + " " + response.message());
                if (response != null) {
                    List<Item> items = response.body().getItems();
                    Log.d(TAG, "Items size: " + items.size());

                    if (!lastQuery.equals(query)) {
                        deleteAllFromRealm();
                        receivedBooks.clear();
                        adapter.notifyDataSetChanged();
                    }

                    for (int i = 0; i < items.size(); i++) {
                        Item currentItem = items.get(i);

                        String title = currentItem.getVolumeInfo().getTitle();
                        String infoLink = currentItem.getVolumeInfo().getInfoLink();
                        String thumbnail = currentItem.getVolumeInfo().getImageLinks().getThumbnail();

                        final Book book = new Book();
                        book.setTitle(title);
                        book.setInfoLink(infoLink);
                        book.setThumbnail(thumbnail);

                        getRealmInstance().executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                realm.copyToRealmOrUpdate(book);
                            }
                        });

                        receivedBooks.add(book);
                        adapter.notifyDataSetChanged();
                    }
                    Log.d(TAG, "ADAPTER COUNT: " + adapter.getItemCount());
                    testRealm();
                }
            }

            @Override
            public void onFailure(Call<CustomResponse> call, Throwable t) {
                Log.d(TAG, "call object: " + call);
                Log.d(TAG, "error message: " + t.getMessage());
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!orientationChange) {
            getRealmInstance().close();
        }
    }

    private SearchView createSearchView(Menu menu) {
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        SearchableInfo searchableInfo = searchManager.getSearchableInfo(getComponentName());
        searchView.setSearchableInfo(searchableInfo);
        searchView.setIconified(false);
        return searchView;
    }

    private void testRealm() {
        int size = getRealmInstance().where(Book.class).findAll().size();
        Log.d(TAG, "testRealm: size: " + size);
    }

    private RealmResults<Book> getRealmResults() {
        return getRealmInstance().where(Book.class).findAll();
    }

    private Realm getRealmInstance() {
        RealmConfiguration realmConfig = new RealmConfiguration.Builder()
                .name(Constants.REALM_NAME)
                .inMemory()
                .build();
        return Realm.getInstance(realmConfig);
    }

    private boolean deleteAllFromRealm() {
        getRealmInstance().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.deleteAll();
            }
        });
        return true;
    }

    private int getColumnsNum() {
        int orientation = getResources().getConfiguration().orientation;
        int columnsNum = orientation == Configuration.ORIENTATION_LANDSCAPE ? 3 : 2;
        return columnsNum;
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(Constants.QUERY, lastQuery);
        orientationChange = true;
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            lastQuery = savedInstanceState.getString(Constants.QUERY);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (receivedBooks != null) {
            adapter.loadNewData(getRealmResults());
        }
    }

    private SwipeRefreshLayout.OnRefreshListener setupRefreshListener() {
        return new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!TextUtils.isEmpty(lastQuery)) {
                    receivedBooks.clear();
                    deleteAllFromRealm();
                    getBooks(lastQuery, startIndex, maxResults);
                    refreshLayout.setRefreshing(false);
                }
            }
        };
    }

    private EndlessRecyclerViewScrollListener setupScrollListener() {
        return new EndlessRecyclerViewScrollListener((StaggeredGridLayoutManager) layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                getBooks(lastQuery, totalItemsCount, maxResults);
            }
        };
    }
}

