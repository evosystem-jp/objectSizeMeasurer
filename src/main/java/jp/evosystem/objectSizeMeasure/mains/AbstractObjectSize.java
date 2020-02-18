package jp.evosystem.objectSizeMeasure.mains;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.bytedeco.javacpp.indexer.FloatRawIndexer;
import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.MatVector;
import org.bytedeco.opencv.opencv_core.Point;
import org.bytedeco.opencv.opencv_core.RotatedRect;
import org.bytedeco.opencv.opencv_core.Scalar;
import org.bytedeco.opencv.opencv_core.Size;

import jp.evosystem.objectSizeMeasure.utils.MathHelper;

/**
 * 画像内の物体の大きさを測定.
 *
 * @author evosystem
 */
public abstract class AbstractObjectSize {

	/**
	 * 使用する輪郭の面積のしきい値.
	 */
	private static final int USE_CONTOUR_AREA_THRESHOLD = 200;

	/**
	 * 使用するピクセル/cm.
	 */
	private static final int USE_PIXEL_PER_CENTIMETER = 40;

	/**
	 * 全ての輪郭を描画するかどうか.
	 */
	private static final boolean DRAW_ALL_CONTOURS = false;

	/**
	 * 回転を考慮しない外接矩形を描画するかどうか.
	 */
	private static final boolean DRAW_RECTANGLE = false;

	/**
	 * 画像処理.
	 *
	 * @param targetImageMat
	 */
	protected static void processTargetImage(Mat targetImageMat) {
		processTargetImage(targetImageMat, USE_CONTOUR_AREA_THRESHOLD);
	}

	/**
	 * 画像処理.
	 *
	 * @param targetImageMat
	 * @param useContourAreaThreshold
	 */
	protected static void processTargetImage(Mat targetImageMat, int useContourAreaThreshold) {
		// グレースケール画像を作成
		Mat targetImageMatGray = new Mat();
		opencv_imgproc.cvtColor(targetImageMat, targetImageMatGray, opencv_imgproc.COLOR_BGR2GRAY);

		// ブラー画像を作成
		Mat targetImageMatBlur = new Mat();
		opencv_imgproc.GaussianBlur(targetImageMatGray, targetImageMatBlur, new Size(7, 7), 0);

		// エッジ抽出
		Mat targetImageMatEdge = new Mat();
		opencv_imgproc.Canny(targetImageMatBlur, targetImageMatEdge, 50, 100);
		opencv_imgproc.dilate(targetImageMatEdge, targetImageMatEdge, new Mat());
		opencv_imgproc.erode(targetImageMatEdge, targetImageMatEdge, new Mat());

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
						.filter(contour -> useContourAreaThreshold < opencv_imgproc.contourArea(contour))
						.collect(Collectors.toList()).toArray(new Mat[0]));
		System.out.println("使用する輪郭数:" + useTargetImageContours.size());

		// 全ての輪郭を描画
		if (DRAW_ALL_CONTOURS) {
			opencv_imgproc.drawContours(targetImageMat, useTargetImageContours, -1, Scalar.RED, 3, 0, new Mat(), 1,
					new Point(0, 0));
		}

		// 全ての輪郭に対して実行
		for (Mat contour : useTargetImageContours.get()) {
			// 輪郭に対する外接矩形を取得
			RotatedRect box = opencv_imgproc.minAreaRect(contour);

			// 回転を考慮しない外接矩形を描画
			if (DRAW_RECTANGLE) {
				opencv_imgproc.rectangle(targetImageMat, box.boundingRect(), Scalar.GREEN);
			}

			// 外接矩形の4点の座標を取得(Mat型)
			Mat points = new Mat();
			opencv_imgproc.boxPoints(box, points);

			// 外接矩形の4点の座標(Point型リスト)
			// tl, tr, br, blの順
			List<Point> pointList = new ArrayList<>();

			// 外接矩形の4辺を描画
			// drawContoursが動かないため4辺をそれぞれ線で描画
			FloatRawIndexer rawIndexer = points.createIndexer();
			for (int i = 0; i < points.rows(); i++) {
				Point point1 = new Point((int) rawIndexer.get(i, 0), (int) rawIndexer.get(i, 1));
				Point point2 = new Point((int) rawIndexer.get(((i + 1) % 4), 0),
						(int) rawIndexer.get(((i + 1) % 4), 1));
				opencv_imgproc.line(targetImageMat, point1, point2, Scalar.BLUE);
				pointList.add(point1);
			}

			// 4点の座標を並び替え
			pointList = MathHelper.orderPoints2(pointList);

			// 4点の座標に円を描画
			for (Point point : pointList) {
				opencv_imgproc.circle(targetImageMat, point, 5, Scalar.RED, -1, opencv_imgproc.FILLED, 0);
			}

			// 外接矩形の4点の座標をそれぞれ変数に代入
			Point tl = pointList.get(0);
			Point tr = pointList.get(1);
			Point br = pointList.get(2);
			Point bl = pointList.get(3);

			// 中間点を計算
			Point tltr = MathHelper.midPoint(tl, tr);
			Point blbr = MathHelper.midPoint(bl, br);
			Point tlbl = MathHelper.midPoint(tl, bl);
			Point trbr = MathHelper.midPoint(tr, br);

			// 中間点に円を描画
			opencv_imgproc.circle(targetImageMat, tltr, 3, Scalar.BLUE, -1, opencv_imgproc.FILLED, 0);
			opencv_imgproc.circle(targetImageMat, blbr, 3, Scalar.BLUE, -1, opencv_imgproc.FILLED, 0);
			opencv_imgproc.circle(targetImageMat, tlbl, 3, Scalar.BLUE, -1, opencv_imgproc.FILLED, 0);
			opencv_imgproc.circle(targetImageMat, trbr, 3, Scalar.BLUE, -1, opencv_imgproc.FILLED, 0);

			// 中間点同士を結ぶ直線を描画
			opencv_imgproc.line(targetImageMat, tltr, blbr, Scalar.BLUE);
			opencv_imgproc.line(targetImageMat, tlbl, trbr, Scalar.BLUE);

			// 中間点間の長さを計算
			double width = MathHelper.distance(tltr, blbr) / USE_PIXEL_PER_CENTIMETER;
			double height = MathHelper.distance(tlbl, trbr) / USE_PIXEL_PER_CENTIMETER;

			// 文字を描画
			opencv_imgproc.putText(targetImageMat, String.format("%.2fcm", width),
					new Point((tltr.x() - 15), (tltr.y() - 10)),
					opencv_imgproc.FONT_HERSHEY_SIMPLEX, 0.5, Scalar.BLACK);
			opencv_imgproc.putText(targetImageMat, String.format("%.2fcm", height),
					new Point((trbr.x() + 10), trbr.y()),
					opencv_imgproc.FONT_HERSHEY_SIMPLEX, 0.5, Scalar.BLACK);

			try {
				// MatVectorを作成
				MatVector pointsMatVector = new MatVector(points);

				// 輪郭を描画
				// TODO 動かない
				opencv_imgproc.drawContours(targetImageMat, pointsMatVector, -1, Scalar.RED);
			} catch (Exception e) {
				e.printStackTrace();

				// 輪郭を描画
				if (DRAW_ALL_CONTOURS) {
					opencv_imgproc.drawContours(targetImageMat, new MatVector(contour), -1, Scalar.YELLOW);
				}
			}
		}
	}
}