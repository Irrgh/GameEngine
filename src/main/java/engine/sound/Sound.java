package engine.sound;


import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.stb.STBVorbis.stb_vorbis_decode_filename;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.libc.LibCStdlib.free;

public class Sound {

    private int bufferId;
    private int sourceId;
    private boolean isPlaying = false;

    private String filePath;

    public Sound (String filePath, boolean loops) {
        this.filePath = filePath;

        stackPush();
        IntBuffer channelsBuffer = stackMallocInt(1);
        stackPush();
        IntBuffer sampleRateBuffer = stackMallocInt(1);

        ShortBuffer rawAudioBuffer = stb_vorbis_decode_filename(filePath, channelsBuffer, sampleRateBuffer);

        if (rawAudioBuffer == null) {
            System.err.println("could not load '" + filePath + "'");
            stackPop();
            stackPop();
            return;

        }

        int channels = channelsBuffer.get();
        int sampleRate = sampleRateBuffer.get();
        stackPop();
        stackPop();

        int format = -1;

        if (channels == 1) {
            format = AL_FORMAT_MONO16;
            System.out.println("mono");
        } else if (channels == 2) {
            format = AL_FORMAT_STEREO16;
            System.out.println("stereo");
        }

        bufferId = alGenBuffers();
        alBufferData(bufferId,format,rawAudioBuffer, sampleRate);


        sourceId = alGenSources();
        alSourcei(sourceId, AL_BUFFER, bufferId);
        alSourcei(sourceId, AL_LOOPING, loops ? 1 : 0);
        alSourcef(sourceId, AL_GAIN, 0.5f);
        alSource3f(sourceId, AL_POSITION, 0,0,0);
        //alSource3f(sourceId, AL_DIRECTION, 0, 0, 0);
        //alSourcei(sourceId, AL_SOURCE_RELATIVE, AL_TRUE);

        int err = alGetError();
        if (AL_NO_ERROR != err) {
            System.err.println(err);
        }

       // free(rawAudioBuffer);   this breaks 3d audio
    }

    public void delete () {
        alDeleteBuffers(bufferId);
        alDeleteSources(sourceId);

    }


    public void play () {

        int state = alGetSourcei(sourceId,AL_SOURCE_STATE);

        if (state == AL_STOPPED) {
            isPlaying = false;
            alSourcei(sourceId, AL_POSITION, 0);
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


    public String getFilePath () {
        return this.filePath;
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
    }



}
