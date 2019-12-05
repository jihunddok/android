package com.team.login;

import android.content.Intent;
import android.os.Bundle;

import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;

public class SplashActivity extends BaseActivity {
    private ISessionCallback callback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        callback = new ISessionCallback() {
            @Override
            public void onSessionOpened() {
                goToMainActivity();
            }

            @Override
            public void onSessionOpenFailed(Exception exception) {
                redirectToLoginActivity();
            }
        };

        Session.getCurrentSession().addCallback(callback);
        findViewById(R.id.splash).postDelayed(() -> {
            if (!Session.getCurrentSession().checkAndImplicitOpen()) {
                redirectToLoginActivity();
            }
        }, 1000); // 1초 보이기
    }

    /* 인트로 끝나고 넘어갈때 세션 죽이기 (다음 화면에서 불러와야함) */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Session.getCurrentSession().removeCallback(callback);
    }

    /* 자동로그인일 경우 바로 서비스 화면으로 */
    private void goToMainActivity() {
        Intent intent = new Intent(SplashActivity.this, ServiceActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    /* 일반 로그인일 경우 로그인 화면으로 */
    private void redirectToLoginActivity() {
        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
