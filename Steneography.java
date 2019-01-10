import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class Steneography {
    public void encoding(String message, String file) throws IOException {
        byte bytesInput[] = message.getBytes();
        BufferedImage image;
        File file1;

        file1 = new File(file);
        image = ImageIO.read(file1);

        int messageSize = bytesInput.length; // length of message
        int p = image.getRGB(0, 0); // get the very first pixel

        int a = (p >> 24) & 0xff; // shift 24 bits to get a part that stands for transparency
        int r = (p >> 16) & 0xff; // shift 16 .... for red color
        int g = (p >> 8) & 0xff; // shift 8 .... for green color
        int b = p & 0xff; // get a part for a blue color


        a = ((a >> 2) << 2) | ((messageSize >> 6) & 3);
        r = ((r >> 2) << 2) | ((messageSize >> 4) & 3);
        g = ((g >> 2) << 2) | ((messageSize >> 2) & 3);
        b = ((b >> 2) << 2) | (messageSize & 3);
        p = (a << 24) | (r << 16) | (g << 8) | b; // put an image size into the first pixel

        image.setRGB(0, 0, p); // set the size into fitst byte

        int width = image.getWidth();
        int height = image.getHeight();

        int i = 0;

        for (int x = 0; x < width; x++) {

            int y = height - 2; // take two last to bits in each row
            while (y != height){
                p = image.getRGB(x, y);

                a = (p >> 24) & 0xff;
                r = (p >> 16) & 0xff;
                g = (p >> 8) & 0xff;
                b = p & 0xff;

                int intToEncode = bytesInput[i];
                a = ((a >> 2) << 2) | ((intToEncode >> 6) & 3);
                r = ((r >> 2) << 2) | ((intToEncode >> 4) & 3);
                g = ((g >> 2) << 2) | ((intToEncode >> 2) & 3);
                b = ((b >> 2) << 2) | (intToEncode & 3);
                p = (a << 24) | (r << 16) | (g << 8) | b;

                image.setRGB(x, y, p); // put a char from a message into the two last bits
                y++;
                i += 1;
            }
            if (i == messageSize)
                break;
        }
        ImageIO.write(image, "png", file1);
    }

    void decoding(String file) throws IOException {

        int messageSize;
        BufferedImage image;
        File file1;

        file1 = new File(file);
        image = ImageIO.read(file1);

        int p = image.getRGB(0, 0);
        int a = ((p >> 24) & 0xff) & 3;
        int r = ((p >> 16) & 0xff) & 3;
        int g = ((p >> 8) & 0xff) & 3;
        int b = (p & 0xff) & 3;

        messageSize = (a << 6) | (r << 4) | (g << 2) | b; // get a message size from a first pixel

        StringBuilder message = new StringBuilder();

        int i = 0;
        int width = image.getWidth();
        int height = image.getHeight();

        for (int x = 0; x < width; ++x) {

            int y = height - 2; // take two last to bits in each row
            while (y != height){

                p = image.getRGB(x, y);
                a = ((p >> 24) & 0xff) & 3;
                r = ((p >> 16) & 0xff) & 3;
                g = ((p >> 8) & 0xff) & 3;
                b = (p & 0xff) & 3;

                message.append((char) ((a << 6) | (r << 4) | (g << 2) | b)); // put an i-th char to the message string

                y++;
                i += 1;
            }
            if (i == messageSize) break;
        }
        System.out.println("Message is '" + message + "'");
    }
}

