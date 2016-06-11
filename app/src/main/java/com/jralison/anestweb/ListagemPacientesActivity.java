package com.jralison.anestweb;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.jralison.anestweb.adapter.PacienteListViewAdapter;
import com.jralison.anestweb.db.PacientesTableHelper;
import com.jralison.anestweb.model.Paciente;
import com.jralison.anestweb.util.AutoLoginHelper;

import java.util.ArrayList;
import java.util.List;

public class ListagemPacientesActivity extends AppCompatActivity
        implements AdapterView.OnItemLongClickListener, AdapterView.OnItemClickListener {

    private final List<Paciente> pacientesList = new ArrayList<>();
    private ListView mPacientesListView;
    private PacienteListViewAdapter mPacientesAdapter;
    private PacientesTableHelper pacientesTableHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listagem_pacientes);

        mPacientesAdapter = new PacienteListViewAdapter(this, pacientesList);
        mPacientesListView = (ListView) findViewById(R.id.listpac_listagem);
        if (null != mPacientesListView) {
            mPacientesListView.setEmptyView(findViewById(R.id.listpac_empty));
            mPacientesListView.setAdapter(mPacientesAdapter);
            mPacientesListView.setOnItemLongClickListener(this);
            mPacientesListView.setOnItemClickListener(this);
        }

        pacientesTableHelper = new PacientesTableHelper(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        atualizaListagemPacientes();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.action_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_criar:
                startActivity(new Intent(this, CadastroPacienteActivity.class));
                break;
            case R.id.action_sair:
                AutoLoginHelper.getInstance().logout();
                Intent intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }

    private void atualizaListagemPacientes() {
        pacientesList.clear();
        pacientesList.addAll(pacientesTableHelper.buscaTodos());
        mPacientesAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        final Paciente paciente = pacientesList.get(position);
        String msg = String.format(getString(R.string.listpac_msg_apagar), paciente.getNome());
        exibeConfirmRemover(msg, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (pacientesTableHelper.apaga(paciente)) {
                    pacientesList.remove(position);
                    mPacientesAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(ListagemPacientesActivity.this, R.string.listpac_err_apagar, Toast.LENGTH_SHORT).show();
                }
            }
        });
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Paciente paciente = pacientesList.get(position);
        Intent intent = new Intent(this, CadastroPacienteActivity.class);
        intent.putExtra("paciente_id", paciente.getId());
        startActivity(intent);
    }

    private void exibeConfirmRemover(String msg, DialogInterface.OnClickListener onPositive) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this, AlertDialog.BUTTON_NEGATIVE);
        alert.setTitle(getResources().getString(R.string.listpac_msg_titulo_apagar));
        alert.setMessage(msg);
        alert.setPositiveButton(R.string.btn_confirmar_exclusao, onPositive);
        alert.setNegativeButton(R.string.btn_nao, null);
        alert.setCancelable(true);
        alert.show();
    }

}
