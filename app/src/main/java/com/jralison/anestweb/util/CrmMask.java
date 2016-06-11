package com.jralison.anestweb.util;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Implementa uma máscara de texto para ser utilizada em campos de texto de crm.
 * <p/>
 * Created by Jonathan Souza on 08/06/2016.
 */
public class CrmMask implements TextWatcher {

    private final Pattern pattern = Pattern.compile("^([0-9]{1,6})([A-Z]{1,2})?");
    private final EditText editTextMasked;

    private boolean ignoreNextInsertion = false;
    private String oldMasked = "";

    public CrmMask(EditText editTextMasked) {
        this.editTextMasked = editTextMasked;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (ignoreNextInsertion || s.toString().isEmpty()) {
            oldMasked = s.toString();
            ignoreNextInsertion = false;
        } else {
            String masked = oldMasked;

            final String unmask = s.toString().toUpperCase().replaceAll("[^0-9A-Z]", "");
            final Matcher matcher = pattern.matcher(unmask);

            if (matcher.matches()) {
                final String rg = matcher.group(1);
                final String uf = matcher.group(2);
                masked = (null != uf && !uf.isEmpty()) ? rg + "/" + uf : rg;
            }

            if (!s.toString().equals(masked)) {
                ignoreNextInsertion = true;
                editTextMasked.setText(masked);
                editTextMasked.setSelection(masked.length());
            }
        }
    }
}
