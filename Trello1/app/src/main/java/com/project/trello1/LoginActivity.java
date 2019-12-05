package com.project.trello1;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

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

public class LoginActivity extends AppCompatActivity {

    EditText edEmail, edPassword;
    Async_userinfo async_userinfo = null;
    String id,pw;
    String getMsg = ""; // 서버로 부터 전달받는 데이터
    InputStream is = null; // JSON INPUTSTREAM
    String r_id = "";
    String r_pw = "";
    String r_name = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edEmail = findViewById(R.id.edEmail);
        edPassword = findViewById(R.id.edPassword);

    }

    public void login(View view) {
        if( TextUtils.isEmpty(edEmail.getText())){
            edEmail.setError( "ID를 입력해주세요" );
        }else if( TextUtils.isEmpty(edPassword.getText())){
            edPassword.setError( "비밀번호를 입력해주세요"+id );
        }else{
            id = edEmail.getText().toString();
            pw = edPassword.getText().toString();
            Async_userinfo async_userinfo = new Async_userinfo();
            async_userinfo.execute(id,pw);
            if(r_id.equals(id) && r_pw.equals(pw)) {
                Toast.makeText( LoginActivity.this, "환영합니다.", Toast.LENGTH_LONG ).show();
                Intent intent = new Intent( LoginActivity.this, TeamActivity.class );
                intent.putExtra( "id",r_id );
                intent.putExtra( "name",r_name );
                startActivity( intent );
                finish();
            }
            else {
                Toast.makeText( LoginActivity.this, "ID 또는 패스워드를 확인해주세요", Toast.LENGTH_LONG ).show();
            }
        }
    }

    public void signup(View view) {
        Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
        startActivity(intent);
        finish();
    }

    class Async_userinfo extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            try {
                HttpPost httpPost = new HttpPost("http://113.198.235.225/login.php");

                // 전달할 인자
                Vector<NameValuePair> nameValue = new Vector<>();
                nameValue.add(new BasicNameValuePair("userId", id));

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
                getMsg = ""; // getMsg 변수 초기화 (리스트 갱신 등의 이후로 기존의 값이 남아 있지 않도록 하기위해)

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
                    r_id = content.getString("id");
                    r_pw = content.getString("pass");
                    r_name = content.getString("name");
                }
                async_userinfo = null;

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
