package renderer;

import engine.entities.Entity;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;
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

    public static void main(String[] args) {

        Entity e = new Entity(new Vector3f(10,3,3));

        System.out.println(e.getWorldTransform());


        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(16);
        matBuffer.rewind();

        e.getWorldTransform().get(matBuffer);
        System.out.println(matBuffer);

        while (matBuffer.hasRemaining()) {
            System.out.println(matBuffer.get());
        }
    }


}
