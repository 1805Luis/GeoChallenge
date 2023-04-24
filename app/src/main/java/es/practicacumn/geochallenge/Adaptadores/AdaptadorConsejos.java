package es.practicacumn.geochallenge.Adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import es.practicacumn.geochallenge.Model.Consejos.Consejos;
import es.practicacumn.geochallenge.R;

public class AdaptadorConsejos extends BaseAdapter {
    Context context;
    List<Consejos> listaConsejos;

    public AdaptadorConsejos(Context context, List<Consejos> listaConsejos) {
        this.context = context;
        this.listaConsejos = listaConsejos;
    }

    @Override
    public int getCount() {
        return listaConsejos.size();
    }

    @Override
    public Object getItem(int i) {
        return listaConsejos.get(i);
    }

    @Override
    public long getItemId(int i) {
        return listaConsejos.get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View vista=view;
        LayoutInflater inflate=LayoutInflater.from(context);
        vista=inflate.inflate(R.layout.item_consejos,null);
        TextView informacion=vista.findViewById(R.id.consejos);
        informacion.setText(listaConsejos.get(i).getOrden());
        return vista;
    }
}
