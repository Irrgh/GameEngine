package engine;

import org.joml.Vector3f;
import renderer.Mesh;
import renderer.Vao;
import renderer.Shader;
import util.Time;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class TestScene extends Scene {


    public TestScene () {

    }

    private Shader defaultShader;

    private Vao vao;

    private Mesh mesh;

    private float[] vertexArray = {
        //position              //uv                    //color
         1.5f, -1.5f,  0.0f,    1.0f, 0.0f,         1.0f, 0.0f, 1.0f, 0.5f,  // Bottom right
        -1.5f,  1.5f,  0.0f,    0.0f, 1.0f,         0.0f, 1.0f, 0.0f, 0.5f,  // Top left
         1.5f,  1.5f,  0.0f,    1.0f, 1.0f,         0.0f, 0.0f, 1.0f, 0.5f,  // Top right
        -1.5f, -1.5f, -0.0f,    0.0f, 0.0f,         1.0f, 1.0f, 0.0f, 0.5f,   // Bottom left
         0.0f, -1.5f,  1.5f,    1.0f, 0.0f,         1.0f, 0.0f, 1.0f, 0.5f,  // Bottom right
         0.0f,  1.5f, -1.5f,    0.0f, 1.0f,         0.0f, 1.0f, 0.0f, 0.5f,  // Top left
         0.0f,  1.5f,  1.5f,    1.0f, 1.0f,         0.0f, 0.0f, 1.0f, 0.5f,  // Top right
        -0.0f, -1.5f, -1.5f,    0.0f, 0.0f,         1.0f, 1.0f, 0.0f, 0.5f   // Bottom left
    };

    private int[] elementArray = {
        2,1,0,  // Top right triangle
        0,1,3,   // bottom left triangle
        6,5,4,
        4,5,7
    };



    @Override
    void update(float dt) {
        // Bind shader program


        camera.update();


        defaultShader.use();
        defaultShader.uploadMat4f("uView",camera.getView());
        defaultShader.uploadMat4f("uProjection", camera.getProjection());
        defaultShader.uploadFloat("uTime", Time.getTime());


        //vao.bind();
        mesh.bind();

        glDrawElements(GL_TRIANGLES, mesh.elementArraySize(), GL_UNSIGNED_INT, 0);

        mesh.unbind();
        //vao.unbind();

        defaultShader.detach();

    }

    @Override
    void init() {
        this.camera = new Camera(new Vector3f(-0.5f,-1.5f,0.4f), new Vector3f( 0.5f, 0.3f, -0.1f).normalize());
        defaultShader = new Shader("assets/shaders/default.glsl");
        defaultShader.compile();
        mesh =Mesh.loadObj("assets/tree.obj");
        mesh.create();

        defaultShader.createAndBindAndUploadTexture("assets/frog.png", "textureSampler");

        // ========================================================
        // Generate VAO, VBO and EBO buffer objects and send to GPU
        // ========================================================


        // Enable depth test
        glEnable(GL_DEPTH_TEST);
        // Accept fragment if it closer to the camera than the former one
        glDepthFunc(GL_LESS);

        //vao = new Vao(vertexArray, elementArray, new int[] {3,2,4});
        //vao.create();



    }



}
