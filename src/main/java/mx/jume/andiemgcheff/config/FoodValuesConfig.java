package mx.jume.andiemgcheff.config;

import java.util.HashMap;
import java.util.Map;

public class FoodValuesConfig {
    public static class FoodEntry {
        public float hungerRestoration;
        public float maxHungerSaturation;
        public float thirstRestoration;

        public FoodEntry() {
        }

        public FoodEntry(float hunger, float saturation, float thirst) {
            this.hungerRestoration = hunger;
            this.maxHungerSaturation = saturation;
            this.thirstRestoration = thirst;
        }
    }

    // Map of ItemID -> Values
    public Map<String, FoodEntry> entries = new HashMap<>();

    public FoodValuesConfig() {
    }
}


