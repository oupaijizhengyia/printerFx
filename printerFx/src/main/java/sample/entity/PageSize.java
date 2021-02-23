package sample.entity;

/**
 * 纸张大小。
 *
 * @author yeyang
 * @date 2021/1/29
 */
public enum PageSize {
    A3(1190,1684),A4(595, 842),A5(297.5,421);
    private final double width;

    private final double height;

    PageSize(double width, double height) {
        this.width = width;
        this.height = height;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }
}
