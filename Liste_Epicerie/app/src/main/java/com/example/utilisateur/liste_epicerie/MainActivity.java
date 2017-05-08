package com.example.utilisateur.liste_epicerie;

import android.content.Context;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    CustomProgressDialog progressDialog;
    Button ajout,afficher ;
    ListView listview;

    EditText article;
    String urladd="http://192.168.2.38/enis_android_club/";
    AddDataAsyncTask AddData;
    ArrayList<Article> listeArticle = new ArrayList<Article>();
    String message;
    int success;
    int categorie=8;
    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);


        spinner = (Spinner) findViewById(R.id.spinnerCategorie);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.spinnerCategorie,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        ajout=(Button)findViewById(R.id.BoutonAjouter);
        afficher =(Button)findViewById(R.id.BoutonAfficher);
        article = (EditText)findViewById(R.id.EditArticle);
        listview = (ListView)findViewById(R.id.LVAffichage);
        listview.setClickable(true);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                Article a =(Article) listview.getItemAtPosition(position);
                try{
                    ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                    nameValuePairs.add(new BasicNameValuePair("8","10"));
                    HttpClient httpclient = new DefaultHttpClient();
                    HttpPost httppost = new HttpPost(urladd+"suppression_bd.php?idArticle="+a.getIdArticle());
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                    HttpResponse response = httpclient.execute(httppost);
                    HttpEntity entity = response.getEntity();
                    InputStream is = entity.getContent();
                }catch(Exception e){
                    Log.e("log_tag", "Error in http connection "+e.toString());
                }
            }
        });
        AddData = new AddDataAsyncTask();
        ajout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                categorie = switchSpinner();


                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        ajouter();

                    }
                }).start();


            }
        });

    }
    public int switchSpinner(){
        int variable = spinner.getSelectedItemPosition();
        switch (variable){
            case 0:variable= 8;
                break;
            case 1:variable= 9;
                break;
            case 2:variable= 10;
                break;
            case 3:variable= 11;
                break;
        }
        return variable;
    }
    public void ajouter(){
        String resultat = "";
        String[] liste =  article.getText().toString().split(" ");
        listeArticle = new ArrayList<Article>();

        resultat = liste[0];
        for (int i = 1;i<liste.length;i++){
            resultat+="_"+liste[i];
        }
        try{
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("8","10"));
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(urladd+"ajout_bd.php?nomArticle="+resultat+"&idCategorie="+categorie);
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            InputStream is = entity.getContent();
        }catch(Exception e){
            Log.e("log_tag", "Error in http connection "+e.toString());
        }
    }
    public void Afficher(View view){
        recevoir();
        ArticleAdapter adap = new ArticleAdapter(this, listeArticle);
        listview.setAdapter(adap);
    }
    public void recevoir(){
        try {
            String result = "";
            URL url = new URL(urladd+"affichage_bd.php");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            listeArticle = new ArrayList<Article>();
            while ((line = rd.readLine()) != null) {
                JSONObject obj = new JSONObject(line);
                JSONArray valeurs = obj.getJSONArray("valeurs");
                for (int i = 0;i<valeurs.length();i++){
                    Article article = new Article();
                    article.setNomArticle(valeurs.getJSONObject(i).getString("nomArticle"))  ;
                    article.setNomCategorie(valeurs.getJSONObject(i).getString("nomCategorie"));
                    article.setIdArticle(valeurs.getJSONObject(i).getString("idArticle"));
                    article.setIdCategorie(valeurs.getJSONObject(i).getString("idCategorie"));
                    listeArticle.add(article);
                }
            }
            rd.close();
        }catch (Exception e){
            article.setText(e.getMessage());

        }
    }

}




 class ArticleAdapter extends ArrayAdapter<Article> {
    ArrayList<Article> donnee;
    Context context;

    public ArticleAdapter(Context context, ArrayList<Article> donnee){
        super(context, R.layout.activity_main, donnee);
        this.context = context;
        this.donnee = donnee;
    }

    private static class ViewHolder {
        TextView txtArticle;
        TextView txtCategorie;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ArticleAdapter.ViewHolder viewHolder;
        Article donnee = getItem(position);

        View result;

        if (convertView == null) {

            viewHolder = new ArticleAdapter.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.lvarticle, parent, false);
            viewHolder.txtArticle = (TextView) convertView.findViewById(R.id.TArticle);
            viewHolder.txtCategorie = (TextView) convertView.findViewById(R.id.TCategorie);

            result = convertView;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ArticleAdapter.ViewHolder) convertView.getTag();
            result = convertView;
        }

        viewHolder.txtArticle.setText("" + donnee.getNomArticle());
        viewHolder.txtCategorie.setText("-" + donnee.getNomCategorie());

        return convertView;
    }
}
