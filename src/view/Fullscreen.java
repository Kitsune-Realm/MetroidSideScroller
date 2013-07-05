package view;

import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Window;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Fullscreen
{
	GraphicsDevice vc;
	
	public Fullscreen()
	{
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		vc = ge.getDefaultScreenDevice();
	}
	
	public void setFullScreen(DisplayMode dm, JFrame win)
	{
		// Removes the titlebar, maximization and minimization button
		win.removeNotify();
		win.setUndecorated(true);
		// Cannot be resized
		win.setResizable(false);
		win.addNotify();
		// Make the win(JFrame) fullscreen now
		vc.setFullScreenWindow(win);
		
		// Check Low-Level display changes are supported for this graphics device
		if (dm != null && vc.isDisplayChangeSupported()) {
			try {
				vc.setDisplayMode(dm);
			}
			catch (Exception ex) {
				JOptionPane.showMessageDialog(null, ex.getMessage());
			}
		}
	}
	
	public void CloseFullScreen()
	{
		Window w = vc.getFullScreenWindow();
		if (w != null) {
			w.dispose();
		}
		vc.setFullScreenWindow(null);
	}
}
