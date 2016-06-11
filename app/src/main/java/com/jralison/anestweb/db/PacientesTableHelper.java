package com.jralison.anestweb.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.jralison.anestweb.model.Paciente;
import com.jralison.anestweb.util.Constantes;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

/**
 * Implementa a classe GenericTableHelper para o objeto Paciente.
 * <p/>
 * Created by Jonathan Souza on 09/06/2016.
 */
public class PacientesTableHelper extends GenericTableHelper {

    public PacientesTableHelper(Context context) {
        super(context);
    }

    public ArrayList<Paciente> buscaTodos() {
        final SQLiteDatabase db = getDatabase(Modo.LEITURA);
        final String sql = "SELECT id,nome,cpf,genero,nascimento FROM pacientes ORDER BY LOWER(nome)";
        final Cursor cursor = db.rawQuery(sql, null);

        final ArrayList<Paciente> pacientes = new ArrayList<>();
        while (cursor.moveToNext()) {
            pacientes.add(criaPacienteFromCursor(cursor));
        }

        closeDatabase(db);
        return pacientes;
    }

    private Paciente criaPacienteFromCursor(Cursor cursor) {
        final Paciente paciente = new Paciente();

        paciente.setId(cursor.getLong(0));
        paciente.setNome(cursor.getString(1));
        paciente.setCpf(cursor.getString(2));

        //
        switch (cursor.getString(3)) {
            case "F":
                paciente.setGenero(Paciente.Genero.FEMININO);
                break;
            case "M":
                paciente.setGenero(Paciente.Genero.MASCULINO);
                break;
            default:
                paciente.setGenero(cursor.getString(3));
        }

        Date dataNasc = null;
        try {
            if (null != cursor.getString(4)) {
                dataNasc = DateFormat.getDateInstance().parse(cursor.getString(4));
            }
        } catch (ParseException ex) {
            Log.e(Constantes.TAG, "buscaPacientePorId: " + ex.getMessage(), ex);
        }
        paciente.setNascimento(dataNasc);

        return paciente;
    }

    public Long salvaOuAtualiza(Paciente paciente) {
        final ContentValues values = new ContentValues();
        values.put("nome", paciente.getNome());
        values.put("cpf", paciente.getCpf());
        values.put("nascimento", paciente.getNascimentoFormatado());
        values.put("genero", paciente.getGenero().toString());

        final SQLiteDatabase db = getDatabase(Modo.ESCRITA);

        final long id;
        if (null != paciente.getId()) {
            db.update("pacientes", values, "id = ?", new String[]{paciente.getId().toString()});
            id = paciente.getId();
        } else {
            id = db.insert("pacientes", null, values);
        }

        closeDatabase(db);

        return id;
    }

    public boolean apaga(Paciente paciente) {
        final SQLiteDatabase db = getDatabase(Modo.ESCRITA);
        boolean b = db.delete("pacientes", "id = ?", new String[]{paciente.getId().toString()}) > 0;
        closeDatabase(db);
        return b;
    }

    public Paciente buscaPorId(long id) {
        final String sql = "SELECT id,nome,cpf,genero,nascimento FROM pacientes WHERE id = ? LIMIT 1";
        final SQLiteDatabase db = getDatabase(Modo.LEITURA);
        final Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(id)});
        final Paciente paciente = cursor.moveToFirst() ? criaPacienteFromCursor(cursor) : null;
        closeDatabase(db);
        return paciente;
    }

}
