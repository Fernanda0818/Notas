package net.vyl.thz.notbook.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import net.vyl.thz.notbook.MainActivity;
import net.vyl.thz.notbook.R;
import net.vyl.thz.notbook.adapters.AdaptadorMultimedia;
import net.vyl.thz.notbook.data.DaoAudioNotas;
import net.vyl.thz.notbook.data.DaoAudioTareas;
import net.vyl.thz.notbook.data.DaoMultimediaNotas;
import net.vyl.thz.notbook.data.DaoMultimediaTareas;
import net.vyl.thz.notbook.data.DaoNotas;
import net.vyl.thz.notbook.models.AudioNotas;
import net.vyl.thz.notbook.models.AudioTareas;
import net.vyl.thz.notbook.models.MultimediaNotas;
import net.vyl.thz.notbook.models.MultimediaTareas;
import net.vyl.thz.notbook.models.Notas;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentoNotaDetalle#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentoNotaDetalle extends Fragment implements Serializable {

    MainActivity mainActivity;
    Context context;
    boolean bandera = false;
    DaoMultimediaTareas daoMultimediaTareas;
    DaoMultimediaNotas daoMultimediaNotas;
    DaoAudioTareas daoAudioTareas;
    DaoAudioNotas daoAudioNotas;
    RecyclerView recyclerView;
    private GridLayoutManager layoutManager;
    public static AdaptadorMultimedia adaptadorMultimedia;
    public static List<Object> lsMultimedia;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final String ARG_ID_NOTA = "id nota";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentoNotaDetalle() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        if (context instanceof MainActivity){
            mainActivity = (MainActivity) context;
        }
        Log.d("Prueba", "onAttach: FragmentoNotaDetalle");
        daoMultimediaNotas = new DaoMultimediaNotas(context);
        daoMultimediaTareas = new DaoMultimediaTareas(context);
        daoAudioNotas = new DaoAudioNotas(context);
        daoAudioTareas = new DaoAudioTareas(context);
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentoNotaDetalle.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentoNotaDetalle newInstance(String param1, String param2) {
        FragmentoNotaDetalle fragment = new FragmentoNotaDetalle();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vista = inflater.inflate(R.layout.fragment_nota_detalle, container, false);
        lsMultimedia = new ArrayList<>();
        Bundle args = getArguments();
        if(args != null){
            int position = args.getInt(ARG_ID_NOTA);
            ponInfoNota(position, vista);
        }else{
            ponInfoNota(0, vista);
        }
        recyclerView = vista.findViewById(R.id.recyclerView3);
        layoutManager = new GridLayoutManager(mainActivity, 2);
        adaptadorMultimedia = new AdaptadorMultimedia(lsMultimedia, mainActivity);
        recyclerView.setLayoutManager(layoutManager);
        adaptadorMultimedia.setOnClickListener(l->{
            MultimediaNotas mulNota;
            MultimediaTareas mulTarea;
            AudioNotas audNota;
            AudioTareas audTarea;
            String[] mul = ((TextView) l.findViewById(R.id.txt_descripcion_multimedia)).getTag().toString().split(",");
            switch (mul[0]){
                case "MulNot":
                    mulNota = daoMultimediaNotas.getOneByID(Integer.parseInt(mul[1]));
                    if (mulNota != null) {
                        Log.d("Prueba", "onCreateView: UriMultimediaSeleccionada" + mulNota.getMultimedia());
                        if (context.getContentResolver().getType(Uri.parse(mulNota.getMultimedia())).contains("image")) {
                            mainActivity.mostrarDetalleImagen(mulNota);
                        } else if (context.getContentResolver().getType(Uri.parse(mulNota.getMultimedia())).contains("video")) {
                            mainActivity.mostrarDetalleVideo(mulNota);
                        }
                    }
                    break;
                case "MulTar":
                    mulTarea = daoMultimediaTareas.getOneByID(Integer.parseInt(mul[1]));
                    if (mulTarea != null) {
                        if (mainActivity.getContentResolver().getType(Uri.parse(mulTarea.getMultimedia())).contains("image")) {
                            mainActivity.mostrarDetalleImagen(mulTarea);
                        } else if (mainActivity.getContentResolver().getType(Uri.parse(mulTarea.getMultimedia())).contains("video")) {
                            mainActivity.mostrarDetalleVideo(mulTarea);
                        }
                    }
                    break;
                case "AudNot":
                    audNota = daoAudioNotas.getOneByID(Integer.parseInt(mul[1]));
                    if (audNota == null) {
                        audNota = (AudioNotas) lsMultimedia.get(Integer.parseInt(mul[3]));
                    }
                    mainActivity.mostrarDetalleAudio(audNota);
                    break;
                case "AudTar":
                    break;
            }
        });
        adaptadorMultimedia.setOnLongClickListener(l->{

            return false;
        });
        recyclerView.setAdapter(adaptadorMultimedia);
        setHasOptionsMenu(true);
        return vista;
    }

    public void ponInfoNota(int id){
        ponInfoNota(id, getView());
    }

    private void ponInfoNota(int id, View vista){
        Log.d("Prueba", "ponInfoNota: " + id);
        Notas notas = FragmentoNota.daoNotas.getOneByID(id);
        if(notas!=null){
            ((TextView) vista.findViewById(R.id.titulo_detalle)).setText(notas.getTitulo());
            ((TextView) vista.findViewById(R.id.descripcion_detalle)).setText(notas.getDescripcion());
            ((TextView) vista.findViewById(R.id.contenido_detalle)).setText(notas.getContenido());
            lsMultimedia.addAll(daoMultimediaNotas.getAllByIDNota(id));
            Log.d("Prueba", "ponInfoNota: longitud multimedia " + lsMultimedia.size());
            lsMultimedia.addAll(daoAudioNotas.getAllByIDNota(id));
        }else{
            try {
                this.finalize();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        //super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menueditar, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.item_editar:
                FragmentManager fragmentManager = mainActivity.getSupportFragmentManager();
                /*if(fragmentManager.findFragmentById(R.id.detalle_fragment) != null){
                    DetalleFragment fragment_detalle = (DetalleFragment) fragmentManager.findFragmentById(R.id.detalle_fragment);
                    fragment_detalle.ponInfoLibro(index);
                } else {*/
                FragmentoCrearNota fragmentoCrearNota = new FragmentoCrearNota();
                Bundle args = getArguments();
                int index = args.getInt(ARG_ID_NOTA);
                Bundle bundle = new Bundle();
                bundle.putInt(fragmentoCrearNota.ARG_ID_NOTA, index);
                fragmentoCrearNota.setArguments(bundle);
                fragmentManager.beginTransaction().remove(fragmentManager.getFragments().get(fragmentManager.getFragments().size()-1)).add(R.id.contenedor_prquenio, fragmentoCrearNota).addToBackStack(null).commit();
                //}
                return true;
        }

        return false;
    }
}