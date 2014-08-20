## Contributing to LibShapeDraw

If you're an open-source Java developer looking to improve LibShapeDraw,
welcome! All reasonable pull requests are considered; feel free to fork and PR.
If you'd like to discuss potential major changes, please open an issue on
GitHub.

Not a developer? No worries: any bug reports, feature requests, and general
comments are much appreciated. There's also a need for some additional
documentation; see below.

## Development roadmap

Possible documentation improvements (volunteers welcome):

 +  A better logo. The [current one](https://github.com/bencvt/LibShapeDraw/blob/master/projects/main/src/main/resources/libshapedraw/logo.png)
    is rather bland. Ideally the logo would include a unique icon and would look
    good at various resolutions.

 +  Expand the demos to include an example Forge/FML-style mod.

 +  YouTube-hosted demo videos would be nice to have as well so you don't
    actually have to install the demos jar to see it in action.

 +  Create a complete tutorial on how to start a fresh mod that uses
    LibShapeDraw, perhaps using a GitHub wiki.

Possible new core features, unprioritized (feedback and patches welcome):

 +  Optimization: where possible, bypass Minecraft's Tessellator and instead use
    [OpenGL VBOs](http://en.wikipedia.org/wiki/Vertex_Buffer_Object). This is a
    significant performance gain for Shapes with many vertices.

 +  More built-in Shapes, perhaps with texture support as well.

 +  Expand the scope of the library to offer GUI/HUD tools. Like the animation
    component, these tools would be strictly optional; mods would be free to
    pick-and-choose the components they want to use.

 +  Add a plugin channel to allow servers to access the API on the client.
    The end user would be able to disable this feature if they'd rather not let
    servers draw shapes on their screen.

### LibShapeDraw 2.0 plans

The official Minecraft API keeps getting pushed back; Mojang's current plan is
for it to be released in **Minecraft 1.5** after a rendering engine overhaul.

Obviously both the Minecraft API and the rendering engine overhaul are very
relevant to LibShapeDraw. The current plan is for **LibShapeDraw 2.0** to be
released along with Minecraft 1.5, with the following adjustments:

 +  If the Minecraft API includes a client-side plugin loading mechanism, make
    LibShapeDraw a *plugin* rather than a *mod*, to standardize and simplify
    installation (no more messing with .class files!) This would also remove the
    ModLoader/Forge requirement.

 +  If possible, LibShapeDraw's semantics will *not* change significantly, but
    the underlying implementation will likely have to change to match
    Minecraft's overhauled rendering engine.
    
    Mojang has not released any technical details yet, but the overhaul may in
    fact remove Minecraft's long-standing use of the OpenGL Fixed Function
    Pipeline, which was [deprecated](http://www.opengl.org/wiki/Legacy_OpenGL)
    several years ago by OpenGL. Using VBOs (Vertex Buffer Objects), etc.
    instead is usually a significant performance boost as data can persist in
    VRAM rather than being copied every render frame.
    
    The downside is that it's more code to set up all those vertex buffers. But
    if Minecraft itself takes that step, LibShapeDraw will follow.

 +  Any method marked as deprecated in LibShapeDraw 1.x will likely be removed
    in 2.0.

## Tips and tricks

 +  You can enable automatic debug dumps and tweak a few other global settings
    by copying `libshapedraw/internal/default-settings.properties`  
    from the jar to the file system at  
    `(minecraft-dir)/mods/LibShapeDraw/settings.properties`. A future version
    of LibShapeDraw will probably auto-copy this file but for now the default
    settings are left in the archive.

 +  Mods can manually trigger debug dumps by calling `LibShapeDraw.debugDump()`.

 +  Minecraft's built-in profiler (accessible via `shift-F3`) includes a
    section named `root.gameRenderer.level.LibShapeDraw`. You can drill down to
    it using the number keys. It should come as no surprise that having a ton of
    Shapes on-screen simulatenously can cause a significant performance hit. The
    profiler can tell you exactly how much of a hit.

## Building LibShapeDraw from source

**NB**: This section is pending a re-write since it is out-of-date due to a
hybrid MCP/Gradle/ForgeGradle system that I employ; the theory is still
applicable though.

If you just want to use the API in your own mod, feel free to skip the rest of
this file and use a prebuilt jar.

1.  Install [Maven](http://maven.apache.org/).
2.  Copy the contents of your Minecraft's `bin` directory to `lib`. Be sure to
    include the `natives` subdirectory; the test suite needs them.
    `minecraft.jar` should be vanilla.
3.  Download the latest recommended universal binary for Minecraft Forge from
    [minecraftforge.net](http://www.minecraftforge.net/forum/index.php/topic,5.0.html).
    Move/rename it to `lib/minecraftforge-universal.jar`.
4.  Create `lib/minecraft-deobf.jar` using MCP. Details in the next section.
5.  Change directory to `projects/all`.
6.  Run Maven by typing `mvn` (no goal arguments needed). The output jars are
    located in `projects/*/target/`. Javadocs are located in
    `projects/dev/target/site/apidocs`.

If you prefer to use an IDE, here's one recommended method:

1.  Follow the above steps first, making sure everything compiles.
2.  Install [Eclipse](http://www.eclipse.org/) and a
    [Maven integration plugin](http://wiki.eclipse.org/M2E).
3.  Import the Maven projects to your workspace.

### Creating a `minecraft-deobf.jar` for `LibShapeDraw-VERSION-dev.jar`

First of all, here's how to create `minecraft-deobf.jar`:

1.  Install MCP in another directory and decompile a `minecraft.jar` with
    ModLoader patched in. Or install MCP with the Forge sources. Either works.
2.  Open up a terminal window and change to the `bin/minecraft` subdirectory
    under the MCP directory. (If using Forge, it's just `bin`.)
3.  Type `jar cfv (repo-dir)/LibShapeDraw/lib/minecraft-deobf.jar .` (including
    the dot at the end, and substituting `(repo-dir)` as appropriate.)

You may be wondering: what's the point of all these jars? If so, gather 'round.
(If not, feel free to skip the rest of this section.)

Using LibShapeDraw (or any external Minecraft API for that matter) in MCP is a
non-trivial problem. The naive, quick-and-dirty method is to just patch
LibShapeDraw into `minecraft.jar` and let MCP decompile it. However this doesn't
work for Forge, which requires a vanilla jar. The decompiling process also
munges class names and removes Javadocs. Clearly we can do better.

Adding the prebuilt LibShapeDraw jar to the classpath is the correct approach,
but we're not out of the woods yet.

By necessity, the main release jar for LibShapeDraw references obfuscated
Minecraft code. However the MCP dev environment lets you test/debug your mod
*without* reobfuscating. This is a problem when referencing a prebuilt jar:
obfuscation and deobfuscation won't mix!

The solution is to have a second prebuilt LibShapeDraw jar that uses
deobfuscated references instead. Note that either jar will work fine for
*compiling*, as the public LibShapeDraw API does not reference any Minecraft
class. The internal obfuscated/deobfuscated code references only matter when
*running* Minecraft.

As a side note, LibShapeDraw does not use MCP directly, instead handling
obfuscation on its own. The main reason for this is that MCP really doesn't
integrate well with Maven, JUnit, and Git.

However, mod developers can (and generally should) still use MCP along with
LibShapeDraw. See the *How to add the LibShapeDraw jar to the classpath in MCP*
section in the main `README.md` for details.

So, the special dev jar (`LibShapeDraw-VERSION-dev.jar`) exists to support mod
devs using MCP. It's identical to the normal release except that:

 +  As described above, `mod_LibShapeDraw` links to deobfuscated identifiers in
    `minecraft-deobf.jar` rather than `minecraft.jar`.
 +  The jar's manifest is not marked as an FML coremod.
 +  The source code is included.
 +  The update check is disabled by default.

## Release procedure

Note to self:

1.  update `CHANGELOG.md`
2.  edit versions in `projects/all/pom.xml`
3.  `cd projects/all; mvn`
4.  verify the build works as expected
5.  `git commit -m '1.x release for Minecraft 1.y'`
6.  `git tag v1.x`
7.  re-edit the version in `projects/all/pom.xml`, appending "-SNAPSHOT"
8.  `git commit -m '1.x post-release'`
9.  `git push origin master --tags`
10. publish all 3 jars and Javadocs to GitHub
11. update minecraftforum.net thread
12. publish new version number to `http://update.bencvt.com/u/LibShapeDraw`
