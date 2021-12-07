package net.vyl.thz.notbook.fragments;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;

import net.vyl.thz.notbook.MainActivity;
import net.vyl.thz.notbook.R;
import net.vyl.thz.notbook.data.DaoAudioNotas;
import net.vyl.thz.notbook.data.DaoAudioTareas;
import net.vyl.thz.notbook.models.AudioNotas;
import net.vyl.thz.notbook.models.AudioTareas;
import net.vyl.thz.notbook.models.MultimediaNotas;

import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentoReproducirAudio#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentoReproducirAudio extends Fragment implements View.OnTouchListener, MediaPlayer.OnPreparedListener, MediaController.MediaPlayerControl {

    private View view;
    MainActivity mainActivity;
    Context context;
    MediaPlayer mediaPlayer;
    MediaController mediaController;
    AudioNotas audioNotas;
    AudioTareas audioTareas;
    DaoAudioNotas daoAudioNotas;
    DaoAudioTareas daoAudioTareas;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final String ARG_ID_AUD = "id_aud";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentoReproducirAudio() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        if (context instanceof MainActivity){
            mainActivity = (MainActivity) context;
        }
        daoAudioNotas = new DaoAudioNotas(context);
        daoAudioTareas = new DaoAudioTareas(context);
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentoGrabarAudio.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentoReproducirAudio newInstance(String param1, String param2) {
        FragmentoReproducirAudio fragment = new FragmentoReproducirAudio();
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
        view = inflater.inflate(R.layout.fragment_grabar_audio, container, false);
        Bundle args = getArguments();
        if(args != null){
            Object position = args.getSerializable(ARG_ID_AUD);
            ponInfoAud(position, view);
        }
        return view;
    }

    public void ponInfoAud(Object id){
        ponInfoAud(id, getView());
    }

    private void ponInfoAud(Object id, View vista){
        String audio;
        if ( id instanceof AudioNotas){
            audioNotas = (AudioNotas) id;
            Log.d("Prueba", "ponInfoVideo: " + audioNotas.getAudio());
            audio = audioNotas.getAudio();
        } else {
            audioTareas = (AudioTareas) id;
            audio = audioTareas.getAudio();
        }
        ((ImageView) view.findViewById(R.id.rep_audio)).setOnTouchListener(this);
        if (mediaPlayer != null){
            mediaPlayer.release();
        }
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnPreparedListener(this);
        mediaController = new MediaController(getActivity());
        try {
            mediaPlayer.setDataSource(audio);
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            Log.e("Audiolibros", "ERROR: No se puede reproducir " + audio, e);
        }
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        mediaController.setMediaPlayer(this);
        mediaController.setAnchorView(((ImageView) view.findViewById(R.id.rep_audio)));
        mediaController.setEnabled(true);
        mediaController.show();
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (view instanceof ImageView){
            mediaController.show();
            return true;
        }
        return false;
    }

    @Override
    public void start() {
        mediaPlayer.start();
    }

    @Override
    public void pause() {
        mediaPlayer.pause();
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
            mediaPlayer.stop();
            mediaPlayer.release();
        } catch (Exception e) {
            Log.d("Prueba", "Error en mediaPlayer.stop()");
        }
        super.onStop();
    }
}