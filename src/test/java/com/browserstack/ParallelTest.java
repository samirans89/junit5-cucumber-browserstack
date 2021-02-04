package com.browserstack;

import io.cucumber.core.cli.Main;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

public class ParallelTest {

    public static ThreadLocal<JSONObject> threadLocalValue = new ThreadLocal<>();

    public static void main(String args[]) throws IOException, ParseException {
        JSONObject config;
        JSONParser parser = new JSONParser();
        if(System.getenv("caps")!= null) {
            config = (JSONObject) parser.parse(System.getenv("caps"));
        } else if(System.getProperty("config") != null) {
            config = (JSONObject) parser.parse(new FileReader(String.format("src/test/resources/config/%s", System.getProperty("config"))));
        } else {
            config = (JSONObject) parser.parse(new FileReader("src/test/resources/config/parallel.config.json"));
        }
        JSONArray environments = (JSONArray)config.get("environments");
        System.out.println(config.toJSONString());
        for (Object obj: environments) {
            Map<String, String> envCapabilities = (Map<String, String>) obj;
            JSONObject capabilities = new JSONObject();
            Iterator it = envCapabilities.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry)it.next();
                capabilities.put(pair.getKey().toString(), pair.getValue().toString());
            }
            Map<String, String> commonCapabilities = (Map<String, String>) config.get("capabilities");
            it = commonCapabilities.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry)it.next();
                if(capabilities.get(pair.getKey().toString()) == null){
                    capabilities.put(pair.getKey().toString(), pair.getValue().toString());
                }
            }
            JSONObject singleConfig = new JSONObject();
            singleConfig.put("user",config.get("user"));
            singleConfig.put("key",config.get("key"));
            singleConfig.put("capabilities",capabilities);
            System.out.println(singleConfig.toJSONString());
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    System.setProperty("parallel","true");
                    threadLocalValue.set(singleConfig);
                    try {
                        String[] argv = new String[]{"-g", "", "src/test/resources/com/browserstack"};
                        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
                        Main.run(argv, contextClassLoader);
                    } catch(Exception e) {
                        e.getStackTrace();
                    } finally {
                        threadLocalValue.remove();
                    }
                }
            });
            thread.start();
        }
    }

}
