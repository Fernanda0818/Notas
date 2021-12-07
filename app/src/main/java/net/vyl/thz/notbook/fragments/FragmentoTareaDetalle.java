package net.vyl.thz.notbook.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
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
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;

import net.vyl.thz.notbook.MainActivity;
import net.vyl.thz.notbook.R;
import net.vyl.thz.notbook.adapters.AdaptadorMultimedia;
import net.vyl.thz.notbook.data.DaoAudioNotas;
import net.vyl.thz.notbook.data.DaoAudioTareas;
import net.vyl.thz.notbook.data.DaoMultimediaNotas;
import net.vyl.thz.notbook.data.DaoMultimediaTareas;
import net.vyl.thz.notbook.data.DaoRecordatoriosTareas;
import net.vyl.thz.notbook.models.AudioNotas;
import net.vyl.thz.notbook.models.AudioTareas;
import net.vyl.thz.notbook.models.MultimediaNotas;
import net.vyl.thz.notbook.models.MultimediaTareas;
import net.vyl.thz.notbook.models.Notas;
import net.vyl.thz.notbook.models.RecordatoriosTareas;
import net.vyl.thz.notbook.models.Tareas;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentoTareaDetalle#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentoTareaDetalle extends Fragment {

    MainActivity mainActivity;
    Context context;
    CheckBox checkBox;
    Spinner spinner;
    ArrayAdapter<String> arrayAdapter;
    ArrayList<String> lsSpinner;
    DaoMultimediaTareas daoMultimediaTareas;
    DaoMultimediaNotas daoMultimediaNotas;
    DaoAudioTareas daoAudioTareas;
    DaoAudioNotas daoAudioNotas;
    DaoRecordatoriosTareas daoRecordatoriosTareas;
    RecyclerView recyclerView;
    private GridLayoutManager layoutManager;
    public static AdaptadorMultimedia adaptadorMultimedia;
    public static List<Object> lsMultimedia;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final String ARG_ID_TAREA = "id_tarea";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentoTareaDetalle() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        if (context instanceof MainActivity){
            mainActivity = (MainActivity) context;
        }
        daoRecordatoriosTareas = new DaoRecordatoriosTareas(context);
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
     * @return A new instance of fragment FragmentoTareaDetalle.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentoTareaDetalle newInstance(String param1, String param2) {
        FragmentoTareaDetalle fragment = new FragmentoTareaDetalle();
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
        View vista = inflater.inflate(R.layout.fragment_tarea_detalle, container, false);
        lsMultimedia = new ArrayList<>();
        lsSpinner = new ArrayList<>();
        checkBox = vista.findViewById(R.id.ckbxCompletada);
        checkBox.setOnClickListener(v -> {
            int x = 0;
            if (checkBox.isChecked()){
                x=1;
            }
            Bundle args = getArguments();
            int position = 0;
            if(args != null){
                 position = args.getInt(ARG_ID_TAREA);
            }
            Tareas tarea = FragmentoTareas.daoTareas.getOneByID(position);
            tarea.setCompletada(x);
            FragmentoTareas.daoTareas.update(tarea);
        });
        Bundle args = getArguments();
        if(args != null){
            int position = args.getInt(ARG_ID_TAREA);
            ponInfoTarea(position, vista);
        }
        spinner = ((Spinner)vista.findViewById(R.id.recordatorios_detalle_tarea));
        if(spinner.getAdapter() == null){
            Log.d("Prueba", "onCreateView: Entró a null");
            arrayAdapter = new ArrayAdapter<String>(mainActivity, android.R.layout.simple_spinner_item, lsSpinner);
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(arrayAdapter);
        }
        recyclerView = vista.findViewById(R.id.recyclerView);
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
                    break;
                case "AudTar":
                    audTarea = daoAudioTareas.getOneByID(Integer.parseInt(mul[1]));
                    if (audTarea == null) {
                        audTarea = (AudioTareas) lsMultimedia.get(Integer.parseInt(mul[3]));
                    }
                    mainActivity.mostrarDetalleAudio(audTarea);
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

    public void ponInfoTarea(int id){
        ponInfoTarea(id, getView());
    }

    private void ponInfoTarea(int id, View vista){

        Tareas tareas = FragmentoTareas.daoTareas.getOneByID(id);
        if (tareas != null) {
            ((TextView) vista.findViewById(R.id.titulo_detalle_tarea)).setText(tareas.getTitulo());
            ((TextView) vista.findViewById(R.id.descripcion_detalle_tarea)).setText(tareas.getDescripcion());
            ((TextView) vista.findViewById(R.id.fecha_detalle_tarea)).setText(tareas.getFecha_cumplimiento());
            ((TextView) vista.findViewById(R.id.contenido_detalle_tarea)).setText(tareas.getContenido());
            if(tareas.getCompletada()==1){
                ((CheckBox) vista.findViewById(R.id.ckbxCompletada)).setChecked(true);
            }
            List<RecordatoriosTareas> ls = daoRecordatoriosTareas.getAllByIDTarea(tareas.getIdTarea());
            Log.d("Prueba", "ponInfoTarea: " + ls.size());
            for(RecordatoriosTareas rec : ls){
                lsSpinner.add(rec.getRecordatorio());
            }
            lsMultimedia.addAll(daoMultimediaTareas.getAllByIDNota(id));
            Log.d("Prueba", "ponInfoNota: longitud multimedia " + lsMultimedia.size());
            lsMultimedia.addAll(daoAudioTareas.getAllByIDTarea(id));
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
                FragmentoCrearTarea fragmentoCrearTarea = new FragmentoCrearTarea();
                Bundle args = getArguments();
                int index = args.getInt(ARG_ID_TAREA);
                Bundle bundle = new Bundle();
                bundle.putInt(fragmentoCrearTarea.ARG_ID_TAREA, index);
                fragmentoCrearTarea.setArguments(bundle);
                fragmentManager.beginTransaction().remove(fragmentManager.getFragments().get(fragmentManager.getFragments().size()-1)).add(R.id.contenedor_prquenio, fragmentoCrearTarea).addToBackStack(null).commit();
                return true;
        }
        return false;
    }
}