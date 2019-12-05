package com.project.trello1;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Vector;

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

    // board에서 받아올 변수
    String boardCode;
    String boardName;
    String finish;
    String cardName;
    // 해당 cardCode에 연결된 task를 불러온다
    String cardCode;

    // 카드의 내용에 해당하는 task
    ListView listView;
    TaskAdapter taskAdapter;
    AsyncTask_C asyncTask_c = null;

    // 뷰페이져에 카드 등록해주는 어댑터
    private ViewPager viewPager;
    private ViewPager viewPager2;

    private CardPagerAdapter cardPagerAdapter;
    private CardPagerAdapter cardPagerAdapter2;
    private ShadowTransformer shadowTransformer;
    private ShadowTransformer shadowTransformer2;

    String getMsg = ""; // 서버로 부터 전달받는 데이터
    InputStream is = null; // JSON INPUTSTREAM
    AsyncTask_V asyncTask_v = null; // Asynctask 클래스를 사용하기 위한 변수

    @Override
    public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);

        // boardActivity에서 boardCode와 boardName을 가져옴
        Intent intent = getIntent();
        boardCode = intent.getExtras().getString("boardCode");
        boardName = intent.getExtras().getString("boardName");
        setTitle(boardName);

        // 뷰페이져 등록, 어댑터 객체 생성
        viewPager = findViewById(R.id.viewPager);
        cardPagerAdapter = new CardPagerAdapter();
        viewPager2 = findViewById(R.id.viewPager2);
        cardPagerAdapter2 = new CardPagerAdapter();

        // 데이터베이스에 존재하는 카드를 가져옴
        asyncTask_v = new AsyncTask_V();
        asyncTask_v.execute();

        // 뷰페이져에 setAdapter 등록, 그림자, 크기, 한번에 나올 숫자 조절
        shadowTransformer = new ShadowTransformer(viewPager, cardPagerAdapter);
        viewPager.setAdapter(cardPagerAdapter);
        viewPager.setPageTransformer(false, shadowTransformer);
        viewPager.setOffscreenPageLimit(3);

        shadowTransformer2 = new ShadowTransformer(viewPager2, cardPagerAdapter2);
        viewPager2.setAdapter(cardPagerAdapter2);
        viewPager2.setPageTransformer(false, shadowTransformer2);
        viewPager2.setOffscreenPageLimit(3);

        /* 카드에 들고올 task 시작 */
//        taskAdapter = new TaskAdapter();
//        setContentView(R.layout.viewpager_adapter);
//        listView.findViewById(R.id.lv_task);

//        listView.setAdapter(taskAdapter);
//        asyncTask_c = new AsyncTask_C();
//        asyncTask_c.execute();

    }

    class AsyncTask_V extends AsyncTask<String, Integer, String> {

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
                    cardCode = content.getString("card_code");
                    cardName = content.getString("card_name");
                     finish = content.getString("finish");

                    if(finish.equals("0")){
                        cardPagerAdapter.addCardItem(cardCode, cardName, finish);
                        cardPagerAdapter.notifyDataSetChanged();
                    }
                    else{
                        cardPagerAdapter2.addCardItem(cardCode,cardName,finish);
                        cardPagerAdapter2.notifyDataSetChanged();
                    }
                }
                asyncTask_v = null;

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    class AsyncTask_C extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            try {
                HttpPost httpPost = new HttpPost("http://113.198.235.225/card.php");

                // 전달할 인자
                Vector<NameValuePair> nameValue = new Vector<NameValuePair>();
                nameValue.add(new BasicNameValuePair("card_code", cardCode));

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
                    String taskName = content.getString("task_name");
                    String dueTo = content.getString("due_to");

                    taskAdapter.addItem(taskName, dueTo);
                    taskAdapter.notifyDataSetChanged();
                }
                asyncTask_c = null;

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

}