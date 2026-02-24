package mx.jume.andiemgcheff.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConfigManager {
    private static final Logger LOGGER = Logger.getLogger("AndiemgCheff");
    private final File configDir;
    private final Gson gson;

    private AndiemgCheffConfig mainConfig = new AndiemgCheffConfig();
    private FoodValuesConfig foodConfig = new FoodValuesConfig();

    public ConfigManager(File configDir) {
        this.configDir = configDir;
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        if (!configDir.exists()) {
            configDir.mkdirs();
        }
        loadAll();
    }

    public void loadAll() {
        loadMainConfig();
        loadFoodConfig();
    }

    private void loadMainConfig() {
        File file = new File(configDir, "AndiemgCheffConfig.json");
        if (!file.exists()) {
            saveMainConfig();
            return;
        }

        try (FileReader reader = new FileReader(file)) {
            AndiemgCheffConfig loaded = gson.fromJson(reader, AndiemgCheffConfig.class);
            if (loaded != null) {
                // Migration logic: version check if needed
                if (loaded.version == null || !loaded.version.equals(mainConfig.version)) {
                    LOGGER.info(
                            "Updating AndiemgCheffConfig from version " + loaded.version + " to " + mainConfig.version);
                    // Merging logic can be added here if fields were added
                }
                this.mainConfig = loaded;
                // Ensure version is current
                this.mainConfig.version = "1.0.0";
                saveMainConfig(); // Persistence of migration
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to load AndiemgCheffConfig.json", e);
        }
    }

    private void loadFoodConfig() {
        File file = new File(configDir, "foodvalues.json");
        if (!file.exists()) {
            // Add default entries
            // foodConfig.entries.put("Andiechef_Food_Item_Soya", new
            // FoodValuesConfig.FoodEntry(5.0f, 100.0f, -2.0f));
            // foodConfig.entries.put("Andiechef_YakimeshiPork", new
            // FoodValuesConfig.FoodEntry(9.0f, 150.0f, 10.0f));
            // foodConfig.entries.put("Andiechef_Food_Rollo", new
            // FoodValuesConfig.FoodEntry(13.0f, 125.0f, 0.0f));
            // foodConfig.entries.put("Andiechef_Food_Nigiri", new
            // FoodValuesConfig.FoodEntry(25.0f, 165.0f, 0.0f));
            // foodConfig.entries.put("Andiechef_Item_Soya_Fermentada",
            // new FoodValuesConfig.FoodEntry(11.0f, 165.0f, 2.0f));
            // foodConfig.entries.put("Andiechef_YakimeshiBeef", new
            // FoodValuesConfig.FoodEntry(10.0f, 150.0f, 10.0f));
            // foodConfig.entries.put("Andiechef_Food_Onigiri", new
            // FoodValuesConfig.FoodEntry(15.0f, 155.0f, 0.0f));
            // foodConfig.entries.put("Andiechef_Ingredient_SalsaSoya",
            // new FoodValuesConfig.FoodEntry(8.0f, 110.0f, 10.0f));
            // foodConfig.entries.put("Andiechef_Food_Sake", new
            // FoodValuesConfig.FoodEntry(2.0f, 105.0f, 40.0f));
            // foodConfig.entries.put("Andiechef_Ingredient_Wasabi", new
            // FoodValuesConfig.FoodEntry(5.0f, 100.0f, -15.0f));
            // foodConfig.entries.put("Andiechef_YakimeshiFish", new
            // FoodValuesConfig.FoodEntry(12.0f, 150.0f, 10.0f));
            // foodConfig.entries.put("Andiechef_YakimeshiChicken", new
            // FoodValuesConfig.FoodEntry(11.0f, 150.0f, 10.0f));
            // foodConfig.entries.put("Andiechef_Food_BubbleTea", new
            // FoodValuesConfig.FoodEntry(3.0f, 100.0f, 25.0f));
            // saveFoodConfig();
            return;
        }

        try (FileReader reader = new FileReader(file)) {
            Type type = new TypeToken<Map<String, FoodValuesConfig.FoodEntry>>() {
            }.getType();
            Map<String, FoodValuesConfig.FoodEntry> loaded = gson.fromJson(reader, type);
            if (loaded != null) {
                this.foodConfig.entries = loaded;
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to load foodvalues.json", e);
        }
    }

    public void saveMainConfig() {
        File file = new File(configDir, "AndiemgCheffConfig.json");
        try (FileWriter writer = new FileWriter(file)) {
            gson.toJson(mainConfig, writer);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to save AndiemgCheffConfig.json", e);
        }
    }

    public void saveFoodConfig() {
        File file = new File(configDir, "foodvalues.json");
        try (FileWriter writer = new FileWriter(file)) {
            gson.toJson(foodConfig.entries, writer);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to save foodvalues.json", e);
        }
    }

    public AndiemgCheffConfig getMainConfig() {
        return mainConfig;
    }

    public FoodValuesConfig getFoodConfig() {
        return foodConfig;
    }
}
