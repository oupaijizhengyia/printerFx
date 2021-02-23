package sample.print;

import sample.entity.PrintPage;

/**
 * 打印接口。
 *
 * @author yeyang
 * @date 2021/2/1
 */
public interface JFXPrinter {
    /**
     * 对于文件进行打印。
     * @param printPage
     * @return
     */
    boolean print(PrintPage printPage);
}
