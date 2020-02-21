package jp.evosystem.objectSizeMeasurer.constants;

/**
 * 環境設定.
 *
 * @author evosystem
 */
public interface Configurations {

	/**
	 * 使用する輪郭の面積のしきい値.
	 */
	int USE_CONTOUR_AREA_THRESHOLD = 200;

	/**
	 * 使用するピクセル/cm.
	 */
	int USE_PIXEL_PER_CENTIMETER = 40;

	/**
	 * 全ての輪郭を描画するかどうか.
	 */
	boolean DRAW_ALL_CONTOURS = false;

	/**
	 * 回転を考慮しない外接矩形を描画するかどうか.
	 */
	boolean DRAW_RECTANGLE = false;

	/**
	 * 対象の画像ファイルのパス.
	 */
	String TARGET_IMAGE_FILE_PATH = "images/example_01.jpg";

	/**
	 * 対象の動画ファイルのパス.
	 */
	String TARGET_VIDEO_FILE_PATH = "videos/example.mp4";

	/**
	 * 使用するWebカメラのデバイス番号.
	 */
	int TARGET_DEVICE_NUMBER = 0;
}