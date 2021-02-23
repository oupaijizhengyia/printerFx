package sample.print.printer;


import cn.hutool.core.io.FileUtil;
import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.ComThread;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;
import sample.entity.PrintPage;
import sample.print.JFXPrinter;
import sample.print.PrintHandler;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * 。
 *
 * @author yeyang
 * @date 2021/2/1
 */
public class DOCPrinter implements JFXPrinter {
    @Override
    public boolean print(PrintPage printPage) {
        //如果需要制定打印某一页 则转为pdf打印  如果全部打印则直接打印
        if(printPage.getRange().getEnd() > 0){
            File file = printPage.getFile();
            FileUtil.mkdir(FileUtil.getTmpDirPath() +  "printCache");
            String toFile = FileUtil.getTmpDirPath() +  "printCache" + File.separator + UUID.randomUUID() + ".pdf";
            File pdfFile = wordToPDF(file.getAbsolutePath(), toFile);
            printPage.setFile(pdfFile);
            boolean print = PrintHandler.newInstance().print(printPage);
            pdfFile.delete();
            return  print;
        } else {
            return printDoc(printPage);
        }
    }

    /**
     * 直接打印doc
     * @param printPage
     * @return
     */
    private boolean printDoc(PrintPage printPage) {
        ActiveXComponent app = new ActiveXComponent("Word.Application");
        Dispatch doc = null;
        try {
            app = new ActiveXComponent("Word.Application");
            app.setProperty("Visible", new Variant(false));
            Dispatch docs = app.getProperty("Documents").toDispatch();
            doc = Dispatch.call(docs, "Open", printPage.getFile().getAbsolutePath()).toDispatch();
            app.setProperty("ActivePrinter",new Variant(printPage.printer.getName()));
            Dispatch.callN(doc, "PrintOut", new Object[]{});
            return true;
        } catch (Exception e) {
            System.out.println("========Error:打印失败：" + e.getMessage());
            return false;
        }finally {
            ComThread.Release();
        }
    }

    /**
     * word转化pdf，传入转换前的文件路径（例："E:\\a.docx"）和转换后的文件路径（例："E:\\a.pdf"）
     */
    public static File wordToPDF(String sfilePath,String toFilePath) {
        System.out.println("启动 Word...");
        long start = System.currentTimeMillis();
        ActiveXComponent app = null;
        File tofile = null;
        Dispatch doc = null;
        try {
            app = new ActiveXComponent("Word.Application");
            app.setProperty("Visible", new Variant(false));
            Dispatch docs = app.getProperty("Documents").toDispatch();
            doc = Dispatch.call(docs, "Open", sfilePath).toDispatch();
            System.out.println("打开文档:" + sfilePath);
            System.out.println("转换文档到 PDF:" + toFilePath);
            tofile = new File(toFilePath);
            if (tofile.exists()) {
                tofile.delete();
            }
            Dispatch.call(doc, "SaveAs", toFilePath, // FileName
                    17);//17是pdf格式
            long end = System.currentTimeMillis();
            System.out.println("转换完成..用时：" + (end - start) + "ms.");

        } catch (Exception e) {
            System.out.println("========Error:文档转换失败：" + e.getMessage());
        } finally {
            Dispatch.call(doc, "Close", false);
            System.out.println("关闭文档");
            if (app != null) {
                app.invoke("Quit", new Variant[]{});
            }
        }

        // 如果没有这句话,winword.exe进程将不会关闭
        ComThread.Release();
        return tofile;
    }

    public static void main(String[] args) {
        String input = "C:\\Users\\叶杨\\Desktop\\统一支付创建订单接口请求超时问题说明.docx";
        String out = "C:\\Users\\叶杨\\Desktop\\sdf\\3.pdf";
        wordToPDF(input,out);
        System.out.println("转换完成");
    }
}
