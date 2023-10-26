package engine.entities;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import renderer.Mesh;

import java.util.UUID;

import static org.lwjgl.opengl.GL15.*;

public class Entity {

    protected Vector3f facing;
    protected Vector3f position;

    protected Vector3f scale;
    protected Quaternionf rotation;

    protected Quaternionf orientation;


    protected Mesh mesh;

    protected boolean isInstance;



    public Entity () {
        orientation = new Quaternionf();
        rotation = new Quaternionf().rotateZ(45);
        position = new Vector3f(0,0,0);
        facing = new Vector3f(0,1,0);
        scale = new Vector3f(1,1,1);
        mesh = null;
    }

    public Entity (Mesh instancing) {
        this();
        mesh = instancing;
    }

    public Entity (Vector3f position) {
        this();
        this.position = position;
    }
    



    public Matrix4f getWorldTransform () {

        //TransformedVector = TranslationMatrix * RotationMatrix * ScaleMatrix * OriginalVector;

        Matrix4f mat = new Matrix4f();

        return mat.translationRotateScale(position,rotation,scale);
    }


    public Vector3f getPosition () {
        return position;
    }

    public Vector3f getFacing () {
        return facing;
    }

    public Vector3f getScale () {
        return scale;
    }

    public void setPosition (Vector3f pos) {
        position.set(pos);
    }

    public void setRotationX (float angle) {
        rotation.identity().rotateX(angle);
    }

    public void setRotationY (float angle) {
        rotation.identity().rotateY(angle);
    }
    public void setRotationZ (float angle) {
        rotation.identity().rotateZ(angle);
    }

    public void rotateX (float angle) {
        rotation.rotateX(angle);
    }

    public void rotateY (float angle) {
        rotation.rotateY(angle);
    }
    public void rotateZ (float angle) {
        rotation.rotateZ(angle);
    }



}
