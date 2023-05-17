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
 * 过滤策略工厂
 * Created by maoyi on 2018/10/30.
 * don't worry , be happy
 *
 * @author maoyi
 * @date 2023/05/17
 */
public class FilterStrategyFactory {

    /**
     * 过滤策略工厂
     */
    private FilterStrategyFactory() {
        throw new IllegalStateException();
    }

    /**
     * 策略
     */
    private static List<Class<? extends FilterStrategy>> strategies = new ArrayList<>();
    /**
     * 策略池
     */
    private static Map<String,Class<? extends FilterStrategy>> strategyPool = new HashMap<>();

    /**
     * init从罐子里
     *
     * @param resource 资源
     * @param pkgPath  pkg路径
     * @throws Exception 异常
     */
 private static void initFromJar(URL resource, String pkgPath) throws Exception {
    URLConnection urlConnection = resource.openConnection();
    JarURLConnection jarURLConnection = (JarURLConnection) urlConnection;
    JarFile jarFile = jarURLConnection.getJarFile();

    JarInputStream jarInputStream = new JarInputStream(jarURLConnection.getJarFileURL().openConnection().getInputStream());
    JarEntry entry;
    while ((entry = jarInputStream.getNextJarEntry()) != null) {
        if (entry.isDirectory()) {
            continue;
        }

        String entryName = entry.getName();
        if (!entryName.startsWith(pkgPath)) {
            continue;
        }

        String className = entryName.substring(0, entryName.length() - 6).replace('/', '.');
        Class<?> loadedClass = Class.forName(className);

        if (loadedClass.equals(FilterStrategy.class) || !FilterStrategy.class.isAssignableFrom(loadedClass)) {
            continue;
        }

        Class<? extends FilterStrategy> strategyClass = (Class<? extends FilterStrategy>) loadedClass;
        strategies.add(strategyClass);

        String strategyName = strategyClass.getSimpleName().substring(0, strategyClass.getSimpleName().length() - 14).toLowerCase();
        strategyPool.put(strategyName, strategyClass);
    }
}


    /**
     * init从文件
     *
     * @param resource 资源
     * @param pkgName  包裹名字
     * @throws Exception 异常
     */
    private static void initFromFile(URL resource, String pkgName) throws Exception {
        File file = new File(resource.toURI());
        File[] files = file.listFiles();
        for (File file2 : files) {
            if (file2.isDirectory()) {
                continue;
            }
            String name1 = file2.getName();
            if (!name1.endsWith(".class")) {
                continue;
            }
            Class<?> aClass = Class.forName(pkgName + "." + name1.substring(0, name1.length() - 6));
            if (FilterStrategy.class.isAssignableFrom(aClass) && !aClass.equals(FilterStrategy.class)) {
                strategies.add((Class<? extends FilterStrategy>) aClass);
                String key = name1.substring(0, name1.length() - 6).toLowerCase();
                strategyPool.put(key, (Class<? extends FilterStrategy>) aClass);
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

    /**
     * 生产
     *
     * @param describe 描述
     * @return {@link FilterStrategy}
     */
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
