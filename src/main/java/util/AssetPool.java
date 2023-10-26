package util;

import renderer.Material;
import sound.Sound;

import java.util.HashMap;
import java.util.Map;

public class AssetPool {

    Map<String, Sound> sounds;
    Map<String, Material> materials;

    public AssetPool () {
        sounds = new HashMap<>();
        materials = new HashMap<>();
    }


    public void addSound (String filePath) {
        if (!sounds.containsKey(filePath)) {
            Sound newSound = new Sound();
            newSound.loadOgg(filePath);
            sounds.put(filePath,newSound);
        }
        System.out.println(this.sounds);
        System.out.println(sounds.containsKey(filePath));
    }

    public Sound getSound (String filePath) {
        System.out.println(sounds.containsKey(filePath));
        return sounds.get(filePath);
    }







}
