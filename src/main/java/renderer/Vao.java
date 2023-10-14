package renderer;

import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Arrays;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;


public class Vao {

    private float[] vertexArray ;
    private int[] elementArray ;

    private int[] attrLengthArray;
    int vaoID, vboID, eboId;


    public Vao(float[] vertexArray, int[] elementArray, int[] attrLengthArray) {
        this.vertexArray = vertexArray;
        this.elementArray = elementArray;
        this.attrLengthArray = attrLengthArray;
    }


    public void create () {

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


        int floatSizeBytes = 4;
        int vertexSizeBytes = Arrays.stream(attrLengthArray).sum() * floatSizeBytes;
        int pointer = 0;

        for (int i = 0; i < attrLengthArray.length; i++) {

            glVertexAttribPointer(i, attrLengthArray[i] , GL_FLOAT,false, vertexSizeBytes,pointer);
            glEnableVertexAttribArray(i);
            pointer += attrLengthArray[i] * floatSizeBytes;
            System.out.println(attrLengthArray[i]);
        }

    }

    public void bind () {

        glBindVertexArray(vaoID);

        for (int i = 0; i < attrLengthArray.length; i++) {
            glEnableVertexAttribArray(i);
        }


    }

    public void unbind () {


        glBindVertexArray(0);

        // unbind everything
        for (int i = 0; i < attrLengthArray.length; i++) {
            glDisableVertexAttribArray(i);
        }


    }




}
