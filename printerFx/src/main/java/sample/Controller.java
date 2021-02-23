package sample;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.FileUtil;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXProgressBar;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.base.ValidatorBase;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.print.Printer;
import javafx.scene.control.Alert;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import sample.entity.PageSize;
import sample.entity.PrintPage;
import sample.print.PrintHandler;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

public class Controller implements Initializable {
    @FXML
    private JFXComboBox<PageSize> pageBox;

    @FXML
    private JFXComboBox<Printer> printerBox;

    @FXML
    private JFXListView<File> fileListView;

    @FXML
    private JFXProgressBar progress;

    @FXML
    private JFXTextField start;

    @FXML
    private JFXTextField end;

    private Alert alert;

    private Set<String> cacheFile = new HashSet<>();

    private PrintPage printPage = new PrintPage();
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        start.setText("1");
        end.setText("∞");
        ValidatorBase value = end.activeValidatorProperty().getValue();

        //打印纸张大小
        pageBox.getItems().addAll(PageSize.A3,PageSize.A4,PageSize.A5);
        pageBox.focusedProperty();
        pageBox.getSelectionModel().select(PageSize.A4);
        //获取打印机
        printerBox.getItems().addAll(Printer.getAllPrinters());
        printerBox.getSelectionModel().select(Printer.getDefaultPrinter());


        ContextMenu contextMenu = new ContextMenu();
        MenuItem item = new MenuItem("删除");
        item.setOnAction(o ->{
            File selectedItem = fileListView.getSelectionModel().getSelectedItem();
            cacheFile.remove(selectedItem.getAbsolutePath());
            fileListView.getItems().remove(selectedItem);
            fileListView.refresh();
        });
        contextMenu.getItems().add(item);
        fileListView.setContextMenu(contextMenu);

        //弹窗
        alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("打印完成");

    }

    @FXML
    public void onPrint(){
        progress.setVisible(true);
        PrintHandler handler = PrintHandler.newInstance();
        printPage.setPrinter(printerBox.getValue());
        int endInt;
        if("∞".endsWith(end.getText())){
            endInt = 0;
        } else {
            endInt = Integer.parseInt(end.getText());
        }
        printPage.setRange(Integer.parseInt(start.getText()),endInt);
        printPage.setSize(pageBox.getValue());

        fileListView.getItems().forEach(o -> {
            printPage.setFile(o);
            boolean print = handler.print(printPage);
            //todo 打印字底变色
        });

        FileUtil.del("C:\\Users\\叶杨\\AppData\\Local\\Temp" + File.separator + "printCache");
        if (CollectionUtil.isNotEmpty(fileListView.getItems())) {
            alert.showAndWait();
            progress.setVisible(false);
        }
    }
    @FXML
    public void onFile(DragEvent event){
        Dragboard dragboard = event.getDragboard();
        List<File> files = dragboard.getFiles();

        List<File> allFile = new ArrayList<>();
        for (File f: files) {
            allFile.addAll(obtainFile(f));
        }

        setFile(allFile);
    }

    public void setFile(List<File> files){
        files.stream()
            .filter( o -> cacheFile.add(o.getAbsolutePath()))
            .forEach(o -> fileListView.getItems().add(o));
    }


    private List<File>  obtainFile(File f) {
        List<File> fileArrayList = new ArrayList<>();
        if(f.isDirectory()){
            File[] files = f.listFiles();
            for (File ff: files
                 ) {
                fileArrayList.addAll(obtainFile(ff));
            }

        } else {
            fileArrayList.add(f);
            return fileArrayList;
        }
        return fileArrayList;
    }


}
