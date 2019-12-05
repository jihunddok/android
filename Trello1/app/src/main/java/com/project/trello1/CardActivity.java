package com.project.trello1;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Vector;
import java.util.concurrent.ExecutionException;

import ch.boye.httpclientandroidlib.HttpEntity;
import ch.boye.httpclientandroidlib.HttpResponse;
import ch.boye.httpclientandroidlib.NameValuePair;
import ch.boye.httpclientandroidlib.client.HttpClient;
import ch.boye.httpclientandroidlib.client.entity.UrlEncodedFormEntity;
import ch.boye.httpclientandroidlib.client.methods.HttpPost;
import ch.boye.httpclientandroidlib.impl.client.DefaultHttpClient;
import ch.boye.httpclientandroidlib.message.BasicNameValuePair;
import ch.boye.httpclientandroidlib.protocol.HTTP;

public class CardActivity extends AppCompatActivity {

    String boardCode;
    String boardName;

    private ViewPager viewPager;
    private CardPagerAdapter cardPagerAdapter;
    private ShadowTransformer shadowTransformer;

    private boolean mShowingFragments = false;

    CardItem cardItem;

    String getMsg = ""; // 서버로 부터 전달받는 데이터
    InputStream is = null; // JSON INPUTSTREAM
    AsyncTask_C asyncTask_c = null; // Asynctask 클래스를 사용하기 위한 변수

    @Override
    public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);

        Intent intent = getIntent();
        boardCode = intent.getExtras().getString("boardCode");
        boardName = intent.getExtras().getString("boardName");
        setTitle(boardName);

        viewPager = findViewById(R.id.viewPager);

        cardPagerAdapter = new CardPagerAdapter();
//        cardPagerAdapter.addCardItem(new CardItem("되라", false));
//        cardPagerAdapter.addCardItem(new CardItem("되라", false));
//        cardPagerAdapter.addCardItem(new CardItem("되라", false));
//        cardPagerAdapter.addCardItem(new CardItem("되라", false));
//        cardPagerAdapter.addCardItem(new CardItem("되라", false));
//        cardPagerAdapter.addCardItem(new CardItem("되라", false));

        asyncTask_c = new AsyncTask_C();
        asyncTask_c.execute();

        shadowTransformer = new ShadowTransformer(viewPager, cardPagerAdapter);

        viewPager.setAdapter(cardPagerAdapter);
        viewPager.setPageTransformer(false, shadowTransformer);
        viewPager.setOffscreenPageLimit(3);



        //shadowTransformer.enableScaling(false);

//        cardPagerAdapter = new CardPagerAdapter();
//        cardView = findViewById(R.id.cardView);
//        cardView.setAdapter(cardPagerAdapter);
//
//        asyncTask_c = new AsyncTask_C();
//        asyncTask_c.execute();

    }

    class AsyncTask_C extends AsyncTask<String, Integer, String> {

         AsyncTask_C asyncTask_c;

        @Override
        protected String doInBackground(String... strings) {
            try {
                HttpPost httpPost = new HttpPost("http://113.198.235.225/card.php");

                // 전달할 인자
                Vector<NameValuePair> nameValue = new Vector<NameValuePair>();
                nameValue.add(new BasicNameValuePair("board_code", boardCode));

                // 웹 접속 - utf-8
                HttpEntity enty = new UrlEncodedFormEntity(nameValue, HTTP.UTF_8);
                httpPost.setEntity(enty);

                HttpClient httpClient = new DefaultHttpClient();
                HttpResponse response = httpClient.execute(httpPost);

                // 아래 코드부터는 요청에 대한 응답을 받아와서 처리하는 코드
                HttpEntity entity = response.getEntity();
                is = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));

                String line = ""; // 결과를 한 줄씩 읽어서 저장할 변수
                getMsg = ""; // getMsg 변수 초기화 (리스트 갱신 등의 이우로 기존의 값이 남아 있지 않도록 하기위해)

                while((line = reader.readLine()) != null){
                    getMsg = getMsg + line;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return getMsg;
        }

        @Override
        protected void onPostExecute(String _result) {
            try {

                JSONObject root = new JSONObject(_result);
                JSONArray results = new JSONArray(root.getString("results"));

                for(int i = 0; i < results.length(); i++){
                    JSONObject content = results.getJSONObject(i);
                    String cardCode = content.getString("card_code");
                    String cardName = content.getString("card_name");
                    String finish = content.getString("finish");

                    cardPagerAdapter.addCardItem(cardCode, cardName, finish);
                    cardPagerAdapter.notifyDataSetChanged();
                }
                asyncTask_c = null;

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}