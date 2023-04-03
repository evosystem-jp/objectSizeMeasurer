package jp.evosystem.objectSizeMeasurer.constants;

/**
 * 環境設定.
 *
 * @author evosystem
 */
public interface Configurations {

	/**
	 * デバッグモードを有効化するかどうか.
	 */
	boolean ENABLE_DEBUG_MODE = false;

	/**
	 * 使用するブラーのサイズ(奇数).
	 */
	int USE_GAUSSIAN_BLUR_SIZE = 7;

	/**
	 * 使用するCanny処理のしきい値1.
	 */
	int USE_CANNY_THRESHOLD_1 = 50;

	/**
	 * 使用するCanny処理のしきい値2.
	 */
	int USE_CANNY_THRESHOLD_2 = 100;

	/**
	 * 使用する輪郭の面積の最小値.
	 */
	int USE_CONTOUR_AREA_MIN_THRESHOLD = 100;

	/**
	 * 使用する輪郭の面積の最大値.
	 */
	int USE_CONTOUR_AREA_MAX_THRESHOLD = 1000;

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
	 * 処理結果をファイルに出力するかどうか.
	 */
	boolean ENABLE_RECORDING = true;

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