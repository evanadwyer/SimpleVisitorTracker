package com.evanadwyer.simplevisitortracker.sound

import android.content.res.AssetFileDescriptor
import android.content.res.AssetManager
import android.media.SoundPool
import android.util.Log

private const val SOUNDS_FOLDER = "sample_sounds"
private const val MAX_SOUNDS = 1

class Sounder(private val assets: AssetManager) {

    private val allSounds: List<Sound>
    private val soundPool = SoundPool.Builder()
        .setMaxStreams(MAX_SOUNDS)
        .build()

    init {
        allSounds = loadSounds()
    }

    private fun loadSounds(): List<Sound> {
        return try {
            val soundNames = assets.list(SOUNDS_FOLDER)!!
            val sounds = mutableListOf<Sound>()
            for (soundName in soundNames) {
                Log.d("Sounder.kt", soundName)
                val sound = Sound("$SOUNDS_FOLDER/$soundName")
                sounds.add(sound)
                load(sound)
            }
            sounds
        } catch (e: Exception) {
            Log.e("Sounder.kt", "lost sound", e)
            emptyList()
        }
    }

    private fun load(sound: Sound) {
        val afd: AssetFileDescriptor = assets.openFd(sound.assetPath)
        val soundID = soundPool.load(afd, 1)
        sound.soundID = soundID
    }

    fun playCheckInSubmission() {
        allSounds[1].soundID?.let {
            soundPool.play(it, 1.0f, 1.0f, 1, 0, 1.0f)
        }
    }

    fun playScan() {
        allSounds[0].soundID?.let {
            soundPool.play(it, 1.0f, 1.0f, 1, 0, 1.0f)
        }
    }

    fun release() {
        soundPool.release()
    }
}