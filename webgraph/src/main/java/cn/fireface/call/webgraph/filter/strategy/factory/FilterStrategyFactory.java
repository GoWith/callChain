package cn.fireface.call.webgraph.filter.strategy.factory;

import cn.fireface.call.webgraph.filter.strategy.FilterStrategy;

import java.io.File;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by maoyi on 2018/10/30.
 * don't worry , be happy
 */
public class FilterStrategyFactory {

    private FilterStrategyFactory() {
        throw new IllegalStateException();
    }

    private static List<Class<? extends FilterStrategy>> strategies = new ArrayList<>();
    private static Map<String,Class<? extends FilterStrategy>> strategyPool = new HashMap<>();

    private static void initFromJar(URL resource,String pkgPath) throws Exception {
        URLConnection urlConnection = resource.openConnection();
        JarURLConnection result = (JarURLConnection) urlConnection;
        JarFile jarFile = ((JarURLConnection) urlConnection).getJarFile();
        JarInputStream jarInputStream = new JarInputStream(result.getJarFileURL().openConnection().getInputStream());

        JarEntry entry;
        while ((entry = jarInputStream.getNextJarEntry()) != null) {
            if(!entry.isDirectory()) {
                String entryName = entry.getName();
                if (entryName.startsWith(pkgPath)) {
                    Class<?> aClass = Class.forName(entryName.substring(0, entryName.length() - 6).replace('/','.'));
                    if (aClass.equals(FilterStrategy.class) || !FilterStrategy.class.isAssignableFrom(aClass)){
                        continue;
                    }
                    strategies.add((Class<? extends FilterStrategy>) aClass);
                    strategyPool.put(aClass.getSimpleName().substring(0,aClass.getSimpleName().length()-14).toLowerCase(),(Class<? extends FilterStrategy>) aClass);
                }
            }
        }
    }
    private static void initFromFile(URL resource,String pkgName) throws Exception {
        String file = resource.getFile();
        File file1 = new File(file);
        File[] files = file1.listFiles();
        for (File file2 : files) {
            if (!file2.isDirectory()) {
                String name1 = file2.getName();
                Class<?> aClass = Class.forName(pkgName + "." + name1.substring(0, name1.length() - 6));
                if (aClass.equals(FilterStrategy.class) || !FilterStrategy.class.isAssignableFrom(aClass)){
                    continue;
                }
                strategies.add((Class<? extends FilterStrategy>) aClass);
                strategyPool.put(name1.substring(0,name1.length()-20).toLowerCase(),(Class<? extends FilterStrategy>) aClass);
            }
        }
    }

    static {
        try {
            String pkgName = FilterStrategy.class.getPackage().getName();
            String pkgPath = pkgName.replace(".", "/");
            URL resource = Thread.currentThread().getContextClassLoader().getResource(pkgPath);
            String protocol = resource.getProtocol();
            if (protocol.equalsIgnoreCase("jar")) {
                initFromJar(resource,pkgPath);
            }else if(protocol.equalsIgnoreCase("file")){
                initFromFile(resource,pkgPath);
            }
        } catch (Exception e) {
            Logger.getLogger("context").log(Level.INFO,"init strategy error:",e);
        }
    }

    public static FilterStrategy produce(String describe){
        try {
            String substring = describe.substring(describe.lastIndexOf('/')+1,describe.indexOf(".json")).toLowerCase();
            return strategyPool.get(substring).newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            Logger.getLogger("context").log(Level.INFO,"init strategy error:",e);
            return null;
        }
    }
}
