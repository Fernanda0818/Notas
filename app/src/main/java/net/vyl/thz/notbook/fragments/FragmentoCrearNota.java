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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import net.vyl.thz.notbook.models.Tareas;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentoCrearNota#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentoCrearNota extends Fragment {


    MainActivity mainActivity;
    Context context;
    View vista;
    DaoNotas daoNotas;
    DaoMultimediaTareas daoMultimediaTareas;
    DaoMultimediaNotas daoMultimediaNotas;
    DaoAudioTareas daoAudioTareas;
    DaoAudioNotas daoAudioNotas;
    private MediaRecorder mediaRecorder;
    private String archivoSalida;
    FloatingActionButton fabCrearNota, fabVideo, fabFoto, fabAudio;
    boolean isFABOpen;
    RecyclerView recyclerView;
    private GridLayoutManager layoutManager;
    public static AdaptadorMultimedia adaptadorMultimedia;
    public static List<Object> lsMultimedia;
    private static final int REQUEST_CODE_EXTERNAL = 1001;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final String ARG_ID_NOTA = "id_nota";

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
        daoNotas = new DaoNotas(context);
        daoMultimediaNotas = new DaoMultimediaNotas(context);
        daoMultimediaTareas = new DaoMultimediaTareas(context);
        daoAudioNotas = new DaoAudioNotas(context);
        daoAudioTareas = new DaoAudioTareas(context);
    }

    public FragmentoCrearNota() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentoCrearNota.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentoCrearNota newInstance(String param1, String param2) {
        FragmentoCrearNota fragment = new FragmentoCrearNota();
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
        validarPermiso();
        vista = inflater.inflate(R.layout.fragment_crear_nota, container, false);
        lsMultimedia = new ArrayList<>();
        fabFoto = vista.findViewById(R.id.fab_imagen_notas);
        fabVideo = vista.findViewById(R.id.fab_video_notas);
        fabAudio = vista.findViewById(R.id.fab_audio_notas);
        fabCrearNota = vista.findViewById(R.id.fab_crear_nota);
        fabFoto.setVisibility(View.GONE);
        fabVideo.setVisibility(View.GONE);
        fabAudio.setVisibility(View.GONE);
        fabCrearNota.setOnClickListener(new View.OnClickListener() {
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
            int position = args.getInt(ARG_ID_NOTA);
            ponInfoNota(position, vista);
        }
        recyclerView = vista.findViewById(R.id.recycler_mult_notas);
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

    public void ponInfoNota(int id){
        ponInfoNota(id, getView());
    }

    private void ponInfoNota(int id, View vista){
        Notas notas = FragmentoNota.daoNotas.getOneByID(id);
        ((TextView) vista.findViewById(R.id.titulo_crear_nota)).setText(notas.getTitulo());
        ((TextView) vista.findViewById(R.id.descripcion_crear_nota)).setText(notas.getDescripcion());
        ((TextView) vista.findViewById(R.id.contenido_crear_nota)).setText(notas.getContenido());
        lsMultimedia.addAll(daoMultimediaNotas.getAllByIDNota(id));
        Log.d("Prueba", "ponInfoNota: longitud multimedia " + lsMultimedia.size());
        lsMultimedia.addAll(daoAudioNotas.getAllByIDNota(id));
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        //super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menuguardar, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Bundle args = getArguments();
        switch (id) {
            case R.id.item_convertir:
                AlertDialog.Builder cuadroDialogo2 = new AlertDialog.Builder(mainActivity);
                cuadroDialogo2.setTitle("¿Desea convertir la nota a tarea?");
                cuadroDialogo2.setItems(new String[]{"Aceptar", "Cancelar"}, (dialogInterface2, j) -> {
                    switch (j){
                        case 0:
                            if(args != null){
                                //cuando se va a convertir a tarea y ya trae información
                                int position = args.getInt(ARG_ID_NOTA);
                                Notas notas = FragmentoNota.daoNotas.getOneByID(position);
                                Calendar c = Calendar.getInstance();
                                int year = c.get(Calendar.YEAR);
                                int month = c.get(Calendar.MONTH);
                                int day = c.get(Calendar.DAY_OF_MONTH);
                                int hour = c.get(Calendar.HOUR_OF_DAY);
                                int minute = c.get(Calendar.MINUTE);
                                String fecha = "" + year + "-" + (month<10?"0"+month:month+"") + "-" + (day<10?"0"+day:day+"") + " " + (hour<10?"0"+hour:hour+"") + ":" + (minute<10?"0"+minute:minute+"");
                                Tareas tareas = new Tareas(0, notas.getTitulo(), notas.getDescripcion(), notas.getContenido(), fecha, "",0);
                                long x = FragmentoTareas.daoTareas.insert(tareas);
                                if(x != -1){
                                    List<MultimediaNotas> multinot = daoMultimediaNotas.getAllByIDNota(position);
                                    if (multinot.size()!= 0){
                                        for (int i=0;i<multinot.size();i++) {
                                            MultimediaTareas m = new MultimediaTareas(0,x,
                                                    multinot.get(i).getDescripcion(),multinot.get(i).getMultimedia());
                                            daoMultimediaTareas.insert(m);
                                        }
                                    }
                                    List<AudioNotas> audNot = daoAudioNotas.getAllByIDNota(position);
                                    if (audNot.size()!= 0){
                                        for (int i=0;i<audNot.size();i++) {
                                            AudioTareas n = new AudioTareas(0,x,audNot.get(i).getAudio());
                                            daoAudioTareas.insert(n);
                                        }
                                    }
                                    daoNotas.delete(notas.getIdNota());
                                    FragmentoNota.actulizarRecycler();
                                    FragmentoTareas.actulizarRecycler();
                                    FragmentManager fragmentManager = mainActivity.getSupportFragmentManager();
                                    fragmentManager.beginTransaction().remove(fragmentManager.getFragments().get(fragmentManager.getFragments().size()-1)).remove(fragmentManager.getFragments().get(fragmentManager.getFragments().size()-1)).commit();
                                    fragmentManager.popBackStack();
                                    mainActivity.onBackPressed();
                                }
                            }else{
                                //cuando no se ha guardado
                                FragmentManager fragmentManager = mainActivity.getSupportFragmentManager();
                                FragmentoCrearTarea fragmentoCrearTarea = new FragmentoCrearTarea();
                                fragmentManager.beginTransaction().remove(fragmentManager.getFragments().get(fragmentManager.getFragments().size()-1)).add(R.id.contenedor_prquenio,fragmentoCrearTarea).addToBackStack(null).commit();
                            }
                            break;
                        case 1:
                            break;
                    }
                });
                cuadroDialogo2.show();
                return true;
            case R.id.item_guardar:
                    //  Guardar en base de datos cuando se va a actualizar
                if(args != null){
                    int position = args.getInt(ARG_ID_NOTA);
                    String titulo = ((TextView) vista.findViewById(R.id.titulo_crear_nota)).getText().toString();
                    String descripcion = ((TextView) vista.findViewById(R.id.descripcion_crear_nota)).getText().toString();
                    String contenido = ((TextView) vista.findViewById(R.id.contenido_crear_nota)).getText().toString();
                    Calendar c = Calendar.getInstance();
                    int year = c.get(Calendar.YEAR);
                    int month = c.get(Calendar.MONTH);
                    int day = c.get(Calendar.DAY_OF_MONTH);
                    int hour = c.get(Calendar.HOUR_OF_DAY);
                    int minute = c.get(Calendar.MINUTE);
                    String fecha = "" + year + "-" + (month<10?"0"+month:month+"") + "-" + (day<10?"0"+day:day+"") + " " + (hour<10?"0"+hour:hour+"") + ":" + (minute<10?"0"+minute:minute+"");
                    Notas notas = new Notas(position, titulo, descripcion, contenido, fecha);
                    if(daoNotas.update(notas)){
                        for (Object obj : lsMultimedia){
                            if (obj instanceof  MultimediaNotas) {
                                MultimediaNotas mul = (MultimediaNotas) obj;
                                if (mul.getIdMulNota() == 0){
                                    mul.setIdNota(notas.getIdNota());
                                    daoMultimediaNotas.insert(mul);
                                }
                            }
                            if (obj instanceof AudioNotas){
                                AudioNotas aud = (AudioNotas) obj;
                                if (aud.getIdAudNota() == 0){
                                    daoAudioNotas.insert(aud);
                                }
                            }
                        }
                        FragmentoNota.actulizarRecycler();
                        Log.d("Prueba", "onOptionsItemSelected: " + FragmentoNota.notasList.size());
                        //Sirve para ocultar teclado
                        InputMethodManager inputMethodManager = (InputMethodManager) mainActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(((TextView) vista.findViewById(R.id.titulo_crear_nota)).getWindowToken(), 0);
                        inputMethodManager.hideSoftInputFromWindow(((TextView) vista.findViewById(R.id.descripcion_crear_nota)).getWindowToken(), 0);
                        inputMethodManager.hideSoftInputFromWindow(((TextView) vista.findViewById(R.id.contenido_crear_nota)).getWindowToken(), 0);
                        mainActivity.onBackPressed();
                    }
                }else{
                    //guardar por primera vez
                    if(vista != null){
                        String titulo = ((TextView) vista.findViewById(R.id.titulo_crear_nota)).getText().toString();
                        String descripcion = ((TextView) vista.findViewById(R.id.descripcion_crear_nota)).getText().toString();
                        String contenido = ((TextView) vista.findViewById(R.id.contenido_crear_nota)).getText().toString();
                        Calendar c = Calendar.getInstance();
                        int year = c.get(Calendar.YEAR);
                        int month = c.get(Calendar.MONTH);
                        int day = c.get(Calendar.DAY_OF_MONTH);
                        int hour = c.get(Calendar.HOUR_OF_DAY);
                        int minute = c.get(Calendar.MINUTE);
                        String fecha = "" + year + "-" + (month<10?"0"+month:month+"") + "-" + (day<10?"0"+day:day+"") + " " + (hour<10?"0"+hour:hour+"") + ":" + (minute<10?"0"+minute:minute+"");
                        Notas notas = new Notas(0, titulo, descripcion, contenido, fecha);
                        long x = FragmentoNota.daoNotas.insert(notas);
                        Log.d("Prueba", "onOptionsItemSelected: ID NOTA: " + x);
                        if( x != -1){
                            for (Object obj : lsMultimedia){
                                if (obj instanceof  MultimediaNotas) {
                                    MultimediaNotas mul = (MultimediaNotas) obj;
                                    mul.setIdNota(x);
                                    daoMultimediaNotas.insert(mul);
                                }
                                if (obj instanceof AudioNotas){
                                    AudioNotas aud = (AudioNotas) obj;
                                    if (aud.getIdNota() == 0){
                                        aud.setIdNota(x);
                                        daoAudioNotas.insert(aud);
                                    }
                                }
                            }
                            FragmentoNota.actulizarRecycler();
                            Log.d("Prueba", "onOptionsItemSelected: " + FragmentoNota.notasList.size());
                            InputMethodManager inputMethodManager = (InputMethodManager) mainActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                            inputMethodManager.hideSoftInputFromWindow(((TextView) vista.findViewById(R.id.titulo_crear_nota)).getWindowToken(), 0);
                            inputMethodManager.hideSoftInputFromWindow(((TextView) vista.findViewById(R.id.descripcion_crear_nota)).getWindowToken(), 0);
                            inputMethodManager.hideSoftInputFromWindow(((TextView) vista.findViewById(R.id.contenido_crear_nota)).getWindowToken(), 0);
                            mainActivity.onBackPressed();
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

        fabCrearNota.animate().rotation(360);
        fabFoto.animate().rotation(360);
        fabVideo.animate().rotation(360);
        fabAudio.animate().rotation(360);

        fabFoto.animate().translationY(-getResources().getDimension(R.dimen.standard_55));
        fabVideo.animate().translationY(-getResources().getDimension(R.dimen.standard_105));
        fabAudio.animate().translationY(-getResources().getDimension(R.dimen.standard_155));
    }

    private void closeFABMenu(){
        isFABOpen=false;

        fabCrearNota.animate().rotation(-360);
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
            AudioNotas audNot;
            Bundle args = getArguments();
            if(args != null){
                int position = args.getInt(ARG_ID_NOTA);
                 audNot = new AudioNotas(0, position, archivoSalida);
            } else{
                audNot = new AudioNotas(0, 0, archivoSalida);
            }
            lsMultimedia.add(audNot);
            adaptadorMultimedia.notifyDataSetChanged();
            mediaRecorder = null;
            Toast.makeText(getContext(), "Grabación finalizada", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            MultimediaNotas multimediaNotas = new MultimediaNotas(0, 0, "", uri.toString());
            lsMultimedia.add(multimediaNotas);
            adaptadorMultimedia.notifyDataSetChanged();
        } else if (requestCode == SELECT_FILE && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            MultimediaNotas multimediaNotas = new MultimediaNotas(0, 0, "", uri.toString());
            lsMultimedia.add(multimediaNotas);
            adaptadorMultimedia.notifyDataSetChanged();
        } else if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            MultimediaNotas multimediaNotas = new MultimediaNotas(0, 0, "", uri.toString());
            lsMultimedia.add(multimediaNotas);
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
        if(requestCode == FragmentoCrearNota.REQUEST_CODE_EXTERNAL){
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
}