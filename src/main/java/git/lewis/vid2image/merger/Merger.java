package git.lewis.vid2image.merger;

public interface Merger <I> {
    I merge(I image1, I image2);
}
