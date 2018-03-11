package com.example.pablo.mycarpet;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import org.reactivestreams.Subscription;

import java.io.IOException;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements CountryAdapter.ListItemClickListener {
    private RecyclerView recyclerView;
    CountryPopulationRank list;
private Disposable disposable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.list);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));

        getData();
    }
public void onDestroy() {

    super.onDestroy();
    if(disposable==null || !disposable.isDisposed()){
        assert disposable != null;
        disposable.dispose();
    }
}
    public void getData() {
        disposable = APIAccess.getApiService()
                    .getCountryList()
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<CountryPopulationRank>() {
                        @Override
                        public void accept(CountryPopulationRank worldpopulations) throws Exception {
                            list=worldpopulations;
                            recyclerView.setAdapter(new CountryAdapter(MainActivity.this, worldpopulations.getWorldpopulation(), MainActivity.this));
                        }
                    }
                    );



    }

    @Override
    public void onListItemClick(int clickedItemIndex) throws IOException {
        Worldpopulation worldpopulation = list.getWorldpopulation().get(clickedItemIndex);
        Intent intent = new Intent(MainActivity.this, Detail_Activity.class);
        intent.putExtra("image_URL", worldpopulation.getFlag());
        intent.putExtra("country_name", worldpopulation.getCountry());
        startActivity(intent);
    }
}
