package engine.entities;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

abstract public class Entity {

    protected Vector3f facing;
    protected Vector3f position;

    protected Vector3f scale;
    protected Quaternionf rotation;



    public Entity () {

    }

    public Matrix4f getWorldTransform () {

        //TransformedVector = TranslationMatrix * RotationMatrix * ScaleMatrix * OriginalVector;

        Matrix4f scaleMat = new Matrix4f();
        scaleMat.scale(scale);

        Matrix4f rotationMat = new Matrix4f();
        rotationMat.rotate(rotation);

        Matrix4f translationMat = new Matrix4f();
        translationMat.translate(position);



        return translationMat.mul(rotationMat.mul(scaleMat));
    }







}
