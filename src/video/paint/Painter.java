package video.paint;

import static org.bytedeco.javacpp.opencv_core.cvScalar;
import static org.bytedeco.javacpp.opencv_imgproc.cvCircle;
import static org.bytedeco.javacpp.opencv_imgproc.cvLine;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.bytedeco.javacpp.opencv_core.CvPoint;
import org.bytedeco.javacpp.opencv_core.CvRect;
import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.FrameGrabber.Exception;
import org.bytedeco.javacv.OpenCVFrameConverter;

public class Painter {

	private static Painter painter;
	private String selector;
	private Map<String, CvPoint> points = new HashMap<>();
	private Map<String, CvRect> rect = new HashMap<>();

	private Painter() {
	}

	public static synchronized Painter getPainter() {
		if (painter == null) {
			painter = new Painter();
		}
		return painter;
	}

	public void changeSelector(String value) {
		selector = value;
		System.out.println("selector is " + value);
	}
	
	/*public Frame drawPoints(MouseEvent e, Frame frame) throws Exception {
		
		Java2DFrameConverter con = new Java2DFrameConverter();
		BufferedImage image = con.convert(frame);
		
		
				WritableRaster raster = image.getRaster();
				ColorModel model = image.getColorModel();
				Object data = raster.getDataElements(e.getX(), e.getY(), null);
				int argb = model.getRGB(data);
				Color color = new Color(argb, true);
				System.out.println(color.getBlue()+" "+color.getGreen()+" "+color.getBlue());
				
				return frame;
	}*/

	public Frame drawPoints(MouseEvent e, Frame frame) throws Exception {
		OpenCVFrameConverter.ToIplImage converter = new OpenCVFrameConverter.ToIplImage();
		IplImage image = converter.convert(frame);
		rect.put(selector, new CvRect(e.getX() - 25, e.getY() - 13, 50, 26));
		points.put(selector, new CvPoint(e.getX(), e.getY()));
		for (CvPoint point : points.values()) {
			cvCircle(image, point, 4, cvScalar(0, 0, 255, 0), -1, 8, 0);
		}
		return converter.convert(image);
	}

	public Frame drawPointsInMove(Frame frame) {
		int width = 50;
		int height = 26;
		OpenCVFrameConverter.ToIplImage converter = new OpenCVFrameConverter.ToIplImage();
		IplImage image = converter.convert(frame);		
		for(Iterator<Map.Entry<String,CvRect>>it=rect.entrySet().iterator(); it.hasNext();){
		     Entry<String, CvRect> entry = it.next();	     
		     CvPoint point = WhiteColorCreator.getCreator().whiteColor(
						entry.getValue(), frame);
				if (point != null) {
					cvCircle(image, point, 4, cvScalar(0, 0, 255, 0), -1, 8, 0);
					points.put(entry.getKey(), point);
					rect.put(entry.getKey(), new CvRect(point.x() - width / 2, point.y()
							- height / 2, width, height));
				}else{
					points.remove(entry.getKey());
					it.remove();
				}		     
		 }
		if (points.containsKey("hip") && points.containsKey("knee")) {
			cvLine(image, points.get("hip"), points.get("knee"), cvScalar(255, 0, 0, 0), 2, 8, 0);
		}
		if (points.containsKey("ankle") && points.containsKey("knee")) {
			cvLine(image, points.get("ankle"), points.get("knee"), cvScalar(0, 255, 0, 0), 2, 8, 0);
		}
		return converter.convert(image);
	}
	
	
	public void clearMaps(){
		rect.clear();
		points.clear();
	}

}
