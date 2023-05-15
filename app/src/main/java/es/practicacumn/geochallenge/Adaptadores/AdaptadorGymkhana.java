package es.practicacumn.geochallenge.Adaptadores;

import android.content.Context;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import es.practicacumn.geochallenge.Model.UsuarioGymkhana.Gymkhana.Gymkhana;
import es.practicacumn.geochallenge.R;

public class AdaptadorGymkhana extends BaseAdapter {
    private Context context;
    private List<Gymkhana> gymkhanas;

    public AdaptadorGymkhana(Context context, List<Gymkhana> gymkhanas) {
        this.context = context;
        this.gymkhanas = gymkhanas;
    }

    @Override
    public int getCount() {
        return gymkhanas.size();
    }

    @Override
    public Object getItem(int i) {
        return gymkhanas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return (long) gymkhanas.get(i).getUbicacionGymkhana().getLatitud();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View vista=view;
        LayoutInflater inflate=LayoutInflater.from(context);
        vista=inflate.inflate(R.layout.item_gymkhana,null);
        TextView titulo = vista.findViewById(R.id.nombregymkhana);
        SpannableString content = new SpannableString(gymkhanas.get(i).getNombre());
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        titulo.setText(content);
        TextView informacion=vista.findViewById(R.id.descripciongymkhana);
        informacion.setText(gymkhanas.get(i).getDescripcion());
        TextView inicio= vista.findViewById(R.id.iniciogymkhana);
        inicio.setText("Inicio: "+gymkhanas.get(i).getDiaInicio()+","+gymkhanas.get(i).getHoraInicio());
        TextView fin= vista.findViewById(R.id.fingymkhana);
        fin.setText("Fin: \n"+gymkhanas.get(i).getDiaFin()+","+gymkhanas.get(i).getHoraFin());
        return vista;
    }
}
