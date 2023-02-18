package com.evanadwyer.simplevisitortracker.sound

import android.content.res.AssetFileDescriptor
import android.content.res.AssetManager
import android.media.SoundPool
import android.util.Log

private const val SOUNDS_FOLDER = "sample_sounds"
private const val MAX_SOUNDS = 1

class Sounder(private val assets: AssetManager) {

    private val soundName: Sound
    private val soundPool = SoundPool.Builder()
        .setMaxStreams(MAX_SOUNDS)
        .build()

    init {
        soundName = loadSound()
    }

    private fun loadSound(): Sound {
        return try {
            val soundNames = assets.list(SOUNDS_FOLDER)!!
            Log.d("Sounder.kt", soundNames[0])
            val sound = Sound("$SOUNDS_FOLDER/${soundNames.asList().first()}")
            load(sound)
            sound
        } catch (e: Exception) {
            Log.e("Sounder.kt", "lost sound", e)
            Sound("")
        }
    }

    private fun load(sound: Sound) {
        val afd: AssetFileDescriptor = assets.openFd(sound.assetPath)
        val soundID = soundPool.load(afd, 1)
        sound.soundID = soundID
    }

    fun play() {
        soundName.soundID?.let {
            soundPool.play(it, 1.0f, 1.0f, 1, 0, 1.0f)
        }
    }

    fun release() {
        soundPool.release()
    }
}