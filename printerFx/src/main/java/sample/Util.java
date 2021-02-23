package sample;

import javax.print.DocFlavor;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.HashPrintRequestAttributeSet;

/**
 * 。
 *
 * @author yeyang
 * @date 2021/2/1
 */
public class Util {

    public static PrintService obtainPrintService(String name,DocFlavor.INPUT_STREAM stream){
        DocFlavor psInFormat = stream;
        HashPrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
        PrintService printService[] = PrintServiceLookup.lookupPrintServices(psInFormat, pras);
        PrintService myPrinter = null;
        // 遍历所有打印机如果没有选择打印机或找不到该打印机则调用默认打印机
        for (PrintService printService2 : printService) {

            String svcname = printService2.toString();
            System.out.println("打印机有："+svcname);
            if (svcname.contains(name)) {
                myPrinter = printService2;
                break;
            }
        }
        if (myPrinter == null) {
            myPrinter = PrintServiceLookup.lookupDefaultPrintService();
        }
        System.out.println("选择的打印机为："+myPrinter);
        return myPrinter;
    }
}
