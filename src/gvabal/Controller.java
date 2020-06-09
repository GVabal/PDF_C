package gvabal;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.pdmodel.encryption.StandardProtectionPolicy;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Controller {
    private ObservableList<File> mergeItems = FXCollections.observableArrayList();
    @FXML
    private ListView<File> mergeList = new ListView<>(mergeItems);
    @FXML
    private TextField selectFileText;
    @FXML
    private PasswordField protectText;
    @FXML
    private PasswordField protectText2;
    @FXML
    public void addFilesToList() {

        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PDF files (*.pdf)", "*.pdf");
        fileChooser.getExtensionFilters().add(extFilter);

        List<File> fileList = fileChooser.showOpenMultipleDialog(null);

        if (fileList != null) {
            mergeItems.addAll(fileList);
        }

        mergeList.setItems(mergeItems);
    }

    @FXML
    public void clearList() {

        mergeItems.clear();
    }

    @FXML
    public void mergePDFs() throws IOException {

            PDFMergerUtility PDFMerger = new PDFMergerUtility();

            FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PDF files (*.pdf)", "*.pdf");
            fileChooser.getExtensionFilters().add(extFilter);
            fileChooser.setInitialFileName("merged document.pdf");

            File mergedFile = fileChooser.showSaveDialog(null);
            PDFMerger.setDestinationFileName(mergedFile.toString());

            for (File item : mergeItems) {
                PDFMerger.addSource(item);
            }

            PDFMerger.mergeDocuments(null);

    }

    @FXML
    public void setSelectFile() {

        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PDF files (*.pdf)", "*.pdf");
        fileChooser.getExtensionFilters().add(extFilter);

        File file = fileChooser.showOpenDialog(null);

        selectFileText.setText(file.toString());

    }

    @FXML
    public void protectFile() throws IOException {

        File file = new File(selectFileText.getText());
        PDDocument document = PDDocument.load(file);

        AccessPermission ap = new AccessPermission();
        StandardProtectionPolicy spp = new StandardProtectionPolicy(protectText.getText(), protectText2.getText(), ap);
        spp.setEncryptionKeyLength(256);
        spp.setPermissions(ap);

        document.protect(spp);

        selectFileText.clear();
        protectText.clear();
        protectText2.clear();

        document.save(file);
        document.close();

    }

}
