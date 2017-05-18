package video;

import java.awt.event.MouseEvent;
import java.io.File;

import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber.Exception;
import org.bytedeco.javacv.OpenCVFrameGrabber;

import video.paint.Painter;


public class VideoGrabber extends Thread {

	private OpenCVFrameGrabber grabber;
	private CanvasFrame canvasFrame;
	private Frame frame;

	public VideoGrabber(File videoFile, CanvasFrame canvasFrame) {
		grabber = new OpenCVFrameGrabber(videoFile);
		this.canvasFrame = canvasFrame;
	}

	public void run() {
		startPlay();
	}

	private void startPlay() {
		try {
			grabber.start();
			int length = grabber.getLengthInFrames();
			while (grabber.getFrameNumber() != length
					&& canvasFrame.isVisible()) {	
				frame = grabber.grab();
				canvasFrame.showImage(Painter.getPainter().drawPointsInMove(frame));
			}
			grabber.stop();
			System.out.println("grabber has stoped");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("end of the file");
		}
	}

	public void oneFrame() throws Exception {
		grabber.start();
		frame = grabber.grab();
		canvasFrame.showImage(frame);
		grabber.stop();		
	}
	
	public void getFirstFrameFromPainter(MouseEvent e) throws Exception{	
		grabber.start();
		canvasFrame.showImage(Painter.getPainter().drawPoints(e, grabber.grab()));
		grabber.stop();	
	}

}
