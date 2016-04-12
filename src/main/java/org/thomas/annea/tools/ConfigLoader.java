package org.thomas.annea.tools;

import org.thomas.annea.beer.BeerWorld;
import org.thomas.annea.tools.settings.AbstractSettings;
import org.thomas.annea.tools.settings.BeerSettings;
import org.thomas.annea.tools.settings.FlatlandSettings;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ConfigLoader {

    private AbstractSettings settings;

    public ConfigLoader() {
        this("settings.conf", "flatland");
    }

    /**
     * Constructor (default file)
     */

    public ConfigLoader(String type) {
        this("settings.conf", type);
    }

    /**
     * Constructor (variable file)
     * @param fileName File to load
     */

    public ConfigLoader(String fileName, String type) {
        // Get filename prefix
        String fileNamePrefix = "flatland";
        if (type.equals("beer")) {
            fileNamePrefix = "beer";
        }

        // Construct full file path
        String fullPath = System.getProperty("user.dir") + "/" + fileNamePrefix + "_" + fileName;
        System.out.println("Trying to load config file: " + fullPath);

        // Create new instance of a setting
        if (type.equals("flatland")) {
            settings = new FlatlandSettings();
        }
        else {
            settings = new BeerSettings();
        }


        // Try to open the file
        try {
            // Java stuff
            File file = new File(fullPath);
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            // Loop each line
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                handleLine(line);
            }

            // Close up the file
            fileReader.close();
        }
        catch (IOException e) {
            // Something went to hell
            System.out.println("Could not read config file. Applying default settings.");
            e.printStackTrace();
        }
    }

    /**
     * Get the current settings instance
     * @return The settings instance
     */

    public AbstractSettings getSettings() {
        return settings;
    }

    /**
     * Handles one line in the config file
     * @param line Line to handle
     */

    private void handleLine(String line) {
        // Check if we can ignore this line
        if (line.length() > 0 && line.charAt(0) != ';' && line.charAt(0) != '#') {
            // Should be valid string
            parseLine(line);
        }
    }

    /**
     * Parses one line and sets the value
     * @param line Line to parse
     */

    private void parseLine(String line) {
        // Split the string
        String[] split = line.split("=");

        // If the length is greater than one we can keep on moving
        if (split.length == 2) {
            // Remove spaces
            String keyword = split[0].replaceAll("\\s", "");
            String value = split[1].replaceAll("\\s", "");

            // Representation Settings
            if (keyword.equals("values")) {
                try {
                    settings.setNumberOfValues(Integer.parseInt(value));
                } catch (NumberFormatException e) {}
            }
            else if (keyword.equals("genotype")) {
                settings.setGType(value);
            }
            else if (keyword.equals("phenotype")) {
                settings.setPType(value);
            }

            // Some limits
            else if (keyword.equals("maxGenerations")) {
                try {
                    settings.setMaxGenerations(Integer.parseInt(value));
                } catch (NumberFormatException e) {}
            }
            else if (keyword.equals("maxChildren")) {
                try {
                    settings.setMaxChildren(Integer.parseInt(value));
                } catch (NumberFormatException e) {}
            }
            else if (keyword.equals("maxAdults")) {
                try {
                    settings.setMaxAdults(Integer.parseInt(value));
                } catch (NumberFormatException e) {}
            }

            // Evolution Settings
            else if (keyword.equals("fitness")) {
                if (value.equals("OneMaxFitness") || value.equals("LolzPrefixFitness") || value.equals("SurprisingSequenceGlobally") || value.equals("SurprisingSequenceLocally")) {
                    settings.setFitness(value);
                }
            }
            else if (keyword.equals("adultSelection")) {
                if (value.equals("FullGenerationalReplacement") || value.equals("Overproduction") || value.equals("GenerationMixing")) {
                    settings.setAdultSelection(value);
                }
            }
            else if (keyword.equals("parentSelection")) {
                if (value.equals("HighestFitness") || value.equals("FitnessProportionate") || value.equals("SigmaScaling") || value.equals("TournamentSelection")) {
                    settings.setParentSelection(value);
                }
            }
            else if (keyword.equals("elitism")) {
                try {
                    settings.setElitism(Integer.parseInt(value));
                } catch (NumberFormatException e) {}
            }

            // Probability
            else if (keyword.equals("doubleCrossover")) {
                try {
                    settings.setProbabilityDoubleCrossover(Integer.parseInt(value));
                } catch (NumberFormatException e) {}
            }
            else if (keyword.equals("mutation")) {
                try {
                    settings.setProbabilityMutation(Integer.parseInt(value));
                } catch (NumberFormatException e) {}
            }
            else if (keyword.equals("crossoverRate")) {
                try {
                    settings.setProbabilityCrossoverRate(Integer.parseInt(value));
                } catch (NumberFormatException e) {}
            }
            else if (keyword.equals("tournamentSelection")) {
                try {
                    settings.setProbabilityTournamentSelection(Integer.parseInt(value));
                } catch (NumberFormatException e) {}
            }
            else if (keyword.equals("tournamentSize")) {
                try {
                    settings.setTournamentSize(Integer.parseInt(value));
                } catch (NumberFormatException e) {}
            }

            // Network, Flatland and Beer stuff
            else if (keyword.equals("mode")) {
                BeerSettings beerSettings = (BeerSettings) settings;

                if (value.equals("standard")) {
                    beerSettings.setMode(BeerWorld.STANDARD);
                }
                else if (value.equals("pull")) {
                    beerSettings.setMode(BeerWorld.PULL);
                }
                else if (value.equals("nowrap")) {
                    beerSettings.setMode(BeerWorld.NOWRAP);
                }
            }
            else if (keyword.equals("network_dimensions")) {
                String[] networkDimensionsSplit = value.split(",");
                List<Integer> networkDimensionsClean = new ArrayList<>();
                for (int i = 0; i < networkDimensionsSplit.length; i++) {
                    String currentValue = networkDimensionsSplit[i].replaceAll("\\s+","");
                    try {
                        int currentValueInt = Integer.parseInt(currentValue);
                        if (currentValueInt > 0) {
                            networkDimensionsClean.add(currentValueInt);
                        }

                    }
                    catch (Exception e) {
                    }
                }

                // Add to final result
                if (networkDimensionsClean.size() > 0) {
                    int []parsedDimensions = new int[networkDimensionsClean.size()];
                    for (int i = 0; i < networkDimensionsClean.size(); i++) {
                        parsedDimensions[i] = networkDimensionsClean.get(i);
                    }

                    // Finally set the parsed dimensions
                    settings.setNetworkDimensions(parsedDimensions);
                }
            }
            // Network and Flatland stuff
            else if (keyword.equals("network_functions")) {
                String[] networkFunctionsSplit = value.split(",");
                List<String> networkFunctionsClean = new ArrayList<>();
                for (int i = 0; i < networkFunctionsSplit.length; i++) {
                    String currentValue = networkFunctionsSplit[i].replaceAll("\\s+","");
                    try {
                        networkFunctionsClean.add(currentValue);
                    }
                    catch (Exception e) {
                    }
                }

                // Add to final result
                if (networkFunctionsClean.size() > 0) {
                    String []parsedDimensions = new String[networkFunctionsClean.size()];
                    for (int i = 0; i < networkFunctionsClean.size(); i++) {
                        parsedDimensions[i] = networkFunctionsClean.get(i);
                    }

                    // Finally set the parsed dimensions
                    settings.setNetworkFunctions(parsedDimensions);
                }
            }
            else if (keyword.equals("timesteps") || keyword.equals("timestep")) {
                try {
                    settings.setMaxTimesteps(Integer.parseInt(value));
                } catch (NumberFormatException e) {}
            }

            // Other Settings
            else if (keyword.length() > 8 && keyword.substring(0, 9).equals("settings_")) {
                String settingsKeyword = keyword.substring(9);
                settings.addSetting(settingsKeyword, value);
            }
        }
    }
}
