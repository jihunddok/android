package com.project.trello1;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

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

public class BoardActivity extends AppCompatActivity {

    ListView listview;
    BoardAdapter boardAdapter;
    String getMsg = ""; // 서버로 부터 전달받는 데이터
    InputStream is = null; // JSON INPUTSTREAM
    AsyncTask_B asyncTask_b = null; // Asynctask 클래스를 사용하기 위한 변수

    String teamCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        Intent intent = getIntent();
        teamCode = intent.getStringExtra("teamCode");
        String teamName = intent.getStringExtra("teamName");
        String position = intent.getStringExtra("position");
        setTitle(teamName);

        boardAdapter = new BoardAdapter();
        listview = findViewById(R.id.lv_board);

        listview.setAdapter(boardAdapter);
        asyncTask_b = new AsyncTask_B();
        asyncTask_b.execute();

        // 리스트뷰 선택했을 때 이벤트 board -> card 목록
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                TextView boardCode = view.findViewById(R.id.boardCode);
                TextView boardName = view.findViewById(R.id.boardName);
                // Toast.makeText(BoardActivity.this, boardCode.getText(), Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getApplicationContext(), CardActivity.class);
                intent.putExtra("boardCode", boardCode.getText());
                intent.putExtra("boardName", boardName.getText());

                startActivity(intent);
            }
        });

    }

    class AsyncTask_B extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            try {
                HttpPost httpPost = new HttpPost("http://113.198.235.225/board.php");

                // 전달할 인자
                Vector<NameValuePair> nameValue = new Vector<>();
                nameValue.add(new BasicNameValuePair("team_code", teamCode));

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
                    String boardCode = content.getString("board_code");
                    String boardName = content.getString("board_name");

                    boardAdapter.addItem(boardCode, boardName);
                    boardAdapter.notifyDataSetChanged();
                }
                asyncTask_b = null;

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
