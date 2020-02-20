package jp.evosystem.objectSizeMeasurer.utils;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.bytedeco.opencv.opencv_core.Point;

/**
 * 計算ヘルパー.
 *
 * @author evosystem
 */
public class MathHelper {

	/**
	 * 2点間の中間点を計算.
	 *
	 * @param point1
	 * @param point2
	 * @return
	 */
	public static Point midPoint(Point point1, Point point2) {
		return new Point((point1.x() + point2.x()) / 2, (point1.y() + point2.y()) / 2);
	}

	/**
	 * 2点間のユークリッド距離を計算.
	 *
	 * @param point1
	 * @param point2
	 * @return
	 */
	public static double distance(Point point1, Point point2) {
		double deltaX = point1.x() - point2.x();
		double deltaY = point1.y() - point2.y();
		double result = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
		return result;
	}

	/**
	 * 4点の座標を並び替え.<br>
	 * tl,tr,br,blの順.<br>
	 *
	 * @see MathHelper#orderPoints2
	 * @see <a href=
	 *      "https://www.pyimagesearch.com/2016/03/21/ordering-coordinates-clockwise-with-python-and-opencv/">Ordering
	 *      coordinates clockwise with Python and OpenCV</a>
	 * @param pointList
	 * @return
	 * @deprecated {@link MathHelper#orderPoints2}を使用してください.
	 */
	@Deprecated
	public static List<Point> orderPoints(List<Point> pointList) {
		Point tl = null;
		Point tr = null;
		Point br = null;
		Point bl = null;

		double min = Double.POSITIVE_INFINITY;
		double max = Double.NEGATIVE_INFINITY;
		for (Point point : pointList) {
			double sum = point.x() + point.y();
			if (sum < min) {
				min = sum;
				tl = point;
			}
			if (max < sum) {
				max = sum;
				br = point;
			}
		}

		min = Double.POSITIVE_INFINITY;
		max = Double.NEGATIVE_INFINITY;
		for (Point point : pointList) {
			double diff = point.y() - point.x();
			if (diff < min) {
				min = diff;
				tr = point;
			}
			if (max < diff) {
				max = diff;
				bl = point;
			}
		}

		return Arrays.asList(tl, tr, br, bl);
	}

	/**
	 * 4点の座標を並び替え.<br>
	 * tl,tr,br,blの順.
	 *
	 * @see MathHelper#orderPoints
	 * @see <a href=
	 *      "https://www.pyimagesearch.com/2016/03/21/ordering-coordinates-clockwise-with-python-and-opencv/">Ordering
	 *      coordinates clockwise with Python and OpenCV</a>
	 * @param pointList
	 * @return
	 */
	public static List<Point> orderPoints2(List<Point> pointList) {
		Point tl = pointList.stream().min(Comparator.comparingInt(point -> point.x() + point.y())).get();
		Point tr = pointList.stream().min(Comparator.comparingInt(point -> point.y() - point.x())).get();
		Point br = pointList.stream().max(Comparator.comparingInt(point -> point.x() + point.y())).get();
		Point bl = pointList.stream().max(Comparator.comparingInt(point -> point.y() - point.x())).get();
		return Arrays.asList(tl, tr, br, bl);
	}
}