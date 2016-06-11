package com.jralison.anestweb;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.jralison.anestweb.util.AutoLoginHelper;
import com.jralison.anestweb.util.Constantes;

public class SplashCadastroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_cadastro);
    }

    public void onClickDepois(View view) {
        finish();
    }

    public void onClickCriarConta(View view) {
        Intent intent = new Intent(this, CadastroProfissionalActivity.class);
        startActivityForResult(intent, Activity.RESULT_FIRST_USER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Activity.RESULT_FIRST_USER) {
            if (resultCode == Activity.RESULT_OK) {
                final Long prof_id = data.getLongExtra("profissional_id", 0);
                if (prof_id > 0) {
                    AutoLoginHelper.getInstance().login(prof_id);
                    Toast.makeText(SplashCadastroActivity.this, R.string.splashcad_msg_contaok, Toast.LENGTH_SHORT).show();

                    final Intent intent = new Intent(this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                } else {
                    Log.e(Constantes.TAG, "onActivityResult: Não foi possível recuperar o primeiro usuário criado.");
                    finish();
                }
            } else {
                Log.e(Constantes.TAG, "onActivityResult: O primeiro usuário não foi cadastrado.");
            }
        }
    }
}
