package com.jralison.anestweb.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.jralison.anestweb.model.Profissional;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementa a classe abstrata SQLiteOpenHelper para gerenciar versões do banco de dados.
 *
 * @TODO Mover os métodos que com os profissionais para a classe ProfissionaisTableHelper.
 * <p/>
 * Created by Jonathan Souza on 08/06/2016.
 */
public class DbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "anestweb";

    private Context contexttmp;

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.contexttmp = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String[] sql_tables = new String[]{
                "CREATE TABLE profissionais (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "nome TEXT NOT NULL," +
                        "crm TEXT NOT NULL UNIQUE," +
                        "senha TEXT NOT NULL" +
                        ")",
                "CREATE TABLE pacientes (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "nome TEXT NOT NULL," +
                        "cpf TEXT DEFAULT NULL," +
                        "rg TEXT DEFAULT NULL," +
                        "genero TEXT NOT NULL," +
                        "nascimento DATE DEFAULT NULL" +
                        ")"
        };

        for (String sql : sql_tables) {
            db.execSQL(sql);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public Profissional buscaProfissionalPorId(Long id) {
        String sql = "SELECT id,crm,nome,senha FROM profissionais WHERE id = ? LIMIT 1";

        Cursor cursor = getReadableDatabase().rawQuery(sql, new String[]{id.toString()});
        if (cursor.moveToFirst()) {
            Profissional profissional = new Profissional();
            profissional.setId(cursor.getLong(0));
            profissional.setCrm(cursor.getString(1));
            profissional.setNome(cursor.getString(2));
            profissional.setSenha(cursor.getString(3));
            return profissional;
        }
        cursor.close();

        return null;
    }

    public int contaProfissionais() {
        int count = 0;
        String sql = "SELECT COUNT(*) FROM profissionais";
        Cursor cursor = getReadableDatabase().rawQuery(sql, new String[]{});
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        return count;
    }

    public List<Profissional> buscaProfissionalPor(String campo, Object valor, Integer limit) {
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT id,nome,crm,senha FROM profissionais";
        final String[] cols = new String[]{"id", "nome", "crm", "senha"};
        final String condition = null != valor ? campo + " = ?" : campo + " IS NULL";
        final String[] params = null != valor ? new String[]{valor.toString()} : new String[]{};
        final Cursor cursor = db.query(false, "profissionais", cols, condition, params, null, null, null, String.valueOf(limit));

        final List<Profissional> lista = new ArrayList<>();
        while (cursor.moveToNext()) {
            final Profissional p = new Profissional();
            p.setId(cursor.getLong(0));
            p.setNome(cursor.getString(1));
            p.setCrm(cursor.getString(2));
            p.setSenha(cursor.getString(3));
            lista.add(p);
        }
        cursor.close();

        return lista;
    }

    public Profissional salvaProfissional(Profissional profissional) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("crm", profissional.getCrm());
        values.put("nome", profissional.getNome());
        values.put("senha", profissional.getSenha());

        Long newId = db.insert("profissionais", null, values);
        if (newId > -1) {
            profissional.setId(newId);
            return profissional;
        }

        return null;
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
