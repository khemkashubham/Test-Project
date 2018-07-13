package com.testapp.myapplication;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.testapp.myapplication.adapters.RepoAdapter;
import com.testapp.myapplication.models.RepoItems;
import com.testapp.myapplication.utils.ApiInterface;
import com.testapp.myapplication.utils.RetrofitManager;
import com.testapp.myapplication.utils.UtilsManager;
import com.testapp.myapplication.utils.VerticalSpaceDecoration;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements Callback<List<RepoItems>> {

    private static final String TAG = MainActivity.class.getSimpleName();


    private ApiInterface apiInterface;

    private RepoAdapter repoAdapter;
    private List<RepoItems> repoItemsList;

    private boolean isLoading = false, isDataAvailable = false, isOnline = false;

    private int page = 0;

    private ProgressBar pbCentre, pbBottom;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initClass();
    }

    private void initClass() {

        repoItemsList = new ArrayList<>();
        repoAdapter = new RepoAdapter(repoItemsList);

        realm = Realm.getDefaultInstance();

        apiInterface = RetrofitManager.getInstance().create(ApiInterface.class);
    }


    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        RecyclerView rvRepoItems = findViewById(R.id.rvRepo);

        pbCentre = findViewById(R.id.pbCentre);

        pbBottom = findViewById(R.id.pbBottom);


        rvRepoItems.setLayoutManager(new LinearLayoutManager(this));
        rvRepoItems.addItemDecoration(new VerticalSpaceDecoration(2));
        rvRepoItems.setAdapter(repoAdapter);


        rvRepoItems.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (dy > 0) {

                    LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();


                    int visibleItemCount = linearLayoutManager.getChildCount();
                    int totalItemCount = linearLayoutManager.getItemCount();
                    int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();

                    if (isOnline) {
                        if (isDataAvailable) {
                            if (!isLoading && (visibleItemCount + firstVisibleItemPosition) >= totalItemCount - 4
                                    && firstVisibleItemPosition >= 0) {
                                isLoading = true;
                                initAPI();
                            }
                        } else {
                            Toast.makeText(MainActivity.this, "No more Repo", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

            }
        });


        getData();
    }

    private void getData() {

        if (UtilsManager.isNetworkConnected(this)) {
            isOnline = true;
            initAPI();
        } else {
            Toast.makeText(this, "Loading Offline Data", Toast.LENGTH_SHORT).show();
            isOnline = false;
            RealmResults<RepoItems> allAsync = realm.where(RepoItems.class).findAll();
            if (allAsync.size() > 0) {
                repoItemsList.addAll(allAsync);
                repoAdapter.notifyDataSetChanged();
                pbCentre.setVisibility(View.GONE);
                pbBottom.setVisibility(View.GONE);

            } else {
                Toast.makeText(this, "No Offline Data Found", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void initAPI() {

        page++;
        if (page == 1) {
            pbCentre.setVisibility(View.VISIBLE);
        } else
            pbBottom.setVisibility(View.VISIBLE);

        Call<List<RepoItems>> repoList = apiInterface.getRepoList(String.valueOf(page), "15");

        repoList.enqueue(this);

    }

    @Override
    public void onResponse
            (@NonNull Call<List<RepoItems>> call, @NonNull Response<List<RepoItems>> response) {

//        Log.i("CALL RESPONSE:", "" + call.request().url());


        pbBottom.setVisibility(View.GONE);
        pbCentre.setVisibility(View.GONE);


        if (response.isSuccessful()) {
            List<RepoItems> repoItems = response.body();

            if (repoItems != null) {
                isDataAvailable = repoItems.size() >= 15;
                repoItemsList.addAll(repoItems);
                saveItemsInLocalDatabase(repoItems);
            }
            repoAdapter.notifyDataSetChanged();
            isLoading = false;
        }

    }


    @Override
    public void onFailure(@NonNull Call<List<RepoItems>> call, @NonNull Throwable t) {

        pbBottom.setVisibility(View.GONE);
        pbCentre.setVisibility(View.GONE);

//        Log.i("CALL FAILURE:", "" + call.request().url());
//        Log.i("THROWABLE: ", "" + t);

    }


    private void saveItemsInLocalDatabase(List<RepoItems> repoItems) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(repoItems);
        realm.commitTransaction();

    }
}
