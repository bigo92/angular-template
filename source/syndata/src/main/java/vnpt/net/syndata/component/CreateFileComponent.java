package vnpt.net.syndata.component;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

@Component
public class CreateFileComponent {

    public String OpenFile(String path) throws Exception {
        BufferedReader fileReader = null;
        try {
            fileReader = new BufferedReader(new FileReader(path));
            //return fileReader.readLine();

            // buffer for storing file contents in memory
            StringBuffer stringBuffer = new StringBuffer("");
            // for reading one line
            String line = null;
            // keep reading till readLine returns null
            while ((line = fileReader.readLine()) != null) {
                // keep appending last line read to buffer
                stringBuffer.append(line);
            } 
            return stringBuffer.toString();
            
        } finally {
            if (fileReader != null) {
                try {
                    fileReader.close();
                } catch (IOException e) {
                    throw new Exception(e.getMessage(), e.getCause());
                }
            }
        }
    }

    public void SaveFile(byte[] source, String fileName, String output) throws Exception {
        FileOutputStream file = null;
        try {
            file = new FileOutputStream(output + fileName);
            StreamUtils.copy(source, file);
        } finally {
            try {
                file.close();
            } catch (IOException e) {
                throw new Exception(e.getMessage(), e.getCause());
            }
        }
    }

    public long getCRC32Checksum(byte[] bytes) {
        Checksum crc32 = new CRC32();
        crc32.update(bytes, 0, bytes.length);
        return crc32.getValue();
    }

    /*
    * Hàm lấy phần Name tĩnh của file
    */
    public String keyMathFile(String format){
        String[] receiveFile = format.split("_");
        for (String item : receiveFile) {
            if (!item.startsWith("\\$\\{")) {
                return item;
            }
        }
        return null;
    }

    public String getExtention(String format){
        return format.substring(format.indexOf("."));
    }

    public String getValueByFormat(String key, String source, String format){
        String extentionFile = getExtention(format);
        String[] sourceProperty = source.replaceAll(extentionFile, "").split("_");
        String[] formatProperty = format.replaceAll(extentionFile, "").split("_");
        int index = -1;
        for (int i = 0; i < formatProperty.length; i++) {
            if (formatProperty[i].equals(key)) {
                index = i;
                break;
            }
        }
        if (index != -1) {
            return sourceProperty[index];
        }
        return null;
    }
}