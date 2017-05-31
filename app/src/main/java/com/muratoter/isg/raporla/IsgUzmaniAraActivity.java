package com.muratoter.isg.raporla;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.mateware.snacky.Snacky;

public class IsgUzmaniAraActivity extends AppCompatActivity implements SearchView.OnQueryTextListener{
    private ProgressBar progressBar;
    private List<Kullanicilar> kullanicilarList=new ArrayList<>();
    private IsgSearchAdapter adapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_isg_uzmani_ara);
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar_search);
        setSupportActionBar(toolbar);
        recyclerView=(RecyclerView)findViewById(R.id.recycler_view_isg_search);
        progressBar=(ProgressBar)findViewById(R.id.progressLoad);

        BackTask backTask=new BackTask(getApplicationContext());
        backTask.execute();

        adapter=new IsgSearchAdapter(this,kullanicilarList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(true);

        //calismiyor
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(),LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //
        recyclerView.setAdapter(adapter);


    }



    //Departman sorumlusunun eşleşme isteği gönderebileceği İSG uzmanların listesini gösterir.
    private class BackTask extends AsyncTask<Void,Kullanicilar,Void>{
        Context ctx;
        public BackTask(Context ctx){
            this.ctx=ctx;
        }

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            String json_string=AppConfig.BaseURL+"isg_uzmanlari.php";
            try {
                URL url=new URL(json_string);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream ınputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(ınputStream));
                StringBuilder stringBuilder = new StringBuilder();
                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line + "\n");
                }

                httpURLConnection.disconnect();
                ınputStream.close();

                //servisten dönen bilgileri kullanma
                String json_str = stringBuilder.toString().trim();
                JSONObject jsonObject = new JSONObject(json_str);
                JSONArray jsonArray = jsonObject.getJSONArray("server_response");

                int count = 0;
                while (count < jsonArray.length()) {
                    JSONObject JO = jsonArray.getJSONObject(count);
                    count++;

                    Kullanicilar kullanicilar=new Kullanicilar(JO.getInt("id"),JO.getString("adsoyad"),JO.getString("eposta"),JO.getString("sifre"),JO.getString("durum"));

                    publishProgress(kullanicilar);

                }

                Log.d("JSON STRING", json_str);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Kullanicilar... values) {
            kullanicilarList.add(values[0]);
            adapter.notifyDataSetChanged();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu,menu);
        MenuItem menuItem=menu.findItem(R.id.search);
        SearchView searchView=(SearchView) MenuItemCompat.getActionView(menuItem);
        menuItem.expandActionView();
        MenuItemCompat.setOnActionExpandListener(menuItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                finish();
                return true;
            }
        });
        searchView.setOnQueryTextListener(this);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    //isg uzmanı arama ve listeyi güncelleme
    @Override
    public boolean onQueryTextChange(String newText) {
        newText=newText.toLowerCase();
        ArrayList<Kullanicilar> list=new ArrayList<>();
        for(Kullanicilar kullanicilar:kullanicilarList){
            String kullaniciAdi=kullanicilar.getKullaniciAdSoyad().toLowerCase();
            if(kullaniciAdi.contains(newText))
                list.add(kullanicilar);
        }
        adapter.setFilter(list);
        return true;
    }


}
