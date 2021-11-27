package git.lewis.vid2image;

import git.lewis.vid2image.merger.BufferedImageMerger;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.FileSystemException;
import java.nio.file.Path;

public class VidToImage {

    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            System.err.println("args = VIDEO_PATH [OUTPUT_PATH]");
        }

        Path path = Path.of(args[0]);

        VideoProcessor processor = new VideoProcessor(BufferedImageMerger.instance());

        System.out.println("Processing: " + path);

        BufferedImage image = processor.process(new FileInputStream(args[0]));

        if (image == null) {
            System.err.println("Processing failed");
        } else {
            Path outputPath = path.getParent();
            String outputName = "processed";
            String outputExtension = "png";

            if (args.length == 2) {
                Path specifiedPath = Path.of(args[1]);
                String name = specifiedPath.getFileName().toString();
                outputName = name.substring(0, name.lastIndexOf('.'));
                outputExtension = name.substring(name.indexOf('.') + 1);
                outputPath = specifiedPath.getParent();
            }

            write(image, outputPath, outputName, outputExtension);
        }
    }

    private static void write(BufferedImage image, Path path, String name, String extension) throws IOException {
        path = path.toAbsolutePath().resolve(name + "." + extension);
        File file = new File(path.toString());

        if (file.exists()) {
            System.out.println("Deleting " + path);
            if (file.delete()) {
                System.out.println("Deleted " + path);
            } else {
                throw new FileSystemException(path + " could not be deleted");
            }
        }

        System.out.println("Writing " + path);
        ImageIO.write(image, extension, file);
        System.out.println("Written " + path);
    }
}
