package lando.systems.ld45.utils;

public enum AssetType {
      ball("ball")
    , peg("peg")
    , bumper("bumper")
    , spinner("spinner")
    , particle_star("particles/particle-star")
    , part_boundary("partboundary")
    , boundary_line("line")
    ;
    public String fileName;
    AssetType(String fileName) {
        this.fileName = fileName;
    }
}
