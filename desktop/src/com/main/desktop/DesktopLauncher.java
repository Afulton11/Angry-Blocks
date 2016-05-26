package com.main.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.main.Application;
import com.main.Constants;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 1280;
		config.height = 720;
		config.resizable = false;
		config.title = Constants.TITLE;
		config.addIcon("imgs/icons/icon_16.png", Files.FileType.Internal);
		config.addIcon("imgs/icons/icon_32.png", Files.FileType.Internal);
		config.addIcon("imgs/icons/icon_48.png", Files.FileType.Internal);
		config.addIcon("imgs/icons/icon_64.png", Files.FileType.Internal);
		config.addIcon("imgs/icons/icon_96.png", Files.FileType.Internal);
		config.addIcon("imgs/icons/icon_128.png", Files.FileType.Internal);
		config.addIcon("imgs/player.png", Files.FileType.Internal);
		new LwjglApplication(new Application(), config);
	}
}
