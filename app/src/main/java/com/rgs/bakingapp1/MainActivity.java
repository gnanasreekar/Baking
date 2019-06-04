package com.rgs.bakingapp1;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    Main_adap main_adap;
    RequestQueue requestQueue;
    @BindView(R.id.recycler_items)
    RecyclerView recyclerItems;
    private Boolean file_save;
    private ArrayList<POJO> list;
    private Gson gson;
    private static final String FILE_NAME = "json.txt";
    private List<POJO.IngredientsBean> ingredientsBeanList;
    private List<POJO.StepsBean> stepsBeans;
    SharedPreferences pref ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        requestQueue = Volley.newRequestQueue(this);
        main_adap = new Main_adap(MainActivity.this);
        getdata();
        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        file_save = pref.getBoolean("saved" , false);



    }








    private void getdata() {
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, getString(R.string.url), null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if(!file_save){
                    json(response);
                }
                savefile(response);
                loadfile();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();

            }
        });
        requestQueue.add(request);
    }

    public void json(JSONArray response)
    {
        list = new ArrayList<>();
        try {
            for (int i = 0; i < response.length(); i++) {
                JSONObject recipeObject = response.getJSONObject(i);
                Gson gson = new GsonBuilder().create();
                POJO r = gson.fromJson(String.valueOf(recipeObject), POJO.class);
                Log.d("whatever", r.getName());
                list.add(r);
            }
            main_adap.setList(list);
            recyclerItems.setAdapter(main_adap);
            recyclerItems.setLayoutManager(new GridLayoutManager(MainActivity.this, 1));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public boolean savefile(JSONArray response) {

        String save_file = response.toString();
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = openFileOutput(FILE_NAME, MODE_PRIVATE);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            fileOutputStream.write(save_file.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        pref.edit().putBoolean("saved" , true).apply();

        Toast.makeText(this, "saved to" + getFilesDir() + "/" + FILE_NAME, Toast.LENGTH_SHORT).show();
        return true;

    }

    public void loadfile() {
        FileInputStream fileInputStream;
        try {
            fileInputStream = openFileInput(FILE_NAME);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder stringBuilder = new StringBuilder();
            String json_file;
            while ((json_file = bufferedReader.readLine()) != null) {
                stringBuilder.append(json_file);
            }
            String file_final = stringBuilder.toString();
            if(file_save)
            {
                JSONArray data = new JSONArray(file_final);
                json(data);
                Toast.makeText(this, "Offilne data", Toast.LENGTH_SHORT).show();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

//    public void parse(String data)
//    {
//        JSONArray jsonArr = null;
//        try {
//            jsonArr = new JSONArray(data);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        for (int i = 0; i < jsonArr.length(); i++)
//            {
//                JSONObject jsonObj = null;
//                try {
//                    jsonObj = jsonArr.getJSONObject(i);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                json(jsonObj);
//            }
//    }





    public Activity getActivity() {
        Context context = this;
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity) context;
            }
            context = ((ContextWrapper) context).getBaseContext();
        }
        return null;
    }
}

