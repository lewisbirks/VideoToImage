package git.lewis.vid2image;

import git.lewis.vid2image.merger.Merger;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.Java2DFrameConverter;

import java.awt.image.BufferedImage;
import java.io.InputStream;

public class VideoProcessor {

    private final Merger<BufferedImage> merger;

    public VideoProcessor(Merger<BufferedImage> merger) {
        this.merger = merger;
    }

    public BufferedImage process(InputStream vid) throws FrameGrabber.Exception {
        FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(vid);
        Java2DFrameConverter converter = new Java2DFrameConverter();

        grabber.start();

        BufferedImage image = null;
        for (int frameNum = 1, numOfFrames = grabber.getLengthInVideoFrames(); frameNum <= numOfFrames; frameNum++) {
            System.out.printf("Processing frame %d of %d (%.2f%%)%n", frameNum, numOfFrames, percent(frameNum, numOfFrames));
            image = merger.merge(image, converter.convert(grabber.grabImage()));
        }

        grabber.release();
        return image;
    }

    private double percent(int current, int total) {
        return (current * 1F / total) * 100;
    }
}
