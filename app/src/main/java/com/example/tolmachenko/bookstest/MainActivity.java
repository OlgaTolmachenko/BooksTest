package com.example.tolmachenko.bookstest;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.widget.SearchView;

import com.example.tolmachenko.bookstest.model.Book;
import com.example.tolmachenko.bookstest.model.CustomResponse;
import com.example.tolmachenko.bookstest.model.Item;
import com.example.tolmachenko.bookstest.util.BooksAdapter;
import com.example.tolmachenko.bookstest.util.Constants;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Realm.init(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        searchView = createSearchView(menu);
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        layoutManager = new StaggeredGridLayoutManager(getColumnsNum(), StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                try {
                    String q = URLEncoder.encode(query, "UTF-8");
                    deleteAllFromRealm();
                    getBooks(q, 0);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d(TAG, "handleIntent: onQueryTextChange" + newText);
                return false;
            }
        });

        searchView.getQuery();
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

    private void getBooks(String query, int startIndex) {
        Call<CustomResponse> call = createService().getBooks(query, startIndex, BuildConfig.apiKey);
        call.enqueue(new Callback<CustomResponse>() {
            @Override
            public void onResponse(Call<CustomResponse> call, Response<CustomResponse> response) {
                Log.d(TAG, "RESPONSE CODE:" + response.code() + " " + response.message());
                if (response != null) {
                    List<Item> items = response.body().getItems();
                    Log.d(TAG, "Items size: " + items.size());

                    final Book book = new Book();

                    for (int i = 0; i < items.size(); i++) {
                        Item currentItem = items.get(i);

                        String title = currentItem.getVolumeInfo().getTitle();
                        String infoLink = currentItem.getVolumeInfo().getInfoLink();
                        String thumbnail = currentItem.getVolumeInfo().getImageLinks().getThumbnail();

                        book.setTitle(title);
                        book.setInfoLink(infoLink);
                        book.setThumbnail(thumbnail);

                        getRealmInstance().executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                realm.copyToRealm(book);
                            }
                        });
                    }
                    testRealm();
                    adapter = new BooksAdapter(MainActivity.this, getBooksList());
                    recyclerView.setAdapter(adapter);
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
    protected void onStop() {
        super.onStop();
        getRealmInstance().close();
    }

    private SearchView createSearchView(Menu menu) {
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        SearchableInfo searchableInfo = searchManager.getSearchableInfo(getComponentName());
        searchView.setSearchableInfo(searchableInfo);
        searchView.setIconified(true);
        return searchView;
    }

    private void testRealm() {
        int size = getRealmInstance().where(Book.class).findAll().size();
        Log.d(TAG, "testRealm: size: " + size);
    }

    private RealmResults<Book> getBooksList() {
        return getRealmInstance().where(Book.class).findAll();
    }

    private Realm getRealmInstance() {
        RealmConfiguration realmConfig = new RealmConfiguration.Builder()
                .name("myrealm.realm")
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
}
