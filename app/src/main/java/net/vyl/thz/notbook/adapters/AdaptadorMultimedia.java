package net.vyl.thz.notbook.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import net.vyl.thz.notbook.R;
import net.vyl.thz.notbook.models.AudioNotas;
import net.vyl.thz.notbook.models.AudioTareas;
import net.vyl.thz.notbook.models.MultimediaNotas;
import net.vyl.thz.notbook.models.MultimediaTareas;

import java.util.List;

public class AdaptadorMultimedia extends RecyclerView.Adapter<AdaptadorMultimedia.ViewHolder> {

    List<Object> lsMultimedia;
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

    public AdaptadorMultimedia(List<Object> ls, Context context){
        this.contexto = context;
        inflador =(LayoutInflater)contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.lsMultimedia = ls;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = inflador.inflate(R.layout.contenido_multimedia, null);
        v.setOnClickListener(this.onClickListener);
        v.setOnLongClickListener(this.onLongClickListener);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(lsMultimedia != null && lsMultimedia.size() > 0){
            if (lsMultimedia.get(position) instanceof MultimediaNotas) {
                MultimediaNotas mulNotas = (MultimediaNotas) lsMultimedia.get(position);
                if (contexto.getContentResolver().getType(Uri.parse(mulNotas.getMultimedia())).contains("image")) {
                    Log.d("Prueba", "onBindViewHolder: Pasó a imagen con uri: " + Uri.parse(mulNotas.getMultimedia()));
                    holder.icono.setImageURI(Uri.parse(mulNotas.getMultimedia()));
                } else {
                    holder.icono.setImageResource(R.drawable.ic_baseline_ondemand_video_24);
                }
                holder.descripcion.setText(mulNotas.getDescripcion());
                holder.descripcion.setTag("MulNot," + mulNotas.getIdMulNota());
            }
            if (lsMultimedia.get(position) instanceof MultimediaTareas) {
                MultimediaTareas mulTareas = (MultimediaTareas) lsMultimedia.get(position);
                if (contexto.getContentResolver().getType(Uri.parse(mulTareas.getMultimedia())).contains("image")) {
                    Log.d("Prueba", "onBindViewHolder: Pasó a imagen con uri: " + Uri.parse(mulTareas.getMultimedia()));
                    holder.icono.setImageURI(Uri.parse(mulTareas.getMultimedia()));
                } else {
                    holder.icono.setImageResource(R.drawable.ic_baseline_ondemand_video_24);
                }
                holder.descripcion.setText(mulTareas.getDescripcion());
                holder.descripcion.setTag("MulTar," + mulTareas.getIdMulTarea());
            }
            if (lsMultimedia.get(position) instanceof AudioNotas) {
                AudioNotas audNota = (AudioNotas) lsMultimedia.get(position);
                holder.icono.setImageResource(R.drawable.ic_baseline_mic_24);
                holder.descripcion.setTag("AudNot," + audNota.getIdAudNota());
            }
            if (lsMultimedia.get(position) instanceof AudioTareas) {
                AudioTareas audTareas = (AudioTareas) lsMultimedia.get(position);
                holder.icono.setImageResource(R.drawable.ic_baseline_mic_24);
                holder.descripcion.setTag("AudTar," + audTareas.getIdAudTarea());
            }
        }
    }

    @Override
    public int getItemCount() {
        return lsMultimedia.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView icono;
        public TextView descripcion;

        public ViewHolder(View itemView)
        {
            super(itemView);
            icono = (ImageView) itemView.findViewById(R.id.img_icono);
            descripcion = (TextView) itemView.findViewById(R.id.txt_descripcion_multimedia);
        }
    }
}
