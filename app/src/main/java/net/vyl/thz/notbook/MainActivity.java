package net.vyl.thz.notbook;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import net.vyl.thz.notbook.fragments.FragmentoImagenes;
import net.vyl.thz.notbook.fragments.FragmentoNotaDetalle;
import net.vyl.thz.notbook.fragments.FragmentoReproducirAudio;
import net.vyl.thz.notbook.fragments.FragmentoTabbed;
import net.vyl.thz.notbook.fragments.FragmentoTareaDetalle;
import net.vyl.thz.notbook.fragments.FragmentoVideo;

import java.io.Serializable;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("Prueba", "OnCreate MainActivity");
        FragmentoTabbed selectorFragment = new FragmentoTabbed();

        if (findViewById(R.id.contenedor_prquenio) != null && getSupportFragmentManager().findFragmentById(R.id.contenedor_prquenio) == null){
            getSupportFragmentManager().beginTransaction().add(R.id.contenedor_prquenio, selectorFragment, "selectorFragment").commit();
        }
    }

    public void mostrarDetalleNota(int index){
        FragmentManager fragmentManager = getSupportFragmentManager();
        /*if(fragmentManager.findFragmentById(R.id.detalle_fragment) != null){
            DetalleFragment fragment_detalle = (DetalleFragment) fragmentManager.findFragmentById(R.id.detalle_fragment);
            fragment_detalle.ponInfoLibro(index);
        } else {*/
            FragmentoNotaDetalle fragmentoNotaDetalle = new FragmentoNotaDetalle();
            Bundle bundle = new Bundle();
            bundle.putInt(fragmentoNotaDetalle.ARG_ID_NOTA, index);
            fragmentoNotaDetalle.setArguments(bundle);
            fragmentManager.beginTransaction().hide(fragmentManager.getFragments().get(0)).add(R.id.contenedor_prquenio, fragmentoNotaDetalle).addToBackStack(null).commit();
        //}
    }

    public void mostrarDetalleTarea(int index){
        FragmentManager fragmentManager = getSupportFragmentManager();
        /*if(fragmentManager.findFragmentById(R.id.detalle_fragment) != null){
            DetalleFragment fragment_detalle = (DetalleFragment) fragmentManager.findFragmentById(R.id.detalle_fragment);
            fragment_detalle.ponInfoLibro(index);
        } else {*/
        FragmentoTareaDetalle fragmentoTareaDetalle = new FragmentoTareaDetalle();
        Bundle bundle = new Bundle();
        bundle.putInt(fragmentoTareaDetalle.ARG_ID_TAREA, index);
        fragmentoTareaDetalle.setArguments(bundle);
        fragmentManager.beginTransaction().hide(fragmentManager.getFragments().get(0)).add(R.id.contenedor_prquenio, fragmentoTareaDetalle)
                .addToBackStack(null).commit();
        //}
    }

    public void mostrarDetalleVideo(Object video){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentoVideo fragmentoVideo = new FragmentoVideo();
        Bundle bundle = new Bundle();
        bundle.putSerializable(fragmentoVideo.ARG_ID_MULT, (Serializable) video);
        fragmentoVideo.setArguments(bundle);
        fragmentManager.beginTransaction().remove(fragmentManager.getFragments().get(fragmentManager.getFragments().size()-1)).add(R.id.contenedor_prquenio, fragmentoVideo)
                .addToBackStack(null).commit();
    }

    public void mostrarDetalleImagen(Object imagen){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentoImagenes fragmentoImagenes = new FragmentoImagenes();
        Bundle bundle = new Bundle();
        bundle.putSerializable(fragmentoImagenes.ARG_ID_MULT, (Serializable) imagen);
        fragmentoImagenes.setArguments(bundle);
        fragmentManager.beginTransaction().remove(fragmentManager.getFragments().get(fragmentManager.getFragments().size()-1)).add(R.id.contenedor_prquenio, fragmentoImagenes)
                .addToBackStack(null).commit();
    }

    public void mostrarDetalleAudio(Object audio) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentoReproducirAudio fragmentoReproducirAudio = new FragmentoReproducirAudio();
        Bundle bundle = new Bundle();
        bundle.putSerializable(fragmentoReproducirAudio.ARG_ID_AUD, (Serializable) audio);
        fragmentoReproducirAudio.setArguments(bundle);
        fragmentManager.beginTransaction().remove(fragmentManager.getFragments().get(fragmentManager.getFragments().size()-1)).add(R.id.contenedor_prquenio, fragmentoReproducirAudio)
                .addToBackStack(null).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menuvacio, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /* int id = item.getItemId();
        switch (id) {
            case R.id.action_settings:
                // do stuff, like showing settings fragment
                return true;
        }*/

        return super.onOptionsItemSelected(item); // important line
    }


}