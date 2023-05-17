package cn.fireface.call.webgraph.filter;

import java.io.*;

/**
 * 工具类
 * Created by maoyi on 2018/5/4.
 * don't worry , be happy
 *
 * @author maoyi
 * @date 2023/05/17
 */
public class Utils {

    /**
     * 默认缓冲区大小
     */
    public final static int DEFAULT_BUFFER_SIZE = 1024 * 4;


    /**
     * 读
     *
     * @param in 在
     * @return {@link String}
     */
    public static String read(InputStream in) {
        InputStreamReader reader;
        try {
            reader = new InputStreamReader(in, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
        return read(reader);
    }

    /**
     * 读
     *
     * @param reader 读者
     * @return {@link String}
     */
    public static String read(Reader reader) {
        try {

            StringWriter writer = new StringWriter();

            char[] buffer = new char[DEFAULT_BUFFER_SIZE];
            int n = 0;
            while (-1 != (n = reader.read(buffer))) {
                writer.write(buffer, 0, n);
            }

            return writer.toString();
        } catch (IOException ex) {
            throw new IllegalStateException("read error", ex);
        }
    }

    /**
     * 从资源读取字节数组
     *
     * @param resource 资源
     * @return {@link byte[]}
     * @throws IOException ioexception
     */
    public static byte[] readByteArrayFromResource(String resource) throws IOException {
        InputStream in = null;
        try {
            in = Thread.currentThread().getContextClassLoader().getResourceAsStream(resource);
            if (in == null) {
                return null;
            }

            return readByteArray(in);
        } finally {
            in.close();
        }
    }

    /**
     * 读取资源
     *
     * @param resource 资源
     * @return {@link String}
     * @throws IOException ioexception
     */
    public static String readFromResource(String resource) throws IOException {
        InputStream in = null;
        try {
            in = Thread.currentThread().getContextClassLoader().getResourceAsStream(resource);
            if (in == null) {
                in = Utils.class.getResourceAsStream(resource);
            }

            if (in == null) {
                return null;
            }

            String text = Utils.read(in);
            return text;
        } finally {
            in.close();
        }
    }

    /**
     * 读取字节数组
     *
     * @param input 输入
     * @return {@link byte[]}
     * @throws IOException ioexception
     */
    public static byte[] readByteArray(InputStream input) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        copy(input, output);
        return output.toByteArray();
    }

    /**
     * 复制
     *
     * @param input  输入
     * @param output 输出
     * @return long
     * @throws IOException ioexception
     */
    public static long copy(InputStream input, OutputStream output) throws IOException {
        final int EOF = -1;

        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];

        long count = 0;
        int n = 0;
        while (EOF != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
            count += n;
        }
        return count;
    }
}
