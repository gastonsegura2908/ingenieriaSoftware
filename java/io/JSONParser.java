package io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import constants.Constants;

import javax.swing.*;

/**
 * Esta clase sirve para almacenar los datos en el disco de la computadora
 */
public class JSONParser {

    /**
     * metodo que lee los datos del archivo que estan en el disco
     *
     * @return datalist lista de objetos que contiene el score y la fecha correspondiente
     */
    public static ArrayList<ScoreData> readFile() throws FileNotFoundException {
        ArrayList<ScoreData> dataList = new ArrayList<ScoreData>();

        File file = new File(Constants.SCORE_PATH);

        if (!file.exists() || file.length() == 0) {
            return dataList;
        }

        JSONTokener parser = new JSONTokener(new FileInputStream(file));
        JSONArray jsonList = new JSONArray(parser);

        try {
            for (int i = 0; i < jsonList.length(); i++) {
                JSONObject obj = (JSONObject) jsonList.get(i);
                ScoreData data = new ScoreData();
                data.setScore(obj.getInt("score"));
                data.setDate(obj.getString("date"));
                data.setName(obj.getString("name"));
                dataList.add(data);
            }
        } catch (JSONException e) {
            JOptionPane.showMessageDialog(null, "Json file corrupted, game data may be lost");
            try {
                JSONParser.writeFile(new ArrayList<>());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            e.printStackTrace();
        }
        return dataList;
    }

    /**
     * metodo que escribe los datos del archivo que estan en el disco
     *
     * @param dataList lista de objetos que quiero escribir
     */
    public static void writeFile(ArrayList<ScoreData> dataList) throws IOException {

        File outputFile = new File(Constants.SCORE_PATH);

        outputFile.getParentFile().mkdirs();
        outputFile.createNewFile();

        JSONArray jsonList = new JSONArray();

        for (ScoreData data : dataList) {

            JSONObject obj = new JSONObject();
            obj.put("score", data.getScore());
            obj.put("date", data.getDate());
            obj.put("name", data.getName());
            jsonList.put(obj);

        }

        BufferedWriter writer = Files.newBufferedWriter(Paths.get(outputFile.toURI()));
        jsonList.write(writer);
        writer.close();

    }

}
