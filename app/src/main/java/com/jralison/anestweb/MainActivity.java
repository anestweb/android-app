package com.jralison.anestweb;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.jralison.anestweb.db.DbHelper;
import com.jralison.anestweb.model.Profissional;
import com.jralison.anestweb.util.AutoLoginHelper;
import com.jralison.anestweb.util.CacheRam;
import com.jralison.anestweb.util.Constantes;

public class MainActivity extends AppCompatActivity {

    private AutoLoginHelper autoLoginHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Handler handler = new Handler();
        Runnable r = new Runnable() {
            @Override
            public void run() {
                onInicializaAplicacao();
                finish();
            }
        };
        handler.postDelayed(r, 1000);
    }

    /**
     * [1] Verifica se o usuário já está conectado;
     * [1.1] Se estiver, chama a tela de entrada;
     * [1.2] Se não estiver, verifica se existe usuário cadastrado no banco de dados;
     * [1.2.1] Se houver usuário cadastrado, chama a tela de login;
     * [1.2.2] Se não houver usuário cadastrado, chama a tela de cadastro.
     */
    private void onInicializaAplicacao() {
        // Inicializa o db.
        final DbHelper dbHelper = new DbHelper(this);

        // Configura o autoLogin.
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        CacheRam.put(Constantes.CHAVE_PREFS, sharedPreferences);
        autoLoginHelper = AutoLoginHelper.getInstance();

        // Identifica qual tela precisa ser exibida.
        if (isAutoLoginConectado()) {
            startActivity(new Intent(this, ListagemPacientesActivity.class));
        } else if (dbHelper.contaProfissionais() > 0) {
            // Existe pelo menos um usuário cadastrado, mas ninguém conectado.
            dbHelper.close();
            startActivity(new Intent(this, LoginActivity.class));
        } else {
            // Nenhum usuário cadastrado.
            startActivity(new Intent(this, SplashCadastroActivity.class));
        }

        finish();
    }

    private boolean isAutoLoginConectado() {
        if (autoLoginHelper.isConectado()) {
            final long idUsuarioAutoLogin = autoLoginHelper.getIdConectado();
            final DbHelper dbHelper = new DbHelper(this);
            final Profissional profissionalConectado = dbHelper.buscaProfissionalPorId(idUsuarioAutoLogin);
            dbHelper.close();
            CacheRam.put(Constantes.USUARIO_CONECTADO, profissionalConectado);
            return true;
        }

        return false;
    }

}
