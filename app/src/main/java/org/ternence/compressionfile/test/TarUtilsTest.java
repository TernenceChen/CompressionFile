package org.ternence.compressionfile.test;

import android.os.Environment;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;

import org.ternence.compressionfile.TarUtils;


/**
 * Tar测试
 *
 * @author <a href="mailto:ternence.c@gmail.com">Ternence</a>
 */
public class TarUtilsTest {
    private String inputStr;
    private String name = Environment.getExternalStorageDirectory().getPath() + File.separator + "11" + File.separator;

    public void before() {
        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" encoding=\"utf-8\" ?>");
        sb.append("\r\n");
        sb.append("<dataGroup>");
        sb.append("\r\n\t");
        sb.append("<dataItem>");
        sb.append("\r\n\t\t");
        sb.append("<data>");
        sb.append("Test");
        sb.append("</data>");
        sb.append("\r\n\t");
        sb.append("<dataItem>");
        sb.append("\r\n");
        sb.append("</dataGroup>");

        inputStr = sb.toString();
    }

    public void testArchiveFile() throws Exception {

        byte[] contentOfEntry = inputStr.getBytes();

        String path = "d:/" + name;

//        FileOutputStream fos = new FileOutputStream(path);
//
//        fos.write(contentOfEntry);
//        fos.flush();
//        fos.close();

        TarUtils.archive(path);

        TarUtils.dearchive(path + ".tar");

        File file = new File(path);

        FileInputStream fis = new FileInputStream(file);

        DataInputStream dis = new DataInputStream(fis);

        byte[] data = new byte[(int) file.length()];

        dis.readFully(data);

        fis.close();

        String outputStr = new String(data);
//        assertEquals(inputStr, outputStr);

    }

    public void testArchiveDir() throws Exception {
        String path = "d:/fd";
        TarUtils.archive(path);

        TarUtils.dearchive(path + ".tar", "d:/fds");
    }

}