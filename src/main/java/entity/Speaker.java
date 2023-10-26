package entity;

import entity.Entity;
import org.joml.Vector3f;
import sound.Sound;

import static org.lwjgl.openal.AL10.*;

public class Speaker extends Entity {
    private Sound sound;
    private int sourceId;

    private boolean isPlaying = false;
    private boolean isLooping = false;

    private float gain;

    private float pitch;

    public Speaker () {


    }


    public void assignSound (Sound sound) {
        sourceId = alGenSources();
        alSourcei(sourceId, AL_BUFFER, sound.getBufferId());
    }

    private void setLooping(boolean looping) {
        isLooping = looping;
        alSourcei(sourceId,AL_LOOPING, looping ? 1 : 0);
    }


    public void deleteSound () {
        alDeleteSources(sourceId);
    }


    public void play () {

        int state = alGetSourcei(sourceId,AL_SOURCE_STATE);

        if (state == AL_STOPPED) {
            isPlaying = false;
            alSource3f(sourceId, AL_POSITION, position.x, position.y , position.z);
        }

        if (!isPlaying) {
            alSourcePlay(sourceId);
            isPlaying = true;
        }

    }


    public void stop () {
        if (isPlaying) {
            alSourceStop(sourceId);
            isPlaying = false;
        }

    }

    public boolean isPlaying () {
        int state = alGetSourcei(sourceId,AL_SOURCE_STATE);
        if (state == AL_STOPPED) {
            isPlaying = false;
        }
        return isPlaying;
    }

    public void setGain (float gain) {
        alSourcef(sourceId,AL_GAIN,gain);
        this.gain = gain;
    }

    public float getGain () {
        return gain;
    }

    public void  setPitch (float pitch) {
        alSourcef(sourceId, AL_PITCH, pitch);
        this.pitch = pitch;
    }

    public float getPitch () {
        return pitch;
    }


    @Override
    public void setPosition (Vector3f pos) {
        super.setPosition(pos);
        alSource3f(sourceId, AL_POSITION, position.x, position.y, position.z);
    }



}
