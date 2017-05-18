package controller;

import java.awt.event.MouseEvent;
import java.io.File;



import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.FrameGrabber.Exception;

import video.VideoGrabber;
import video.paint.Painter;

public class EventController {
	
	private VideoGrabber video;
	
	public EventController(){}
	
	public void addVideo(File file, CanvasFrame main){
			video  = new VideoGrabber(file, main);
			try {
				video.oneFrame();
			} catch (Exception e) {
				e.printStackTrace();
			}
	}
	public void startPlay(){
		video.setDaemon(true);
		video.start();		
	}
	
	public void addMouseAction(MouseEvent click){
		if (video != null){
			try {
				video.getFirstFrameFromPainter(click);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}		
	}
	
	public void changeSelector(String selector){
		Painter.getPainter().changeSelector(selector);
	}
	
	/*public void first(){
		System.out.println("first");
	}
	
	public void second(){
		System.out.println("second");
	}
*/

}
