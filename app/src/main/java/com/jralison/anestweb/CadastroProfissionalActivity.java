package com.jralison.anestweb;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.jralison.anestweb.db.DbHelper;
import com.jralison.anestweb.model.Profissional;
import com.jralison.anestweb.util.CrmMask;
import com.jralison.anestweb.util.StringUtil;

import java.util.List;
import java.util.regex.Pattern;

public class CadastroProfissionalActivity extends AppCompatActivity {

    private EditText edtNome, edtCrm, edtSenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_profissional);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        edtCrm = (EditText) findViewById(R.id.cadprof_input_crm);
        if (null != edtCrm) edtCrm.addTextChangedListener(new CrmMask(edtCrm));

        edtNome = (EditText) findViewById(R.id.cadprof_input_nome);
        edtSenha = (EditText) findViewById(R.id.cadprof_input_senha);

//        edtCrm.setText("1234/JS");
//        edtNome.setText("Jonathan Souza");
//        edtSenha.setText("123456");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onClickSalvarProfissional(View view) {
        // Validar
        final String nome = edtNome.getText().toString().trim();
        final String crm = edtCrm.getText().toString().trim();
        final String senha = edtSenha.getText().toString().trim();

        Integer err_msg = null;
        if (nome.isEmpty()) {
            err_msg = R.string.cadprof_err_nome_vazio;
            edtNome.requestFocus();
        } else if (crm.isEmpty()) {
            err_msg = R.string.cadprof_err_crm_vazio;
            edtCrm.requestFocus();
        } else if (!Pattern.compile("^[0-9]{1,6}/[A-Z]{2}$").matcher(crm).matches()) {
            err_msg = R.string.cadprof_err_crm_invalido;
            edtCrm.requestFocus();
        } else if (senha.length() <= 4) {
            err_msg = R.string.cadprof_err_senha_curta;
            edtSenha.requestFocus();
        } else {
            // CRM já existe?
            final DbHelper dbHelper = new DbHelper(this);
            final List<Profissional> profs = dbHelper.buscaProfissionalPor("crm", crm, 1);

            if (profs.size() > 0) {
                err_msg = R.string.cadprof_err_crm_duplicado;
                edtCrm.requestFocus();
            } else {
                Profissional profissional = new Profissional();
                profissional.setCrm(crm);
                profissional.setNome(nome);
                profissional.setSenha(StringUtil.hash256(senha));

                if (null == profissional.getSenha()) {
                    err_msg = R.string.cadprof_err_senha_nohash;
                } else {
                    profissional = dbHelper.salvaProfissional(profissional);
                    if (null != profissional) {
                        Intent resultIntent = getIntent();
                        resultIntent.putExtra("profissional_id", profissional.getId());
                        setResult(Activity.RESULT_OK, resultIntent);
                        finish();
                    } else {
                        AlertDialog.Builder alert = new AlertDialog.Builder(this);
                        alert.setTitle(R.string.cadprof_err_alert_title_erro);
                        alert.setMessage(R.string.cadprof_err_alert_msg_erro);
                        alert.setNeutralButton(R.string.btn_ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        alert.show();
                    }
                }
            }

            dbHelper.close();
        }

        if (null != err_msg) {
            Toast.makeText(CadastroProfissionalActivity.this, err_msg, Toast.LENGTH_LONG).show();
        }
    }

}
