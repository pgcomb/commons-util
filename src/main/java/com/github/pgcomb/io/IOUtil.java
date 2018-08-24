package com.github.pgcomb.io;

import java.io.*;
import java.util.function.Consumer;

/**
 * Title: IOUtil <br>
 * Description: IOUtil <br>
 * Date: 2018年08月07日
 *
 * @author 王东旭
 * @version 1.0.0
 * @since jdk8
 */
public class IOUtil {

    public static BufferedReader newBR(File file) throws FileNotFoundException {
        return new BufferedReader(new InputStreamReader(new FileInputStream(file)));
    }

    public static BufferedReader newBR(String file) throws FileNotFoundException {
        return newBR(new File(file));
    }

    public static BufferedWriter newBW(File file) throws FileNotFoundException {
        return new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
    }

    public static BufferedWriter newBW(String file) throws FileNotFoundException {
        return newBW(new File(file));
    }
    public static void readLine(String file, Consumer<String> consumer){
        String a;
        try {
            BufferedReader br = newBR(file);
            while ((a = br.readLine()) != null){
                consumer.accept(a);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
