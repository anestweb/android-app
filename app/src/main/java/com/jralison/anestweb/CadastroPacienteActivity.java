package com.jralison.anestweb;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.jralison.anestweb.db.PacientesTableHelper;
import com.jralison.anestweb.model.Paciente;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CadastroPacienteActivity extends AppCompatActivity {

    private EditText edtNome, edtNasc, edtCpf;
    private RadioGroup rgGenero;
    private RadioButton rbHomem, rbMulher;
    private PacientesTableHelper pacientesTableHelper;
    private Paciente pacienteEmTela = new Paciente();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_paciente);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

        edtNome = (EditText) findViewById(R.id.cadpac_input_nome);
        edtNasc = (EditText) findViewById(R.id.cadpac_input_nasc);
        edtCpf = (EditText) findViewById(R.id.cadpac_input_cpf);
        rgGenero = (RadioGroup) findViewById(R.id.cadpac_grupo_genero);
        rbHomem = (RadioButton) findViewById(R.id.cadpac_radio_masculino);
        rbMulher = (RadioButton) findViewById(R.id.cadpac_radio_feminino);

        edtNasc.setKeyListener(null);
        edtNasc.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    EditText e = (EditText) v;
                    e.setSelection(0, e.getText().length());
                    abreDatePickerDialog();
                }
            }
        });

        pacientesTableHelper = new PacientesTableHelper(this);

        long paciente_id = getIntent().getLongExtra("paciente_id", 0);
        if (paciente_id > 0) {
            pacienteEmTela = pacientesTableHelper.buscaPorId(paciente_id);
            edtNome.setText(pacienteEmTela.getNome());
            edtNasc.setText(pacienteEmTela.getNascimentoFormatado());
            edtCpf.setText(pacienteEmTela.getCpf());
            switch (pacienteEmTela.getGenero()) {
                case MASCULINO:
                    rbHomem.setChecked(true);
                    break;
                case FEMININO:
                    rbMulher.setChecked(true);
                    break;
            }

            setTitle(R.string.title_activity_edicao_paciente);
            ((TextView) findViewById(R.id.cadprof_text_subtitulo)).setText(R.string.cadpac_subtitulo_editar);
        } else {
            setTitle(R.string.title_activity_cadastro_paciente);
            ((TextView) findViewById(R.id.cadprof_text_subtitulo)).setText(R.string.cadpac_subtitulo_novo);
        }
    }

    private void abreDatePickerDialog() {
        Date curNasc = new Date();
        try {
            curNasc = DateFormat.getDateInstance().parse(edtNasc.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        final Calendar cal = Calendar.getInstance();
        cal.setTime(curNasc);

        DatePickerDialog datePicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                String dataFormatada = String.format(Locale.getDefault(), "%1$02d/%2$02d/%3$4d",
                        dayOfMonth, monthOfYear + 1, year);
                edtNasc.setText(dataFormatada);
                edtCpf.requestFocus();
            }
        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
        datePicker.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                edtCpf.requestFocus();
            }
        });
        datePicker.show();
    }

    public void onClickSalvarPaciente(View view) {
        final String nome = edtNome.getText().toString().trim();
        final String nasc = edtNasc.getText().toString().trim();
        final String cpf = edtCpf.getText().toString().trim();

        Integer err_msg = null;
        if (nome.isEmpty()) {
            err_msg = R.string.cadpac_err_nome_vazio;
            edtNome.requestFocus();
        }

        // Data de nascimento
        Date dataNasc = null;
        if (null == err_msg && !nasc.isEmpty()) {
            try {
                dataNasc = DateFormat.getDateInstance().parse(nasc);
            } catch (ParseException e) {
                err_msg = R.string.cadpac_err_data_invalida;
                edtNasc.requestFocus();
            }
        }

        // Gênero
        Paciente.Genero genero = null;
        if (null == err_msg) {
            switch (rgGenero.getCheckedRadioButtonId()) {
                case R.id.cadpac_radio_masculino:
                    genero = Paciente.Genero.MASCULINO;
                    break;
                case R.id.cadpac_radio_feminino:
                    genero = Paciente.Genero.FEMININO;
                    break;
                case -1:
                    err_msg = R.string.cadpac_err_genero;
                    break;
                default:
                    err_msg = R.string.cadpac_err_genero_invalido;
            }
        }

        // Tem erro?
        if (null == err_msg) {
            // Salvar
            pacienteEmTela.setNome(nome);
            pacienteEmTela.setNascimento(dataNasc);
            pacienteEmTela.setGenero(genero);
            pacienteEmTela.setCpf(cpf.isEmpty() ? null : cpf);

            if (pacientesTableHelper.salvaOuAtualiza(pacienteEmTela) != -1) {
                Intent resultIntent = getIntent();
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            } else {
                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setTitle(R.string.cadpac_err_alert_title_erro);
                alert.setMessage(R.string.cadpac_err_alert_msg_erro);
                alert.setNeutralButton(R.string.btn_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alert.show();
            }
        } else {
            // Mostrar mensagem
            Toast.makeText(CadastroPacienteActivity.this, err_msg, Toast.LENGTH_SHORT).show();
        }
    }
}
