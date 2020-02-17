package jp.evosystem.strawberryMeasure.utils;

import org.bytedeco.opencv.opencv_core.Point;

/**
 * 計算ヘルパー.
 *
 * @author cyrus
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
		double deltaX = point1.y() - point2.y();
		double deltaY = point1.x() - point2.x();
		double result = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
		return result;
	}
}