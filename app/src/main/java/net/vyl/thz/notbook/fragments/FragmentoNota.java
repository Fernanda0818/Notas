package net.vyl.thz.notbook.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import net.vyl.thz.notbook.MainActivity;
import net.vyl.thz.notbook.R;
import net.vyl.thz.notbook.adapters.AdaptadorNotas;
import net.vyl.thz.notbook.data.DaoNotas;
import net.vyl.thz.notbook.models.Notas;

import java.util.ArrayList;
import java.util.List;

public class FragmentoNota extends Fragment {
    private RecyclerView recycler;
    public static AdaptadorNotas adaptadorNotas;
    private GridLayoutManager layoutManager;
    MainActivity mainActivity;
    Context context;
    public static DaoNotas daoNotas;
    public static List<Notas> notasList;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        if (context instanceof MainActivity){
            mainActivity = (MainActivity) context;
        }
        daoNotas = new DaoNotas(context);
        notasList = daoNotas.getAll();
        /*notasList = new ArrayList<>();
        notasList.add(new Notas(0,"Lista del mandado", "Comprar los suministros de despensa", "1 kg arroz, 2 kg cebolla", "2020-11-06"));
        notasList.add(new Notas(1,"Psychosocial", "Canción compuesta por Slipknot", "I love your projection but I don't love you Your perceived perfection though it's just not true I want an escape and tonight that's you", "2020-11-06"));
         */
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_nota, container, false);
        recycler = view.findViewById(R.id.recyclerNotas);
        layoutManager = new GridLayoutManager(getActivity(), 2);
        adaptadorNotas = new AdaptadorNotas(notasList, getActivity());
        recycler.setLayoutManager(layoutManager);
        adaptadorNotas.setOnClickListener(vl -> {
            mainActivity.mostrarDetalleNota(Integer.parseInt(((TextView) vl.findViewById(R.id.titulo)).getTag().toString()));
        });
        adaptadorNotas.setOnLongClickListener(vl -> {
            AlertDialog.Builder cuadroDialogo = new AlertDialog.Builder(mainActivity);
            cuadroDialogo.setTitle("Opciones");
            //cuadroDialogo.setMessage("Este es un cuadro de texto");
            cuadroDialogo.setItems(new String[]{"Editar", "Eliminar"}, (dialogInterface, i) -> {
                //Toast.makeText(getActivity(), "Opción selecionada " + i, Toast.LENGTH_LONG).show();
                switch (i){
                    case 0:
                        FragmentManager fragmentManager = mainActivity.getSupportFragmentManager();
                        FragmentoCrearNota fragmentoCrearNota = new FragmentoCrearNota();
                        //notasList.get(recycler.getChildAdapterPosition(vl)).getIdNota();
                        int index = Integer.parseInt(((TextView) vl.findViewById(R.id.titulo)).getTag().toString());
                        Log.d("Prueba", "onCreateView: " + index);
                        //Cuando se maneje la DB utilizar el tag del TextView Título
                        Log.d("Prueba", "onCreateView: " + ((TextView) vl.findViewById(R.id.titulo)).getTag());
                        Bundle bundle = new Bundle();
                        bundle.putInt(fragmentoCrearNota.ARG_ID_NOTA, index);
                        fragmentoCrearNota.setArguments(bundle);
                        fragmentManager.beginTransaction().hide(fragmentManager.getFragments().get(0)).add(R.id.contenedor_prquenio, fragmentoCrearNota).addToBackStack(null).commit();
                        break;
                    case 1:
                        AlertDialog.Builder cuadroDialogo2 = new AlertDialog.Builder(mainActivity);
                        cuadroDialogo2.setTitle("¿Estás seguro de que desea eliminar la nota?");
                        cuadroDialogo2.setItems(new String[]{"Aceptar", "Cancelar"}, (dialogInterface2, j) -> {
                            switch (j){
                                case 0:
                                    daoNotas.delete(Integer.parseInt(((TextView) vl.findViewById(R.id.titulo)).getTag().toString()));
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
        recycler.setAdapter(adaptadorNotas);
        return view;
    }

    public static void actulizarRecycler(){
        notasList.clear();
        notasList.addAll(daoNotas.getAll());
        adaptadorNotas.notifyDataSetChanged();
    }
}
