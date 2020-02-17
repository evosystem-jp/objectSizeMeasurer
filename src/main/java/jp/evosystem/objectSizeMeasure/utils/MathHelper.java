package jp.evosystem.objectSizeMeasure.utils;

import java.util.List;

import org.bytedeco.opencv.opencv_core.Point;

/**
 * 計算ヘルパー.
 *
 * @author evosystem
 */
public class MathHelper {

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
	 * 4点の座標を並び替え.
	 *
	 * @param pointList
	 * @return
	 */
	public static List<Point> orderPoints(List<Point> pointList) {
		// TODO perspective.order_points

		return pointList;
	}
}