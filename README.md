# GameEngine
A custom GameEngine written in Java using LWJGL.

What can you currently do with this?

- Import triangulated .obj Models with xyz uv and normals.
- Import Textures that can me mapped onto those objects with the uv
- Explore your 3d-model in either orthographic or perspective camera mode
- Load sounds and play them with speakers in space



![tree imported from blender colored with normals](/assets/models/preview.png?raw=true)


# Goals:

- Figuring out a proper architecture for game loop / asset management / events / multiplayer


## Shortterm goals:

- Entities that can instance meshes.
- Multi-Draw Indirect (or indirect instanced rendering). This approach allows you to specify multiple draw calls with varying parameters in a single buffer, which is then processed efficiently by the GPU.
- Optimize Mesh loader support:
  - support loading materials as basic shaders
  - optional interlaced vertex buffers
  - optimized parsing
  - reduce memory by joining identical vertexes (used by multiple tris).
  - support non triangulated meshes


- adding an in game / extern console to better debug
- other debug tools