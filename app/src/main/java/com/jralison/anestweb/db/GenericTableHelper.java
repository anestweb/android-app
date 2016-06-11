package com.jralison.anestweb.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Esta classe provê um modelo para gerenciar Helpers para cada tabela/objeto no banco de dados.
 * <p/>
 * Created by Jonathan Souza on 09/06/2016.
 */
public abstract class GenericTableHelper {

    private final Context context;

    public GenericTableHelper(Context context) {
        this.context = context;
    }

    protected Context getContext() {
        return this.context;
    }

    protected SQLiteDatabase getDatabase(Modo modo) {
        final DbHelper dbHelper = new DbHelper(context);
        switch (modo) {
            case LEITURA:
                return dbHelper.getReadableDatabase();
            case ESCRITA:
                return dbHelper.getWritableDatabase();
            default:
                throw new RuntimeException("Modo de abertura do banco de dados não suportado.");
        }
    }

    protected void closeDatabase(SQLiteDatabase db) {
        if (null != db && db.isOpen()) {
            db.close();
        }
    }

    protected enum Modo {
        LEITURA,
        ESCRITA
    }

}
