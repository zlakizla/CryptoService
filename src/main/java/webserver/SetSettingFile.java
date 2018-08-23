package webserver;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

public class SetSettingFile {
    static Integer SERVER_PORT;
    static String SERVER_BIND;
    static ArrayList<String> WHITE_LIST = new ArrayList<>();

    /**
     * Create setting file if not exist and read file.
     * @throws Exception
     */
    public void SettingFile() throws Exception {
        File setting = new File("setting.json");

        if (!setting.exists()) {
            JSONObject jsonObject = new JSONObject();
            FileWriter fileWriter = new FileWriter("setting.json");

            ArrayList<String> arrayList = new ArrayList<>();
            jsonObject.put("bind", "127.0.0.1");
            arrayList.add("127.0.0.1");
            jsonObject.put("port", "8181");
            jsonObject.put("ip", arrayList);
            fileWriter.write(jsonObject.toJSONString());
            fileWriter.flush();
            fileWriter.close();
        }

        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(setting));
            String line;
            String fileSetting = "";

            while ((line = bufferedReader.readLine()) != null) {
                fileSetting += line;
            }
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(fileSetting);
            SERVER_BIND = jsonObject.get("bind").toString();
            SERVER_PORT = Integer.parseInt(jsonObject.get("port").toString());
            JSONArray jsonArray = (JSONArray) jsonObject.get("ip");

            for (Object aJsonArray : jsonArray) {
                WHITE_LIST.add(aJsonArray.toString());
            }


        } catch (Exception e) {
            throw new Exception(e);
        }
    }
}
