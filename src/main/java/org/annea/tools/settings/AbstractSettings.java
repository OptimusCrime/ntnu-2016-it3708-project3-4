package org.annea.tools.settings;

import java.util.HashMap;

public abstract class AbstractSettings {

    /**
     * Various things
     */

    protected int numberOfValues = 10;
    protected String gtype = "BinaryGType";
    protected String ptype = "BinaryPType";
    protected String fitness = "OneMaxFitness";
    protected String adultSelection = "FullGenerationalReplacement";
    protected String parentSelection = "FitnessProportionate";
    protected int elitism = 0;

    protected int maxChildren = 100;
    protected int maxAdults = 100;
    protected int maxGenerations = 100;
    protected int tournamentSize = 10;

    protected HashMap<String, String> settings = new HashMap<>();

    protected int[] networkDimensions;
    protected String[] networkFuctions;
    protected int maxTimesteps = 10;

    protected int probabilityMutation = 5;
    protected int probabilityDoubleCrossover = 50;
    protected int probabilityCrossoverRate = 20;
    protected int probabilityTournamentSelection = 70;

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
