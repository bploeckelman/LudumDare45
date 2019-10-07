package lando.systems.ld45.ui;

import lando.systems.ld45.utils.ArtPack;

public enum UpgradeProps {
      special_effects(3, "Buy Special Effects",
                      "{SPEED=5}{GRADIENT=black;gray}Purchase special effects:\n\nParticles, Ball trails, Screenshake{ENDGRADIENT}"
                      , "{SPEED=5}{GRADIENT=black;gray}Purchase special effects: {ENDGRADIENT} \n\n{GRADIENT=forest;olive}Particles, {ENDGRADIENT}{GRADIENT=black;gray}Ball trails, Screenshake{ENDGRADIENT}"
                      , "{SPEED=5}{GRADIENT=black;gray}Purchase special effects: {ENDGRADIENT} \n\n{GRADIENT=forest;olive}Particles, Ball trails, {ENDGRADIENT}{GRADIENT=black;gray}Screenshake{ENDGRADIENT}"
                      , "{SPEED=5}{GRADIENT=black;gray}Purchase special effects: {ENDGRADIENT} {RAINBOW} COMPLETE {ENDRAINBOW} \n\n{GRADIENT=forest;olive}Particles, Ball trails, Screenshake {ENDGRADIENT} "
      )
    , pegs(3, "Buy Pegs",
           "{SPEED=5}{GRADIENT=black;gray}Purchase pegs:\n\nUnlock first, then purchase additional pegs up to X{ENDGRADIENT}"
    )
    , bumpers(3, "Buy Bumpers",
              "{SPEED=5}{GRADIENT=black;gray}Purchase bumpers:\n\nUnlock first, then purchase additional bumpers up to X{ENDGRADIENT}"
    )
    , spinners_left(4, "Buy Left Spinner",
                    "{SPEED=5}{GRADIENT=black;gray}Purchase spinners (rotating left):\n\nUnlock first, then purchase additional spinners up to X{ENDGRADIENT}"
    )
    , spinners_right(4, "Buy Right Spinner",
                     "{SPEED=5}{GRADIENT=black;gray}Purchase spinners (rotating right):\n\nUnlock first, then purchase additional spinners up to X{ENDGRADIENT}"
    )
    , cash_multipliers(4,"Buy Cash Multiplier",
                       "{SPEED=5}{GRADIENT=black;gray}Purchase cash multiplier:\n\nIncrease the amount of money you get for everything;\n2x, 4x, 8x, and 16x{ENDGRADIENT}"
    )
    , ball_multipliers(5, "Buy Ball Multiplier",
                       "{SPEED=5}{GRADIENT=black;gray}Purchase ball multiplier:\n\nDrop more balls each playthrough;\n3x, 10x, 50x, 100x{ENDGRADIENT}"
    )
    , audio_packs(2, "Buy Audio",
                  "{SPEED=5}{GRADIENT=black;gray}Purchase audio:\n\nIt's like graphics, but for your ears!\nUnlock placeholder pack, then final version{ENDGRADIENT}"
    )
    // NOTE: can't use ArtPack.foo.niceName here because enum initializer order I guess?
    , art_packs(4, "Buy Art Packs",
//                "{SPEED=5}{GRADIENT=black;gray}Purchase art:\n\nIt's like audio, but for your eyes!\nHire an artist & upgrade through four distinct art packs!\n    Pencil sketch, Flat colors, Simple pixels, HD pixels {ENDGRADIENT}"
                  "{SPEED=5}{GRADIENT=black;gray}Purchase art:\n\nIt's like audio, but for your eyes!\nHire an artist & upgrade through four distinct art packs!{ENDGRADIENT}\n {GRADIENT=forest;olive}   Pencil sketch, {ENDGRADIENT}{GRADIENT=black;gray} Flat colors, Simple pixels, HD pixels {ENDGRADIENT} "
                , "{SPEED=5}{GRADIENT=black;gray}Purchase art:\n\nIt's like audio, but for your eyes!\nHire an artist & upgrade through four distinct art packs!{ENDGRADIENT}\n {GRADIENT=forest;olive}   Pencil sketch, Flat colors, {ENDGRADIENT}{GRADIENT=black;gray} Simple pixels, HD pixels {ENDGRADIENT} "
                , "{SPEED=5}{GRADIENT=black;gray}Purchase art:\n\nIt's like audio, but for your eyes!\nHire an artist & upgrade through four distinct art packs!{ENDGRADIENT}\n {GRADIENT=forest;olive}   Pencil sketch, Flat colors, Simple pixels, {ENDGRADIENT}{GRADIENT=black;gray} HD pixels {ENDGRADIENT} "
                , "{SPEED=5}{GRADIENT=black;gray}Purchase art: {ENDGRADIENT} {RAINBOW} COMPLETE {ENDRAINBOW} \n\n{GRADIENT=black;gray}It's like audio, but for your eyes!\nHire an artist & upgrade through four distinct art packs!{ENDGRADIENT}\n {GRADIENT=forest;olive}   Pencil sketch, Flat colors, Simple pixels, HD pixels {ENDGRADIENT} "
    )
    ;

    final int numAvailableOptions;
    final String text;
    final String[] descriptions;
    int currentDescription;

    UpgradeProps(int numAvailableOptions, String text, String... descriptions) {
        this.numAvailableOptions = numAvailableOptions;
        this.text = text;
        this.descriptions = descriptions;
        this.currentDescription = 0;
    }

    public String nextDescription() {
        if (currentDescription <= descriptions.length - 1) {
            currentDescription += 1;
        }
        return descriptions[currentDescription];
    }

    public String setToLastDescription() {
        currentDescription = descriptions.length - 1;
        return descriptions[currentDescription];
    }

    public String getCurrentDescription() {
        return descriptions[currentDescription];
    }

}
