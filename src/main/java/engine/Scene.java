package engine;

import engine.entities.Camera;

public abstract class Scene {

    protected Camera camera;
    abstract void update (float dt);

    abstract void init ();



}
