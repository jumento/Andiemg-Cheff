package mx.jume.andiemgcheff.logic;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import mx.jume.andiemgcheff.config.AndiemgCheffConfig;
import mx.jume.andiemgcheff.config.FoodValuesConfig;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Merger {
    private static final Logger LOGGER = Logger.getLogger("AndiemgCheff");
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public record MergeResult(int added, int skipped, int overwritten, boolean targetFound, String targetPath) {
    }

    public MergeResult performSync(AndiemgCheffConfig config, FoodValuesConfig foodValues) {
        String targetPathStr = "mods/Aqua-Thirst-hunger/ExternalFoodsConfig.json";
        File targetFile = new File(targetPathStr);

        if (!targetFile.exists()) {
            return new MergeResult(0, 0, 0, false, targetFile.getAbsolutePath());
        }

        Map<String, FoodValuesConfig.FoodEntry> targetData = loadTarget(targetFile);
        if (targetData == null) {
            targetData = new LinkedHashMap<>(); // Treat as empty if corrupted or new
        }

        int added = 0;
        int skipped = 0;
        int overwritten = 0;

        for (Map.Entry<String, FoodValuesConfig.FoodEntry> entry : foodValues.entries.entrySet()) {
            String itemId = entry.getKey();
            FoodValuesConfig.FoodEntry values = entry.getValue();

            if (!targetData.containsKey(itemId)) {
                targetData.put(itemId, values);
                added++;
            } else {
                if (config.forceOverwrite) {
                    targetData.put(itemId, values);
                    overwritten++;
                } else {
                    skipped++;
                }
            }
        }

        if (added > 0 || overwritten > 0) {
            saveTargetAtomic(targetFile, targetData);
        }

        return new MergeResult(added, skipped, overwritten, true, targetFile.getAbsolutePath());
    }

    private Map<String, FoodValuesConfig.FoodEntry> loadTarget(File file) {
        try (FileReader reader = new FileReader(file)) {
            Type type = new TypeToken<Map<String, FoodValuesConfig.FoodEntry>>() {
            }.getType();
            return gson.fromJson(reader, type);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to load target config: " + file.getPath(), e);
            return null;
        }
    }

    private void saveTargetAtomic(File targetFile, Map<String, FoodValuesConfig.FoodEntry> data) {
        File tempFile = new File(targetFile.getParent(), targetFile.getName() + ".tmp");
        try (FileWriter writer = new FileWriter(tempFile)) {
            gson.toJson(data, writer);
            writer.flush();
            writer.close(); // Close before move

            Files.move(tempFile.toPath(), targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING,
                    StandardCopyOption.ATOMIC_MOVE);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to save target config atomically", e);
            if (tempFile.exists()) {
                tempFile.delete();
            }
        }
    }
}


