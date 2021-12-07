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
import net.vyl.thz.notbook.fragments.FragmentoNota;
import net.vyl.thz.notbook.fragments.FragmentoTareas;
import net.vyl.thz.notbook.models.Notas;
import net.vyl.thz.notbook.models.Tareas;

import java.util.List;

public class AdaptadorTareas extends RecyclerView.Adapter<AdaptadorTareas.ViewHolder>{

    private List<Tareas> tareasList;
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

    public AdaptadorTareas(List<Tareas> tareasList, Context contexto) {
        inflador = (LayoutInflater) contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.tareasList = tareasList;
        this.contexto = contexto;
    }

    public void busquedaTareas(String busqueda){

        if(busqueda.length() == 0){
            tareasList.clear();
            tareasList.addAll(FragmentoTareas.daoTareas.getAll());
        }else{
            tareasList.clear();
            tareasList.addAll(FragmentoTareas.daoTareas.getAllByName(busqueda));

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
        Tareas tarea = tareasList.get(position);
        holder.fecha.setText(tarea.getFecha_modificacion());
        holder.titulo.setText(tarea.getTitulo());
        Log.d("Prueba", "onBindViewHolder: " +  tarea.getIdTarea());
        holder.titulo.setTag(tarea.getIdTarea());
        holder.descripcion.setText(tarea.getDescripcion());
    }

    @Override
    public int getItemCount() {
        return tareasList.size();
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
