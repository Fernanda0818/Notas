package net.vyl.thz.notbook.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import net.vyl.thz.notbook.MainActivity;
import net.vyl.thz.notbook.R;
import net.vyl.thz.notbook.data.DaoMultimediaNotas;
import net.vyl.thz.notbook.data.DaoMultimediaTareas;
import net.vyl.thz.notbook.models.MultimediaNotas;
import net.vyl.thz.notbook.models.MultimediaTareas;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentoImagenes#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentoImagenes extends Fragment {

    MainActivity mainActivity;
    Context context;
    View vista;
    MultimediaNotas imagenNotas;
    MultimediaTareas imagenTareas;
    DaoMultimediaTareas daoMultimediaTareas;
    DaoMultimediaNotas daoMultimediaNotas;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final String ARG_ID_MULT = "id_mult";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentoImagenes() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        if (context instanceof MainActivity){
            mainActivity = (MainActivity) context;
        }
        daoMultimediaNotas = new DaoMultimediaNotas(context);
        daoMultimediaTareas = new DaoMultimediaTareas(context);
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentoImagenes.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentoImagenes newInstance(String param1, String param2) {
        FragmentoImagenes fragment = new FragmentoImagenes();
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
        vista = inflater.inflate(R.layout.fragment_imagenes, container, false);
        Bundle args = getArguments();
        if(args != null){
            Object position = args.getSerializable(ARG_ID_MULT);
            ponInfoImagen(position, vista);
        }
        setHasOptionsMenu(true);
        return vista;
    }

    public void ponInfoImagen(Object id){
        ponInfoImagen(id, getView());
    }

    private void ponInfoImagen(Object id, View vista){
        Uri imagen;
        if ( id instanceof MultimediaNotas){
            imagenNotas = (MultimediaNotas) id;
            Log.d("Prueba", "ponInfoImagen: " + imagenNotas.getMultimedia());
            imagen = Uri.parse(imagenNotas.getMultimedia());
            ((EditText) vista.findViewById(R.id.descripcion_imagen)).setText(imagenNotas.getDescripcion());
            ((ImageView) vista.findViewById(R.id.imagen_multimedia)).setImageURI(imagen);
        } else {
            imagenTareas = (MultimediaTareas) id;
            imagen = Uri.parse(imagenTareas.getMultimedia());
            ((EditText) vista.findViewById(R.id.descripcion_imagen)).setText(imagenTareas.getDescripcion());
            ((ImageView) vista.findViewById(R.id.imagen_multimedia)).setImageURI(imagen);
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        //super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menuguardarvideo, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Bundle args = getArguments();
        switch (id) {
            case R.id.guardar_video:
                if (imagenNotas != null){
                    imagenNotas.setDescripcion(((EditText) vista.findViewById(R.id.descripcion_imagen)).getText().toString());
                    if (daoMultimediaNotas.update(imagenNotas)){
                        InputMethodManager inputMethodManager = (InputMethodManager) mainActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(((EditText) vista.findViewById(R.id.descripcion_imagen)).getWindowToken(), 0);
                        mainActivity.onBackPressed();
                    } else {
                        Toast.makeText(mainActivity, "No se pudo actualizar", Toast.LENGTH_LONG).show();
                    }
                } else if (imagenTareas != null) {
                    imagenTareas.setDescripcion(((EditText) vista.findViewById(R.id.descripcion_imagen)).getText().toString());
                    if (daoMultimediaTareas.update(imagenTareas)){
                        InputMethodManager inputMethodManager = (InputMethodManager) mainActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(((EditText) vista.findViewById(R.id.descripcion_imagen)).getWindowToken(), 0);
                        mainActivity.onBackPressed();
                    } else {
                        Toast.makeText(mainActivity, "No se pudo actualizar", Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
        return false;
    }
}