package net.vyl.thz.notbook.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import net.vyl.thz.notbook.R;
import net.vyl.thz.notbook.data.DaoNotas;
import net.vyl.thz.notbook.fragments.FragmentoNota;
import net.vyl.thz.notbook.models.Notas;

import java.util.ArrayList;
import java.util.List;

public class AdaptadorNotas extends RecyclerView.Adapter<AdaptadorNotas.ViewHolder> {

    private List<Notas> notasList;
    private Context contexto;
    private LayoutInflater inflador;

    private View.OnClickListener onClickListener;
    private View.OnLongClickListener onLongClickListener;

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public void setOnLongClickListener(View.OnLongClickListener onLongClickListener) {
        this.onLongClickListener = onLongClickListener;
    }

    public AdaptadorNotas(List<Notas> notasList, Context contexto) {
        inflador =(LayoutInflater)contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.notasList = notasList;
        this.contexto = contexto;
    }

    public void busqueda(String busqueda){
        Log.d("Prueba", "Entró a búsqueda");
        if(busqueda.length() == 0){
            Log.d("Prueba", "busqueda con 0");
            notasList.clear();
            notasList.addAll(FragmentoNota.daoNotas.getAll());
        }else{
            Log.d("Prueba", "busqueda mayor a 0");
            notasList.clear();
            notasList.addAll(FragmentoNota.daoNotas.getAllByName(busqueda));
            Log.d("Prueba", "busqueda: " + notasList.size());
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = inflador.inflate(R.layout.contenido_nota, null);
        v.setOnClickListener(this.onClickListener);
        v.setOnLongClickListener(this.onLongClickListener);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Notas nota = notasList.get(position);
        holder.fecha.setText(nota.getFecha_modificacion());
        holder.titulo.setText(nota.getTitulo());
        Log.d("Prueba", "onBindViewHolder: " +  nota.getIdNota());
        holder.titulo.setTag(nota.getIdNota());
        holder.descripcion.setText(nota.getDescripcion());
    }

    @Override
    public int getItemCount() {
        return notasList.size();
    }

    //Creamos nuestro ViewHolder, con los tipos de elementos a modificar
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView fecha;
        public TextView titulo;
        public TextView descripcion;

        public ViewHolder(View itemView)
        {
            super(itemView);
            fecha = (TextView) itemView.findViewById(R.id.fecha);
            titulo = (TextView) itemView.findViewById(R.id.titulo);
            descripcion = (TextView) itemView.findViewById(R.id.descripcion);
        }
    }
}
