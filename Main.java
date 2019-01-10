import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException{
        String file = ""; // set your path with a photo in a png format
        String message = ""; // enter a message for encoding and decoding it
        Steneography coding = new Steneography();
        coding.encoding(message, file); // encodes a given message into a png file
        coding.decoding(file); // just prints a decoded message
    }

}