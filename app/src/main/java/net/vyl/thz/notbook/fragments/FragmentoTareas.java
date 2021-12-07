package net.vyl.thz.notbook.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.vyl.thz.notbook.MainActivity;
import net.vyl.thz.notbook.R;
import net.vyl.thz.notbook.adapters.AdaptadorNotas;
import net.vyl.thz.notbook.adapters.AdaptadorTareas;
import net.vyl.thz.notbook.data.DaoNotas;
import net.vyl.thz.notbook.data.DaoTareas;
import net.vyl.thz.notbook.models.Notas;
import net.vyl.thz.notbook.models.Tareas;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentoTareas#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentoTareas extends Fragment {

    private RecyclerView recycler;
    public static AdaptadorTareas adaptadorTareas;
    private GridLayoutManager layoutManager;
    MainActivity mainActivity;
    Context context;
    public static DaoTareas daoTareas;
    public static List<Tareas> tareasList;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        if (context instanceof MainActivity){
            mainActivity = (MainActivity) context;
        }
        daoTareas = new DaoTareas(context);
        tareasList = daoTareas.getAll();
    }

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentoTareas() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentoTareas.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentoTareas newInstance(String param1, String param2) {
        FragmentoTareas fragment = new FragmentoTareas();
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
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_tareas, container, false);
        recycler = view.findViewById(R.id.recyclerTareas);
        layoutManager = new GridLayoutManager(getActivity(), 2);
        adaptadorTareas = new AdaptadorTareas(tareasList, getActivity());
        recycler.setLayoutManager(layoutManager);
        adaptadorTareas.setOnClickListener(vl -> {
            mainActivity.mostrarDetalleTarea(Integer.parseInt(((TextView) vl.findViewById(R.id.titulo)).getTag().toString()));
        });
        adaptadorTareas.setOnLongClickListener(vl -> {
            AlertDialog.Builder cuadroDialogo = new AlertDialog.Builder(mainActivity);
            cuadroDialogo.setTitle("Opciones");
            //cuadroDialogo.setMessage("Este es un cuadro de texto");
            cuadroDialogo.setItems(new String[]{"Editar", "Eliminar"}, (dialogInterface, i) -> {
                //Toast.makeText(getActivity(), "Opción selecionada " + i, Toast.LENGTH_LONG).show();
                switch (i){
                    case 0:
                        FragmentManager fragmentManager = mainActivity.getSupportFragmentManager();
                        FragmentoCrearTarea fragmentoCrearTarea = new FragmentoCrearTarea();
                        //notasList.get(recycler.getChildAdapterPosition(vl)).getIdNota();
                        int index = Integer.parseInt(((TextView) vl.findViewById(R.id.titulo)).getTag().toString());

                        //Cuando se maneje la DB utilizar el tag del TextView Título

                        Bundle bundle = new Bundle();
                        bundle.putInt(fragmentoCrearTarea.ARG_ID_TAREA, index);
                        fragmentoCrearTarea.setArguments(bundle);
                        fragmentManager.beginTransaction().hide(fragmentManager.getFragments().get(0)).add(R.id.contenedor_prquenio, fragmentoCrearTarea).addToBackStack(null).commit();
                        break;
                    case 1:
                        AlertDialog.Builder cuadroDialogo2 = new AlertDialog.Builder(mainActivity);
                        cuadroDialogo2.setTitle("¿Estás seguro de que desea eliminar la tarea?");
                        cuadroDialogo2.setItems(new String[]{"Aceptar", "Cancelar"}, (dialogInterface2, j) -> {
                            switch (j){
                                case 0:
                                    daoTareas.delete(Integer.parseInt(((TextView) vl.findViewById(R.id.titulo)).getTag().toString()));
                                    actulizarRecycler();
                                    break;
                                case 1:
                                    break;
                            }
                        });
                        cuadroDialogo2.show();
                        break;
                }
            });
            //cuadroDialogo.setPositiveButton("Ok", (vcd, i) -> {});
            cuadroDialogo.show();
            return false;
        });
        recycler.setAdapter(adaptadorTareas);
        return view;
    }

    public static void actulizarRecycler(){
        tareasList.clear();
        tareasList.addAll(daoTareas.getAll());
        adaptadorTareas.notifyDataSetChanged();
    }
}