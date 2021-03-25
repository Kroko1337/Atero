package de.verschwiegener.atero.audio.ilovemusik;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.IOUtils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;

import de.verschwiegener.atero.Management;
import de.verschwiegener.atero.audio.Stream;
import de.verschwiegener.atero.audio.Stream.Provider;
import de.verschwiegener.atero.audio.StreamLoader;
import net.sf.json.JSON;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

public class IloveMusikStreamLoader extends StreamLoader {

  public IloveMusikStreamLoader() {
    super("https://api.ilovemusic.team/traffic/");
  }

  @Override
  public void loadStreams() {
    try {
      InputStream inputStream = new URL(getDEFAULT_BASE_URL()).openStream();
      JsonParser parser = new JsonParser();
      JsonObject jsonObject = parser.parse(IOUtils.toString(inputStream)).getAsJsonObject();
      System.out.println("URL: " + jsonObject.entrySet());
      //Laden von channels
      Set<Map.Entry<String, JsonElement>> entries = jsonObject.entrySet();
      for (Map.Entry<String, JsonElement> entry: entries) {
    	    System.out.println("Key: " + entry.getKey());
    	}
      //Management.instance.streamManager.getStreams().remove(Management.instance.streamManager.getStreamByName(json.getString("name")));
     // Management.instance.streamManager.getStreams().add(new Stream(Provider.ILoveMusik,json.getString("name"), json.getString("stream_url"), json.getString("title"), json.getString("artist"), json.getString("cover")));
    } catch (JsonSyntaxException | IOException e) {
      e.printStackTrace();
    }
  }
}
