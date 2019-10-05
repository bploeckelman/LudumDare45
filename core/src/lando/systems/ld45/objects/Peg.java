package lando.systems.ld45.objects;

import lando.systems.ld45.screens.GameScreen;

public class Peg extends GameObject {

    public static Peg getPeg(GameScreen screen, int level, int gfxPack) {
        return new Peg(screen, level, gfxPack, 16);
    }

    public Peg(GameScreen screen, int level, int gfxPack, float size) {
        super(screen, screen.assets.pegs[gfxPack][level], size, size);
    }
}
