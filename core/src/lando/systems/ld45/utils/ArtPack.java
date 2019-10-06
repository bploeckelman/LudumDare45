package lando.systems.ld45.utils;

public enum ArtPack {
      a("black-and-white")
    , b("pencil-sketch")
    , c("flat-color")
    , d("high-quality")
    ;
    private final String niceName;
    ArtPack(String niceName) {
        this.niceName = niceName;
    }
}
