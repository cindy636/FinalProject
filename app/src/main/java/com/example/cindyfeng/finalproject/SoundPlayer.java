package com.example.cindyfeng.finalproject;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

public class SoundPlayer {
    private static SoundPool soundPool;
    //private static int hitSound;
    private static int overSound;
    private static int momSound;
    private static int wowSound;

    public SoundPlayer(Context context) {
        //SoundPool (int maxStreams, int StreamType, int srcQuality);
        soundPool = new SoundPool(2, AudioManager. STREAM_MUSIC, 0);

        //hitSound = soundPool.load(context, R.raw.hit, 1);
        wowSound = soundPool.load(context, R.raw.wow, 1);

        overSound = soundPool.load(context, R.raw.over, 1);

        momSound = soundPool.load(context, R.raw.yourmom, 1);


    }
    public void playWowSound() {
        soundPool.play(wowSound, 1.0f, 1.0f, 1, 0, 1.0f);
    }
    /**public void playYourMom() {
        soundPool.play(momSound, 1.0f, 1.0f, 1, 0, 1.0f);
    }*/

    /*public void playHitSound() {
        soundPool.play(hitSound, 1.0f, 1.0f, 1, 0, 1.0f);
    }*/
    public void playOverSound() {
        soundPool.play(overSound, 2.0f, 2.0f, 1, 0, 0.5f);

    }
}
