package net.vyl.thz.notbook.fragments;

import android.app.SearchManager;
import android.content.ClipData;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import net.vyl.thz.notbook.MainActivity;
import net.vyl.thz.notbook.R;
import net.vyl.thz.notbook.ui.main.PlaceholderFragment;
import net.vyl.thz.notbook.ui.main.SectionsPagerAdapter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class FragmentoTabbed extends Fragment {
    Context context;
    MainActivity mainActivity;
    TabLayout tabs;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        if (context instanceof MainActivity){
            mainActivity = (MainActivity) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_tabbed, container, false);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(context, mainActivity.getSupportFragmentManager());
        ViewPager viewPager = view.findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        tabs = view.findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        FloatingActionButton fab = view.findViewById(R.id.fab);
        Log.d("Prueba", "OnCreateView FragmentTabbed");
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Prueba", "onClick: " + tabs.getSelectedTabPosition());
                if(tabs.getSelectedTabPosition() == 0){
                    FragmentManager fragmentManager = mainActivity.getSupportFragmentManager();
                    FragmentoCrearNota fragmentoCrearNota = new FragmentoCrearNota();
                    fragmentManager.beginTransaction().hide(fragmentManager.getFragments().get(0)).add(R.id.contenedor_prquenio, fragmentoCrearNota).addToBackStack(null).commit();

                } else if(tabs.getSelectedTabPosition() == 1){

                    FragmentManager fragmentManager = mainActivity.getSupportFragmentManager();
                    FragmentoCrearTarea fragmentoCrearTarea = new FragmentoCrearTarea();
                    fragmentManager.beginTransaction().hide(fragmentManager.getFragments().get(0)).add(R.id.contenedor_prquenio,fragmentoCrearTarea).addToBackStack(null).commit();


                }

            }
        });
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        //super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menubuscar, menu);
        SearchManager searchManager = (SearchManager) mainActivity.getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.item_busqueda).getActionView();
        if (searchView != null){
            searchView.setSearchableInfo(searchManager.getSearchableInfo(mainActivity.getComponentName()));
            searchView.setIconifiedByDefault(false);
        }
        SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                if (tabs.getSelectedTabPosition() == 0){
                    FragmentoNota.adaptadorNotas.busqueda(query);
                } else if(tabs.getSelectedTabPosition() == 1){
                    //Busqueda de tareas
                    FragmentoTareas.adaptadorTareas.busquedaTareas(query);
                }
                return true;
            }


            @Override
            public boolean onQueryTextChange(String newText) {
                if (tabs.getSelectedTabPosition() == 0){
                    FragmentoNota.adaptadorNotas.busqueda(newText);
                }else if(tabs.getSelectedTabPosition() == 1){
                    //Busqueda de tareas
                    FragmentoTareas.adaptadorTareas.busquedaTareas(newText);
                }
                return true;
            }
        };
        searchView.setOnQueryTextListener(queryTextListener);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        /*switch (id) {
            case R.id.item_busqueda:
                // do stuff
                return true;
        }*/
        return false;
    }
}
