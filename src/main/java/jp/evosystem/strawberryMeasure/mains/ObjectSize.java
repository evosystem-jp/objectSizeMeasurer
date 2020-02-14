package jp.evosystem.strawberryMeasure.mains;

import java.io.File;
import java.util.Arrays;
import java.util.stream.Collectors;

import javax.swing.WindowConstants;

import org.bytedeco.javacpp.indexer.FloatRawIndexer;
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
	 * 対象の画像ファイルのパス.
	 */
	private static final String TARGET_IMAGE_FILE_PATH = "src/main/resources/Strawberry-Benefits.jpg";

	/**
	 * 使用する輪郭の面積のしきい値.
	 */
	private static final int USE_COUTOUR_AREA_THRESHOLD = 100;

	/**
	 * main.
	 *
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		// 対象の画像ファイル
		File targetImagefile = new File(TARGET_IMAGE_FILE_PATH);

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
		System.out.println("検出した輪郭数:" + targetImageContours.size());

		// 使用するものだけ抽出
		MatVector useTargetImageContours = new MatVector(
				Arrays.stream(targetImageContours.get())
						.filter(contour -> USE_COUTOUR_AREA_THRESHOLD < opencv_imgproc.contourArea(contour))
						.collect(Collectors.toList()).toArray(new Mat[0]));
		System.out.println("使用する輪郭数:" + useTargetImageContours.size());

		// 全ての輪郭を描画
		opencv_imgproc.drawContours(targetImageMat, useTargetImageContours, -1, Scalar.RED, 3, 0, new Mat(), 1,
				new Point(0, 0));

		// 全ての輪郭に対して実行
		for (Mat contour : useTargetImageContours.get()) {
			// 輪郭に対する外接矩形を取得
			RotatedRect box = opencv_imgproc.minAreaRect(contour);

			// 回転を考慮しない外接矩形を描画
			opencv_imgproc.rectangle(targetImageMat, box.boundingRect(), Scalar.GREEN);

			// 外接矩形の4点の座標を取得
			Mat points = new Mat();
			opencv_imgproc.boxPoints(box, points);

			// 外接矩形の4辺を描画
			// drawContoursが動かないため4辺をそれぞれ線で描画
			FloatRawIndexer rawIndexer = points.createIndexer();
			for (int i = 0; i < points.rows(); i++) {
				Point point1 = new Point((int) rawIndexer.get(i, 0), (int) rawIndexer.get(i, 1));
				Point point2 = new Point((int) rawIndexer.get(((i + 1) % 4), 0),
						(int) rawIndexer.get(((i + 1) % 4), 1));
				opencv_imgproc.line(targetImageMat, point1, point2, Scalar.BLUE);
			}

			try {
				// MatVectorを作成
				MatVector pointsMatVector = new MatVector(points);

				// 輪郭を描画
				// TODO 動かない
				opencv_imgproc.drawContours(targetImageMat, pointsMatVector, -1, Scalar.RED);
			} catch (Exception e) {
				e.printStackTrace();

				// 輪郭を描画
				opencv_imgproc.drawContours(targetImageMat, new MatVector(contour), -1, Scalar.YELLOW);
			}
		}

		// 画像を表示
		display(targetImageMat, "タイトル");
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