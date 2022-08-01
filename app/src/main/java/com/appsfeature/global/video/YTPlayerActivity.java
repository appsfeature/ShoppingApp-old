package com.appsfeature.global.video;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.appsfeature.global.AppApplication;
import com.appsfeature.global.R;
import com.appsfeature.global.model.ExtraProperty;
import com.appsfeature.global.util.AppConstant;
import com.appsfeature.global.util.DynamicUrlCreator;
import com.appsfeature.global.util.SupportUtil;
import com.appsfeature.global.video.util.YTUtility;
import com.dynamic.model.DMVideo;
import com.dynamic.util.DMDataManager;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.helper.util.BaseUtil;
import com.helper.util.Logger;

/**
 * @author Created by Abhijit on 25/06/2018.
 */
public class YTPlayerActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {

    private YouTubePlayer youTubePlayer;
    private boolean mOrientationLandScape = false;
    private String mVideoId;
    private String mTitle = "Player";
    private String mDescription;
    private ExtraProperty extraProperty;
    private int videoTime;
    private boolean isPlayerStyleMinimal = true;
    private int mCatId;
    private TextView tvTitle, tvDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube_player);
        initViews();
        getArguments(getIntent());
    }

    private void initViews() {
        tvTitle = findViewById(R.id.tv_title);
        tvDescription = findViewById(R.id.tv_description);
    }

    private void getArguments(Intent intent) {
        if (intent.getSerializableExtra(AppConstant.CATEGORY_PROPERTY) instanceof ExtraProperty) {
            extraProperty = (ExtraProperty) intent.getSerializableExtra(AppConstant.CATEGORY_PROPERTY);
            if(extraProperty != null && extraProperty.getVideoId() != null){
                mTitle = extraProperty.getTitle();
                mDescription = extraProperty.getDescription();
                mCatId = extraProperty.getCatId();
                mVideoId = extraProperty.getVideoId();
                videoTime = extraProperty.getVideoTime();
                isPlayerStyleMinimal = extraProperty.isPlayerStyleMinimal();
                loadDetail();
                initToolBarTheme(mTitle);
                loadVideo(null);
            }else {
                SupportUtil.showToast(this, AppConstant.INVALID_PROPERTY);
                finish();
            }
        } else {
            SupportUtil.showToast(this, AppConstant.INVALID_PROPERTY);
            finish();
        }
    }

    private void loadDetail() {
        if(tvTitle != null){
            tvTitle.setText(mTitle);
            tvTitle.setVisibility(TextUtils.isEmpty(mTitle) ? View.GONE : View.VISIBLE);
        }
        if(tvDescription != null){
            tvDescription.setText(BaseUtil.fromHtml(mDescription));
            tvDescription.setVisibility(TextUtils.isEmpty(mDescription) ? View.GONE : View.VISIBLE);
        }
    }


    private void loadVideo(String apiKey) {
        if (TextUtils.isEmpty(apiKey) && AppApplication.getInstance() != null) {
            apiKey = AppApplication.getInstance().getApiKey();
            if (TextUtils.isEmpty(apiKey)) {
                if (!TextUtils.isEmpty(mVideoId)) {
                    YTUtility.openYoutubeApp(this, mVideoId);
                }
                finish();
            }
        }
        YouTubePlayerView playerView = findViewById(R.id.youtubePlayerView);
        playerView.initialize(apiKey, this);
    }


    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean wasRestored) {
        this.youTubePlayer = youTubePlayer;
        Logger.d("onInitializationSuccess");

        youTubePlayer.setPlaybackEventListener(playbackEventListener);
        youTubePlayer.setPlayerStateChangeListener(playerStateChangeListener);

        youTubePlayer.setFullscreenControlFlags(YouTubePlayer.FULLSCREEN_FLAG_CONTROL_SYSTEM_UI);

        if (!wasRestored) {
            youTubePlayer.cueVideo(mVideoId, videoTime);
        }

//        if(isPlayerStyleMinimal) {
            youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.MINIMAL);
//        }

        youTubePlayer.setOnFullscreenListener(new YouTubePlayer.OnFullscreenListener() {
            @Override
            public void onFullscreen(boolean isFullScreen) {
                mOrientationLandScape = isFullScreen;
            }
        });
    }


    //Toast pop up messaging to show errors.
    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        final int REQUEST_CODE = 1;

        if (youTubeInitializationResult.isUserRecoverableError()) {
            youTubeInitializationResult.getErrorDialog(this, REQUEST_CODE).show();

        } else {
            String errorMessage = String.format("There was an error initializing the YoutubePlayer (&1$s)", youTubeInitializationResult.toString());
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
        }

    }

    private final YouTubePlayer.PlaybackEventListener playbackEventListener = new YouTubePlayer.PlaybackEventListener() {
        @Override
        public void onPlaying() {
            Logger.d("onPlaying");
        }

        @Override
        public void onPaused() {
            Logger.d("onPaused");
        }

        @Override
        public void onStopped() {
            Logger.d("onStopped");
        }

        @Override
        public void onBuffering(boolean b) {
            Logger.d("onBuffering");
        }

        @Override
        public void onSeekTo(int i) {
            Logger.d("onSeekTo");
        }
    };

    private final YouTubePlayer.PlayerStateChangeListener playerStateChangeListener = new YouTubePlayer.PlayerStateChangeListener() {
        @Override
        public void onLoading() {
            Logger.d("onLoading");
        }

        @Override
        public void onLoaded(String s) {
            Logger.d("onLoaded");
            youTubePlayer.play();
        }

        @Override
        public void onAdStarted() {
            Logger.d("onAdStarted");
        }

        @Override
        public void onVideoStarted() {
            Logger.d("onVideoStarted");
        }

        @Override
        public void onVideoEnded() {
            Logger.d("onVideoEnded");
        }

        @Override
        public void onError(YouTubePlayer.ErrorReason errorReason) {
            Logger.d("onError");
        }
    };

    public void initToolBarTheme(String title) {
        TextView tvTitle = findViewById(R.id.tv_titile);
        tvTitle.setText(title);
        (findViewById(R.id.iv_action_back)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        (findViewById(R.id.iv_action_full_screen)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (youTubePlayer != null) {
                    mOrientationLandScape = !mOrientationLandScape;
                    youTubePlayer.setFullscreen(mOrientationLandScape);
                }
            }
        });
        (findViewById(R.id.iv_action_share)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DynamicUrlCreator(YTPlayerActivity.this)
                        .shareVideo(extraProperty.getVideoId(), extraProperty
                                , extraProperty.getTitle());
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (youTubePlayer != null) {
            youTubePlayer.setFullscreen(false);
        }
    }

    @Override
    public void onBackPressed() {
        if (youTubePlayer != null && mOrientationLandScape) {
            mOrientationLandScape = false;
            youTubePlayer.setFullscreen(false);
        } else {
            closeYoutubePlayer();
            super.onBackPressed();
        }
    }

    private void closeYoutubePlayer() {
        try {
            if (youTubePlayer != null) {
                youTubePlayer.pause();
                DMVideo mVideo = new DMVideo();
                mVideo.setVideoId(mVideoId);
                mVideo.setCatId(mCatId);
                mVideo.setVideoTime(youTubePlayer.getCurrentTimeMillis());
                mVideo.setVideoDuration(youTubePlayer.getDurationMillis());
                DMDataManager.get(this).insertVideo(mVideo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (youTubePlayer != null && youTubePlayer.isPlaying()) {
            if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
                mOrientationLandScape = false;
                youTubePlayer.setFullscreen(false);
            } else if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                mOrientationLandScape = true;
                youTubePlayer.setFullscreen(true);
            }
        }
    }
}
