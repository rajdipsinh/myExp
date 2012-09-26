import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: rajdip
 * Date: 25/09/12
 * Time: 12:39
 * To change this template use File | Settings | File Templates.
 */
public class MachoParsing {

    public static List<Integer> PARREADER_ARCH = new ArrayList<Integer>();
    public static List<Integer> ADFFMPEG_ARCH = new ArrayList<Integer>();


    public static enum DATA_MODEL {
        DATA_MODEL32(32),
        DATA_MODEL64(64);

        private final int dataModel;

        DATA_MODEL(int i) {
            this.dataModel = i;
        }

    }

    public static enum MAC_CPU_TYPE {
        CPU_TYPE_I386(0x00000007),
        CPU_TYPE_I386_FAT(0x07000000),
        CPU_TYPE_X86_64(0x01000007),
        CPU_TYPE_X86_64_FAT(0x07000001),
        CPU_TYPE_POWERPC(0x12000000);

        private final int cpuType;

        MAC_CPU_TYPE(int i) {
            this.cpuType = i;
        }

        public int getCpuType() {
            return this.cpuType;
        }
    }

    public static enum MAC_CPU_SUBTYPE {
        CPU_SUBTYPE_X86_64_ALL(0x00000003),
        CPU_SUBTYPE_X86_ALL(0x03000000),
        CPU_SUBTYPE_POWERPC_ALL(0x00000000);

        private final int cpuSubType;

        MAC_CPU_SUBTYPE(int i) {
            this.cpuSubType = i;
        }

        public int getCpuType() {
            return this.cpuSubType;
        }
    }

    public static enum MAC_MAGIC_NUMBER {
        UNKNOWN(-1),
        FAT_CIGAM(0xBEBAFECA),
        FAT_MAGIC(0xFEEDFACE),
        MH_MAGIC_64(0xFEEDFACF),
        PPC(0xCEFAEDFE);

        private final int magicNumber;

        private MAC_MAGIC_NUMBER(int i) {
            this.magicNumber = i;
        }

        public int getMagicNumber() {
            return this.magicNumber;
        }
    }

    public static void main(String args[]) throws IOException {
        //magic Number
        //final int FAT_CIGAM = 0xBEBAFECA;
        //final int FAT_MAGIC = 0xFEEDFACE;
        //final int MH_MAGIC_64 = 0xFEEDFACF;
        //final int PPC = 0xCEFAEDFE;

        //CPU Types
        //final int CPU_TYPE_I386 = 0x07000000;
        //final int CPU_TYPE_X86_64 = 0x07000001;
        //final int CPU_TYPE_POWERPC = 0x12000000;

        //CPU SubTypes
        //final int CPU_SUBTYPE_X86_64_ALL = 0x00000003;
        //final int CPU_SUBTYPE_X86_ALL = 0x03000000;
        //final int CPU_SUBTYPE_POWERPC_ALL = 0x00000000;
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
            int ct;
            int cpuSubTypeTemp;
            System.out.println("Magic : " + header);

            switch (getMac_magic_number(header)) {
                case FAT_CIGAM:
                    System.out.println("is FAT_CIGAM");
                    byte[] numberOfArch = new byte[4];
                    mbb.get(numberOfArch);
                    System.out.println("Number Of Arch: " + byteToInt(numberOfArch));// /*+ mbb.getInt());//*/ + numberOfArch[0] + numberOfArch[1] + numberOfArch[2] + numberOfArch[3]);
                    for (int i = 0; i < byteToInt(numberOfArch); i++) {
                        //mbb.get(cpuType);
                        ct = mbb.getInt();
                        System.out.println("CPU TYPE: " + ct);
                        if (ct == MAC_CPU_TYPE.CPU_TYPE_I386_FAT.getCpuType() ||
                                ct == MAC_CPU_TYPE.CPU_TYPE_POWERPC.getCpuType()) {
                            ADFFMPEG_ARCH.add(DATA_MODEL.DATA_MODEL32.dataModel);
                        } else if (ct == MAC_CPU_TYPE.CPU_TYPE_X86_64_FAT.getCpuType()) {
                            ADFFMPEG_ARCH.add(DATA_MODEL.DATA_MODEL64.dataModel);
                        }
                        cpuSubTypeTemp = mbb.getInt();
                        System.out.println("CPU SUB Type: " + cpuSubTypeTemp);//cpuSubType[0]+":"+cpuSubType[1]+":"+cpuSubType[2]+":"+cpuSubType[3]);
                        //mbb.position(mbb.position()+ 3);
                        System.out.println("Offset: " + mbb.getInt());
                        System.out.println("Size: " + mbb.getInt());
                        System.out.println("align: " + mbb.getInt());
                        System.out.println("===========================\n");

                    }
                    break;
                case FAT_MAGIC:
                    System.out.println("is FAT_MAGIC");
                    ct = mbb.getInt();
                    System.out.println("CPU TYPE: " + ct);
                    if (ct == MAC_CPU_TYPE.CPU_TYPE_I386.getCpuType() ||
                            ct == MAC_CPU_TYPE.CPU_TYPE_POWERPC.getCpuType()) {
                        ADFFMPEG_ARCH.add(DATA_MODEL.DATA_MODEL32.dataModel);
                    } else if (ct == MAC_CPU_TYPE.CPU_TYPE_X86_64.getCpuType()) {
                        ADFFMPEG_ARCH.add(DATA_MODEL.DATA_MODEL64.dataModel);
                    }
                    cpuSubTypeTemp = mbb.getInt();
                    System.out.println("CPU SUB Type: " + cpuSubTypeTemp);//cpuSubType[0]+":"+cpuSubType[1]+":"+cpuSubType[2]+":"+cpuSubType[3]);
                    //mbb.position(mbb.position()+ 3);
                    System.out.println("Offset: " + mbb.getInt());
                    System.out.println("Size: " + mbb.getInt());
                    System.out.println("align: " + mbb.getInt());
                    System.out.println("===========================\n");

                    break;
                case MH_MAGIC_64:
                    System.out.println("is MH_MAGIC_64");
                    ct = mbb.getInt();
                    System.out.println("CPU TYPE: " + ct);
                    if (ct == MAC_CPU_TYPE.CPU_TYPE_I386.getCpuType() ||
                            ct == MAC_CPU_TYPE.CPU_TYPE_POWERPC.getCpuType()) {
                        ADFFMPEG_ARCH.add(DATA_MODEL.DATA_MODEL32.dataModel);
                    } else if (ct == MAC_CPU_TYPE.CPU_TYPE_X86_64.getCpuType()) {
                        ADFFMPEG_ARCH.add(DATA_MODEL.DATA_MODEL64.dataModel);
                    }
                    cpuSubTypeTemp = mbb.getInt();
                    System.out.println("CPU SUB Type: " + cpuSubTypeTemp);//cpuSubType[0]+":"+cpuSubType[1]+":"+cpuSubType[2]+":"+cpuSubType[3]);
                    //mbb.position(mbb.position()+ 3);
                    System.out.println("Offset: " + mbb.getInt());
                    System.out.println("Size: " + mbb.getInt());
                    System.out.println("align: " + mbb.getInt());
                    System.out.println("===========================\n");
                    break;
                case PPC:
                    System.out.println("is PPC");
                    ct = mbb.getInt();
                    System.out.println("CPU TYPE: " + ct);
                    if (ct == MAC_CPU_TYPE.CPU_TYPE_I386.getCpuType() ||
                            ct == MAC_CPU_TYPE.CPU_TYPE_POWERPC.getCpuType()) {
                        ADFFMPEG_ARCH.add(DATA_MODEL.DATA_MODEL32.dataModel);
                    } else if (ct == MAC_CPU_TYPE.CPU_TYPE_X86_64.getCpuType()) {
                        ADFFMPEG_ARCH.add(DATA_MODEL.DATA_MODEL64.dataModel);
                    }
                    cpuSubTypeTemp = mbb.getInt();
                    System.out.println("CPU SUB Type: " + cpuSubTypeTemp);//cpuSubType[0]+":"+cpuSubType[1]+":"+cpuSubType[2]+":"+cpuSubType[3]);
                    //mbb.position(mbb.position()+ 3);
                    System.out.println("Offset: " + mbb.getInt());
                    System.out.println("Size: " + mbb.getInt());
                    System.out.println("align: " + mbb.getInt());
                    System.out.println("===========================\n");
                    break;
                case UNKNOWN:
                default:
                    System.out.println("Unknown Magic Number: " + header);
            }


            System.out.println("CPU_TYPE_I386: " + MAC_CPU_TYPE.CPU_TYPE_I386.getCpuType());
            System.out.println("CPU_TYPE_I386_FAT: " + MAC_CPU_TYPE.CPU_TYPE_I386_FAT.getCpuType());
            System.out.println("CPU_TYPE_X86_64: " + MAC_CPU_TYPE.CPU_TYPE_X86_64.getCpuType());
            System.out.println("CPU_TYPE_X86_64_FAT: " + MAC_CPU_TYPE.CPU_TYPE_X86_64_FAT.getCpuType());
            System.out.println("CPU_TYPE_POWERPC: " + MAC_CPU_TYPE.CPU_TYPE_POWERPC.getCpuType());
            System.out.println("Size: " + ADFFMPEG_ARCH.size());

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

    public static MAC_MAGIC_NUMBER getMac_magic_number(int magicNumber) {
        for (int i = 0; i < MAC_MAGIC_NUMBER.values().length; i++) {
            if (MAC_MAGIC_NUMBER.values()[i].magicNumber == magicNumber) {
                return MAC_MAGIC_NUMBER.values()[i];
            }
        }
        return MAC_MAGIC_NUMBER.UNKNOWN;
    }
}
