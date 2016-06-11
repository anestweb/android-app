package com.jralison.anestweb;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.jralison.anestweb.db.DbHelper;
import com.jralison.anestweb.model.Profissional;
import com.jralison.anestweb.util.AutoLoginHelper;
import com.jralison.anestweb.util.CacheRam;
import com.jralison.anestweb.util.Constantes;
import com.jralison.anestweb.util.CrmMask;
import com.jralison.anestweb.util.StringUtil;

import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private EditText edtCrm, edtSenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtSenha = (EditText) findViewById(R.id.login_input_senha);
        edtCrm = (EditText) findViewById(R.id.login_input_crm);
        if (null != edtCrm) edtCrm.addTextChangedListener(new CrmMask(edtCrm));

//        edtCrm.setText("1234/JS");
//        edtSenha.setText("123456");
    }

    public void onClickLogin(View view) {
        final String crm = edtCrm.getText().toString().trim();

        if (crm.isEmpty()) {
            Toast.makeText(LoginActivity.this, R.string.login_err_crm_vazio, Toast.LENGTH_SHORT).show();
            return;
        }

        DbHelper dbHelper = new DbHelper(this);
        final List<Profissional> profs = dbHelper.buscaProfissionalPor("crm", crm, 1);
        dbHelper.close();

        if (profs.size() == 1) {
            // Checa a senha
            final Profissional profissional = profs.get(0);
            final String senha = edtSenha.getText().toString().trim();
            if (null != profissional.getSenha() && StringUtil.hash256(senha).equals(profissional.getSenha())) {
                AutoLoginHelper.getInstance().login(profissional.getId());
                CacheRam.put(Constantes.USUARIO_CONECTADO, profissional);

                Intent intent = new Intent(this, ListagemPacientesActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(LoginActivity.this, R.string.login_err_generico, Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(LoginActivity.this, R.string.login_err_inexistente, Toast.LENGTH_SHORT).show();
        }
    }
}
