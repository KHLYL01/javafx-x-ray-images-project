package com.example.multimedia;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ReportController {

    @FXML
    private ImageView imageView;

    @FXML
    private Button stopButton;

    @FXML
    private Button playButton;

    @FXML
    private Label label1;
    @FXML
    private Label label2;
    @FXML
    private Label label3;
    @FXML
    private Label label4;
    @FXML
    private Label label5;
    @FXML
    private Label label6;

    @FXML
    private TextField text1;
    @FXML
    private TextField text2;
    @FXML
    private TextArea text3;
    @FXML
    private TextField text4;
    @FXML
    private TextArea text5;

    private TargetDataLine microphone;
    private File audioFile;
    private Clip clip;

    private boolean isRecording = false;

    public void receiveImage(Image image) {
        imageView.setImage(image);

        try {
            BufferedImage bImage = SwingFXUtils.fromFXImage(imageView.getImage(), null);
            ImageIO.write(bImage, "png", new File("image.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onActionRecordButton() {
        stopButton.setVisible(true);
        label6.setVisible(true);
        isRecording = true;
        new Thread(() -> {
            try {
                AudioFormat format = new AudioFormat(16000.0f, 16, 2, true, true);
                DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

                if (!AudioSystem.isLineSupported(info)) {
                    System.err.println("Line not supported");
                    return;
                }

                microphone = (TargetDataLine) AudioSystem.getLine(info);
                microphone.open(format);

                ByteArrayOutputStream out = new ByteArrayOutputStream();
                int numBytesRead;


                byte[] data = new byte[microphone.getBufferSize() / 5];
                microphone.start();

                while (isRecording) {
                    numBytesRead = microphone.read(data, 0, data.length);
                    out.write(data, 0, numBytesRead);
                }

                byte[] audioData = out.toByteArray();
                ByteArrayInputStream bais = new ByteArrayInputStream(audioData);
                AudioInputStream audioStream = new AudioInputStream(bais, format, audioData.length / format.getFrameSize());

                audioFile = new File("test.wav");
                AudioSystem.write(audioStream, AudioFileFormat.Type.WAVE, audioFile);

            } catch (LineUnavailableException | IOException ex) {
                ex.printStackTrace();
            }
        }).start();
    }

    public void onActionStopButton() {
        isRecording = false;
        playButton.setVisible(true);
        label6.setVisible(false);
        if (microphone.isOpen()) {
            microphone.stop();
            microphone.close();
        } else {
            clip.stop();
            clip.close();
        }
    }

    public void onActionPlayButton() {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(audioFile);
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void onActionPdfButton() {


        FileChooser fileChooser = new FileChooser();

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PDF", "*.pdf")
        );

        fileChooser.setInitialDirectory(new File("C:\\Users\\KHALYL\\Desktop"));
        fileChooser.setInitialFileName("report.pdf");

        File file = fileChooser.showSaveDialog(label1.getScene().getWindow());

        if (file != null) {
            createPDF(file.getPath());
        }


    }


    private void createPDF(String filePath) {
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            BufferedImage bImage = SwingFXUtils.fromFXImage(imageView.getImage(), null);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            ImageIO.write(bImage, "png", stream);
            byte[] res = stream.toByteArray();
            stream.close();

            com.itextpdf.text.Image image = com.itextpdf.text.Image.getInstance(res);
            image.setAlignment(Element.ALIGN_RIGHT | com.itextpdf.text.Image.TEXTWRAP);
            image.scaleToFit(200, 320);
            document.add(image);

            // إضافة نص
            document.add(new Paragraph(label1.getText() + " " + text1.getText()));
            document.add(new Paragraph(label2.getText() + " " + text2.getText()));

            Paragraph paragraph1 = new Paragraph(label3.getText());
            paragraph1.setFont(FontFactory.getFont(FontFactory.HELVETICA, 20, Font.BOLD));
            Paragraph paragraph2 = new Paragraph(label4.getText());
            paragraph1.setFont(FontFactory.getFont(FontFactory.HELVETICA, 20, Font.BOLD));
            Paragraph paragraph3 = new Paragraph(label5.getText());
            paragraph1.setFont(FontFactory.getFont(FontFactory.HELVETICA, 20, Font.BOLD));

            document.add(paragraph1);
            document.add(new Paragraph(text3.getText()));
            document.add(paragraph2);
            document.add(new Paragraph(text4.getText()));
            document.add(paragraph3);
            document.add(new Paragraph(text5.getText()));
            document.close();

        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }
    }

    public void onActionZipButton() {


        FileChooser fileChooser = new FileChooser();

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("ZIP", "*.zip")
        );

        fileChooser.setInitialDirectory(new File("C:\\Users\\KHALYL\\Desktop"));
        fileChooser.setInitialFileName("compress.zip");

        File file = fileChooser.showSaveDialog(label1.getScene().getWindow());

        if (file != null) {
            createZIP(file.getPath());
        }
    }

    private void createZIP(String path) {
        try {
            byte[] buffer = new byte[1024];

            createPDF("report.pdf");

            String[] files = {"image.png", "test.wav", "report.pdf"};

            FileOutputStream fos = new FileOutputStream(path);
            ZipOutputStream zos = new ZipOutputStream(fos);

            for (String pathFile : files) {
                File srcFile = new File(pathFile);
                if (srcFile.exists()) {
                    FileInputStream fis = new FileInputStream(srcFile);
                    zos.putNextEntry(new ZipEntry(srcFile.getName()));

                    int length;
                    while ((length = fis.read(buffer)) > 0) {
                        zos.write(buffer, 0, length);
                    }

                    zos.closeEntry();
                    fis.close();
                }
            }

            zos.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public void onActionShareButton(ActionEvent actionEvent) {
        try {

            createZIP("compress.zip");

            FXMLLoader loader = new FXMLLoader(getClass().getResource("share-view.fxml"));
            Parent shareView = loader.load();
            Scene shareScene = new Scene(shareView);

            Stage newStage = new Stage();

            newStage.initModality(Modality.NONE);
            newStage.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
            newStage.setTitle("Share");
            newStage.setScene(shareScene);
            newStage.show();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
