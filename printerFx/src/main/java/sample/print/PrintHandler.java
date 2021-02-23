package sample.print;

import sample.Human;
import sample.entity.PrintPage;
import sample.print.printer.DOCPrinter;
import sample.print.printer.PDFPrinter;

/**
 * 。
 *
 * @author yeyang
 * @date 2021/2/1
 */
public class PrintHandler implements  JFXPrinter{

    private PrintHandler(){}

    private static PrintHandler handler = new PrintHandler();

    public static PrintHandler newInstance(){
        return handler;
    }

    @Override
    public boolean print(PrintPage printPage) {
        JFXPrinter printer = null;
        String name = printPage.getFile().getName();
        if(name.endsWith("pdf")){
            printer = new PDFPrinter();
        } else if (name.endsWith(".docx") || name.endsWith(".doc")){
            printer = new DOCPrinter();
        } else {
            return false;
        }
        return printer.print(printPage);
    }

}
