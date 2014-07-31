package libshapedraw.demos;

/**
 * Need to define a fake LiteLoader 'BaseMod' so that LiteLoader does not discover it.
 */
public abstract class BaseMod {
    public abstract String getVersion();
    public abstract void init();
}
