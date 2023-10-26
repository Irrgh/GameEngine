package entity;

import engine.KeyListener;
import engine.MouseListener;
import engine.Tickable;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.openal.AL10;


import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.openal.AL10.AL_NO_ERROR;
import static org.lwjgl.openal.AL10.alGetError;

public class Camera extends Entity implements Tickable {

    private Matrix4f viewMatrix, projectionMatrix;

    private Vector3f cameraUp;
    private float fov;

    private float orthographicScale;

    public boolean rotationEnabled = true;
    private float yaw, pitch;

    private Projection type;

    public enum Projection {
        PERSPECTIVE,
        ORTHOGRAPHIC
    }

    public void setProjectionType(Projection proj) {
        type = proj;
    }


    public Camera (Vector3f position, Vector3f facing) {
        this.position = position;
        this.facing = facing;
        this.viewMatrix = new Matrix4f();
        this.projectionMatrix = new Matrix4f();
        this.fov = (float) Math.toRadians(90);
        this.cameraUp = new Vector3f(0,0,1);
        pitch = (float) Math.acos(facing.z / facing.length());
        yaw = (float) (Math.signum(facing.y) * Math.acos(facing.x / Math.sqrt(facing.x*facing.x + facing.y* facing.y)));
        type = Projection.PERSPECTIVE;


        updateProjection();
    }


    public void update (float dt) {

        float distance = dt * 5;   // 10 units / s

        float[] pos = new float[] {position.x, position.y, position.z};
        float[] or = new float[]{facing.x, facing.y, facing.z, cameraUp.x, cameraUp.y, cameraUp.z};

        //System.out.println(Arrays.toString(pos));
        //System.out.println(Arrays.toString(or));

        AL10.alListener3f(AL10.AL_POSITION, position.x, position.y, position.z);    // TODO: DIRECTIONAL AUDIO DOESN'T FUCKING WORK
        AL10.alListenerfv(AL10.AL_ORIENTATION, or);

        int err = alGetError();
        if (AL_NO_ERROR != err) {
            System.err.println(err);
        }


        if (rotationEnabled) {
            rotate(MouseListener.getDx() , MouseListener.getDy());
        }


        Vector3f newPos = new Vector3f(facing.x, facing.y, 0).normalize(distance);
        if(KeyListener.isKeyPressed(GLFW_KEY_W)) {
            position.add(newPos);
        }
        if (KeyListener.isKeyPressed(GLFW_KEY_S)) {
            position.sub(newPos);
        }

        newPos.rotateZ((float) Math.toRadians(90)); // rotated to A-D axis

        if (KeyListener.isKeyPressed(GLFW_KEY_A)) {
            position.add(newPos);
        }
        if (KeyListener.isKeyPressed(GLFW_KEY_D)) {
            position.sub(newPos);
        }

        newPos.set(new Vector3f(0,0,distance));

        if (KeyListener.isKeyPressed(GLFW_KEY_SPACE)) {
            position.add(newPos);
        }

        if (KeyListener.isKeyPressed(GLFW_KEY_LEFT_SHIFT)) {
            position.sub(newPos);
        }

        fov += MouseListener.getScrollY() / 30;
        updateProjection();
    }



    public void rotate (float x, float y) {

        yaw +=  Math.atan2(x,350f/2);
        pitch -= Math.atan2(y, 350/2);
        pitch = (float) (Math.max(0.001, Math.min(Math.PI-0.001, pitch)));

        //System.out.println("yaw: " + yaw + " pitch: " + pitch);

        float newX = (float) (Math.sin(pitch) * Math.cos(yaw));
        float newY = (float) (Math.sin(pitch) * Math.sin(yaw));
        float newZ = (float) Math.cos(pitch);

        //System.out.println(facing);

        facing.set(newX, newY, newZ);

        //System.out.println(facing.length());

        facing.normalize();

    };


    public void updateProjection (){
        if (type == Projection.PERSPECTIVE) {
            setPerspectiveProjection(fov, 16f/9f, 0.1f, 1000);
        } else if (type == Projection.ORTHOGRAPHIC) {
            setOrthographicProjection(10,10,1,1000);
        }

    }


    public void setOrthographicProjection(float x, float y, float near, float far) {
        projectionMatrix.identity();
        projectionMatrix.ortho(-x/2, x/2, -x/2, x/2, near, far);
    }

    public void setPerspectiveProjection(float fovy, float aspect, float near, float far) {
        projectionMatrix.identity();
        projectionMatrix.perspective(fovy,aspect,near,far);
    }


    public Matrix4f getView() {
        Vector3f direction = new Vector3f(facing);


        viewMatrix.identity();
        viewMatrix.lookAt(new Vector3f(position.x, position.y, position.z),
                        direction.add(new Vector3f(position.x, position.y, position.z))
                        ,cameraUp);

        return  viewMatrix;
    }

    public Matrix4f getProjection() {
        return projectionMatrix;
    }


}
