package com.project.trello1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import ch.boye.httpclientandroidlib.HttpEntity;
import ch.boye.httpclientandroidlib.HttpResponse;
import ch.boye.httpclientandroidlib.client.HttpClient;
import ch.boye.httpclientandroidlib.client.methods.HttpPost;
import ch.boye.httpclientandroidlib.impl.client.DefaultHttpClient;

public class TeamActivity extends AppCompatActivity {

    ListView listview;
    TeamAdapter teamAdapter;
    String getMsg = ""; // 서버로 부터 전달받는 데이터
    InputStream is = null; // JSON INPUTSTREAM
    AsyncTask_T test = null; // Asynctask 클래스를 사용하기 위한 변수

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Team");
        teamAdapter = new TeamAdapter();
        listview = findViewById(R.id.listView);

        listview.setAdapter(teamAdapter);
        test = new AsyncTask_T();
        test.execute();

        // 리스트뷰 선택했을 때 이벤트 team -> board 목록
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                TextView teamCode = view.findViewById(R.id.teamCode);
                TextView teamName = view.findViewById(R.id.teamName);
                // Toast.makeText(TeamActivity.this, teamCode.getText(), Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getApplicationContext(), BoardActivity.class);
                intent.putExtra("teamCode", teamCode.getText());
                intent.putExtra("teamName", teamName.getText());

                startActivity(intent);
            }
        });

    }

    class AsyncTask_T extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            try {

                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("http://113.198.235.225/team.php");

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
                JSONArray results = new JSONArray(root.getString("result"));

                for(int i = 0; i < results.length(); i++){
                    JSONObject content = results.getJSONObject(i);
                    String teamCode = content.getString("team_code");
                    String teamName = content.getString("team_name");
                    String teamMemberNo = content.getString("members");

                    teamAdapter.addItem(teamCode, teamName, teamMemberNo);
                    teamAdapter.notifyDataSetChanged();
                }
                test = null;

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}