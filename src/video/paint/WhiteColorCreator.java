package video.paint;

import static org.bytedeco.javacpp.opencv_core.IPL_DEPTH_8U;
import static org.bytedeco.javacpp.opencv_core.cvInRangeS;
import static org.bytedeco.javacpp.opencv_core.cvScalar;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

import org.bytedeco.javacpp.opencv_core.CvPoint;
import org.bytedeco.javacpp.opencv_core.CvRect;
import org.bytedeco.javacpp.opencv_core.CvScalar;
import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.OpenCVFrameConverter;

class WhiteColorCreator {
	
	private static WhiteColorCreator creator;
	private CvScalar rgba_min = cvScalar(60, 0, 0, 0);
	private CvScalar rgba_max = cvScalar(255, 150, 70, 0);
	private OpenCVFrameConverter.ToIplImage converter = new OpenCVFrameConverter.ToIplImage();
	
	private WhiteColorCreator(){	
	}
	
	public static WhiteColorCreator getCreator(){
		if(creator == null){
			creator = new WhiteColorCreator();
		}
		return creator;
	}
	
	

	private Frame create(Frame frame){	
		IplImage tmp = converter.convert(frame);
		IplImage image = IplImage.create(tmp.width(), tmp.height(),
				IPL_DEPTH_8U, 1);
		cvInRangeS(tmp, rgba_min, rgba_max, image);
		frame = converter.convert(image);
		return frame;
	}
	
	CvPoint whiteColor(CvRect rect, Frame frame){
		
		Java2DFrameConverter con = new Java2DFrameConverter();
		BufferedImage image = con.convert(create(frame));
		
		int pointX = 0;
		int pointY = 0;
		int pointCount = 0;

		for (int x = rect.x(); x < rect.x() + rect.width(); x++) {
			for (int y = rect.y(); y < rect.y() + rect.height(); y++) {
				WritableRaster raster = image.getRaster();
				ColorModel model = image.getColorModel();
				Object data = raster.getDataElements(x, y, null);
				int argb = model.getRGB(data);
				Color color = new Color(argb, true);
				if (color.getRGB() == Color.WHITE.getRGB()) {
					pointX += x;
					pointY += y;
					pointCount++;
				}
			}
		}
		if (pointCount != 0) {
			return new CvPoint(pointX / pointCount, pointY / pointCount);
		} else {
			return null;
		}
		
	}
	
}
