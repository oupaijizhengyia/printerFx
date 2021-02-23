package sample.entity;

import javafx.print.Printer;

import java.io.File;
import java.util.List;

/**
 * 。
 *
 * @author yeyang
 * @date 2021/2/1
 */
public class PrintPage {
    /**
     * 需要打印的文件
     */
    private File file;
    /**
     * 打印机
     */
    public Printer printer;
    /**
     * 纸张尺寸
     */
    private PageSize size;
    /**
     * 打印范围
     */
    private PrintRange range;

    public static class PrintRange {
        private int start;

        private int end;

        public int getStart() {
            return start;
        }

        public void setStart(int start) {
            this.start = start;
        }

        public int getEnd() {
            return end;
        }

        public void setEnd(int end) {
            this.end = end;
        }

        public PrintRange(int start, int end) {
            this.start = start;
            this.end = end;
        }
    }

    public void setRange(int start,int end) {
        this.range = new PrintRange(start,end);
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public Printer getPrinter() {
        return printer;
    }

    public void setPrinter(Printer printer) {
        this.printer = printer;
    }

    public PageSize getSize() {
        return size;
    }

    public void setSize(PageSize size) {
        this.size = size;
    }

    public PrintRange getRange() {
        return range;
    }

    public void setRange(PrintRange range) {
        this.range = range;
    }
    public boolean between(int v){
        return v >= range.start && v <= range.end;
    }
}
