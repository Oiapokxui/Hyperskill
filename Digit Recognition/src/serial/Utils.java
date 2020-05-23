package serial;
import java.io.*;

public class Utils {
    private static String fileName;

    public Utils (String fileName) throws
            IOException, ClassNotFoundException {
        this.fileName = fileName;
    }


    public static void serialize (Object object) throws IOException {
        try {
            File arquivo = new File(fileName);
            arquivo.createNewFile();
            FileOutputStream fos = new FileOutputStream(arquivo);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(object);
            oos.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
    public static Object deserialize () throws
            IOException, ClassNotFoundException
    {
        Object object = null;
        try {
            FileInputStream fis = new FileInputStream(fileName);
            BufferedInputStream bis = new BufferedInputStream(fis);
            ObjectInputStream ois = new ObjectInputStream(bis);
            object = ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return object;
    }
}
