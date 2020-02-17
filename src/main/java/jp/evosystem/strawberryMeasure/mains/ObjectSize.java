package jp.evosystem.strawberryMeasure.mains;

import java.io.File;

import org.bytedeco.opencv.global.opencv_imgcodecs;
import org.bytedeco.opencv.opencv_core.Mat;

/**
 * 画像内の物体の大きさを測定.
 *
 * @author cyrus
 */
public class ObjectSize extends AbstractObjectSize {

	/**
	 * 対象の画像ファイルのパス.
	 */
	private static final String TARGET_IMAGE_FILE_PATH = "src/main/resources/example_01.jpg";

	/**
	 * main.
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		// 対象の画像ファイル
		File targetImagefile = new File(TARGET_IMAGE_FILE_PATH);

		// 対象の画像ファイルを読み込み
		Mat targetImageMat = opencv_imgcodecs.imread(targetImagefile.getAbsolutePath());

		// 画像処理
		processTargetImage(targetImageMat);

		// 画像を表示
		display(targetImageMat, "タイトル");
	}
}