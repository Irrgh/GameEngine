package engine;

import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import util.Time;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {
    private int width,height;
    private String title;
    private long glfwWindow;

    private Scene scene = new TestScene();

    private static Window window = null;

    private Window() {
        this.width = 1920/2;
        this.height = 1200/2;
        this.title = "";
    }


    public static Window get() {
        if (Window.window == null) {
            Window.window = new Window();
        }
        return Window.window;
    }

    public void run () {
        System.out.println("Hello LWJGL " + Version.getVersion() + "!");

        init();
        loop();

        // Free the memory
        glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);

        // Terminate GLFW and Free error Callbacks

        glfwTerminate();
        glfwSetErrorCallback(null).free();

    }

    public void init() {
        // Setup an Error Callback
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW
        if (!glfwInit()) {
            throw new IllegalStateException ("Unable to initialize GLFW");
        }

        // Configure GLFW
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);
        //glfwWindowHint(GLFW_TRANSPARENT_FRAMEBUFFER, GLFW_TRUE);
        //glfwWindowHint(GLFW_FLOATING, GLFW_TRUE);
        glfwWindowHint(GLFW_DECORATED, GLFW_FALSE);
        glfwWindowHint(GLFW_SRGB_CAPABLE, GLFW_TRUE);

        // Create Window


        glfwWindow = glfwCreateWindow(this.width, this.height, this.title,NULL,NULL);
        if (glfwWindow == NULL) {
            throw new IllegalStateException ("Failed to create the GLFW window");
        }




        glfwSetCursorPosCallback(glfwWindow, MouseListener::mousePosCallback);
        glfwSetMouseButtonCallback(glfwWindow, MouseListener::mouseButtonCallback);
        glfwSetScrollCallback(glfwWindow, MouseListener::mouseScrollCallback);
        glfwSetKeyCallback(glfwWindow, KeyListener::keyCallback);

        if (glfwRawMouseMotionSupported()) {

            glfwSetInputMode(glfwWindow, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
            glfwSetInputMode(glfwWindow, GLFW_RAW_MOUSE_MOTION, GLFW_TRUE);
            glfwSetCursorPos(glfwWindow, width,height);
            MouseListener.setX(0);
            MouseListener.setY(0);    // TODO: fix the jerking when starting app
            System.out.println("supported");
        }



        // Make the Open Gl context current

        glfwMakeContextCurrent(glfwWindow);

        // Enable V-Sync
        glfwSwapInterval(1);

        // Show the window
        glfwShowWindow(glfwWindow);

        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();


    }

    public void loop() {

        float beginTime = Time.getTime();
        float endTime = Time.getTime();
        float dt = endTime - beginTime;

        scene.init();

        while (!glfwWindowShouldClose(glfwWindow)) {

            // Poll Events
            glfwPollEvents();

            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);


            scene.update(dt);

            if (KeyListener.isKeyPressed(GLFW_KEY_ESCAPE)) {

                if (scene.camera.rotationEnabled) {
                    glfwSetInputMode(glfwWindow, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
                    scene.camera.rotationEnabled = false;
                } else {
                    glfwSetInputMode(glfwWindow, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
                    scene.camera.rotationEnabled = true;
                }

            }



            glfwSwapBuffers(glfwWindow);

            MouseListener.endFrame();


            endTime = Time.getTime();
            dt = endTime - beginTime;
            beginTime = endTime;
            //System.out.println("This frame took " + dt + "seconds to render");
        }

    }


}
