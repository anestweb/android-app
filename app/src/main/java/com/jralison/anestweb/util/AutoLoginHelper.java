package com.jralison.anestweb.util;

import android.content.SharedPreferences;
import android.util.Log;

import java.util.Date;

/**
 * Esta classe fornece os métodos para gerenciar o AutoLogin da aplicação.
 * <p/>
 * Created by Jonathan Souza on 08/06/2016.
 */
public class AutoLoginHelper {

    private static AutoLoginHelper thisInstance = null;
    private final SharedPreferences preferences;

    private AutoLoginHelper() {
        preferences = (SharedPreferences) CacheRam.get(Constantes.CHAVE_PREFS);
    }

    public static AutoLoginHelper getInstance() {
        if (null == thisInstance) {
            thisInstance = new AutoLoginHelper();
        }
        return thisInstance;
    }

    public long getIdConectado() {
        return preferences.getLong(Constantes.CHAVE_AUTOLOGIN_USUARIO, 0);
    }

    public void login(Long prof_id) {
        final long agora = (new Date()).getTime();
        final long prazo = agora + (15 * 60 * 1000); // +15 mins

        CacheRam.put(Constantes.CHAVE_AUTOLOGIN_USUARIO, prof_id);
        CacheRam.put(Constantes.CHAVE_AUTOLOGIN_PRAZO, prazo);

        salvaPreferencias();
    }

    private void salvaPreferencias() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong(Constantes.CHAVE_AUTOLOGIN_PRAZO, (long) CacheRam.get(Constantes.CHAVE_AUTOLOGIN_PRAZO))
                .putLong(Constantes.CHAVE_AUTOLOGIN_USUARIO, (long) CacheRam.get(Constantes.CHAVE_AUTOLOGIN_USUARIO))
                .apply();
        Log.d(Constantes.TAG, "salvaPreferencias: preferências atualizadas.");
    }

    public boolean isConectado() {
        final Long idUsuarioAutoLogin = preferences.getLong(Constantes.CHAVE_AUTOLOGIN_USUARIO, 0);
        final Long autoLoginExpiresTime = preferences.getLong(Constantes.CHAVE_AUTOLOGIN_PRAZO, 0);

        Log.d(Constantes.TAG, "isAutoLoginConectado: idUsuarioAutoLogin = " + idUsuarioAutoLogin
                + "; autoLoginExpiresTime = " + autoLoginExpiresTime);

        if (idUsuarioAutoLogin == 0 || autoLoginExpiresTime == 0) {
            return false;
        } else {
            final Long agora = (new Date()).getTime();
            if (autoLoginExpiresTime > agora) {
                Log.d(Constantes.TAG, "isAutoLoginConectado: autoLogin ainda válido!");
                return true;
            } else {
                Log.d(Constantes.TAG, "isAutoLoginConectado: autoLogin já expirou!");
                return false;
            }
        }
    }

    public Date getPrazo() {
        final long prazo = preferences.getLong(Constantes.CHAVE_AUTOLOGIN_PRAZO, 0);
        if (prazo > 0) {
            Date dPrazo = new Date();
            dPrazo.setTime(prazo);
            return dPrazo;
        }
        return null;
    }

    public void logout() {
        preferences.edit()
                .remove(Constantes.CHAVE_AUTOLOGIN_USUARIO)
                .remove(Constantes.CHAVE_AUTOLOGIN_PRAZO)
                .apply();

        CacheRam.remove(Constantes.CHAVE_AUTOLOGIN_USUARIO);
        CacheRam.remove(Constantes.CHAVE_AUTOLOGIN_PRAZO);
    }

}
