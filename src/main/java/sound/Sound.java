package sound;


import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.stb.STBVorbis.stb_vorbis_decode_filename;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.libc.LibCStdlib.free;

public class Sound {

    private int bufferId;

    private String filePath;


    public Sound () {}


    public String getFilePath () {
        return filePath;
    }

    public int getBufferId () {
        return bufferId;
    }

    public void  loadOgg(String filePath) {

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

    }







}
