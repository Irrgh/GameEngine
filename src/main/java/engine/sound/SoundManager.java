package engine.sound;

import org.lwjgl.openal.AL;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALCCapabilities;
import org.lwjgl.openal.ALCapabilities;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.openal.ALC10.*;

public class SoundManager {

    private long device;
    private long context;

    public void init() {
        // Initialize OpenAL
        device = alcOpenDevice((ByteBuffer) null);


        int[] attributes = {0};

        context = alcCreateContext(device, attributes);
        alcMakeContextCurrent(context);

        // Check for OpenAL errors
        if (alcGetError(device) != ALC_NO_ERROR) {
            System.err.println("Failed to initialize OpenAL.");
            return;
        }

        // Initialize AL (OpenAL)

        ALCCapabilities deviceCaps = ALC.createCapabilities(device);
        ALCapabilities alCaps = AL.createCapabilities(deviceCaps);

        if (!alCaps.OpenAL10) {
            System.err.println("Audio library not Supported");

        }

    }

    public void cleanup() {
        // Release OpenAL resources
        ALCCapabilities alcCaps = ALC.createCapabilities(device);
        alcMakeContextCurrent(0);
        alcDestroyContext(context);
        alcCloseDevice(device);
    }

    // Other OpenAL-related methods can be added here
}
