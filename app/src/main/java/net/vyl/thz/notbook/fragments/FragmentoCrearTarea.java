package net.vyl.thz.notbook.fragments;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.Data;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import net.vyl.thz.notbook.MainActivity;
import net.vyl.thz.notbook.R;
import net.vyl.thz.notbook.adapters.AdaptadorMultimedia;
import net.vyl.thz.notbook.data.DaoAudioNotas;
import net.vyl.thz.notbook.data.DaoAudioTareas;
import net.vyl.thz.notbook.data.DaoMultimediaNotas;
import net.vyl.thz.notbook.data.DaoMultimediaTareas;
import net.vyl.thz.notbook.data.DaoNotas;
import net.vyl.thz.notbook.data.DaoRecordatoriosTareas;
import net.vyl.thz.notbook.data.DaoTareas;
import net.vyl.thz.notbook.models.AudioNotas;
import net.vyl.thz.notbook.models.AudioTareas;
import net.vyl.thz.notbook.models.MultimediaNotas;
import net.vyl.thz.notbook.models.MultimediaTareas;
import net.vyl.thz.notbook.models.Notas;
import net.vyl.thz.notbook.models.RecordatoriosTareas;
import net.vyl.thz.notbook.models.Tareas;
import net.vyl.thz.notbook.ui.WorkManagerNotify;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentoCrearTarea#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentoCrearTarea extends Fragment {

    MainActivity mainActivity;
    Context context;
    View vista;
    Button btnCalendario;
    Button btnNotificaciones;
    Spinner spinner;
    DaoTareas daoTareas;
    DaoNotas daoNotas;
    DaoMultimediaTareas daoMultimediaTareas;
    DaoMultimediaNotas daoMultimediaNotas;
    DaoRecordatoriosTareas daoRecordatoriosTareas;
    DaoAudioTareas daoAudioTareas;
    DaoAudioNotas daoAudioNotas;
    ArrayAdapter<String> arrayAdapter;
    ArrayList<String> lsSpinner;
    FloatingActionButton fabCrearTarea, fabVideo, fabFoto, fabAudio;
    RecyclerView recyclerView;
    String arrNot [] = new String[4];
    private GridLayoutManager layoutManager;
    boolean isFABOpen;
    public static List<Object> lsMultimedia;
    private MediaRecorder mediaRecorder;
    private String archivoSalida;
    public static AdaptadorMultimedia adaptadorMultimedia;
    private static final int REQUEST_CODE_EXTERNAL = 1001;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final String ARG_ID_TAREA = "id_tarea";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        if (context instanceof MainActivity){
            mainActivity = (MainActivity) context;
        }
        daoTareas = new DaoTareas(context);
        daoNotas = new DaoNotas(context);
        daoMultimediaNotas = new DaoMultimediaNotas(context);
        daoMultimediaTareas = new DaoMultimediaTareas(context);
        daoRecordatoriosTareas = new DaoRecordatoriosTareas(context);
        daoAudioNotas = new DaoAudioNotas(context);
        daoAudioTareas = new DaoAudioTareas(context);
    }

    public FragmentoCrearTarea() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentoCrearTarea.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentoCrearTarea newInstance(String param1, String param2) {
        FragmentoCrearTarea fragment = new FragmentoCrearTarea();
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
        vista = inflater.inflate(R.layout.fragment_crear_tarea, container, false);
        //asignarle la fecha y hora
        lsMultimedia = new ArrayList<>();
        TextView tv =(TextView)vista.findViewById(R.id.fecha_crear_tarea);
        tv.setText(R.string.fecha_cumplimiento_crear_tarea);
        btnCalendario = vista.findViewById(R.id.btn_calendario_tarea);
        btnCalendario.setOnClickListener( v -> {
            tv.setText(R.string.fecha_cumplimiento_crear_tarea);
            FragmentoFecha fragmentoFecha = new FragmentoFecha(tv, mainActivity);
            fragmentoFecha.show(mainActivity.getSupportFragmentManager(), "datePicker");
        });
        spinner = ((Spinner)vista.findViewById(R.id.recordatorios_crear_tarea));
        if(spinner.getAdapter() == null){
            Log.d("Prueba", "onCreateView: Entró a null");
            lsSpinner = new ArrayList<String>();
            arrayAdapter = new ArrayAdapter<String>(mainActivity, android.R.layout.simple_spinner_item, lsSpinner);
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(arrayAdapter);
        }
        btnNotificaciones = vista.findViewById(R.id.btn_recordatorio_tarea);
        btnNotificaciones.setOnClickListener(v -> {
            FragmentoFecha fragmentoFecha = new FragmentoFecha(spinner, mainActivity);
            fragmentoFecha.show(mainActivity.getSupportFragmentManager(), "datePicker");
        });
        TextView txtEliminar = vista.findViewById(R.id.eliminar_notificación);
        txtEliminar.setOnClickListener(v -> {
            if (((ArrayAdapter)spinner.getAdapter()).getCount() != 0){
                Bundle args = getArguments();
                if(args != null){
                    int position = args.getInt(ARG_ID_TAREA);
                    List<RecordatoriosTareas> ls = daoRecordatoriosTareas.getAllByIDTarea(position);
                    RecordatoriosTareas rc = ls.get(spinner.getSelectedItemPosition());
                    if (daoRecordatoriosTareas.delete(rc.getIdRecTarea())){
                        ((ArrayAdapter)spinner.getAdapter()).remove(((ArrayAdapter)spinner.getAdapter()).getItem(spinner.getSelectedItemPosition()));
                    }
                }else {
                    ((ArrayAdapter)spinner.getAdapter()).remove(((ArrayAdapter)spinner.getAdapter()).getItem(spinner.getSelectedItemPosition()));
                }
            }
        });
        fabFoto = vista.findViewById(R.id.fab_imagen_tareas);
        fabVideo = vista.findViewById(R.id.fab_video_tareas);
        fabAudio = vista.findViewById(R.id.fab_audio_tarea);
        fabCrearTarea = vista.findViewById(R.id.fab_crear_tarea);
        fabFoto.setVisibility(View.GONE);
        fabVideo.setVisibility(View.GONE);
        fabAudio.setVisibility(View.GONE);
        fabCrearTarea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isFABOpen){
                    showFABMenu();
                }else{
                    closeFABMenu();
                }
            }
        });
        fabFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeFABMenu();
                dispatchTakePictureIntent();
            }
        });
        fabVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeFABMenu();
                dispatchTakeVideoIntent();
            }
        });
        fabAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeFABMenu();
                grabarAudio();
            }
        });
        Bundle args = getArguments();
        if(args != null){
            int position = args.getInt(ARG_ID_TAREA);
            ponInfoTarea(position, vista);
        }
        recyclerView = vista.findViewById(R.id.multimedia_tarea);
        layoutManager = new GridLayoutManager(mainActivity, 2);
        //if (adaptadorMultimedia == null) {
            adaptadorMultimedia = new AdaptadorMultimedia(lsMultimedia, mainActivity);
        //}
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
                        Log.d("Prueba", "onCreateView: UriMultimediaSeleccionada - " + mulNota.getMultimedia() + " / " + context.getContentResolver().getType(Uri.parse(mulNota.getMultimedia())));
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
                    if (audNota != null) {
                        mainActivity.mostrarDetalleAudio(audNota);
                    }
                    break;
                case "AudTar":
                    audTarea = daoAudioTareas.getOneByID(Integer.parseInt(mul[1]));
                    if (audTarea != null) {
                        mainActivity.mostrarDetalleAudio(audTarea);
                    }
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
        ((TextView) vista.findViewById(R.id.titulo_crear_tarea)).setText(tareas.getTitulo());
        ((TextView) vista.findViewById(R.id.descripcion_crear_tarea)).setText(tareas.getDescripcion());
        ((TextView) vista.findViewById(R.id.fecha_crear_tarea)).setText(tareas.getFecha_cumplimiento());
        ((TextView) vista.findViewById(R.id.contenido_crear_tarea)).setText(tareas.getContenido());
        List<RecordatoriosTareas> ls = daoRecordatoriosTareas.getAllByIDTarea(tareas.getIdTarea());
        Log.d("Prueba", "ponInfoTarea: " + ls.size());
        for(RecordatoriosTareas rec : ls){
            lsSpinner.add(rec.getRecordatorio());
        }
        arrayAdapter.notifyDataSetChanged();
        lsMultimedia.addAll(daoMultimediaTareas.getAllByIDNota(id));
        Log.d("Prueba", "ponInfoNota: longitud multimedia " + lsMultimedia.size());
        lsMultimedia.addAll(daoAudioTareas.getAllByIDTarea(id));
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        //super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menuguardar, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        Bundle args = getArguments();
        switch (id) {
            case R.id.item_convertir:
                AlertDialog.Builder cuadroDialogo2 = new AlertDialog.Builder(mainActivity);
                cuadroDialogo2.setTitle("¿Desea convertir la tarea a nota?");
                cuadroDialogo2.setItems(new String[]{"Aceptar", "Cancelar"}, (dialogInterface2, j) -> {
                    switch (j){
                        case 0:
                            if(args != null){
                                //cuando ya trae información
                                int position = args.getInt(ARG_ID_TAREA);
                                Tareas tareas = FragmentoTareas.daoTareas.getOneByID(position);
                                Calendar c = Calendar.getInstance();
                                int year = c.get(Calendar.YEAR);
                                int month = c.get(Calendar.MONTH);
                                int day = c.get(Calendar.DAY_OF_MONTH);
                                int hour = c.get(Calendar.HOUR_OF_DAY);
                                int minute = c.get(Calendar.MINUTE);
                                String fecha = "" + year + "-" + (month<10?"0"+month:month+"") + "-" + (day<10?"0"+day:day+"") + " " + (hour<10?"0"+hour:hour+"") + ":" + (minute<10?"0"+minute:minute+"");
                                Notas notas = new Notas(0, tareas.getTitulo(), tareas.getDescripcion(), tareas.getContenido(),fecha);
                                long x = FragmentoNota.daoNotas.insert(notas);
                                if(x != -1){
                                    List<MultimediaTareas> multiTar = daoMultimediaTareas.getAllByIDNota(position);
                                    if (multiTar.size()!= 0){
                                        for (int i=0;i<multiTar.size();i++) {
                                            MultimediaNotas n = new MultimediaNotas(0,x,
                                                    multiTar.get(i).getDescripcion(),multiTar.get(i).getMultimedia());
                                            daoMultimediaNotas.insert(n);
                                        }
                                    }
                                    List<AudioTareas> audTar = daoAudioTareas.getAllByIDTarea(position);
                                    if (audTar.size()!= 0){
                                        for (int i=0;i<audTar.size();i++) {
                                            AudioNotas n = new AudioNotas(0,x,audTar.get(i).getAudio());
                                            daoAudioNotas.insert(n);
                                        }
                                    }
                                    daoTareas.delete(tareas.getIdTarea());
                                    FragmentoTareas.actulizarRecycler();
                                    FragmentoNota.actulizarRecycler();
                                    FragmentManager fragmentManager = mainActivity.getSupportFragmentManager();
                                    fragmentManager.beginTransaction().remove(fragmentManager.getFragments().get(fragmentManager.getFragments().size()-1)).remove(fragmentManager.getFragments().get(fragmentManager.getFragments().size()-1)).commit();
                                    fragmentManager.popBackStack();
                                    mainActivity.onBackPressed();

                                }
                            }else{
                                /* Llamar al fragment de crear Nota sin parámetros.*/
                                setHasOptionsMenu(false);
                                FragmentManager fragmentManager = mainActivity.getSupportFragmentManager();
                                FragmentoCrearNota fragmentoCrearNota = new FragmentoCrearNota();
                                fragmentManager.beginTransaction().remove(fragmentManager.getFragments().get(fragmentManager.getFragments().size()-1)).add(R.id.contenedor_prquenio, fragmentoCrearNota).addToBackStack(null).commit();
                            }
                            break;
                        case 1:
                            break;
                    }
                });

                cuadroDialogo2.show();
                return true;
            case R.id.item_guardar:
                //  Guardar en base de datos al actualizar
                if(args != null){
                    int position = args.getInt(ARG_ID_TAREA);
                    String titulo = ((TextView) vista.findViewById(R.id.titulo_crear_tarea)).getText().toString();
                    String descripcion = ((TextView) vista.findViewById(R.id.descripcion_crear_tarea)).getText().toString();
                    String contenido = ((TextView) vista.findViewById(R.id.contenido_crear_tarea)).getText().toString();
                    Calendar c = Calendar.getInstance();
                    int year = c.get(Calendar.YEAR);
                    int month = c.get(Calendar.MONTH);
                    int day = c.get(Calendar.DAY_OF_MONTH);
                    int hour = c.get(Calendar.HOUR_OF_DAY);
                    int minute = c.get(Calendar.MINUTE);
                    String fecha = "" + year + "-" + (month<10?"0"+month:month+"") + "-" + (day<10?"0"+day:day+"") + " " + (hour<10?"0"+hour:hour+"") + ":" + (minute<10?"0"+minute:minute+"");

                    String fechaCumplimiento = ((TextView)vista.findViewById(R.id.fecha_crear_tarea)).getText().toString();

                    Tareas tareas = new Tareas(position,titulo,descripcion,contenido,fecha,fechaCumplimiento,0);
                    if(daoTareas.update(tareas)){
                        daoRecordatoriosTareas.deleteAllTarea(tareas.getIdTarea());
                        for (int i = 0; i<((ArrayAdapter)spinner.getAdapter()).getCount(); i++){
                            RecordatoriosTareas recordatoriosTareas = new RecordatoriosTareas(0, tareas.getIdTarea(), ((ArrayAdapter)spinner.getAdapter()).getItem(i).toString());
                            Long x = daoRecordatoriosTareas.insert(recordatoriosTareas);
                            arrNot[0] = x + "";
                            arrNot[1] = tareas.getTitulo();
                            arrNot[2] = tareas.getDescripcion();
                            arrNot[3] = ((ArrayAdapter)spinner.getAdapter()).getItem(i).toString();
                            saveNotify(arrNot);
                        }
                        for (Object obj : lsMultimedia){
                            if (obj instanceof  MultimediaTareas) {
                                MultimediaTareas mul = (MultimediaTareas) obj;
                                if (mul.getIdMulTarea() == 0){
                                    mul.setIdTarea(tareas.getIdTarea());
                                    daoMultimediaTareas.insert(mul);
                                }
                            }
                            if (obj instanceof AudioTareas){
                                AudioTareas aud = (AudioTareas) obj;
                                if (aud.getIdAudTarea() == 0){
                                    daoAudioTareas.insert(aud);
                                }
                            }
                        }
                        FragmentoTareas.actulizarRecycler();
                        InputMethodManager inputMethodManager = (InputMethodManager) mainActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(((TextView) vista.findViewById(R.id.titulo_crear_tarea)).getWindowToken(), 0);
                        inputMethodManager.hideSoftInputFromWindow(((TextView) vista.findViewById(R.id.descripcion_crear_tarea)).getWindowToken(), 0);
                        inputMethodManager.hideSoftInputFromWindow(((TextView) vista.findViewById(R.id.contenido_crear_tarea)).getWindowToken(), 0);
                        inputMethodManager.hideSoftInputFromWindow(((TextView) vista.findViewById(R.id.fecha_crear_tarea)).getWindowToken(), 0);
                        mainActivity.onBackPressed();
                    }
                }else{
                    if(vista != null){
                        String titulo = ((TextView) vista.findViewById(R.id.titulo_crear_tarea)).getText().toString();
                        String descripcion = ((TextView) vista.findViewById(R.id.descripcion_crear_tarea)).getText().toString();
                        String contenido = ((TextView) vista.findViewById(R.id.contenido_crear_tarea)).getText().toString();
                        Calendar c = Calendar.getInstance();
                        int year = c.get(Calendar.YEAR);
                        int month = c.get(Calendar.MONTH);
                        int day = c.get(Calendar.DAY_OF_MONTH);
                        int hour = c.get(Calendar.HOUR_OF_DAY);
                        int minute = c.get(Calendar.MINUTE);
                        String fecha = "" + year + "-" + (month<10?"0"+month:month+"") + "-" + (day<10?"0"+day:day+"") + " " + (hour<10?"0"+hour:hour+"") + ":" + (minute<10?"0"+minute:minute+"");
                        String fechaCumplimiento = ((TextView) vista.findViewById(R.id.fecha_crear_tarea)).getText().toString();
                        //verificar si tiene fecha de cumplimiento para guardar
                        if(fechaCumplimiento.equals("") | fechaCumplimiento.equals(R.string.fecha_cumplimiento_crear_tarea) | fechaCumplimiento.equals("Due date")){
                            //si no tiene:
                            Toast toast1 =
                                    Toast.makeText(getContext(),
                                            "No puedes guardar sin colocar fecha de cumplimiento", Toast.LENGTH_SHORT);

                            toast1.show();
                        }else {
                            //Si tiene hacer el proceso
                            Tareas tareas = new Tareas(0,titulo,descripcion,contenido,fecha,fechaCumplimiento,0);
                            long valorPrueba = daoTareas.insert(tareas);
                            arrNot[0] = valorPrueba + "";
                            arrNot[1] = tareas.getTitulo();
                            arrNot[2] = tareas.getDescripcion();
                            arrNot[3] = tareas.getFecha_cumplimiento();
                            saveNotify(arrNot);
                            //guardar los recordatorios

                            if(valorPrueba != -1){
                                for (int i = 0; i<((ArrayAdapter)spinner.getAdapter()).getCount(); i++){
                                    RecordatoriosTareas recordatoriosTareas = new RecordatoriosTareas(0, valorPrueba, ((ArrayAdapter)spinner.getAdapter()).getItem(i).toString());
                                    Long x = daoRecordatoriosTareas.insert(recordatoriosTareas);
                                    arrNot[0] = x + "";
                                    arrNot[1] = tareas.getTitulo();
                                    arrNot[2] = tareas.getDescripcion();
                                    arrNot[3] = ((ArrayAdapter)spinner.getAdapter()).getItem(i).toString();
                                    saveNotify(arrNot);
                                }
                                for (Object obj : lsMultimedia){
                                    if (obj instanceof  MultimediaTareas) {
                                        MultimediaTareas mul = (MultimediaTareas) obj;
                                        mul.setIdTarea(valorPrueba);
                                        daoMultimediaTareas.insert(mul);
                                    }
                                    if (obj instanceof AudioTareas){
                                        AudioTareas aud = (AudioTareas) obj;
                                        Log.d("Prueba", "onOptionsItemSelected: Instancia de AudioTareas IDTarea " + aud.getIdTarea());
                                        if (aud.getIdTarea() == 0){
                                            aud.setIdTarea(valorPrueba);
                                            Log.d("Prueba", "onOptionsItemSelected: " + daoAudioTareas.insert(aud));
                                        }
                                    }
                                }
                                FragmentoTareas.actulizarRecycler();
                                InputMethodManager inputMethodManager = (InputMethodManager) mainActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                                inputMethodManager.hideSoftInputFromWindow(((TextView) vista.findViewById(R.id.titulo_crear_tarea)).getWindowToken(), 0);
                                inputMethodManager.hideSoftInputFromWindow(((TextView) vista.findViewById(R.id.descripcion_crear_tarea)).getWindowToken(), 0);
                                inputMethodManager.hideSoftInputFromWindow(((TextView) vista.findViewById(R.id.contenido_crear_tarea)).getWindowToken(), 0);
                                inputMethodManager.hideSoftInputFromWindow(((TextView) vista.findViewById(R.id.fecha_crear_tarea)).getWindowToken(), 0);
                                mainActivity.onBackPressed();
                            }
                        }

                    }
                }
                return true;
        }
        return false;
    }

    private void showFABMenu(){
        isFABOpen=true;

        fabFoto.setVisibility(View.VISIBLE);
        fabVideo.setVisibility(View.VISIBLE);
        fabAudio.setVisibility(View.VISIBLE);

        fabCrearTarea.animate().rotation(360);
        fabFoto.animate().rotation(360);
        fabVideo.animate().rotation(360);
        fabAudio.animate().rotation(360);

        fabFoto.animate().translationY(-getResources().getDimension(R.dimen.standard_55));
        fabVideo.animate().translationY(-getResources().getDimension(R.dimen.standard_105));
        fabAudio.animate().translationY(-getResources().getDimension(R.dimen.standard_155));
    }

    private void closeFABMenu(){
        isFABOpen=false;

        fabCrearTarea.animate().rotation(-360);
        fabFoto.animate().rotation(-360);
        fabVideo.animate().rotation(-360);
        fabAudio.animate().rotation(-360);

        fabFoto.animate().translationY(0);
        fabVideo.animate().translationY(0);
        fabAudio.animate().translationY(0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if(!isFABOpen) {
                    fabFoto.setVisibility(View.GONE);
                    fabVideo.setVisibility(View.GONE);
                    fabAudio.setVisibility(View.GONE);
                }
            }
        });
    }

    static final int SELECT_FILE = 0;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_VIDEO_CAPTURE = 2;

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(mainActivity.getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void dispatchTakeVideoIntent() {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (takeVideoIntent.resolveActivity(mainActivity.getPackageManager()) != null) {
            startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
        }
    }

    private void grabarAudio() {
        if(mediaRecorder == null) {
            Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);
            int second = c.get(Calendar.SECOND);
            String fecha = "" + year + "" + (month<10?"0"+month:month+"") + "" + (day<10?"0"+day:day+"") + "" + (hour<10?"0"+hour:hour+"") + "" + (minute<10?"0"+minute:minute+"") + "" + (second<10?"0"+second:second+"");
            archivoSalida = Environment.getExternalStorageDirectory().getAbsolutePath() + "/audio" + fecha + ".mp3";
            mediaRecorder = new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mediaRecorder.setOutputFile(archivoSalida);
            try {
                mediaRecorder.prepare();
                mediaRecorder.start();
            }catch (IOException e){
                Toast.makeText(getContext(), "Error al grabar " + e, Toast.LENGTH_LONG).show();
            }
            Toast.makeText(getContext(), "Grabando", Toast.LENGTH_LONG).show();
        } else if (mediaRecorder != null){
            mediaRecorder.stop();
            mediaRecorder.release();
            AudioTareas audTar;
            Bundle args = getArguments();
            if(args != null){
                int position = args.getInt(ARG_ID_TAREA);
                audTar = new AudioTareas(0, position, archivoSalida);
            } else{
                audTar = new AudioTareas(0, 0, archivoSalida);
            }
            lsMultimedia.add(audTar);
            adaptadorMultimedia.notifyDataSetChanged();
            mediaRecorder = null;
            Toast.makeText(getContext(), "Grabación finalizada", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            MultimediaTareas multimediaTareas = new MultimediaTareas(0, 0, "", uri.toString());
            lsMultimedia.add(multimediaTareas);
            adaptadorMultimedia.notifyDataSetChanged();
        } else if (requestCode == SELECT_FILE && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            MultimediaTareas multimediaTareas = new MultimediaTareas(0, 0, "", uri.toString());
            lsMultimedia.add(multimediaTareas);
            adaptadorMultimedia.notifyDataSetChanged();
        } else if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            MultimediaTareas multimediaTareas = new MultimediaTareas(0, 0, "", uri.toString());
            lsMultimedia.add(multimediaTareas);
            adaptadorMultimedia.notifyDataSetChanged();
        }
    }

    private void validarPermiso() {
        if (ContextCompat.checkSelfPermission(mainActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(mainActivity, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(mainActivity, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {

        } else if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE) &&
                shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE) &&
                shouldShowRequestPermissionRationale(Manifest.permission.RECORD_AUDIO)) {
            Toast.makeText(mainActivity, "Es necesario conceder los permisos para utilizar la app", Toast.LENGTH_LONG).show();
            ActivityCompat.requestPermissions(mainActivity, new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE , Manifest.permission.RECORD_AUDIO },
                    REQUEST_CODE_EXTERNAL);
        } else {
            // You can directly ask for the permission.
            ActivityCompat.requestPermissions(mainActivity, new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE , Manifest.permission.RECORD_AUDIO },
                    REQUEST_CODE_EXTERNAL);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == FragmentoCrearTarea.REQUEST_CODE_EXTERNAL){
            if(permissions[0].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){

                }else{
                    Toast.makeText(mainActivity, "Inhabilitado por falta de permisos", Toast.LENGTH_LONG).show();
                    mainActivity.onBackPressed();
                }
            }
            if(permissions[1].equals(Manifest.permission.READ_EXTERNAL_STORAGE)){
                if(grantResults[1] == PackageManager.PERMISSION_GRANTED){

                }else{
                    Toast.makeText(mainActivity, "Inhabilitado por falta de permisos", Toast.LENGTH_LONG).show();
                    mainActivity.onBackPressed();
                }
            }
            if(permissions[2].equals(Manifest.permission.READ_EXTERNAL_STORAGE)){
                if(grantResults[2] == PackageManager.PERMISSION_GRANTED){

                }else{
                    Toast.makeText(mainActivity, "Inhabilitado por falta de permisos", Toast.LENGTH_LONG).show();
                    mainActivity.onBackPressed();
                }
            }
        }
    }

    /**
     * Transform the date contained in the chip button to a Date format and create a new notify with the
     * saveNotification method.
     */
    private void saveNotify(String[] arr) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = new Date();
        try {
            date = simpleDateFormat.parse(arr[3]);
        } catch (ParseException ex) {
            Log.v("Exception", ex.getLocalizedMessage());
        }
        Date justNow = new Date();
        long alertTime = date.getTime() - justNow.getTime();
        Data data = saveData(arr[1], arr[2], Integer.parseInt(arr[0]));
        WorkManagerNotify.saveNotification(alertTime, data, arr[0] + "");

    }

    /**
     * Make a Data type to send it to the workmanager and save the notification.
     *
     * @param title          Title that the notification will have
     * @param content        Content of the notification
     * @param idNotification id to identify the notification
     * @return Data that contain the title, content and id of the notification
     */
    private Data saveData(String title, String content, long idNotification) {
        return new Data.Builder()
                .putString("Title", title)
                .putString("Content", content)
                .putLong("id", idNotification).build();
    }

    /**@Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("Adapter", adaptadorMultimedia);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            if ((AdaptadorMultimedia) savedInstanceState.getParcelable("Adapter") != null) {
                adaptadorMultimedia = (AdaptadorMultimedia) savedInstanceState.getParcelable("Adapter");
                recyclerView.setAdapter(adaptadorMultimedia);
                adaptadorMultimedia.notifyDataSetChanged();
            }
        }
    }*/
}