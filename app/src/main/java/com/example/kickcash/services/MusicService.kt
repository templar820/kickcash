package com.example.kickcash.services

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.IBinder
import android.util.Log

import java.util.concurrent.Executors
import android.media.AudioManager.AUDIOFOCUS_GAIN
import android.media.AudioManager.AUDIOFOCUS_LOSS
import android.media.AudioManager.AUDIOFOCUS_LOSS_TRANSIENT
import com.example.kickcash.R


class MusicService:Service(), AudioManager.OnAudioFocusChangeListener {
  private var mediaPlayer:MediaPlayer?=null
  private var mAudioFocusRequest:AudioFocusRequest?=null
  private var mAudioManager:AudioManager?=null
  private var broadcastReceiver:BroadcastReceiver?=null
  private val currentContext:Context
  get() {
    return this
  }
  override fun onCreate() {
    Log.d("MusicService", "onCreate")
    mAudioManager = this.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    //int volume = mAudioManager.getStreamMaxVolume(mAudioManager.STREAM_MUSIC) / 2;
    //mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, AudioManager.FLAG_SHOW_UI);
    var mAudioAttributes = AudioAttributes.Builder()
    .setUsage(AudioAttributes.USAGE_MEDIA)
    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
    .build()
    mAudioFocusRequest = AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
    .setAudioAttributes(mAudioAttributes)
    .setAcceptsDelayedFocusGain(true)
    .setOnAudioFocusChangeListener(this)
    .build()
    broadcastReceiver = object:BroadcastReceiver() {
      override fun onReceive(context:Context, intent:Intent) {
        val command = intent.getStringExtra("Command")
        Log.d("MusicService DEBUG", command)
        if (command == null)
        {
          return
        }
        when (command) {
          "Stop" -> if (mediaPlayer != null)
          {
            mediaPlayer?.stop()
            //stopService();
          }
          "Play" -> {
            var focus = mAudioFocusRequest;
            if(focus != null){
              Executors.newSingleThreadExecutor().submit(playMusic(currentContext))
              mAudioManager?.requestAudioFocus(focus)
            }
          }
        }
      }
    }
    val filter = IntentFilter()
    filter.addAction("com.example.kickcash.services.MusicService")
    registerReceiver(broadcastReceiver, filter)
    //Executors.newSingleThreadExecutor().submit(playMusic(this));
  }
  private fun stopService() {
    unregisterReceiver(broadcastReceiver)
    this.stopSelf()
  }
  override fun onAudioFocusChange(focusChange:Int) {
    when (focusChange) {
      AUDIOFOCUS_GAIN -> Log.d(TAG, "AUDIOFOCUS_GAIN")
      AUDIOFOCUS_LOSS -> Log.d(TAG, "AUDIOFOCUS_LOSS")
      AUDIOFOCUS_LOSS_TRANSIENT -> {
        Log.d(TAG, "AUDIOFOCUS_LOSS_TRANSIENT")
        var focus = mAudioFocusRequest;
        if(focus != null){
          val focusRequest = mAudioManager?.requestAudioFocus(focus)
          Log.d(TAG, "focusRequest: " + focusRequest)
        }

      }
    }
  }
  override fun onDestroy() {
    if (mediaPlayer != null)
    {
      mediaPlayer?.stop()
    }
  }
  private fun playMusic(context:Context):Runnable {
    val runnable = object:Runnable {
      public override fun run() {
        mediaPlayer = MediaPlayer.create(context, R.raw.song)
        mediaPlayer?.start()
      }
    }
    return runnable
  }
   override fun onBind(intent:Intent):IBinder?{
      return null
    }
  companion object {
    private val TAG = "MUSIC SERVICE DEBUG"
  }
}
