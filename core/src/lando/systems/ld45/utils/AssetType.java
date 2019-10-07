package lando.systems.ld45.utils;

public enum AssetType {
      ball("ball")
    , peg("peg")
    , bumper("bumper")
    , spinner("spinner")
    , particle_star("particles/particle-star")
    , part_boundary("partboundary")
    , boundary_line("line")
    , boundary_line_long_top("line-long-top")
    , boundary_line_long_left("line-long-left")
    , boundary_line_long_right("line-long-right")
    , boundary_line_long_bottom("line-long-bottom")
    , boundary_line_short_top("line-short-top")
    , boundary_line_short_left("line-short-left")
    , boundary_line_short_right("line-short-right")
    , boundary_line_short_bottom("line-short-bottom")
    , boundary_line_corner("line-corner")
    ;
    public String fileName;
    AssetType(String fileName) {
        this.fileName = fileName;
    }
}
