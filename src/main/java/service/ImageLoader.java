package service;

import javax.swing.*;
import java.io.File;

public class ImageLoader {

    /**
     * Loads an image based on the file path
     *
     * @param filePath The file path of the image
     * @return The ImageIcon of the image
     */
    public static ImageIcon loadCardImage(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            return new ImageIcon(filePath);
        } else {
            System.err.println("Card image not found: " + filePath);
            return null;
        }
    }
}
