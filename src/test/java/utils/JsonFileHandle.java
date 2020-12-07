package utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import com.google.gson.Gson;

public class JsonFileHandle {
	
	public  <T> List<T> parseJSONToListObject(String filePath, Class<T[]> classObject) throws IOException {
		BufferedReader reader = null;
		T[] listObjects = null;
		try {
			reader = new BufferedReader(new FileReader(filePath));
			listObjects = new Gson().fromJson(reader, classObject);
		} finally {
			if (reader != null) reader.close();
		}
		return Arrays.asList(listObjects);
	}
	
	public static <T> List<T> mapObjectFromJSON(String jsonPath) {
		T[] listObjects = null;
		return Arrays.asList(listObjects);
	}

}
