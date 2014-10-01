package org.sikuli.bot;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;

import org.sikuli.api.Screen;

public class AndroidScreen implements Screen {
	
		private BufferedImage black;
		private BufferedImage image;

		static String screenshotFileName = "screen.png";

		public AndroidScreen(){
			try {
				black = ImageIO.read(getClass().getResource("/black.png"));
			} catch (IOException e) {
			}
		}	

		@Override
		public BufferedImage getScreenshot(int arg0, int arg1, int arg2, int arg3) {
			executeAdbCommandScreenGrab();			
			try {
				image = ImageIO.read(new File(screenshotFileName));
			} catch (IOException e) {
				image = null;
			}
			if (image == null){
				image = black;
			}
			return image;
		}

		@Override
		public Dimension getSize() {
			return new Dimension(1280,800);
		}

		private void executeAdbCommandScreenGrab(){
			System.out.println("capturing a screenshot");		
			String adb = "/adt/sdk/platform-tools/adb";
			String s = null;
			try {
				// run the Unix "ps -ef" command
				// using the Runtime exec method:

				String cmd = adb + " shell screencap -p | perl -pe 's/\\x0D\\x0A/\\x0A/g' > " + screenshotFileName;

				String[] cmds = {
						"/bin/sh",
						"-c",
						cmd
				};

				Process p = Runtime.getRuntime().exec(cmds);

				BufferedReader stdInput = new BufferedReader(new
						InputStreamReader(p.getInputStream()));

				BufferedReader stdError = new BufferedReader(new
						InputStreamReader(p.getErrorStream()));
				while ((s = stdInput.readLine()) != null) {
					System.out.println(s);
				}
				// read any errors from the attempted command
				while ((s = stdError.readLine()) != null) {
					System.out.println("capturing failed");
					System.out.println(s);
				}
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}

	}