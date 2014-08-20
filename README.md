Quick links: [ [For Developers](#for-developers) | [Javadocs](http://xaeroverse.github.io/LibShapeDraw/apidocs/index.html) | [Demos](https://github.com/xaeroverse/LibShapeDraw/tree/master/projects/demos/src/main/java/libshapedraw/demos) | [Releases](https://github.com/xaeroverse/LibShapeDraw/releases) ]

# For Players

LibShapeDraw is a Minecraft client library mod that doesn't do anything on its
own. Rather, it provides a set of flexible and powerful drawing and animation
tools for other mods to use.

See the [current LibShapeDraw thread on minecraftforum.net](http://www.minecraftforum.net/forums/t/2188255)
for some screenshots and videos of what sort visual effects are possible.

## Installation

First of all, make sure that either
[LiteLoader](http://www.minecraftforum.net/topic/1868280-) or
[Forge](http://www.minecraftforge.net/forum/) is installed. LibShapeDraw is
compatible with either.

Next, download the jar and move it to the versioned `mods` directory.  This is simply a subdirectory under `mods` named after the Minecraft version desired.  For example, jars for Minecraft 1.7.10 are placed under `mods/1.7.10/`.

## Compatibility

LibShapeDraw was designed with compatibility in mind. It does not modify *any*
vanilla classes directly via jar-modding and therefore should be compatible with
virtually every mod that works with LiteLoader or Forge.

## Troubleshooting

If Minecraft is crashing, check [Minecaft Modded Client Support](http://www.minecraftforum.net/forums/support/modded-client-support)
on minecraftforum.net as a first step.

If it's not, the next step is to verify that LibShapeDraw is installed properly.
Browse to your Minecraft directory and look for a `mods/LibShapeDraw`
subdirectory. There should be a file named `LibShapeDraw.log` in it; you can
open it in a text editor. If this file doesn't exist or has an old timestamp
then LibShapeDraw is not installed correctly. Try reinstalling.

If you're still stuck with a specific problem, see the contact section at the
bottom. Please have the crash report and `LibShapeDraw.log` handy.

----

# For Developers

If your mod could use any of the following things, LibShapeDraw is the library
for you:

 +  The ability to create arbitrary shapes and **draw in the game world itself**
    *without* having to mess with OpenGL contexts, finding a render hook, or
    other tedious details.

 +  The ability to **smoothly animate** shapes (and anything else, really) using
    the easy-to-use Trident animation library built-in to LibShapeDraw.

 +  Vector3 and Color classes, full of well-documented and throughly-tested
    **convenience methods**. Want to calculate the pitch and yaw angles between
    two 3-dimensional points in a couple lines of code? No problem.

## Design goals

LibShapeDraw is designed to be easy to use, both for devs and for players. These
are the design goals to that end:

 +  **Minimal dependencies.** Either LiteLoader or Forge is required to get up
    and running. That's all.

 +  **Maximal compatibility.** LibShapeDraw does not modify the bytecode of
    *any* vanilla Minecraft class via jar-modding. You are free to modify
    Minecraft classes in your own mod if needed; LibShapeDraw will not
    interfere.

 +  **Unobtrusive.** Pick and choose the components you want to use.
    LibShapeDraw is a toolkit for your mod to use. It is *not* a heavy
    DoEverythingThisWay framework.

 +  **Powerful.** What good is an API that doesn't let you do cool stuff? Check
    the demos for some of the many possibilities.

 +  **Concise and clear.** Convenience methods, fluent interfaces, etc. let you
    write less code to do more. That's what LibShapeDraw is all about.

 +  **Well-documented.** The key to success for any API, really.

 +  **Throughly tested.** A full JUnit test suite to help prevent bugs.

 +  **Open source.** MIT-licensed and open to community feedback and patches.

Enough high-level overview stuff. Let's dive in and actually use the API:

## Basic usage

Add the jar to your project's classpath and instantiate `LibShapeDraw` somewhere
in your code. Create and add some `Shape`s for the API instance to render.

Each Shape type is defined using classes like `Color`, `Vector3`, and
`ShapeTransform`. You can easily animate a shape by calling `animateStart` and
`animateStop` on these classes.

Plenty of sample code is available; see the *Documentation* section below.
Here's a quick example:

        // Create a 1x1x1 wireframe box at x=0, y=63 (sea level), z=0.
        this.libShapeDraw = new LibShapeDraw();
        Vector3 pointA = new Vector3(0, 63, 0);
        Vector3 pointB = new Vector3(1, 64, 1);
        WireframeCuboid shape = new WireframeCuboid(pointA, pointB);
        Color lineColor = Color.CYAN.copy();
        shape.setLineStyle(lineColor, 2.0F, false);
        libShapeDraw.addShape(shape);
        
        // Reposition it up a dozen blocks and make it 2x1x1 instead of 1x1x1.
        pointA.addY(12.0);
        pointB.addY(12.0).addX(1.0);
        
        // Animate it! Smoothly change color from cyan to green and back every 5
        // seconds, and also spin in place, rotating once every 7 seconds.
        lineColor.animateStartLoop(Color.GREEN, true, 5000);
        ShapeRotate rotate = new ShapeRotate(0.0, Axis.Y);
        shape.addTransform(rotate);
        rotate.animateStartLoop(360.0, false, 7000);

## How to add the LibShapeDraw jar to the classpath in MCP

[Minecraft Coder Pack (MCP)](http://mcp.ocean-labs.de/index.php/MCP_Releases)
is an excellent tool for creating mods, letting you work with deobfuscated
Minecraft code. Using external libraries (such as LibShapeDraw) involves a few
extra steps:

1.  **Get a copy of a LibShapeDraw release jar.** You can use either the normal
    release or the special dev release (named `LibShapeDraw-VERSION-dev.jar`).
    Check the [releases list](https://github.com/xaeroverse/LibShapeDraw/releases)
    for the latest.
    
    The dev release is recommended: either release type will work for
    *compiling* your mod, but the dev release also lets you *test/debug* your
    MCP mod without doing a full reobfuscate/deploy.

2.  **Edit `conf/mcp.cfg`.** Scroll down to the `[RECOMPILE]` section. There is
    a property named `ClassPathClient`; this is where we will add a reference to
    the LibShapeDraw jar. Add `,%(DirJars)s/bin/LibShapeDraw-VERSION-dev.jar`
    (changing `VERSION` as appropriate) to the end of the line.

3.  **Copy the LibShapeDraw jar to `jars/mods/MCVERSION`.**  MCVERSION
    cooresponds to the Minecraft version that you are running.

4.  **If you are using Eclipse, add the LibShapeDraw jar to the project's build
    build path.** Go to the Project Explorer pane and expand `jars/mods`.
    Right-click the LibShapeDraw jar, Build Path, Add to Build Path.
    
    A separate sources release also includes the source code for convenience,
    giving you easy access to the Javadocs in your IDE.

## How to add the LibShapeDraw jar to the classpath in ForgeGradle

[ForgeGradle](http://github.com/MinecraftForge/ForgeGradle/) is Forge's
replacement build system for MCP's python scripts, allowing for automatic
dependency resolution/managment and customized output all controlled with a
build.gradle file written in a Groovy DSL.  This project does not yet have a
Maven repository, so you will have to manually copy the library:

1.  **Get a copy of a LibShapeDraw release jar.** See previous discussion.

2.  **Copy the LibShapeDraw jar to `project/libs`.** Create this directory as a
    sibling of your `build.gradle` if it doesn't yet exist. ForgeGradle will
    automatically add this `libs` folder to the build path.

3.  **Re-run any applicable IDE tasks.** Refresh your IDE as well if you
    imported your build.gradle.

## Documentation

 +  [Javadocs](http://xaeroverse.github.io/LibShapeDraw/apidocs/index.html) are
    available.

 +  [Browse the demos](https://github.com/xaeroverse/LibShapeDraw/tree/master/projects/demos/src/main/java/libshapedraw/demos),
    located in `projects/demos`. To see the demos in action, you can
    [download](https://github.com/xaeroverse/LibShapeDraw/releases) the pre-built
    demos jar and install it like any other mod.

 +  See [README-Trident.md](https://github.com/xaeroverse/LibShapeDraw/blob/master/README-Trident.md)
    for information about the built-in Trident animation library.

 +  See [README-contributing.md](https://github.com/xaeroverse/LibShapeDraw/blob/master/README-contributing.md)
    for information about contributing to LibShapeDraw itself, including build
    instructions and a list of features planned for future releases.

 +  If you'd like additional guidance, check the contacts section below.

## Contact

This current project's GitHub by xaeroverse is located at 
[github.com/xaeroverse/LibShapeDraw](https://github.com/xaeroverse/LibShapeDraw).
Anyone is free to open an issue.

You can also try the [current LibShapeDraw thread on minecraftforum.net](http://www.minecraftforum.net/forums/t/2188255).

Finally, you can also ping me (xaero) on the Esper.net IRC network.  I'm usually lurking in various channels related to Minecraft development.

## History

The original project's GitHub repo by bencvt is located at
[github.com/bencvt/LibShapeDraw](https://github.com/bencvt/LibShapeDraw).  And also here is the [original LibShapeDraw thread on minecraftforum.net](http://www.minecraftforum.net/topic/1458931-libshapedraw/).

AesenV ported the mod to 1.7.2 and maintained it briefly; you can check out the repo at [github.com/AesenV/LibShapeDraw-1.7](https://github.com/AesenV/LibShapeDraw-1.7).

The version history is maintained in [CHANGELOG.md](https://github.com/xaeroverse/LibShapeDraw/blob/master/CHANGELOG.md).
