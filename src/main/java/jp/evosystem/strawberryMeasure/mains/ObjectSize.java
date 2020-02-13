package jp.evosystem.strawberryMeasure.mains;

import java.io.File;
import java.util.Arrays;
import java.util.stream.Collectors;

import javax.swing.WindowConstants;

import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.opencv.global.opencv_imgcodecs;
import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.MatVector;
import org.bytedeco.opencv.opencv_core.Point;
import org.bytedeco.opencv.opencv_core.RotatedRect;
import org.bytedeco.opencv.opencv_core.Scalar;
import org.bytedeco.opencv.opencv_core.Size;

/**
 * 画像の大きさを測定.
 *
 * @author cyrus
 */
public class ObjectSize {

	/**
	 * main.
	 *
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		// 対象の画像ファイル
		File targetImagefile = new File("src/main/resources/example_01.jpg");

		// 対象の画像ファイルを読み込み
		Mat targetImageMat = opencv_imgcodecs.imread(targetImagefile.getAbsolutePath());

		// グレースケール画像を作成
		Mat targetImageMatGray = new Mat();
		opencv_imgproc.cvtColor(targetImageMat, targetImageMatGray, opencv_imgproc.COLOR_BGR2GRAY);

		// ブラー画像を作成
		Mat targetImageMatBlur = new Mat();
		opencv_imgproc.GaussianBlur(targetImageMatGray, targetImageMatBlur, new Size(9, 9), 0);

		// エッジ抽出
		Mat targetImageMatEdge = new Mat();
		opencv_imgproc.Canny(targetImageMatBlur, targetImageMatEdge, 50, 100);
		opencv_imgproc.dilate(targetImageMatEdge, targetImageMatEdge, new Mat(), null, 1, 0, null);
		opencv_imgproc.erode(targetImageMatEdge, targetImageMatEdge, new Mat(), null, 1, 0, null);

		// 輪郭を検出
		MatVector targetImageContours = new MatVector();
		Mat targetImageHierarchy = new Mat();
		opencv_imgproc.findContours(targetImageMatEdge, targetImageContours, targetImageHierarchy,
				opencv_imgproc.RETR_EXTERNAL,
				opencv_imgproc.CHAIN_APPROX_SIMPLE);
		System.out.println("輪郭数:" + targetImageContours.size());

		// 使用するものだけ抽出
		MatVector useTargetImageContours = new MatVector(
				Arrays.stream(targetImageContours.get())
						.filter(contour -> 100 < opencv_imgproc.contourArea(contour))
						.collect(Collectors.toList()).toArray(new Mat[0]));
		System.out.println("輪郭数:" + useTargetImageContours.size());

		// 全ての輪郭を描画
		opencv_imgproc.drawContours(targetImageMat, useTargetImageContours, -1, Scalar.WHITE, 3, 0,
				new Mat(), 1, new Point(0, 0));

		// 全ての輪郭に対して実行
		for (Mat contour : useTargetImageContours.get()) {
			try {
				System.out.println("contour:" + contour);

				// ?
				RotatedRect box = opencv_imgproc.minAreaRect(contour);
				System.out.println("box:" + box);

				// ?
				Mat points = new Mat();
				opencv_imgproc.boxPoints(box, points);
				System.out.println("points:" + points);

				// ?
				MatVector pointsMatVector = new MatVector(points);
				System.out.println("pointsMatVector:" + pointsMatVector);

				// 輪郭を描画(1)
				opencv_imgproc.rectangle(targetImageMat, box.boundingRect(), Scalar.GREEN);

				// 輪郭を描画(2)
				opencv_imgproc.drawContours(targetImageMat, pointsMatVector, -1, Scalar.RED, 3, 0,
						new Mat(), 1, new Point(0, 0));
			} catch (Exception e) {
				e.printStackTrace();

				// 輪郭を描画
				opencv_imgproc.drawContours(targetImageMat, new MatVector(contour), -1, Scalar.BLACK);
			}
		}

		// 画像を表示
		display(targetImageMat, "タイトル");

		// リソース開放?
		useTargetImageContours.close();
	}

	/**
	 * 画像を表示.
	 *
	 * @param image
	 * @param caption
	 */
	private static void display(Mat image, String caption) {
		// Create image window named "My Image".
		final CanvasFrame canvas = new CanvasFrame(caption, 1.0);

		// Request closing of the application when the image window is closed.
		canvas.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		// Convert from OpenCV Mat to Java Buffered image for display
		OpenCVFrameConverter<?> converter = new OpenCVFrameConverter.ToMat();

		// Show image on window.
		canvas.showImage(converter.convert(image));
	}
}