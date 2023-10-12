package renderer;

import org.joml.Matrix2f;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.joml.Vector3f;
import org.joml.Vector2f;

import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBImage;
import util.Time;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class Shader {

    private int shaderProgramId;

    private boolean isUsed = false;

    private String vertexSource;
    private String fragmentSource;
    private String filePath;

    private HashMap <String,Integer> loadedTextures = new HashMap<>();


    public Shader (String filePath) {
        this.filePath = filePath;

        try {
            String source = Files.readString(Path.of(filePath));
            String[] splitString = source.split("(//type)( )+([a-zA-Z]+)");

            int index = source.indexOf("//type") + 7;
            int eol = source.indexOf("\r\n",index);
            String firstPattern = source.substring(index,eol).trim();


            index = source.indexOf("//type",eol) + 7;
            eol = source.indexOf("\r\n",index);
            String secondPattern = source.substring(index,eol).trim();


            switch (firstPattern) {
                case "vertex": vertexSource = splitString[1];
                    break;
                case "fragment": fragmentSource = splitString[1];
                    break;
                default: throw new IOException("Unexpected Token '" + firstPattern + "'");
            }

            switch (secondPattern) {
                case "vertex": vertexSource = splitString[2];
                    break;
                case "fragment": fragmentSource = splitString[2];
                    break;
                default: throw new IOException("Unexpected Token '" + secondPattern + "'");
            }

        } catch (IOException e) {
            e.printStackTrace();
            assert false : "Error loading shader file '" + filePath + "'";
        }



    }

    public int getShaderProgramId () {
        return shaderProgramId;
    }

    public void compile () {

        // ========================
        // Compile and Link Shaders
        // ========================

        int vertexID, fragmentID;

        // load and compile vertex shader

        vertexID = glCreateShader(GL_VERTEX_SHADER);

        // Pass Shader source to GPU
        glShaderSource(vertexID,vertexSource);
        glCompileShader(vertexID);

        // Check for errors
        int success = glGetShaderi(vertexID, GL_COMPILE_STATUS);
        if (success == GL_FALSE) {
            int length = glGetShaderi(vertexID, GL_INFO_LOG_LENGTH);
            System.out.println("Error: '" +  filePath + "' \n\t Vertex Shader Compilation failed");
            System.out.println(glGetShaderInfoLog(vertexID,length));
            assert false : "";
        }

        // load and compile vertex shader

        fragmentID = glCreateShader(GL_FRAGMENT_SHADER);

        // Pass Shader source to GPU
        glShaderSource(fragmentID,fragmentSource);
        glCompileShader(fragmentID);

        // Check for errors
        success = glGetShaderi(fragmentID, GL_COMPILE_STATUS);
        if (success == GL_FALSE) {
            int length = glGetShaderi(fragmentID, GL_INFO_LOG_LENGTH);
            System.out.println("Error: '" + filePath + "' \n\t Fragment Shader Compilation failed");
            System.out.println(glGetShaderInfoLog(fragmentID,length));
            assert false : "";
        }


        // Link shader and check for errors

        shaderProgramId = glCreateProgram();
        glAttachShader(shaderProgramId,vertexID);
        glAttachShader(shaderProgramId,fragmentID);
        glLinkProgram(shaderProgramId);

        success = glGetProgrami(shaderProgramId,GL_LINK_STATUS);
        if (success == GL_FALSE) {
            int length = glGetProgrami(shaderProgramId, GL_INFO_LOG_LENGTH);
            System.out.println("Error: '" + filePath + "' \n\t Linking Shaders failed");
            System.out.println(glGetProgramInfoLog(shaderProgramId,length));
            assert false : "";
        }
        
        
        
    }








    public void use () {
        if (!isUsed) {
            isUsed = true;
            glUseProgram(shaderProgramId);
        }

    }

    public void detach () {
        glUseProgram(0);
        isUsed = false;
    }

    public void uploadMat4f (String varName, Matrix4f mat4) {
        int varLocation = glGetUniformLocation(shaderProgramId, varName);
        use();
        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(16);
        mat4.get(matBuffer);
        glUniformMatrix4fv(varLocation, false, matBuffer);
        
    }

    public void uploadMat3f (String varName, Matrix3f mat3) {
        int varLocation = glGetUniformLocation(shaderProgramId, varName);
        use();
        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(9);
        mat3.get(matBuffer);
        glUniformMatrix4fv(varLocation, false, matBuffer);

    }

    public void uploadMat2f (String varName, Matrix2f mat2) {
        int varLocation = glGetUniformLocation(shaderProgramId, varName);
        use();
        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(4);
        mat2.get(matBuffer);
        glUniformMatrix4fv(varLocation, false, matBuffer);

    }

    public void uploadVec4f (String varName, Vector4f vec4) {
        int varLocation = glGetUniformLocation(shaderProgramId,varName);
        use();
        glUniform4f(varLocation, vec4.x, vec4.y, vec4.z, vec4.z);
    }

    public void uploadVec3f (String varName, Vector3f vec3) {
        int varLocation = glGetUniformLocation(shaderProgramId,varName);
        use();
        glUniform3f(varLocation, vec3.x, vec3.y, vec3.z);
    }

    public void uploadVec2f (String varName, Vector2f vec2) {
        int varLocation = glGetUniformLocation(shaderProgramId,varName);
        use();
        glUniform2f(varLocation, vec2.x, vec2.y);
    }

    public void uploadFloat (String varName, float val) {
        int varLocation = glGetUniformLocation(shaderProgramId,varName);
        use();
        glUniform1f(varLocation, val);
    }

    public void uploadInt (String varName, int val) {
        int varLocation = glGetUniformLocation(shaderProgramId,varName);
        use();
        glUniform1i(varLocation, val);
    }



    public void createAndBindAndUploadTexture (String filePath, String varName) {

        if (!loadedTextures.containsKey(filePath)) {


            int[] width = new int[1];
            int[] height = new int[1];
            int[] channels = new int[1];
            ByteBuffer imageBuffer = STBImage.stbi_load(filePath, width, height, channels, 4); // 4 channels for RGBA


            int textureID = glGenTextures();
            glBindTexture(GL_TEXTURE_2D, textureID);
            int error = glGetError();
            if (error != GL_NO_ERROR) {
                System.err.println("OpenGL Error: " + error);
            }

            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR_MIPMAP_LINEAR);

            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width[0], height[0], 0, GL_RGBA, GL_UNSIGNED_BYTE, imageBuffer);

            STBImage.stbi_image_free(imageBuffer);


            glBindTexture(GL_TEXTURE_2D, textureID);
            loadedTextures.put(filePath, textureID);
            System.out.println(loadedTextures.size());


            // Render the object using the bound texture

            // Set the uniform in the shader program to the texture unit index
            int textureUniformLocation = glGetUniformLocation(shaderProgramId, varName);
            glUniform1i(textureUniformLocation, 0); // 0 corresponds to GL_TEXTURE0
        }

    }



}
