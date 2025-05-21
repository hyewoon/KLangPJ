package com.hye.sesac.klangpj.manager

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class AudioPlayerManager(private val context: Context) {
    private var player: ExoPlayer? = null
    private val _isAudioPlaying: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isAudioPlaying = _isAudioPlaying.asStateFlow()

    init {
        initPlayer()
    }

    /*
    *
    * player?.isPlaying -> ExoPlayer객체를 통해 얻은 실제 재생 되는지를 확인하는 속성
    * 구현 조건: 하나의 버튼(재생 & 정지 표현)
    * Player.STATE_READY-> _isPlaying.value =  player?.isPlaying == true 의미
    * 1.
    *
    * */
    private fun initPlayer() {
        player = ExoPlayer.Builder(context).build()
        player?.addListener(object:Player.Listener{
            @SuppressLint("SwitchIntDef")
            override fun onPlaybackStateChanged(playbackState: Int) {
                super.onPlaybackStateChanged(playbackState)
                when(playbackState){
                    Player.STATE_READY->{
                        _isAudioPlaying.value = when(player?.isPlaying){
                            true -> true
                            else -> false
                        }
                    }
                   // Player.STATE_READY -> _isAudioPlaying.value = player?.isPlaying == true
                    Player.STATE_ENDED-> _isAudioPlaying.value = false
                    Player.STATE_IDLE -> _isAudioPlaying.value = false
                }
            }
            //실제 재생상태파악
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                super.onIsPlayingChanged(isPlaying)
                _isAudioPlaying.value = isPlaying
            }

            override fun onPlayerError(error: PlaybackException) {
                super.onPlayerError(error)
                _isAudioPlaying.value = false

                when(error.errorCode){
                    PlaybackException.ERROR_CODE_IO_NETWORK_CONNECTION_FAILED,
                    PlaybackException.ERROR_CODE_IO_NETWORK_CONNECTION_TIMEOUT ->
                        Log.e("AudioPlayerManager", "네트워크 연결 실패")

                    PlaybackException.ERROR_CODE_IO_FILE_NOT_FOUND,
                        PlaybackException.ERROR_CODE_IO_INVALID_HTTP_CONTENT_TYPE ->
                            Log.e("AudioPlayerManager", "파일을 찾을 수 없습니다.")

                    PlaybackException.ERROR_CODE_AUDIO_TRACK_INIT_FAILED,
                    PlaybackException.ERROR_CODE_DECODER_INIT_FAILED,
                    PlaybackException.ERROR_CODE_DECODER_QUERY_FAILED -> {
                        Log.e("AudioPlayerManager", "Media decoding error: ${error.message}")

                    }
                }
            }
        })
    }


    fun playAudio(url: String) {
        val mediaItem = MediaItem.fromUri(url)
        player?.let {
            it.setMediaItem(mediaItem)
            it.prepare()
            it.play()
        }
    }

    fun stopAudio() {
        player?.stop()
    }

    fun releasePlayer() {
        player?.release()
        player = null

    }

}