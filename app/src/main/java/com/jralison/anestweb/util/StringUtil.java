package com.jralison.anestweb.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Classe fornece métodos úteis para lidar com Strings.
 * <p/>
 * Created by Jonathan Souza on 08/06/2016.
 */
public class StringUtil {

    public static String hash256(String text) {
        try {
            if (text.isEmpty()) {
                return "";
            } else {
                final byte[] input = text.getBytes();
                final byte[] digest = MessageDigest.getInstance("SHA-256").digest(input);
                return String.format("%064x", new BigInteger(1, digest));
            }
        } catch (NoSuchAlgorithmException ex) {
            return null;
        }
    }

}
