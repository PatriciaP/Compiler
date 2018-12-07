package compiler.virtual_machine;

public class Variables {

    public String varId;

    public int levelInt;

    public float levelFloat;

    public int type;

    public Variables(String varId, int levelInt, int type) {
        this.varId = varId;
        this.levelInt = levelInt;
        this.type = type;
    }

    public Variables(String varId, float levelFloat, int type) {
        this.varId = varId;
        this.levelFloat = levelFloat;
        this.type = type;
    }
}
