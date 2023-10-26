package renderer;

import entity.Entity;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

public class Material {

    Vector3f ambient;
    Vector3f diffuse;
    Vector3f specular;
    Float specularExponent;   // 1-1000
    Float transparency;   // 1 == off   0 == full

    Vector3f transmissionFilter;

    Float ior;   // index of refraction


    public static Material parse(String filePath) {

        return null;
    }

}
