package lando.systems.ld45.utils;

public enum ArtPack {
      a("Pencil sketch")
    , b("Flat colors")
    , c("Simple pixel")
    , d("HD pixel")
    ;
    public final String niceName;
    ArtPack(String niceName) {
        this.niceName = niceName;
    }

    public ArtPack getNext() {
        switch (this) {
            case a: return b;
            case b: return c;
            case c: return d;
            case d:
            default: return a;
        }
    }
}
