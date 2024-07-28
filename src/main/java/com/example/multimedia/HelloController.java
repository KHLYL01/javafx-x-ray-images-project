package com.example.multimedia;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.*;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Path;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.Rectangle;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.jtransforms.fft.DoubleFFT_2D;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

public class HelloController implements Initializable {

    @FXML
    private ImageView imageView1;

    @FXML
    private ImageView imageView2;

    @FXML
    private Label label1;

    @FXML
    private Label label2;

    @FXML
    private TextField text1;
    @FXML
    private TextField text2;
    @FXML
    private TextField text3;

    @FXML
    private TextField text4;


    @FXML
    private Button button1;
    @FXML
    private Button button2;

    @FXML
    private Button button3;

    @FXML
    private Button button4;

    @FXML
    private Button button7;

    @FXML
    private Button button8;

    @FXML
    private Button button9;

    @FXML
    private Button button10;

    @FXML
    private ComboBox<String> stringComboBox;

    @FXML
    private Label sizeLabel;

    @FXML
    private ComboBox<String> sizeComboBox;

    @FXML
    private TableView<ImageInfo> tableView;

    @FXML
    public TableColumn<ImageInfo, String> columnName;

    @FXML
    public TableColumn<ImageInfo, Long> columnSize;

    @FXML
    public TableColumn<ImageInfo, String> columnLastModified;

    @FXML
    public Rectangle rectangle;

    @FXML
    public Polyline polyline;

    int imageRadiusHeight = 0;
    int imageRadiusWidth = 0;
    int xMouse = 0;
    int yMouse = 0;
    double totalIntensity = 0;

    Color[] colors1 = {Color.DARKRED, Color.RED, Color.INDIANRED, Color.DARKGREEN, Color.GREEN, Color.LIGHTGREEN, Color.DARKBLUE, Color.BLUE, Color.LIGHTBLUE, Color.YELLOW};
    Color[] colors2 = {Color.DARKCYAN, Color.CYAN, Color.LIGHTCYAN, Color.DARKMAGENTA, Color.MAGENTA, Color.ORANGERED, Color.YELLOWGREEN, Color.YELLOW, Color.LIGHTYELLOW, Color.RED};
    Color[] colors3 = {Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.CYAN, Color.MAGENTA, Color.GRAY, Color.ORANGE, Color.PINK, Color.INDIANRED};
    Color[] colors = new Color[10];

    int radius;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        stringComboBox.setItems(FXCollections.observableArrayList(
                "colors 1", "colors 2", "colors 3"
        ));
        sizeComboBox.setItems(FXCollections.observableArrayList(
                "8", "16", "24", "32", "40", "48", "56", "64", "72", "80"
        ));
        colors = colors1;
        radius = 32;

        columnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        columnSize.setCellValueFactory(new PropertyValueFactory<>("size"));
        columnLastModified.setCellValueFactory(new PropertyValueFactory<>("lastModified"));

        tableView.getSelectionModel().selectedItemProperty().addListener(
                (observableValue, imageInfo, t1) -> {
                    if (t1 != null) {
                        File file = new File(text3.getText() + "\\" + t1.getName());
                        try {
                            imageView1.setImage(new Image(new FileInputStream(file)));
                            label2.setVisible(true);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        label1.setVisible(false);
                        button1.setVisible(true);
                        button9.setVisible(true);
                        button10.setVisible(true);
                        stringComboBox.setVisible(true);
                        sizeLabel.setVisible(true);
                        sizeComboBox.setVisible(true);

                    }
                }
        );
    }

    // copy images
    @FXML
    protected void onDragOverLabel(DragEvent dragEvent) {
        System.out.println("over");
        Dragboard dragboard = dragEvent.getDragboard();
        if (dragboard.hasImage() || dragboard.hasFiles()) {
            dragEvent.acceptTransferModes(TransferMode.COPY);
        }
        dragEvent.consume();
    }

    // view image1
    @FXML
    protected void onDragDroppedLabel1(DragEvent dragEvent) {
        Dragboard dragboard = dragEvent.getDragboard();
        boolean success = false;
        if (dragboard.hasImage() || dragboard.hasFiles()) {
            try {
                Image image = new Image(new FileInputStream(dragboard.getFiles().get(0).toString()));
                imageView1.setImage(image);
                label2.setVisible(true);
                success = true;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        label1.setVisible(false);
        button1.setVisible(true);
        button9.setVisible(true);
        button10.setVisible(true);
        button9.setDisable(false);
        stringComboBox.setVisible(true);
        sizeLabel.setVisible(true);
        sizeComboBox.setVisible(true);

        dragEvent.setDropCompleted(success);
        dragEvent.consume();
    }

    // view image2
    @FXML
    protected void onDragDroppedLabel2(DragEvent dragEvent) {
        Dragboard dragboard = dragEvent.getDragboard();
        boolean success = false;
        if (dragboard.hasImage() || dragboard.hasFiles()) {
            try {
                Image image = new Image(new FileInputStream(dragboard.getFiles().get(0).toString()));
                imageView2.setImage(image);
                success = true;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        label2.setVisible(false);
        button3.setVisible(true);
        dragEvent.setDropCompleted(success);
        dragEvent.consume();
    }

    // view image1
    @FXML
    protected void onMouseClickedLabel1(MouseEvent mouseEvent) {
        try {
            FileChooser fileChooser = new FileChooser();
            File file = fileChooser.showOpenDialog(label1.getScene().getWindow());
            if (file != null) {
                Image image = new Image(new FileInputStream(file));
                imageView1.setImage(image);
                label1.setVisible(false);
                label2.setVisible(true);
                button1.setVisible(true);
                button9.setVisible(true);
                button10.setVisible(true);
                button9.setDisable(false);
                stringComboBox.setVisible(true);
                sizeLabel.setVisible(true);
                sizeComboBox.setVisible(true);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        mouseEvent.consume();
    }

    // view image2
    @FXML
    protected void onMouseClickedLabel2(MouseEvent mouseEvent) {
        try {
            FileChooser fileChooser = new FileChooser();
            File file = fileChooser.showOpenDialog(label1.getScene().getWindow());
            if (file != null) {
                Image image = new Image(new FileInputStream(file));
                imageView2.setImage(image);
                label2.setVisible(false);
                button3.setVisible(true);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        mouseEvent.consume();
    }

    // reset images
    @FXML
    protected void onActionButton1(ActionEvent actionEvent) {
        imageView1.setImage(null);
        imageView2.setImage(null);
        label1.setVisible(true);
        label2.setVisible(false);
        text1.setVisible(false);
        button1.setVisible(false);
        button2.setVisible(false);
        button3.setVisible(false);
        stringComboBox.setVisible(false);
        sizeLabel.setVisible(false);
        sizeComboBox.setVisible(false);
        text1.setVisible(false);
        button4.setVisible(false);
        text2.setVisible(false);
        totalIntensity = 0;
        button7.setVisible(false);
        button8.setVisible(false);
        button9.setVisible(false);
        button10.setVisible(false);
        rectangle.setVisible(false);
        polyline.setVisible(false);
        actionEvent.consume();
    }

    // choose color map
    @FXML
    protected void onActionComboBox(ActionEvent actionEvent) {
        if (stringComboBox.getValue().equals("colors 1")) {
            colors = colors1;
        } else if (stringComboBox.getValue().equals("colors 2")) {
            colors = colors2;
        } else {
            colors = colors3;
        }
        actionEvent.consume();
    }

    // choose radius size
    @FXML
    protected void onActionSizeComboBox(ActionEvent actionEvent) {
        radius = Integer.parseInt(sizeComboBox.getValue());
        actionEvent.consume();
    }

    // save image
    @FXML
    protected void onActionButton2(ActionEvent actionEvent) throws IOException {
        FileChooser fileChooser = new FileChooser();

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Image", "*.*"),
                new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("PNG", "*.png")
        );

        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(imageView1.getImage(), null);

        fileChooser.setInitialDirectory(new File("C:\\Users\\KHALYL\\Desktop"));
        fileChooser.setInitialFileName("image.png");

        File file = fileChooser.showSaveDialog(button2.getScene().getWindow());

        if (file != null) {
            ImageIO.write(bufferedImage, "png", file);
        }

        actionEvent.consume();

    }


    // selected area in image
    @FXML
    protected void onMouseClickedImageView1(MouseEvent mouseEvent) {

        Image image = imageView1.getImage();

        imageRadiusWidth = (int) (radius * imageView1.getImage().getWidth() / imageView1.getFitWidth());
        imageRadiusHeight = (int) (radius * imageView1.getImage().getHeight() / imageView1.getFitHeight());

        xMouse = (int) (mouseEvent.getX() * image.getWidth() / imageView1.getFitWidth());
        yMouse = (int) (mouseEvent.getY() * image.getHeight() / imageView1.getFitHeight());

        rectangle.setWidth(radius * 2);
        rectangle.setHeight(radius * 2);

        rectangle.setX((mouseEvent.getX() - radius));
        rectangle.setY((mouseEvent.getY() - radius));

        rectangle.setVisible(true);

        button2.setVisible(true);
        button4.setVisible(true);
        button4.setDisable(false);
        button7.setVisible(true);
        button8.setVisible(true);
    }

    // return difference between two images
    @FXML
    protected void onActionButton3(ActionEvent actionEvent) {
        Image image1 = imageView1.getImage();
        Image image2 = imageView2.getImage();
        PixelReader pixelReader1 = image1.getPixelReader();
        PixelReader pixelReader2 = image2.getPixelReader();

        double intensity1 = 0;
        double intensity2 = 0;

        for (int x = 0; x < (int) image1.getWidth(); x++) {
            for (int y = 0; y < (int) image1.getHeight(); y++) {
                Color pixelColorRBG1 = pixelReader1.getColor(x, y);
                Color pixelColorRBG2 = pixelReader2.getColor(x, y);

                intensity1 += (pixelColorRBG1.getRed() + pixelColorRBG1.getGreen() + pixelColorRBG1.getBlue());
                intensity2 += (pixelColorRBG2.getRed() + pixelColorRBG2.getGreen() + pixelColorRBG2.getBlue());
            }
        }
//        for (int x = 0; x < (int) image2.getWidth(); x++) {
//            for (int y = 0; y < (int) image2.getHeight(); y++) {
//                Color pixelColorRBG = pixelReader2.getColor(x, y);
//                intensity2 += (pixelColorRBG.getRed() + pixelColorRBG.getGreen() + pixelColorRBG.getBlue());
//            }
//        }

        text1.setVisible(true);

        if (intensity1 > intensity2) {
            text1.setText("تقدم بالعلاج");
        } else if (intensity1 < intensity2) {
            text1.setText("تطور بالمرض");
        } else {
            text1.setText("لا يوجد تقدم بالعلاج او تطور بالمرض");
        }

        actionEvent.consume();

    }

    // select folder image
    @FXML
    protected void onActionButton5(ActionEvent actionEvent) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("choose image folder");
        directoryChooser.setInitialDirectory(new File("D:"));
        File file = directoryChooser.showDialog(text3.getScene().getWindow());
        if (file != null) {
            text3.setText(file.getPath());
            File folder = new File(file.getPath());
            File[] listOfImage = folder.listFiles();

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

            ObservableList<ImageInfo> imageInfos = FXCollections.observableArrayList();

            if (listOfImage != null) {
                for (File image : listOfImage) {
                    if (image.isFile()) {
                        ImageInfo imageInfo = ImageInfo.builder()
                                .name(image.getName())
                                .size(((double) image.length() / 1000) + " KB")
                                .lastModified(sdf.format(new Date(image.lastModified())))
                                .build();

                        imageInfos.add(imageInfo);

                    }
                    tableView.setItems(imageInfos);
                }
            }

        }
        actionEvent.consume();
    }

    // search by size or last modified
    @FXML
    protected void onActionButton6(ActionEvent actionEvent) {
        File folder = new File(text3.getText());
        File[] listOfImage = folder.listFiles();

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        ObservableList<ImageInfo> imageInfos = FXCollections.observableArrayList();

        if (listOfImage != null) {
            for (File image : listOfImage) {
                if (image.isFile()) {

                    String size = (image.length() / 1000) + "";

                    if (
                            text4.getText().isEmpty()
                                    || (sdf.format(image.lastModified()).equals(text4.getText()))
                                    || size.equals(text4.getText())
                    ) {
                        ImageInfo imageInfo = ImageInfo.builder()
                                .name(image.getName())
                                .size(((double) image.length() / 1000) + " KB")
                                .lastModified(sdf.format(new Date(image.lastModified())))
                                .build();
                        imageInfos.add(imageInfo);
                    }
                }
                tableView.setItems(imageInfos);
                actionEvent.consume();
            }
        }
    }

    // view status of selected area of image
    @FXML
    protected void onActionButton4(ActionEvent actionEvent) {
        Image image = imageView1.getImage();
        PixelReader pixelReader = image.getPixelReader();
        for (int x = 0; x < (int) image.getWidth(); x++) {
            for (int y = 0; y < (int) image.getHeight(); y++) {
                Color pixelColorRBG = pixelReader.getColor(x, y);
                if ((x <= xMouse + imageRadiusWidth && x >= xMouse - imageRadiusWidth) &&
                        (y <= yMouse + imageRadiusHeight && y >= yMouse - imageRadiusHeight)) {
                    double intensity = (pixelColorRBG.getRed() + pixelColorRBG.getGreen() + pixelColorRBG.getBlue()) / 3;
                    totalIntensity += intensity;
                }
            }
        }

        double per100 = totalIntensity / (imageRadiusHeight * imageRadiusWidth * 4);
        text2.setVisible(true);

        switch (radius) {
            case 8, 16, 24 -> {
                if (per100 > 0.9) {//&& radius >= 48
                    text2.setText("إصابة متوسطة");
                } else {
                    text2.setText("إصابة خفيفة");
                }
            }
            case 32, 40, 48, 56 -> {
                if (per100 > 0.7) {//&& radius >= 48
                    text2.setText("إصابة شديدة");
                } else if (per100 > 0.5) {//&& radius >= 24
                    text2.setText("إصابة متوسطة");
                } else {
                    text2.setText("إصابة خفيفة");
                }
            }
            case 64, 72, 80 -> {
                if (per100 > 0.4) {//&& radius >= 48
                    text2.setText("إصابة شديدة");
                } else if (per100 > 0.3) {//&& radius >= 24
                    text2.setText("إصابة متوسطة");
                } else {
                    text2.setText("إصابة خفيفة");
                }
            }
        }

        button4.setDisable(true);
        totalIntensity = 0;
        actionEvent.consume();
    }

    //cut image
    @FXML
    protected void onActionButton7(ActionEvent actionEvent) {
        Image image = imageView1.getImage();
        PixelReader pixelReader = image.getPixelReader();
        WritableImage writableImage = new WritableImage((int) image.getWidth(), (int) image.getHeight());
        PixelWriter pixelWriter = writableImage.getPixelWriter();
        for (int x = 0; x < (int) image.getWidth(); x++) {
            for (int y = 0; y < (int) image.getHeight(); y++) {

                if ((x <= xMouse + imageRadiusWidth && x >= xMouse - imageRadiusWidth) &&
                        (y <= yMouse + imageRadiusHeight && y >= yMouse - imageRadiusHeight)) {
                    Color color = pixelReader.getColor(x, y);
                    pixelWriter.setColor(x, y, color);
                }
            }
        }
        rectangle.setVisible(false);
        imageView1.setImage(writableImage);
        actionEvent.consume();
    }

    // color image
    @FXML
    protected void onActionButton8(ActionEvent actionEvent) {
        Image image = imageView1.getImage();
        PixelReader pixelReader = image.getPixelReader();
        WritableImage writableImage = new WritableImage((int) image.getWidth(), (int) image.getHeight());
        PixelWriter pixelWriter = writableImage.getPixelWriter();

        for (int x = 0; x < (int) image.getWidth(); x++) {
            for (int y = 0; y < (int) image.getHeight(); y++) {

                Color pixelColorRBG = pixelReader.getColor(x, y);

                Color color = pixelColorRBG;

                if ((x <= xMouse + imageRadiusWidth && x >= xMouse - imageRadiusWidth) &&
                        (y <= yMouse + imageRadiusHeight && y >= yMouse - imageRadiusHeight)) {
                    double intensity = (pixelColorRBG.getRed() + pixelColorRBG.getGreen() + pixelColorRBG.getBlue()) / 3;
                    color = colors[(int) (intensity * 9)];
                    totalIntensity += intensity;
                }

                pixelWriter.setColor(x, y, color);
            }
        }
        imageView1.setImage(writableImage);

        actionEvent.consume();
    }


    @FXML
    protected void onMousePressedImageView1(MouseEvent mouseEvent) {
        polyline.getPoints().clear();
        polyline.setVisible(true);
        polyline.getPoints().addAll(mouseEvent.getX() - 60, mouseEvent.getY() - 50);
//        polyline.getPoints().addAll(mouseEvent.getX(), mouseEvent.getY());
        mouseEvent.consume();
    }

    @FXML
    protected void onMouseDraggedImageView1(MouseEvent mouseEvent) {
        if (imageView1.getFitHeight() > mouseEvent.getY() && 0 <= mouseEvent.getY() && imageView1.getFitWidth() > mouseEvent.getX() && 0 <= mouseEvent.getX()) {
            polyline.getPoints().addAll(mouseEvent.getX() - 60, mouseEvent.getY() - 50);
//            polyline.getPoints().addAll(mouseEvent.getX(), mouseEvent.getY());
        }
        mouseEvent.consume();
    }

    @FXML
    protected void onMouseReleasedImageView1(MouseEvent mouseEvent) {

        Image image = imageView1.getImage();

        System.out.println("hi");
        int minx = polyline.getPoints().get(0).intValue();
        int maxx = polyline.getPoints().get(0).intValue();
        int miny = polyline.getPoints().get(1).intValue();
        int maxy = polyline.getPoints().get(1).intValue();

        for (int i = 0; i < polyline.getPoints().size(); i++) {
            int val = polyline.getPoints().get(i).intValue();
            if (i % 2 == 0) {
                if (val < minx) {
                    minx = val;
                }
                if (val > maxx) {
                    maxx = val;
                }
            } else {
                if (val < miny) {
                    miny = val;
                }
                if (val > maxy) {
                    maxy = val;
                }
            }
//            System.out.println(i + " " + val);
        }

        int radius1 = (maxx - minx) / 2;
        int radius2 = (maxy - miny) / 2;

//        xMouse = maxx + 60 - imageRadiusWidth;
//        yMouse = maxy + 50 - imageRadiusHeight;

        imageRadiusWidth = (int) (radius1 * imageView1.getImage().getWidth() / imageView1.getFitWidth());
        imageRadiusHeight = (int) (radius2 * imageView1.getImage().getHeight() / imageView1.getFitHeight());

        xMouse = (int) ((maxx + 60 - radius1) * image.getWidth() / imageView1.getFitWidth());
        yMouse = (int) ((maxy + 50 - radius2) * image.getHeight() / imageView1.getFitHeight());

        System.out.println(maxx);
        System.out.println(maxy);
        System.out.println(imageRadiusWidth);
        System.out.println(imageRadiusHeight);
        System.out.println(xMouse);
        System.out.println(yMouse);


        rectangle.setVisible(true);

        rectangle.setWidth(radius1 * 2);
        rectangle.setHeight(radius2 * 2);

        rectangle.setX(((maxx + 60 - radius1) - radius1));
        rectangle.setY(((maxy + 50 - radius2) - radius2));

        polyline.setVisible(false);
        rectangle.setVisible(true);

        button2.setVisible(true);
        button4.setVisible(true);
        button4.setDisable(false);
        button7.setVisible(true);
        button8.setVisible(true);

    }

    // raising quality of image
    @FXML
    protected void onActionButton9(ActionEvent actionEvent) {

        Image image = imageView1.getImage();


//        PixelReader pixelReader = image.getPixelReader();
//        WritableImage grayImage = new WritableImage((int) image.getWidth(), (int) image.getHeight());
//        PixelWriter pixelWriter = grayImage.getPixelWriter();
//
//        // تحويل الصورة إلى اللون الرمادي
//        for (int y = 0; y < (int) image.getHeight(); y++) {
//            for (int x = 0; x < (int) image.getWidth(); x++) {
//                Color color = pixelReader.getColor(x, y);
//                double gray = 0.2125 * color.getRed() + 0.7154 * color.getGreen() + 0.0721 * color.getBlue();
//                pixelWriter.setColor(x, y, new Color(gray, gray, gray, color.getOpacity()));
//            }
//        }
//
////        تهيئة المصفوفة لتحويل فورييه
////        int width = (int) grayImage.getWidth();
////        int height = (int) grayImage.getHeight();
//
//
//        int width = (int) image.getWidth();
//        int height = (int) image.getHeight();
//
//        double[][] complexImage = new double[height][2 * width];
//        PixelReader grayPixelReader = image.getPixelReader();
//
//        // تعبئة المصفوفة بقيم السطوع للصورة الرمادية
//        for (int y = 0; y < height; y++) {
//            for (int x = 0; x < width; x++) {
//                Color color = grayPixelReader.getColor(x, y);
//                complexImage[y][2 * x] = color.getBrightness();
//                // تعيين الجزء الخيالي إلى 0
////                complexImage[y][2 * x + 1] = 0.0;
//            }
//        }
//
//        // تطبيق تحويل فورييه
//        DoubleFFT_2D fft = new DoubleFFT_2D(height, width);
//        fft.realForwardFull(complexImage);
//
//        // تطبيق الفلتر العالي التردد
////        applyHighPassFilter(complexImage, 90);
//        applyHighPass(complexImage, width, height);
//
//        // تطبيق تحويل فورييه العكسي
//        fft.complexInverse(complexImage, true);
//
//        // إنشاء الصورة النهائية من المصفوفة
//        WritableImage toImage = new WritableImage(width, height);
//        PixelWriter toImagePixelWriter = toImage.getPixelWriter();
//
//        for (int y = 0; y < height; y++) {
//            for (int x = 0; x < width; x++) {
//                double value = complexImage[y][2 * x];
//                value = (value + 1) / 2; // تطبيع القيمة
//                if (value > 1.0) {
//                    value = 1;
//                }
//                toImagePixelWriter.setColor(x, y, new Color(value, value, value, 1.0));
//            }
//        }
//
//        // تحديث الصورة في ImageView
//        imageView1.setImage(toImage);

        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);

        float[] kernel = {
                -1 / 9f, 3 / 9f, -1 / 9f,
                -1 / 9f, 8 / 9f, -1 / 9f,
                -1 / 9f, 3 / 9f, -1 / 9f,
        };

        BufferedImageOp highPass = new ConvolveOp(new Kernel(3, 3, kernel), ConvolveOp.EDGE_NO_OP, null);

        BufferedImage highPassImage = highPass.filter(bufferedImage, null);

        Image image1 = SwingFXUtils.toFXImage(highPassImage, null);

        imageView1.setImage(image1);

        button9.setDisable(true);

    }

//    private void applyHighPass(double[][] channel, int width, int height) {
//        // تحديد القيمة العتبية للترددات التي يجب حجبها
//        double threshold = 0.1; // يمكن تعديل هذه القيمة حسب الحاجة
//
//        for (int y = 0; y < height; y++) {
//            for (int x = 0; x < width; x++) {
//                // حساب الترددات
//                double distance = Math.sqrt((y - height / 2) * (y - height / 2) + (x - width / 2) * (x - width / 2));
//                if (distance < threshold * Math.max(width, height)) {
//                    channel[y][2 * x] = 0;
//                    channel[y][2 * x + 1] = 0;
//                }
//            }
//        }
//    }

    /////|
//    private void applyHighPassFilter(double[][] complexImage, double cutoff) {
//        int width = complexImage[0].length / 2;
//        int height = complexImage.length;
//        int centerX = width / 2;
//        int centerY = height / 2;
//
//        for (int y = 0; y < height; y++) {
//            for (int x = 0; x < width; x++) {
//                double distance = Math.sqrt((x - centerX) * (x - centerX) + (y - centerY) * (y - centerY));
//                if (distance < cutoff) {
//                    complexImage[y][2 * x] = 0.0; // الجزء الحقيقي
//                    complexImage[y][2 * x + 1] = 0.0; // الجزء الخيالي
//                }
//            }
//        }
//    }

    public void onActionButton10(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("report-view.fxml"));
            Parent reportView = loader.load();
            Scene reportScene = new Scene(reportView);

            ReportController reportController = loader.getController();
            reportController.receiveImage(imageView1.getImage());

            Stage newStage = new Stage();

            newStage.initModality(Modality.NONE);
            newStage.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
            newStage.setTitle("Add Report");
            newStage.setScene(reportScene);
            newStage.show();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}