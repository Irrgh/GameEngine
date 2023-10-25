package engine;

import engine.entities.Camera;
import engine.entities.Entity;
import engine.sound.Sound;
import org.joml.Vector3f;
import org.lwjgl.system.MemoryUtil;
import renderer.Mesh;
import renderer.Vao;
import renderer.Shader;
import util.Time;

import java.util.ArrayList;
import java.util.HashMap;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;
import static org.lwjgl.opengl.GL31.glDrawElementsInstanced;
import static org.lwjgl.opengl.GL43.GL_DEBUG_OUTPUT;
import static org.lwjgl.opengl.GL43.glDebugMessageCallback;

public class TestScene extends Scene {


    public TestScene () {

    }

    private Shader defaultShader;

    private ArrayList<Entity> entities;

    private Mesh mesh;



    @Override
    void update(float dt) {
        // Bind shader program


        camera.update(dt);


        defaultShader.use();
        defaultShader.uploadMat4f("uView",camera.getView());
        defaultShader.uploadMat4f("uProjection", camera.getProjection());
        defaultShader.uploadFloat("uTime", Time.getTime());


        mesh.bind();
        mesh.bindInstanced();




        glDrawElementsInstanced(GL_TRIANGLES, mesh.elementArraySize(), GL_UNSIGNED_INT, 0, 1);

        mesh.unbind();
        //vao.unbind();

        defaultShader.detach();

    }

    @Override
    void init() {

        glDebugMessageCallback((source, type, id, severity, length, message, userParam) -> {
            System.err.println("OpenGL Debug Message:");
            System.err.println("  Source: " + source);
            System.err.println("  Type: " + type);
            System.err.println("  ID: " + id);
            System.err.println("  Severity: " + severity);
            System.err.println("  Message: " + MemoryUtil.memUTF8(message, length));
        }, 0);
        glEnable(GL_DEBUG_OUTPUT);


        this.camera = new Camera(new Vector3f(-0.5f,-1.5f,0.4f), new Vector3f( 0.5f, 0.3f, -0.1f).normalize());
        //camera.setProjectionType(Camera.Projection.ORTHOGRAPHIC);
        defaultShader = new Shader("assets/shaders/default.glsl");
        defaultShader.compile();
        mesh = Mesh.loadObj("assets/tree.obj");
        mesh.createBuffers();
        Mesh.instance(mesh, new Entity());

        Sound sound = new Sound("assets/dreaming.ogg", true);

        sound.play();





        mesh.bindInstanced();


        defaultShader.createAndBindAndUploadTexture("assets/frog.png", "textureSampler");


        // ========================================================
        // Generate VAO, VBO and EBO buffer objects and send to GPU
        // ========================================================


        // Enable depth test
        glEnable(GL_DEPTH_TEST);
        // Accept fragment if it closer to the camera than the former one
        glDepthFunc(GL_LESS);


    }



}
