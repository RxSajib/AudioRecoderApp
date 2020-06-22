package com.example.audiorecoderapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class FragementRecode extends Fragment {

    private NavController navController;
    private ImageView musiclist_button;
    private RelativeLayout recode_button;
    private String auiofile;

    /// recode start component
    private RelativeLayout recode_button_body;
    private RelativeLayout recode_button_border;
    private ImageView recode_image;
    /// recode start component
    private boolean isrecode ;
    private final  int Permission_code = 01;
    private MediaRecorder recorder;
    private String Current_time_date;

    private String Mediafile = null;

    public FragementRecode() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fragement_recode, container, false);


        return  view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {


        Mediafile = Environment.getExternalStorageDirectory().getAbsolutePath();
        Mediafile += "music.3gp";

        navController = Navigation.findNavController(view);
        musiclist_button = view.findViewById(R.id.MusicListButtonID);
        musiclist_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_fragementRecode_to_listFragement);
            }
        });


        recode_button = view.findViewById(R.id.MicButtonID);
        recode_button_body = view.findViewById(R.id.RecodeBodyID);
        recode_button_border = view.findViewById(R.id.MicButtonID);
        recode_image = view.findViewById(R.id.RecodeImageID);


        recode_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isrecode){

                    stopRecoding();

                    recode_image.setImageResource(R.drawable.mic_black);
                    recode_button_border.setBackgroundResource(R.drawable.mic_back_border);
                    recode_button_body.setBackgroundResource(R.drawable.mic_black_layout);
                    isrecode = false;
                }
                else {

                    if(cheackpermission()) {
                        startRecoding();
                        recode_image.setImageResource(R.drawable.mic_white);
                        recode_button_border.setBackgroundResource(R.drawable.mic_start_bborder);
                        recode_button_body.setBackgroundResource(R.drawable.mic_start_body);
                        isrecode = true;
                    }
                }

            }
        });






        super.onViewCreated(view, savedInstanceState);
    }

    private boolean cheackpermission(){
        if(ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED){
            return true;
        }
        else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.RECORD_AUDIO}, Permission_code);
            return false;
        }
    }

    private void startRecoding(){

        String recodepath = getActivity().getExternalFilesDir("/").getAbsolutePath();

        auiofile = "Recoder.3gp";

        Calendar calendartime_date = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat_time_date = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");
        Current_time_date = simpleDateFormat_time_date.format(calendartime_date.getTime());



        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile(recodepath+"/"+Current_time_date+auiofile);
        /*recorder.setOutputFile(Mediafile);*/
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);


        try {
            recorder.prepare();
            Toast.makeText(getActivity(), "start", Toast.LENGTH_LONG).show();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        recorder.start();
    }

    private void stopRecoding(){
        recorder.stop();
        recorder.release();
        recorder = null;

      Toast.makeText(getActivity(), "stop", Toast.LENGTH_LONG).show();
    }
}