package lando.systems.ld45.utils;

public enum ArtPack {
      a("pencil-sketch")
    , b("flat-color")
    , c("simple-pixel")
    , d("high-quality")
    ;
    private final String niceName;
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
