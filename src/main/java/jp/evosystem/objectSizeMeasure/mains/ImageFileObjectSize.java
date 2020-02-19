package jp.evosystem.objectSizeMeasure.mains;

import java.io.File;

import javax.swing.WindowConstants;

import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.opencv.global.opencv_imgcodecs;
import org.bytedeco.opencv.opencv_core.Mat;

/**
 * 画像内の物体の大きさを測定.
 *
 * @author evosystem
 */
public class ImageFileObjectSize extends AbstractObjectSize {

	/**
	 * 対象の画像ファイルのパス.
	 */
	private static final String TARGET_IMAGE_FILE_PATH = "images/example_01.jpg";

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

	/**
	 * 画像を表示.
	 *
	 * @param image
	 * @param caption
	 */
	private static void display(Mat image, String caption) {
		// コンバーターを作成
		OpenCVFrameConverter.ToMat converter = new OpenCVFrameConverter.ToMat();

		// 画面を作成
		CanvasFrame canvas = new CanvasFrame(caption, 1.0);

		// ウィンドウを閉じたときに終了するように設定
		canvas.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		// フレームを表示
		canvas.showImage(converter.convert(image));
	}
}