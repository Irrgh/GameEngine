package renderer;


import org.lwjgl.BufferUtils;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;


import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class Mesh {

    float[] vertexArray;

    float[] uvArray;

    float[] normalArray;

    int[] elementArray;


    // x,y,z,  u,v, xn,yn,zn

    private int vboID, uvboId, norboId;
    private int vaoID;
    private int eboId;


    private Mesh (float[] vertexArray, float[] uvArray, float[] normalArray, int[] elementArray) {
        this.vertexArray = vertexArray;
        this.uvArray = uvArray;
        this.normalArray = normalArray;
        this.elementArray = elementArray;
    }


    public int elementArraySize() {
        return elementArray.length;
    }


    public static Mesh loadObj(String filePath)  {


        ArrayList<String> file = null;

        ArrayList<Float> tempVertices = new ArrayList<>();
        ArrayList<Float> tempNormals = new ArrayList<>();
        ArrayList<Float> tempuvs = new ArrayList<>();

        ArrayList<Integer> vertexIndices = new ArrayList<>();
        ArrayList<Integer> normalsIndices = new ArrayList<>();
        ArrayList<Integer> uvIndices = new ArrayList<>();

        int facecount = 0;


        try {
            file = (ArrayList<String>) Files.readAllLines(Path.of(filePath));

        } catch (IOException e) {
            e.printStackTrace();

        }


        for (int i = 0; i < file.size(); i++) {

            String str = file.get(i);

            if (str.startsWith("v ")) {

                String[] line = str.split(" ");
                tempVertices.add(Float.parseFloat(line[1]));
                tempVertices.add(Float.parseFloat(line[2]));
                tempVertices.add(Float.parseFloat(line[3]));




            }

            if (str.startsWith("vt ")) {


                String[] line = str.split(" ");
                tempuvs.add(Float.parseFloat(line[1]));
                tempuvs.add(Float.parseFloat(line[2]));



            }

            if (str.startsWith("vn ")) {


                String[] line = str.split(" ");
                tempNormals.add(Float.parseFloat(line[1]));
                tempNormals.add(Float.parseFloat(line[2]));
                tempNormals.add(Float.parseFloat(line[3]));



            }

            if (str.startsWith("f ")) {


                String[] line = str.split("[ /]");

                facecount++;

                    vertexIndices.add(Integer.parseInt(line[1]));
                    vertexIndices.add(Integer.parseInt(line[4]));
                    vertexIndices.add(Integer.parseInt(line[7]));

                    uvIndices.add(Integer.parseInt(line[2]));
                    uvIndices.add(Integer.parseInt(line[5]));
                    uvIndices.add(Integer.parseInt(line[8]));

                    normalsIndices.add(Integer.parseInt(line[3]));
                    normalsIndices.add(Integer.parseInt(line[6]));
                    normalsIndices.add(Integer.parseInt(line[9]));


            }

        }

        facecount *=3;   // TODO I'll look into how to make this more beautiful

        float[] vertexArray = new float[facecount*3];

        for (int i = 0; i < vertexArray.length / 3; i++) {

            int index = (vertexIndices.get(i)-1)*3;
            vertexArray[i*3] = tempVertices.get(index);
            vertexArray[i*3+1] = tempVertices.get(index+1);
            vertexArray[i*3+2] = tempVertices.get(index+2);

        }

        float[] uvArray = new float[facecount*2];

        for (int i = 0; i < uvArray.length / 2; i++) {

            int index = (uvIndices.get(i)-1)*2;
            uvArray[i*2] = tempuvs.get(index);
            uvArray[i*2+1] = tempuvs.get(index+1);
        }

        float[] normalArray = new float[facecount*3];
        int[] elementArray = new int[facecount];

        for (int i = 0; i < normalArray.length / 3; i++) {

            int index = (normalsIndices.get(i)-1) * 3;
            normalArray[i*3] = tempNormals.get(index);
            normalArray[i*3+1] = tempNormals.get(index+1);
            normalArray[i*3+2] = tempNormals.get(index+2);
        }

        for (int i = 0; i < elementArray.length; i++) {
            elementArray[i] = i;
        }

        //System.out.println(tempVertices);
        //System.out.println(tempVertices.size());


        //System.out.println(vertexIndices);
        //System.out.println(vertexIndices.size());

        //System.out.println(Arrays.toString(vertexArray));
        //System.out.println(vertexArray.length);


        //System.out.println(Arrays.toString(elementArray));
        //System.out.println(elementArray.length);



        return new Mesh(vertexArray, uvArray, normalArray, elementArray);
    }



    public void create () {

        // Create a float buffer of vertices

        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexArray.length);
        vertexBuffer.put(vertexArray).flip();   // flip is important

        FloatBuffer uvBuffer = BufferUtils.createFloatBuffer(uvArray.length);
        uvBuffer.put(uvArray).flip();   // flip is important


        FloatBuffer normalBuffer = BufferUtils.createFloatBuffer(normalArray.length);
        normalBuffer.put(normalArray).flip();   // flip is important

        IntBuffer elementBuffer = BufferUtils.createIntBuffer(elementArray.length);
        elementBuffer.put(elementArray).flip(); // flip is important




        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);
        // Create VBO and vertex buffer


        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);
        glVertexAttribPointer(0,  3, GL_FLOAT,false, 0,0);




        uvboId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, uvboId);
        glBufferData(GL_ARRAY_BUFFER, uvBuffer, GL_STATIC_DRAW);
        glVertexAttribPointer(1,  2, GL_FLOAT,false, 0,0);




        norboId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER,norboId);
        glBufferData(GL_ARRAY_BUFFER, normalBuffer, GL_STATIC_DRAW);
        glVertexAttribPointer(2,  3, GL_FLOAT,false, 0,0);


        // Create indices and upload


        eboId = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboId);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL_STATIC_DRAW);




    }

    public void bind () {

        glBindVertexArray(vaoID);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);

    }

    public void unbind () {


        glBindVertexArray(0);
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(2);

    }


}
