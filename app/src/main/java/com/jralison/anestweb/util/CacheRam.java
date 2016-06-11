package com.jralison.anestweb.util;

import java.util.HashMap;

/**
 * Armazena na memória informações compartilhadas entre todas as activities.
 * <p/>
 * Created by Jonathan Souza on 07/06/2016.
 */
public class CacheRam {

    private static final HashMap<String, Object> map = new HashMap<>();

    public static void put(String key, Object value) {
        map.put(key, value);
    }

    public static Object get(String key) {
        return map.get(key);
    }

    public static void remove(String key) {
        map.remove(key);
    }

    public static boolean has(String key) {
        return map.containsKey(key);
    }

}
