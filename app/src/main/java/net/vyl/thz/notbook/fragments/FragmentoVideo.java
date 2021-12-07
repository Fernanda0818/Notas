package net.vyl.thz.notbook.fragments;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import net.vyl.thz.notbook.MainActivity;
import net.vyl.thz.notbook.R;
import net.vyl.thz.notbook.data.DaoMultimediaNotas;
import net.vyl.thz.notbook.data.DaoMultimediaTareas;
import net.vyl.thz.notbook.models.MultimediaNotas;
import net.vyl.thz.notbook.models.MultimediaTareas;

import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentoVideo#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentoVideo extends Fragment implements View.OnTouchListener, MediaPlayer.OnPreparedListener, MediaController.MediaPlayerControl{

    MainActivity mainActivity;
    Context context;
    MediaPlayer mediaPlayer;
    MediaController mediaController;
    View vista;
    MultimediaNotas videoNotas;
    MultimediaTareas videoTareas;
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

    public FragmentoVideo() {
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
     * @return A new instance of fragment FragmentoVideo.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentoVideo newInstance(String param1, String param2) {
        FragmentoVideo fragment = new FragmentoVideo();
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
        vista = inflater.inflate(R.layout.fragment_video, container, false);
        Bundle args = getArguments();
        if(args != null){
            Object position = args.getSerializable(ARG_ID_MULT);
            ponInfoVideo(position, vista);
        }
        setHasOptionsMenu(true);
        return vista;
    }

    public void ponInfoVideo(Object id){
        ponInfoVideo(id, getView());
    }

    private void ponInfoVideo(Object id, View vista){
        Uri video;
        if ( id instanceof MultimediaNotas){
            videoNotas = (MultimediaNotas) id;
            Log.d("Prueba", "ponInfoVideo: " + videoNotas.getMultimedia());
            video = Uri.parse(videoNotas.getMultimedia());
            ((EditText) vista.findViewById(R.id.descripcion_video)).setText(videoNotas.getDescripcion());
            ((VideoView) vista.findViewById(R.id.reproducir_video)).setVideoURI(video);
        } else {
            videoTareas = (MultimediaTareas) id;
            video = Uri.parse(videoTareas.getMultimedia());
            ((EditText) vista.findViewById(R.id.descripcion_video)).setText(videoTareas.getDescripcion());
            ((VideoView) vista.findViewById(R.id.reproducir_video)).setVideoURI(video);
        }
        ((VideoView) vista.findViewById(R.id.reproducir_video)).setOnTouchListener(this);
        if (mediaPlayer != null){
            mediaPlayer.release();
        }
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnPreparedListener(this);
        mediaController = new MediaController(getActivity());
        try {
            mediaPlayer.setDataSource(getActivity(), video);
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            Log.e("Audiolibros", "ERROR: No se puede reproducir " + video, e);
        }
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        //mediaPlayer.start();
        //((VideoView) vista.findViewById(R.id.reproducir_video)).start();
        mediaController.setMediaPlayer(this);
        mediaController.setAnchorView(((VideoView) vista.findViewById(R.id.reproducir_video)));
        mediaController.setEnabled(true);
        mediaController.show();
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (view instanceof VideoView){
            mediaController.show();
            return true;
        }
        return false;
    }

    @Override
    public void start() {
        mediaPlayer.start();
        ((VideoView) vista.findViewById(R.id.reproducir_video)).start();
    }

    @Override
    public void pause() {
        mediaPlayer.pause();
        ((VideoView) vista.findViewById(R.id.reproducir_video)).pause();
    }

    @Override
    public int getDuration() {
        return mediaPlayer.getDuration();
    }

    @Override
    public int getCurrentPosition() {
        return mediaPlayer.getCurrentPosition();
    }

    @Override
    public void seekTo(int i) {
        mediaPlayer.seekTo(i);
        ((VideoView) vista.findViewById(R.id.reproducir_video)).seekTo(i);
    }

    @Override
    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public boolean canPause() {
        return true;
    }

    @Override
    public boolean canSeekBackward() {
        return true;
    }

    @Override
    public boolean canSeekForward() {
        return true;
    }

    @Override
    public int getAudioSessionId() {
        return mediaPlayer.getAudioSessionId();
    }

    @Override
    public void onStop() {
        mediaController.hide();
        try {
            ((VideoView) vista.findViewById(R.id.reproducir_video)).stopPlayback();
            mediaPlayer.stop();
            mediaPlayer.release();
        } catch (Exception e) {
            Log.d("Prueba", "Error en mediaPlayer.stop()");
        }
        super.onStop();
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
                    if (videoNotas != null){
                        videoNotas.setDescripcion(((EditText) vista.findViewById(R.id.descripcion_video)).getText().toString());
                        if (daoMultimediaNotas.update(videoNotas)){
                            InputMethodManager inputMethodManager = (InputMethodManager) mainActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                            inputMethodManager.hideSoftInputFromWindow(((EditText) vista.findViewById(R.id.descripcion_video)).getWindowToken(), 0);
                            mainActivity.onBackPressed();
                        } else {
                            Toast.makeText(mainActivity, "No se pudo actualizar", Toast.LENGTH_LONG).show();
                        }
                    } else if (videoTareas != null) {
                        videoTareas.setDescripcion(((EditText) vista.findViewById(R.id.descripcion_video)).getText().toString());
                        if (daoMultimediaTareas.update(videoTareas)){
                            InputMethodManager inputMethodManager = (InputMethodManager) mainActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                            inputMethodManager.hideSoftInputFromWindow(((EditText) vista.findViewById(R.id.descripcion_video)).getWindowToken(), 0);
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