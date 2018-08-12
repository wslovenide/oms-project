package com.ws.oms.image;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by gongmei on 2018/8/12.
 */
public class AsciiPic {

    public static void createAsciiPic(final String path) {
        String base = "#8XOHLTI)i=+;:,. ";// 字符串由复杂到简单
        //base = "@#&$%*o!;.";
        try {
            final BufferedImage image = ImageIO.read(new File(path));
            for (int y = 0; y < image.getHeight(); y+=2) {
                for (int x = 0; x < image.getWidth(); x+=1) {
                    final int pixel = image.getRGB(x, y);
                    final int r = (pixel & 0xff0000) >> 16, g = (pixel & 0xff00) >> 8, b = pixel & 0xff;
                    final float gray = 0.299f * r + 0.578f * g + 0.114f * b;
                    final int index = Math.round(gray * (base.length() + 1) / 255);
                    System.out.print(index >= base.length() ? " " : String.valueOf(base.charAt(index)));
                }
                System.out.print("\r\n");
            }
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * test
     *
     * @param args
     */
    public static void main(final String[] args) {
        AsciiPic.createAsciiPic("/Users/gongmei/Desktop/XRV雅韵金/IMG_4160_1.JPG");
    }


}
