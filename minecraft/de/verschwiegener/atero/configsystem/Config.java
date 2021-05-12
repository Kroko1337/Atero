package de.verschwiegener.atero.configsystem;

import com.google.gson.*;

import de.verschwiegener.atero.Management;
import de.verschwiegener.atero.module.Module;
import net.minecraft.client.Minecraft;

import java.io.*;

public class Config {
    private String name;
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public Config(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void deleteConfig() {
        try {
            File file = new File(Minecraft.getMinecraft().mcDataDir + "\\Atero\\Configs\\" + name + ".json");
            file.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadonlineconfig(String input) {
        try {
            JsonElement jsonElement = gson.fromJson(input, JsonElement.class);
            if (jsonElement instanceof JsonNull)
                return;
            JsonObject jsonObject = (JsonObject) jsonElement;
            for (Module module : Management.instance.modulemgr.getModules()) {
                if(jsonObject.has(module.getName())) {
                    module.setEnabled(jsonObject.get(module.getName()).getAsBoolean());
                }
            }
            Management.instance.settingsmgr.getSettings().forEach(setting -> {
                setting.getItems().forEach(settingsItem -> {
                    if(jsonObject.has(settingsItem.getName())) {
                        switch (settingsItem.getCategory()) {
                            case Slider:
                                settingsItem.setCurrentValue(jsonObject.get(settingsItem.getName()).getAsFloat());
                                break;
                            case Checkbox:
                                settingsItem.setState(jsonObject.get(settingsItem.getName()).getAsBoolean());
                                break;
                            case Combobox:
                                settingsItem.setChild(jsonObject.get(settingsItem.getName()).getAsString());
                                break;
                            case Textfield:
                                settingsItem.setCurrent(jsonObject.get(settingsItem.getName()).getAsString());
                                break;
                        }
                    }
                });
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadConfig() {
        try {

            BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(Management.instance.fileManager.getConfigDirectory(), name + ".json")));
            JsonElement jsonElement = gson.fromJson(bufferedReader, JsonElement.class);
            if (jsonElement instanceof JsonNull)
                return;
            JsonObject jsonObject = (JsonObject) jsonElement;
            for (Module module : Management.instance.modulemgr.getModules()) {
                if(jsonObject.has(module.getName())) {
                    module.setEnabled(jsonObject.get(module.getName()).getAsBoolean());
                }
            }
            Management.instance.settingsmgr.getSettings().forEach(setting -> {
                setting.getItems().forEach(settingsItem -> {
                    System.out.println("setting value");
                    if(jsonObject.has(settingsItem.getName())) {
                        switch (settingsItem.getCategory()) {
                            case Slider:
                                settingsItem.setCurrentValue(jsonObject.get(settingsItem.getName()).getAsFloat());
                                break;
                            case Checkbox:
                                settingsItem.setState(jsonObject.get(settingsItem.getName()).getAsBoolean());
                                break;
                            case Combobox:
                                settingsItem.setChild(jsonObject.get(settingsItem.getName()).getAsString());
                                break;
                            case Textfield:
                                settingsItem.setCurrent(jsonObject.get(settingsItem.getName()).getAsString());
                                break;
                        }
                    }
                });
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveConfig() {
        try {
            JsonObject jsonObject = new JsonObject();
            Management.instance.settingsmgr.getSettings().forEach(setting -> {
                setting.getItems().forEach(settingsItem -> {
                    switch (settingsItem.getCategory()) {
                        case Textfield:
                            jsonObject.addProperty(settingsItem.getName(), settingsItem.getCurrent());
                            break;
                        case Combobox:
                            jsonObject.addProperty(settingsItem.getName(), settingsItem.getChild());
                            break;
                        case Checkbox:
                            jsonObject.addProperty(settingsItem.getName(), settingsItem.isState());
                            break;
                        case Slider:
                            jsonObject.addProperty(settingsItem.getName(), settingsItem.getCurrentValue());
                            break;
                    }
                });
            });
            Management.instance.modulemgr.getModules().forEach(module -> {
                jsonObject.addProperty(module.getName(), module.isEnabled());
            });
            try (PrintWriter pwriter = new PrintWriter(new BufferedWriter(new FileWriter(new File(Management.instance.fileManager.getConfigDirectory(), name + ".json"))), true)) {
                pwriter.println(gson.toJson(jsonObject));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
