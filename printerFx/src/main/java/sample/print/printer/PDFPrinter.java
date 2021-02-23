package sample.print.printer;

import com.itextpdf.text.pdf.PdfReader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.printing.PDFPrintable;
import org.apache.pdfbox.printing.Scaling;
import sample.Util;
import sample.entity.PrintPage;
import sample.print.JFXPrinter;

import javax.print.DocFlavor;
import java.awt.print.Book;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * 。
 *
 * @author yeyang
 * @date 2021/2/1
 */
public class PDFPrinter implements JFXPrinter {
    @Override
    public boolean print(PrintPage printPage) {
        PdfReader reader = null;
        try {
            reader = new PdfReader(new FileInputStream(printPage.getFile()));
            int numberOfPages = reader.getNumberOfPages();
            if(printPage.getRange().getEnd() == 0){
                printPage.getRange().setEnd(numberOfPages);
            }

            //若结束页码大于所有页码， 则打印全部
            if(numberOfPages > printPage.getRange().getEnd()){
                if (!printPage.between(numberOfPages)) {
                    return false;
                }
            }

            boolean flag = true;
            int i = 1;
            while (flag){
                try {
                    printFile(printPage,i);
                } catch (PrinterException e) {
                   //todo  显示打印失败
                }
                i++;
                flag = printPage.between(i);
            }

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    private void printFile(PrintPage printPage,int number) throws IOException, PrinterException {
        PDDocument document = PDDocument.load(printPage.getFile());
        Printable printable = new PDFPrintable(document);

        PrinterJob job = PrinterJob.getPrinterJob();
        job.setJobName("打印：" + printPage.getFile().getName() + "第 " + number+" 页");
        job.setPrintable(printable);
        job.setCopies(1);
        job.setPrintService(Util.obtainPrintService(printPage.getPrinter().getName(), DocFlavor.INPUT_STREAM.PDF));

        Paper paper = new Paper();
        paper.setSize(printPage.getSize().getWidth(),printPage.getSize().getHeight());
        paper.setImageableArea(0,0,printPage.getSize().getWidth(),printPage.getSize().getHeight());

        PageFormat pageFormat = new PageFormat();
        pageFormat.setPaper(paper);

        Book book = new Book();
        /**
         *实际大小 ACTUAL_SIZE,
         *缩小     SHRINK_TO_FIT,
         *拉伸     STRETCH_TO_FIT,
         *适应     SCALE_TO_FIT;
         **/
        PDFPrintable pdfPrintable = new PDFPrintable(document, Scaling.SCALE_TO_FIT);

        //页码
        book.append(pdfPrintable,pageFormat,number);
        job.setPageable(book);
        job.print();
    }
}
