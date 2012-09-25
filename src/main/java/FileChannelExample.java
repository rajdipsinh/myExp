import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created with IntelliJ IDEA.
 * User: rajdip
 * Date: 25/09/12
 * Time: 12:39
 * To change this template use File | Settings | File Templates.
 */
public class FileChannelExample {
    public static void main(String args[]) throws IOException {
        FileInputStream fis = null;
        MappedByteBuffer mbb ;
        try {
            fis = new FileInputStream("bash");
            FileChannel fileChannel = fis.getChannel();
            mbb = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, fileChannel.size());
            mbb.order(ByteOrder.nativeOrder());
            mbb.position(18);
            System.out.println(mbb.getShort());
            /*ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

            int bytes = fileChannel.read(byteBuffer);
            while (bytes != -1) {
                byteBuffer.flip();
                while (byteBuffer.hasRemaining()) {
                    System.out.print((char) byteBuffer.get());
                }
                byteBuffer.clear();
                bytes = fileChannel.read(byteBuffer);
            }
            if (fis != null) {
                fis.close();
            }*/
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                fis.close();
            }
        }
    }
}
