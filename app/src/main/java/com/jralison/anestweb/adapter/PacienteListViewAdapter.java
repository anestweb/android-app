package com.jralison.anestweb.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jralison.anestweb.R;
import com.jralison.anestweb.model.Paciente;

import java.text.DateFormat;
import java.util.List;

/**
 * Implementa um BaseAdapter para ser utilizado com o ListView de pacientes.
 * <p/>
 * Created by Jonathan Souza on 09/06/2016.
 */
public class PacienteListViewAdapter extends BaseAdapter {

    private final List<Paciente> pacientes;
    private final LayoutInflater mInflater;

    public PacienteListViewAdapter(Context context, List<Paciente> pacientes) {
        this.pacientes = pacientes;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return pacientes.size();
    }

    @Override
    public Object getItem(int position) {
        return pacientes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder vHolder;

        if (null == convertView) {
            convertView = mInflater.inflate(R.layout.item_paciente_listview, parent);
            vHolder = new ViewHolder();
            vHolder.mNome = (TextView) convertView.findViewById(R.id.item_paciente_nome);
            vHolder.mGenero = (TextView) convertView.findViewById(R.id.item_paciente_genero);
            vHolder.mIdade = (TextView) convertView.findViewById(R.id.item_paciente_idade);
            vHolder.mNascimento = (TextView) convertView.findViewById(R.id.item_paciente_nascimento);
            vHolder.mAvatar = (ImageView) convertView.findViewById(R.id.item_paciente_avatar);
            convertView.setTag(vHolder);
        } else {
            vHolder = (ViewHolder) convertView.getTag();
        }

        final Paciente paciente = (Paciente) getItem(position);
        vHolder.mNome.setText(paciente.getNome());

        final Integer idade = paciente.getIdade();
        if (null != idade) {
            vHolder.mIdade.setVisibility(View.VISIBLE);
            vHolder.mIdade.setText(String.format("%1$d ano%2$s", idade, (idade > 1 ? "s" : "")));
        } else {
            vHolder.mIdade.setVisibility(View.GONE);
        }

        if (null != paciente.getGenero()) {
            final boolean isCrianca = (null != idade && idade <= 12);
            final boolean isIdoso = (null != idade && idade >= 64);

            // Gênero & Avatar
            final String genero;
            final int avatar;

            switch (paciente.getGenero()) {
                case FEMININO:
                    genero = isCrianca ? "Menina" : "Mulher";
                    avatar = isCrianca ? R.drawable.ic_baby_female : (isIdoso ? R.drawable.ic_senior_female : R.drawable.ic_female);
                    break;
                case MASCULINO:
                    genero = isCrianca ? "Menino" : "Homem";
                    avatar = isCrianca ? R.drawable.ic_baby_male : (isIdoso ? R.drawable.ic_senior_male : R.drawable.ic_male);
                    break;
                default:
                    genero = "Não Informado";
                    avatar = R.drawable.ic_avatar;
            }

            vHolder.mGenero.setVisibility(View.VISIBLE);
            vHolder.mGenero.setText(genero);
            vHolder.mAvatar.setImageResource(avatar);
        } else {
            vHolder.mAvatar.setImageResource(R.drawable.ic_avatar);
            vHolder.mGenero.setVisibility(View.GONE);
        }

        if (null != paciente.getNascimento()) {
            vHolder.mNascimento.setVisibility(View.VISIBLE);
            vHolder.mNascimento.setText(DateFormat.getDateInstance().format(paciente.getNascimento()));
        } else {
            vHolder.mNascimento.setVisibility(View.GONE);
        }

        return convertView;
    }

    private class ViewHolder {
        TextView mNome, mGenero, mIdade, mNascimento;
        ImageView mAvatar;
    }
}
