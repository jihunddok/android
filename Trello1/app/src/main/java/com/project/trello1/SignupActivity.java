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

public class SignupActivity extends AppCompatActivity {

    EditText edName, edEmail, edPassword, edConfirmPassword;
    String id, pw, name;
    RegistDB registDB = null;
    String getMsg = ""; // 서버로 부터 전달받는 데이터
    InputStream is = null; // JSON INPUTSTREAM
    String r_id = "";
    boolean insert = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_signup );

        edName = findViewById( R.id.edName );
        edEmail = findViewById( R.id.edEmail );
        edPassword = findViewById( R.id.edPassword );
        edConfirmPassword = findViewById( R.id.edConfirmPassword );

        insert = false;

    }

    public void signup(View view) {
        if (TextUtils.isEmpty( edName.getText() )) {
            edName.setError( "이름을 입력해주세요" );
        } else if (TextUtils.isEmpty( edEmail.getText() )) {
            edEmail.setError( "ID를 입력해주세요" );
        } else if (TextUtils.isEmpty( edPassword.getText() )) {
            edPassword.setError( "비밀번호를 입력해주세요" );
        } else if (TextUtils.isEmpty( edConfirmPassword.getText() )) {
            edConfirmPassword.setError( "비밀번호 확인을 입력해주세요" );
        } else if (!edPassword.getText().toString().equals( edConfirmPassword.getText().toString() )) {
            Toast.makeText( SignupActivity.this, "비밀번호와 비밀번호 확인이 일치하지 않습니다", Toast.LENGTH_LONG ).show();
        } else {
            name = edName.getText().toString();
            id = edEmail.getText().toString();
            pw = edPassword.getText().toString();
            RegistDB registDB = new RegistDB();
            registDB.execute();
            if(insert == true){
                Intent intent = new Intent(SignupActivity.this , LoginActivity.class);
                startActivity(intent);
            }
        }
    }

    public class RegistDB extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... strings) {
            try {
                HttpPost httpPost = new HttpPost( "http://113.198.235.225/sign_up.php" );
                // 전달할 인자
                Vector<NameValuePair> nameValue = new Vector<>();
                nameValue.add( new BasicNameValuePair( "userId", id ) );
                nameValue.add( new BasicNameValuePair( "userPw", pw ) );
                nameValue.add( new BasicNameValuePair( "userName", name ) );

                // 웹 접속 - utf-8
                HttpEntity enty = new UrlEncodedFormEntity( nameValue, HTTP.UTF_8 );
                httpPost.setEntity( enty );

                HttpClient httpClient = new DefaultHttpClient();
                HttpResponse response = httpClient.execute( httpPost );

                // 아래 코드부터는 요청에 대한 응답을 받아와서 처리하는 코드
                HttpEntity entity = response.getEntity();
                is = entity.getContent();
                BufferedReader reader = new BufferedReader( new InputStreamReader( is, "UTF-8" ) );

                String line = ""; // 결과를 한 줄씩 읽어서 저장할 변수
                getMsg = ""; // getMsg 변수 초기화 (리스트 갱신 등의 이우로 기존의 값이 남아 있지 않도록 하기위해)

                while ((line = reader.readLine()) != null) {
                    getMsg = getMsg + line;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return getMsg;
        }

        @Override
        protected void onPostExecute(String s) {
            try {

                JSONObject root = new JSONObject(s);
                JSONArray results = new JSONArray(root.getString("results"));

                for(int i = 0; i < results.length(); i++){
                    JSONObject content = results.getJSONObject(i);
                    r_id = content.getString("id");
                }
                insert = true;

                registDB = null;

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}