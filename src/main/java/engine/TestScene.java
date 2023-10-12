package engine;

import org.joml.AxisAngle4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import renderer.Shader;
import util.Time;

import java.awt.event.KeyEvent;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class TestScene extends Scene {


    public TestScene () {

    }

    private Shader defaultShader;

    private float[] vertexArray = {
        //position               //color
         1.5f, -1.5f, 0.0f,      1.0f, 0.0f, 1.0f, 0.5f,  // Bottom right
         -1.5f, 1.5f, 0.0f,      0.0f, 1.0f, 0.0f, 0.5f,  // Top left
         1.5f, 1.5f, 0.0f,      0.0f, 0.0f, 1.0f, 0.5f,  // Top right
        -1.5f, -1.5f, -0.0f,      1.0f, 1.0f, 0.0f, 0.5f,   // Bottom left
         0.0f,-1.5f, 1.5f,      1.0f, 0.0f, 1.0f, 0.5f,  // Bottom right
         0.0f,1.5f,  -1.5f,     0.0f, 1.0f, 0.0f, 0.5f,  // Top left
        0.0f, 1.5f,  1.5f,     0.0f, 0.0f, 1.0f, 0.5f,  // Top right
        -0.0f, -1.5f,  -1.5f,      1.0f, 1.0f, 0.0f, 0.5f   // Bottom left
    };

    private int[] elementArray = {
        2,1,0,  // Top right triangle
        0,1,3,   // bottom left triangle
        6,5,4,
        4,5,7
    };

    private  int vaoID, vboID, eboId;

    @Override
    void update(float dt) {
        // Bind shader program


        camera.update();


        defaultShader.use();
        defaultShader.uploadMat4f("uView",camera.getView());
        defaultShader.uploadMat4f("uProjection", camera.getProjection());
        defaultShader.uploadFloat("uTime", Time.getTime());



        // Bind the VAO
        glBindVertexArray(vaoID);

        // Enable the vertex attribute pointers
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, elementArray.length, GL_UNSIGNED_INT, 0);

        // unbind everything

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);

        glBindVertexArray(0);

        defaultShader.detach();

    }

    @Override
    void init() {
        this.camera = new Camera(new Vector3f(-0.5f,-1.5f,0.4f), new Vector3f( 0.5f, 0.3f, -0.1f).normalize());
        defaultShader = new Shader("assets/shaders/default.glsl");
        defaultShader.compile();



        // ========================================================
        // Generate VAO, VBO and EBO buffer objects and send to GPU
        // ========================================================


        // Enable depth test
        glEnable(GL_DEPTH_TEST);
        // Accept fragment if it closer to the camera than the former one
        glDepthFunc(GL_LESS);

        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        // Create a float buffer of vertices

        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexArray.length);
        vertexBuffer.put(vertexArray).flip();   // flip is important

        // Create VBO and vertex buffer

        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);

        // Create indices and upload

        IntBuffer elementBuffer = BufferUtils.createIntBuffer(elementArray.length);
        elementBuffer.put(elementArray).flip(); // flip is important


        eboId = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboId);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL_STATIC_DRAW);

        // Add vertex attribute pointers
        int positionsSize = 3;
        int colorSize = 4;
        int floatSizeBytes = 4;
        int vertexSizeBytes = (positionsSize + colorSize) * floatSizeBytes;
        glVertexAttribPointer(0, positionsSize, GL_FLOAT,false, vertexSizeBytes,0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, colorSize, GL_FLOAT,false, vertexSizeBytes,positionsSize*floatSizeBytes);
        glEnableVertexAttribArray(1);


    }

}
