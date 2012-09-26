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
public class MachoParsing {
    public static void main(String args[]) throws IOException {
        final int FAT_CIGAM = 0xBEBAFECA;
        final int FAT_MAGIC = 0xFEEDFACE;
        final int MH_MAGIC_64 = 0xFEEDFACF;
        final int CPU_TYPE_I386 = 0x00001B58;
        final int CPU_TYPE_X86_64 = 0x01000007;
        final int CPU_SUBTYPE_X86_64_ALL = 0x00000003;
        FileInputStream fis = null;
        MappedByteBuffer mbb;
        try {
            fis = new FileInputStream("/Users/rajdip/lib/ppc/libadffmpeg.jnilib");
            FileChannel fileChannel = fis.getChannel();
            mbb = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, fileChannel.size());
            System.out.println(mbb.order());
            mbb.order(ByteOrder.nativeOrder());
            mbb.position(0);
            int header = mbb.getInt();
            byte[] cpuType = new byte[4];
            byte[] cpuSubType = new byte[4];
            System.out.println("Magic : " + header);
            switch (header) {
                case FAT_CIGAM:
                    System.out.println("is FAT_CIGAM");
                    byte[] numberOfArch = new byte[4];
                    mbb.get(numberOfArch);
                    System.out.println("Number Of Arch: " + byteToInt(numberOfArch));// /*+ mbb.getInt());//*/ + numberOfArch[0] + numberOfArch[1] + numberOfArch[2] + numberOfArch[3]);
                    for (int i = 0; i < byteToInt(numberOfArch); i++) {
                        mbb.get(cpuType);
                        System.out.println("CPU TYPE: " + /*mbb.getInt());*/cpuType[0] + cpuType[1] + cpuType[2] + cpuType[3]);

                        //mbb.get(cpuSubType);
                        System.out.println("CPU SUB Type: " + mbb.getInt());//cpuSubType[0]+":"+cpuSubType[1]+":"+cpuSubType[2]+":"+cpuSubType[3]);
                    }
                    break;
                case FAT_MAGIC:
                    System.out.println("is FAT_MAGIC");

                    mbb.get(cpuType);
                    System.out.println("CPU TYPE: " + /*mbb.getInt());*/cpuType[0] + cpuType[1] + cpuType[2] + cpuType[3]);

                    //mbb.get(cpuSubType);
                    System.out.println("CPU SUB Type: " + mbb.getInt());//cpuSubType[0]+":"+cpuSubType[1]+":"+cpuSubType[2]+":"+cpuSubType[3]);
                    break;
                case MH_MAGIC_64:
                    System.out.println("is MH_MAGIC_64");
                    //mbb.get(cpuType);
                    System.out.println("CPU TYPE: " + mbb.getInt());//cpuType[0] + cpuType[1] + cpuType[2] + cpuType[3]);
                    //mbb.get(cpuSubType);
                    System.out.println("CPU SUB Type: " + mbb.getInt());//cpuSubType[0]+":"+cpuSubType[1]+":"+cpuSubType[2]+":"+cpuSubType[3]);
                    break;
            }

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

    public static int byteToInt(byte[] bytes) {
        return bytes[0] << 24 |
                bytes[1] << 16 |
                bytes[2] << 8 |
                bytes[3];
    }
}
