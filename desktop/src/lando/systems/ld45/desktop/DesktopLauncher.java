package lando.systems.ld45.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import lando.systems.ld45.Config;
import lando.systems.ld45.Game;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = Config.gameWidth;
		config.height = Config.gameHeight;
		new LwjglApplication(new Game(), config);
	}
}
