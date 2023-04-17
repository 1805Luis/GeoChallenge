package es.practicacumn.geochallenge.Adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;
import es.practicacumn.geochallenge.Model.UsuarioGymkhana.Gymkhana.Prueba;
import es.practicacumn.geochallenge.R;

public class AdaptadorPruebas extends BaseAdapter {
    Context context;
    List<Prueba> listaPruebas;

    public AdaptadorPruebas(Context context, List<Prueba> listaPruebas) {
        this.context = context;
        this.listaPruebas = listaPruebas;
    }

    @Override
    public int getCount() {
        return listaPruebas.size();
    }

    @Override
    public Object getItem(int i) {
        return listaPruebas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return listaPruebas.get(i).getOrden();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View vista=view;
        LayoutInflater inflate=LayoutInflater.from(context);
        vista=inflate.inflate(R.layout.item_prueba,null);
        TextView titulo = vista.findViewById(R.id.titulo);
        TextView ubicacion=vista.findViewById(R.id.Descripccion);
        titulo.setText("Número de la prueba: "+listaPruebas.get(i).getOrden());
        ubicacion.setText("La prueba se ubica en la latitud "+listaPruebas.get(i).getLatitud()+" y en la longitud. "+listaPruebas.get(i).getLongitud());
        return vista;
    }
}
