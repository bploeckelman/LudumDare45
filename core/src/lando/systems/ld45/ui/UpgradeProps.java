package lando.systems.ld45.ui;

public enum UpgradeProps {
      special_effects(3, "Buy Special Effects", new Integer[]{0, 100, 500, 1000}
           , "{SPEED=7}{GRADIENT=black;gray}Purchase special effects:\n\nParticles, Ball trails, Screenshake{ENDGRADIENT}"
           , "{SPEED=7}{GRADIENT=black;gray}Purchase special effects: {ENDGRADIENT} \n\n{GRADIENT=forest;olive}Particles, {ENDGRADIENT}{GRADIENT=black;gray}Ball trails, Screenshake{ENDGRADIENT}"
           , "{SPEED=7}{GRADIENT=black;gray}Purchase special effects: {ENDGRADIENT} \n\n{GRADIENT=forest;olive}Particles, Ball trails, {ENDGRADIENT}{GRADIENT=black;gray}Screenshake{ENDGRADIENT}"
           , "{SPEED=7}{GRADIENT=black;gray}Purchase special effects: {ENDGRADIENT} {RAINBOW} COMPLETE {ENDRAINBOW} \n\n{GRADIENT=forest;olive}Particles, Ball trails, Screenshake {ENDGRADIENT} "
      )
    , pegs(3, "Buy Pegs", new Integer[]{0, 100, 500, 1000}
           , "{SPEED=7}{GRADIENT=black;gray}Purchase pegs:\n\nPegs are simple obstacles to bounce off of.\nBuy more of them to place!\n         1x, 4x, 8x, 10x{ENDGRADIENT}"
           , "{SPEED=7}{GRADIENT=black;gray}Purchase pegs:\n\nPegs are simple obstacles to bounce off of.\nBuy more of them to place! {ENDGRADIENT} \n {GRADIENT=forest;olive}        1x, {ENDGRADIENT} {GRADIENT=black;gray} 4x, 8x, 10x{ENDGRADIENT}"
           , "{SPEED=7}{GRADIENT=black;gray}Purchase pegs:\n\nPegs are simple obstacles to bounce off of.\nBuy more of them to place! {ENDGRADIENT} \n {GRADIENT=forest;olive}        1x, 4x, {ENDGRADIENT} {GRADIENT=black;gray} 8x, 10x{ENDGRADIENT}"
           , "{SPEED=7}{GRADIENT=black;gray}Purchase pegs:\n\nPegs are simple obstacles to bounce off of.\nBuy more of them to place! {ENDGRADIENT} \n {GRADIENT=forest;olive}        1x, 4x, 8x, {ENDGRADIENT} {GRADIENT=black;gray} 10x{ENDGRADIENT}"
           , "{SPEED=7}{GRADIENT=black;gray}Purchase pegs: {ENDGRADIENT} {RAINBOW} COMPLETE {ENDRAINBOW} \n\n{GRADIENT=black;gray}Pegs are simple obstacles to bounce off of.\nBuy more of them to place! {ENDGRADIENT} \n    {GRADIENT=forest;olive}     1x, 4x, 8x, 10x{ENDGRADIENT}"
    )
    , bumpers(3, "Buy Bumpers", new Integer[]{0, 100, 500, 1000}
            , "{SPEED=7}{GRADIENT=black;gray}Purchase bumpers:\n\nBumpers are obstacles that balls bounce off of.\nBuy more of them to place!\n         1x, 4x, 8x, 10x{ENDGRADIENT}"
            , "{SPEED=7}{GRADIENT=black;gray}Purchase bumpers:\n\nBumpers are obstacles that balls bounce off of.\nBuy more of them to place! {ENDGRADIENT} \n {GRADIENT=forest;olive}        1x, {ENDGRADIENT} {GRADIENT=black;gray} 4x, 8x, 10x{ENDGRADIENT}"
            , "{SPEED=7}{GRADIENT=black;gray}Purchase bumpers:\n\nBumpers are obstacles that balls bounce off of.\nBuy more of them to place! {ENDGRADIENT} \n {GRADIENT=forest;olive}        1x, 4x, {ENDGRADIENT} {GRADIENT=black;gray} 8x, 10x{ENDGRADIENT}"
            , "{SPEED=7}{GRADIENT=black;gray}Purchase bumpers:\n\nBumpers are obstacles that balls bounce off of.\nBuy more of them to place! {ENDGRADIENT} \n {GRADIENT=forest;olive}        1x, 4x, 8x, {ENDGRADIENT} {GRADIENT=black;gray} 10x{ENDGRADIENT}"
            , "{SPEED=7}{GRADIENT=black;gray}Purchase bumpers: {ENDGRADIENT} {RAINBOW} COMPLETE {ENDRAINBOW} \n\n{GRADIENT=black;gray}Bumpers are obstacles that balls bounce off of.\nBuy more of them to place! {ENDGRADIENT} \n    {GRADIENT=forest;olive}     1x, 4x, 8x, 10x{ENDGRADIENT}"
    )
    , spinners_left(3, "Buy Left Spinner", new Integer[]{0, 100, 500, 1000}
            , "{SPEED=7}{GRADIENT=black;gray}Purchase spinners (rotating left):\n\nSpinners send balls flying in their rotation direction.\nBuy more of them to place!\n         1x, 4x, 8x, 10x{ENDGRADIENT}"
            , "{SPEED=7}{GRADIENT=black;gray}Purchase spinners (rotating left):\n\nSpinners send balls flying in their rotation direction.\nBuy more of them to place! {ENDGRADIENT} \n {GRADIENT=forest;olive}        1x, {ENDGRADIENT} {GRADIENT=black;gray} 4x, 8x, 10x{ENDGRADIENT}"
            , "{SPEED=7}{GRADIENT=black;gray}Purchase spinners (rotating left):\n\nSpinners send balls flying in their rotation direction.\nBuy more of them to place! {ENDGRADIENT} \n {GRADIENT=forest;olive}        1x, 4x, {ENDGRADIENT} {GRADIENT=black;gray} 8x, 10x{ENDGRADIENT}"
            , "{SPEED=7}{GRADIENT=black;gray}Purchase spinners (rotating left):\n\nSpinners send balls flying in their rotation direction.\nBuy more of them to place! {ENDGRADIENT} \n {GRADIENT=forest;olive}        1x, 4x, 8x, {ENDGRADIENT} {GRADIENT=black;gray} 10x{ENDGRADIENT}"
            , "{SPEED=7}{GRADIENT=black;gray}Purchase spinners (rotating left): {ENDGRADIENT} {RAINBOW} COMPLETE {ENDRAINBOW} \n\n{GRADIENT=black;gray}Spinners send balls flying in their rotation direction.\nBuy more of them to place! {ENDGRADIENT} \n    {GRADIENT=forest;olive}     1x, 4x, 8x, 10x{ENDGRADIENT}"
    )
    , spinners_right(3, "Buy Right Spinner", new Integer[]{0, 100, 500, 1000}
            , "{SPEED=7}{GRADIENT=black;gray}Purchase spinners (rotating right):\n\nSpinners send balls flying in their rotation direction.\nBuy more of them to place!\n         1x, 4x, 8x, 10x{ENDGRADIENT}"
            , "{SPEED=7}{GRADIENT=black;gray}Purchase spinners (rotating right):\n\nSpinners send balls flying in their rotation direction.\nBuy more of them to place! {ENDGRADIENT} \n {GRADIENT=forest;olive}        1x, {ENDGRADIENT} {GRADIENT=black;gray} 4x, 8x, 10x{ENDGRADIENT}"
            , "{SPEED=7}{GRADIENT=black;gray}Purchase spinners (rotating right):\n\nSpinners send balls flying in their rotation direction.\nBuy more of them to place! {ENDGRADIENT} \n {GRADIENT=forest;olive}        1x, 4x, {ENDGRADIENT} {GRADIENT=black;gray} 8x, 10x{ENDGRADIENT}"
            , "{SPEED=7}{GRADIENT=black;gray}Purchase spinners (rotating right):\n\nSpinners send balls flying in their rotation direction.\nBuy more of them to place! {ENDGRADIENT} \n {GRADIENT=forest;olive}        1x, 4x, 8x, {ENDGRADIENT} {GRADIENT=black;gray} 10x{ENDGRADIENT}"
            , "{SPEED=7}{GRADIENT=black;gray}Purchase spinners (rotating right): {ENDGRADIENT} {RAINBOW} COMPLETE {ENDRAINBOW} \n\n{GRADIENT=black;gray}Spinners send balls flying in their rotation direction.\nBuy more of them to place! {ENDGRADIENT} \n    {GRADIENT=forest;olive}     1x, 4x, 8x, 10x{ENDGRADIENT}"
    )
    , cash_multipliers(4,"Buy Cash Multiplier", new Integer[]{0, 100, 500, 1000}
            ,"{SPEED=7}{GRADIENT=black;gray}Purchase cash multiplier:\n\nIncrease the amount of money you earn for things; {ENDGRADIENT} \n {GRADIENT=forest;olive}         1x, {ENDGRADIENT} {GRADIENT=black;gray} 2x, 4x, 8x, 16x{ENDGRADIENT}"
            , "{SPEED=7}{GRADIENT=black;gray}Purchase cash multiplier:\n\nIncrease the amount of money you earn for things; {ENDGRADIENT} \n {GRADIENT=forest;olive}         1x, 2x, {ENDGRADIENT} {GRADIENT=black;gray} 4x, 8x, 16x{ENDGRADIENT}"
            , "{SPEED=7}{GRADIENT=black;gray}Purchase cash multiplier:\n\nIncrease the amount of money you earn for things; {ENDGRADIENT} \n {GRADIENT=forest;olive}         1x, 2x, 4x, {ENDGRADIENT} {GRADIENT=black;gray} 8x, 16x{ENDGRADIENT}"
            , "{SPEED=7}{GRADIENT=black;gray}Purchase cash multiplier:\n\nIncrease the amount of money you earn for things; {ENDGRADIENT} \n {GRADIENT=forest;olive}         1x, 2x, 4x, 8x, {ENDGRADIENT} {GRADIENT=black;gray} 16x{ENDGRADIENT}"
            , "{SPEED=7}{GRADIENT=black;gray}Purchase cash multiplier: {ENDGRADIENT} {RAINBOW} COMPLETE {ENDRAINBOW} \n\n{GRADIENT=black;gray}Increase the amount of money you earn for things; {ENDGRADIENT} \n {GRADIENT=forest;olive}         1x, 2x, 4x, 8x, 16x{ENDGRADIENT}"
    )
    , ball_multipliers(5, "Buy More Balls", new Integer[]{0, 100, 500, 1000}
           , "{SPEED=7}{GRADIENT=black;gray}Purchase more balls:\n\nDrop more balls each playthrough...{ENDGRADIENT} \n    {GRADIENT=forest;olive}+5, {ENDGRADIENT} {GRADIENT=black;gray}+10, +25, +50, +100, +200{ENDGRADIENT}"
           , "{SPEED=7}{GRADIENT=black;gray}Purchase more balls:\n\nDrop more balls each playthrough...{ENDGRADIENT} \n    {GRADIENT=forest;olive}+5, +10,{ENDGRADIENT} {GRADIENT=black;gray} +25, +50, +100, +200{ENDGRADIENT}"
           , "{SPEED=7}{GRADIENT=black;gray}Purchase more balls:\n\nDrop more balls each playthrough...{ENDGRADIENT} \n    {GRADIENT=forest;olive}+5, +10, +25,{ENDGRADIENT} {GRADIENT=black;gray} +50, +100, +200{ENDGRADIENT}"
           , "{SPEED=7}{GRADIENT=black;gray}Purchase more balls:\n\nDrop more balls each playthrough...{ENDGRADIENT} \n    {GRADIENT=forest;olive}+5, +10, +25, +50,{ENDGRADIENT} {GRADIENT=black;gray} +100, +200{ENDGRADIENT}"
           , "{SPEED=7}{GRADIENT=black;gray}Purchase more balls:\n\nDrop more balls each playthrough...{ENDGRADIENT} \n    {GRADIENT=forest;olive}+5, +10, +25, +50, +100,{ENDGRADIENT} {GRADIENT=black;gray} +200{ENDGRADIENT}"
           , "{SPEED=7}{GRADIENT=black;gray}Purchase more balls: {ENDGRADIENT} {RAINBOW} COMPLETE {ENDRAINBOW} \n\n{GRADIENT=black;gray}Drop more balls each playthrough...{ENDGRADIENT} \n    {GRADIENT=forest;olive}+5, +10, +25, +50, +100, +200 {ENDGRADIENT} "
    )
    , audio_packs(2, "Buy Audio", new Integer[]{0, 100, 1000}
           ,"{SPEED=7}{GRADIENT=black;gray}Purchase audio:\n\nIt's like graphics, but for your ears!{ENDGRADIENT} \n\n {GRADIENT=forest;olive}    No sounds, {ENDGRADIENT} {GRADIENT=black;gray} Placeholder sounds, Final sounds{ENDGRADIENT}"
           , "{SPEED=7}{GRADIENT=black;gray}Purchase audio:\n\nIt's like graphics, but for your ears!{ENDGRADIENT} \n\n {GRADIENT=forest;olive}    No sounds, Placeholder sounds, {ENDGRADIENT} {GRADIENT=black;gray} Final sounds{ENDGRADIENT}"
           , "{SPEED=7}{GRADIENT=black;gray}Purchase audio: {ENDGRADIENT} {RAINBOW} COMPLETE {ENDRAINBOW} \n\n{GRADIENT=black;gray}It's like graphics, but for your ears!{ENDGRADIENT} \n\n {GRADIENT=forest;olive}    No sounds, Placeholder sounds, Final sounds{ENDGRADIENT}"
    )
    // NOTE: can't use ArtPack.foo.niceName here because enum initializer order I guess?
    , art_packs(4, "Buy Art Packs", new Integer[]{0, 100, 500, 1000, 10000}
           , "{SPEED=7}{GRADIENT=black;gray}Purchase art:\n\nIt's like audio, but for your eyes... Hire an artist!\nUpgrade through four distinct art packs!{ENDGRADIENT}\n {GRADIENT=forest;olive}   Pencil sketch, {ENDGRADIENT}{GRADIENT=black;gray} Flat colors, Simple pixels, HD pixels {ENDGRADIENT} "
           , "{SPEED=7}{GRADIENT=black;gray}Purchase art:\n\nIt's like audio, but for your eyes... Hire an artist\nUpgrade through four distinct art packs!{ENDGRADIENT}\n {GRADIENT=forest;olive}   Pencil sketch, Flat colors, {ENDGRADIENT}{GRADIENT=black;gray} Simple pixels, HD pixels {ENDGRADIENT} "
           , "{SPEED=7}{GRADIENT=black;gray}Purchase art:\n\nIt's like audio, but for your eyes... Hire an artist\nUpgrade through four distinct art packs!{ENDGRADIENT}\n {GRADIENT=forest;olive}   Pencil sketch, Flat colors, Simple pixels, {ENDGRADIENT}{GRADIENT=black;gray} HD pixels {ENDGRADIENT} "
           , "{SPEED=7}{GRADIENT=black;gray}Purchase art: {ENDGRADIENT} {RAINBOW} COMPLETE {ENDRAINBOW} \n\n{GRADIENT=black;gray}It's like audio, but for your eyes... Hire an artist!\nUpgrade through four distinct art packs!{ENDGRADIENT}\n {GRADIENT=forest;olive}   Pencil sketch, Flat colors, Simple pixels, HD pixels {ENDGRADIENT} "
    )
    , win_game(1, "Win the game", new Integer[]{0, 50000}
            , "{SPEED=7}{GRADIENT=gold;tan}Pay to win the game!{ENDGRADIENT} \n\n{GRADIENT=black;gray}Some people say paying to win is bad...\n\nbut this is the ultimate {ENDGRADIENT}{WAVE}{RAINBOW}BALL OF DUTY{ENDRAINBOW}{ENDWAVE} "
            , "{SPEED=10}{RAINBOW}YOU DID IT!{ENDRAINBOW} \n\n{GRADIENT=black;gray}Some people say paying to win is bad...\n\nbut this is the ultimate {ENDGRADIENT}{WAVE}{RAINBOW}BALL OF DUTY{ENDRAINBOW}{ENDWAVE} "
    )
    ;

    final int numAvailableOptions;
    final String text;
    final String[] descriptions;
    final Integer[] costs;
    int currentDescription;

    UpgradeProps(int numAvailableOptions, String text, Integer[] costs, String... descriptions) {
        this.numAvailableOptions = numAvailableOptions;
        this.text = text;
        this.costs = costs;
        this.descriptions = descriptions;
        this.currentDescription = 0;
    }

    public String nextDescription() {
        if (currentDescription <= descriptions.length - 1) {
            currentDescription += 1;
        }
        return descriptions[currentDescription];
    }

    public int getCurrentCost() {
        return costs[currentDescription];
    }

    public String getCurrentDescription() {
        return descriptions[currentDescription];
    }

}
