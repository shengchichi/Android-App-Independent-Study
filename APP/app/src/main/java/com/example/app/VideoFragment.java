package com.example.app;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.VideoView;

/**
 * Created by 鵬仁 on 2017/5/9.
 */
public class VideoFragment extends Fragment {

    private View view;

    private VideoView mVideoView;
    private MediaController vidControl;


    String vidAddress = "android.resource://com.example.app/" + R.raw.video;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_video, container, false);

        mVideoView = (VideoView)view.findViewById(R.id.video);
        vidControl = new MediaController(view.getContext());
        vidControl.setAnchorView(mVideoView);
        mVideoView.setMediaController(vidControl);

        Uri vidUri = Uri.parse(vidAddress);
        mVideoView.setVideoURI(vidUri);
        mVideoView.start();


        return view;
    }


}
