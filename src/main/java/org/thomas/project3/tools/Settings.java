package org.thomas.project3.tools;

import java.util.HashMap;

public class Settings {

    private int numberOfValues = 10;
    private String gtype = "BinaryGType";
    private String ptype = "BinaryPType";
    private String fitness = "OneMaxFitness";
    private String adultSelection = "FullGenerationalReplacement";
    private String parentSelection = "FitnessProportionate";
    private int elitism = 0;

    private int maxChildren = 100;
    private int maxAdults = 100;
    private int maxGenerations = 100;
    private int tournamentSize = 10;

    private HashMap<String, String> settings = new HashMap<>();

    private int[] networkDimensions;
    private String[] networkFuctions;
    private int maxTimesteps = 10;

    private boolean demo = true;
    private boolean plot = false;

    private int probabilityMutation = 5;
    private int probabilityDoubleCrossover = 50;
    private int probabilityCrossoverRate = 20;
    private int probabilityTournamentSelection = 70;

    /**
     * Various setters
     */

    public void setNumberOfValues(int num) {
        numberOfValues = num;
    }
    public void setGType(String type) {
        gtype = type;
    }
    public void setPType(String type) {
        ptype = type;
    }
    public void setFitness(String method) {
        fitness = method;
    }
    public void setAdultSelection(String method) {
        adultSelection = method;
    }
    public void setParentSelection(String method) {
        parentSelection = method;
    }
    public void setElitism(int value) {
        elitism = value;
    }

    public void setMaxGenerations(int max) {
        maxGenerations = max;
    }
    public void setMaxChildren(int max) {
        maxChildren = max;
    }
    public void setMaxAdults(int max) {
        maxAdults = max;
    }
    public void setTournamentSize(int size) {
        tournamentSize = size;
    }

    public void addSetting(String key, String value) {
        settings.put(key, value);
    }

    public void setNetworkDimensions(int[] dims) {
        networkDimensions = dims;
    }
    public void setNetworkFunctions(String[] dims) {
        networkFuctions = dims;
    }
    public void setMaxTimesteps(int max) {
        maxTimesteps = max;
    }

    public void setDemo(boolean b) {
        demo = b;
    }
    public void setPlot(boolean b) {
        plot = b;
    }

    public void setProbabilityMutation(int percent) {
        probabilityMutation = percent;
    }
    public void setProbabilityDoubleCrossover(int percent) {
        probabilityDoubleCrossover = percent;
    }
    public void setProbabilityCrossoverRate(int percent) {
        probabilityCrossoverRate = percent;
    }
    public void setProbabilityTournamentSelection(int percent) {
        probabilityTournamentSelection = percent;
    }

    /**
     * Various getters
     */

    public int getNumberOfValues() {
        return numberOfValues;
    }
    public String getGType() {
        return gtype;
    }
    public String getPType() {
        return ptype;
    }
    public String getFitness() {
        return fitness;
    }
    public String getAdultSelection() {
        return adultSelection;
    }
    public String getParentSelection() {
        return parentSelection;
    }
    public int getElitism() {
        return elitism;
    }

    public int getMaxGenerations() {
        return maxGenerations;
    }
    public int getMaxChildren() {
        return maxChildren;
    }
    public int getMaxAdults() {
        return maxAdults;
    }
    public int getTournamentSize() {
        return tournamentSize;
    }

    public String getSetting(String key) {
        return settings.get(key);
    }

    public int[] getNetworkDimensions() {
        if (networkDimensions == null) {
            return new int[]{3, 4, 3};
        }
        return networkDimensions;
    }
    public String[] getNetworkFunctions() {
        if (networkFuctions == null) {
            return new String[]{"Sigmoid", "Sigmoid", "Sigmoid"};
        }
        return networkFuctions;
    }
    public int getMaxTimesteps() {
        return maxTimesteps;
    }

    public boolean demo() {
        return demo;
    }
    public boolean plot() {
        return plot;
    }

    public int getProbabilityMutation() {
        return probabilityMutation;
    }
    public int getProbabilityDoubleCrossover() {
        return probabilityDoubleCrossover;
    }
    public int getProbabilityCrossoverRate() {
        return probabilityCrossoverRate;
    }
    public int getProbabilityTournamentSelection() {
        return probabilityTournamentSelection;
    }
}
