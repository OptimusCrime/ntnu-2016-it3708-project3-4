package org.annea.ea;

import org.annea.ea.gtype.AbstractGType;
import org.annea.tools.CrossoverHelper;
import org.annea.tools.RandomHelper;
import org.annea.ea.gtype.Beer8BitGType;
import org.annea.ea.gtype.BinaryGType;
import org.annea.ea.gtype.FloatGType;
import org.annea.tools.settings.AbstractSettings;

import java.util.ArrayList;
import java.util.Collections;

public class EA {

    private AbstractSettings settings;

    // For stats
    private double allTimeMaxFitness;

    // Lists of children and adults
    private ArrayList<AbstractGType> children;
    private ArrayList<AbstractGType> adults;
    private ArrayList<AbstractGType> elites;

    /**
     * Constructor!
     */

    public EA(AbstractSettings s) {
        // Set settings
        settings = s;

        // Children, adults, and elites
        children = new ArrayList<>();
        adults = new ArrayList<>();
        elites = new ArrayList<>();

        // Set all time max fitness to 0
        allTimeMaxFitness = 0;
    }

    /**
     * Initialize the lists with n children
     * @param num Number of children
     */

    public void initialize(int num) {
        // Create apply members
        for (int i = 0; i < num; i++) {
            // Find the correct G-Type
            AbstractGType newIndividual;
            if (settings.getGType().equals("BinaryGType")) {
               newIndividual = new BinaryGType(settings, settings.getNumberOfValues());
            }
            else if (settings.getGType().equals("Beer8BitGType")) {
                newIndividual = new Beer8BitGType(settings, settings.getNumberOfValues());
            }
            else {
                newIndividual = new FloatGType(settings, settings.getNumberOfValues());
            }

            // Add the new individual to the list of children
            children.add(newIndividual);
        }
    }

    /**
     * Store the x best individuals in a list to keep track of the elites
     * @param num Number of elites to store
     * @return Arraylist of the elites
     */

    private ArrayList<AbstractGType> getElites(int num) {
        // To hold the n best adults
        ArrayList<AbstractGType> tempElites = new ArrayList<>();

        // Copy the list
        ArrayList<AbstractGType> adultsCopy = new ArrayList<>(adults);

        // Sort the list
        Collections.sort(adultsCopy);

        // Loop the collection
        for (int i = 0; i < num; i++) {
            tempElites.add(adultsCopy.get(i));
        }

        // Return the elites
        return tempElites;
    }

    /**
     * Adds the elites to the adult pool if they are not present
     */

    private void extractElites() {
        // Loop the elites
        for (int i = 0; i < elites.size(); i++) {
            // Check if the elite is present in the adult pool
            if (!adults.contains(elites.get(i))) {
                // Add the elite to the pool
                adults.add(elites.get(i));
            }
        }

        // Clear the elite list
        elites.clear();
    }

    /**
     * Adult selection
     */

    public void adultSelection() {
        // Check if we are adding elitism
        if (settings.getElitism() > 0 && adults.size() > 0) {
            elites = getElites(settings.getElitism());
        }

        if (settings.getAdultSelection().equals("FullGenerationalReplacement") ||
                settings.getAdultSelection().equals("Overproduction")) {
            // Full Generational Replacement
            adultSelectionReplaceAndReduce();
        }
        else {
            // Generational Mixing
            adultSelectionGenerationalMixing();
        }

        // Check if we should add the elites back
        if (elites.size() > 0) {
            extractElites();
        }
    }
    private void adultSelectionReplaceAndReduce() {
        // Remove all adults
        adults.clear();

        // Move children to the population
        for (int i = 0; i < children.size(); i++) {
            adults.add(children.get(i));
        }

        // Reduce the population to fit the maximum number of adults
        adultSelectionReduce();

        // Clear all children
        children.clear();
    }
    private void adultSelectionGenerationalMixing() {
        // Move children and adults to the population
        for (int i = 0; i < children.size(); i++) {
            adults.add(children.get(i));
        }

        // Reduce the population to fit the maximum number of adults
        adultSelectionReduce();

        // Clear all children and adults
        children.clear();
    }
    private void adultSelectionReduce() {
        if (adults.size() <= settings.getMaxAdults()) {
            return;
        }

        // Sort the list
        Collections.sort(adults);

        // Remove the exceeding part of the population
        adults.subList(settings.getMaxAdults(), adults.size()).clear();
    }

    /**
     * Scaling
     */

    public double scaleFitnessProportionate(AbstractGType individual) {
        // Sum the fitness
        double sum = 0;
        for (int i = 0; i < adults.size(); i++) {
            sum += adults.get(i).getFitness();
        }

        // Calculate the scaled fitness
        return individual.getFitness() / sum;
    }
    public double scaleSigma(AbstractGType individual) {
        // Sum the fitness and sum the squared fitness value
        double sum = 0;
        double fitnessSquared = 0;
        for (int i = 0; i < adults.size(); i++) {
            sum += adults.get(i).getFitness();
            fitnessSquared += Math.pow(adults.get(i).getFitness(), 2);
        }

        // Calculate some stuff
        double average = sum / (double) adults.size();
        double deviation = (Math.sqrt(fitnessSquared - Math.pow(average, 2))) / adults.size();

        // Return the sigma-scale
        return (1 + ((individual.getFitness() - average) / deviation)) / adults.size();
    }

    /**
     * Parent selection
     */

    public void parentSelection() {
        if (settings.getParentSelection().equals("HighestFitness")) {
            // Highest fitness
            parentSelectionHighestFitness();
        }
        else if (settings.getParentSelection().equals("FitnessProportionate") || settings.getParentSelection().equals("SigmaScaling")) {
            // All functions that uses the roulette wheel
            parentSelectionRouletteWheel();
        }
        else {
            // Tournament selection
            parentSelectionTournamentSelection();
        }
    }
    private void parentSelectionHighestFitness() {
        // Sort the list
        Collections.sort(adults);

        // Chose new parents
        ArrayList<AbstractGType> newParents = new ArrayList<>();
        for (int i = 0; i < Math.min(settings.getMaxChildren(), adults.size()); i++) {
            newParents.add(adults.get(i));
        }

        // Create new children
        createChildren(newParents);
    }
    private void parentSelectionRouletteWheel() {
        double currentValue = 0;
        double[] values = new double[adults.size()];

        // Add limit values for the roulette wheel
        for (int i = 0; i < adults.size(); i++) {
            if (settings.getParentSelection().equals("FitnessProportionate")) {
                currentValue += scaleFitnessProportionate(adults.get(i));
            }
            else {
                currentValue += scaleSigma(adults.get(i));
            }
            values[i] = currentValue;
        }

        // Chose new parents
        ArrayList<AbstractGType> newParents = new ArrayList<>();
        while (newParents.size() < Math.min(settings.getMaxChildren(), adults.size())) {
            double targetValue = RandomHelper.rand();
            for (int i = 0; i < values.length; i++) {
                if (values[i] > targetValue) {
                    newParents.add(adults.get(i));
                    break;
                }
            }
        }

        // Create new children
        createChildren(newParents);
    }
    private void parentSelectionTournamentSelection() {
        ArrayList<AbstractGType> newParents = new ArrayList<>();
        while (newParents.size() < Math.min(settings.getMaxChildren(), adults.size())) {
            ArrayList<AbstractGType> tournamentPool = new ArrayList<>();
            for (int i = 0; i < Math.min(adults.size(), settings.getTournamentSize()); i++) {
                tournamentPool.add(adults.get(RandomHelper.randint(0, adults.size())));
            }

            // Get the best participant
            AbstractGType participant1 = tournamentPool.get(0);
            for (int i = 1; i < tournamentPool.size(); i++) {
                if (tournamentPool.get(i).getFitness() > participant1.getFitness()) {
                    participant1 = tournamentPool.get(i);
                }
            }

            // Make sure we don't pick the same adult twice
            AbstractGType participant2;
            while (true) {
                participant2 = tournamentPool.get(RandomHelper.randint(0, tournamentPool.size()));

                // Break the loop if they differ
                if (participant1 != participant2) {
                    break;
                }
            }

            // Order the participants by fitness
            AbstractGType participantHighestFitness = participant1;
            AbstractGType participantLeastFitness = participant2;
            if (participantHighestFitness.getFitness() < participantLeastFitness.getFitness()) {
                participantHighestFitness = participant2;
                participantLeastFitness = participant1;
            }

            // Run probability
            if (RandomHelper.randomInPercentage(settings.getProbabilityTournamentSelection())) {
                newParents.add(participantHighestFitness);
            }
            else {
                newParents.add(participantLeastFitness);
            }
        }

        // Create new children
        createChildren(newParents);
    }

    /**
     * Generate children
     */

    private void createChildren(ArrayList<AbstractGType> parents) {
        // Loop the collection and set get the sets of parents
        for (int i = 1; i < parents.size(); i += 2) {
            AbstractGType parent1 = parents.get(i - 1);
            AbstractGType parent2 = parents.get(i);

            // Check if we should clone or do crossover
            if (RandomHelper.randomInPercentage(settings.getProbabilityCrossoverRate())) {
                // Children should be cloned
                generateOffspring(parent1, parent2);
            }
            else {
                // Children should be created by crossover
                cloneOffspring(parent1, parent2);
            }
        }
    }
    private void cloneOffspring(AbstractGType parent1, AbstractGType parent2) {
        AbstractGType newChild1;
        AbstractGType newChild2;

        if (settings.getGType().equals("BinaryGType")) {
            newChild1 = new BinaryGType(settings, settings.getNumberOfValues());
            newChild2 = new BinaryGType(settings, settings.getNumberOfValues());
        }
        else if (settings.getGType().equals("Beer8BitGType")) {
            newChild1 = new Beer8BitGType(settings, settings.getNumberOfValues());
            newChild2 = new Beer8BitGType(settings, settings.getNumberOfValues());
        }
        else {
            newChild1 = new FloatGType(settings, settings.getNumberOfValues());
            newChild2 = new FloatGType(settings, settings.getNumberOfValues());
        }


        // Simply clone the current values
        newChild1.setValue(parent1.getAsArray(), 0);
        newChild2.setValue(parent2.getAsArray(), 0);

        // Check if we should mutate or something
        if (RandomHelper.randomInPercentage(settings.getProbabilityMutation())) {
            newChild1.mutate();
        }
        if (RandomHelper.randomInPercentage(settings.getProbabilityMutation())) {
            newChild2.mutate();
        }

        // Add the children to the list
        children.add(newChild1);
        children.add(newChild2);
    }
    private void generateOffspring(AbstractGType parent1, AbstractGType parent2) {
        // Get a crossover
        int[] crossovers = CrossoverHelper.getCrossover(settings);

        // Create a new G-type and set apply the crossover
        AbstractGType newChild1;
        AbstractGType newChild2;
        if (settings.getGType().equals("BinaryGType")) {
            newChild1 = new BinaryGType(settings, settings.getNumberOfValues());
            newChild2 = new BinaryGType(settings, settings.getNumberOfValues());
        }
        else if (settings.getGType().equals("Beer8BitGType")) {
            newChild1 = new Beer8BitGType(settings, settings.getNumberOfValues());
            newChild2 = new Beer8BitGType(settings, settings.getNumberOfValues());
        }
        else {
            newChild1 = new FloatGType(settings, settings.getNumberOfValues());
            newChild2 = new FloatGType(settings, settings.getNumberOfValues());
        }

        // Apply the crossover
        newChild1.applyCrossover(crossovers, 0, parent1, parent2);
        newChild2.applyCrossover(crossovers, 1, parent1, parent2);

        // Check if we should mutate or something
        if (RandomHelper.randomInPercentage(settings.getProbabilityMutation())) {
            newChild1.mutate();
        }
        if (RandomHelper.randomInPercentage(settings.getProbabilityMutation())) {
            newChild2.mutate();
        }

        // Add the children to the list
        children.add(newChild1);
        children.add(newChild2);
    }

    /**
     * Method for returning the best individual in the entire pool
     * @return The best individual
     */

    public AbstractGType getBest() {
        AbstractGType best = children.get(0);
        for (int i = 0; i < children.size(); i++) {
            if (children.get(i).getFitness() > best.getFitness()) {
                best = children.get(i);
            }
        }
        for (int i = 0; i < adults.size(); i++) {
            if (adults.get(i).getFitness() > best.getFitness()) {
                best = adults.get(i);
            }
        }

        return best;
    }

    /**
     * Various output things
     */

    public void getStats() {
        double sum = 0;
        double max = 0;
        int maxIndex = 0;
        AbstractGType individual = null;

        // Find max value and sum all the fitnesses
        for (int i = 0; i < children.size(); i++) {
            if (individual == null) {
                individual = children.get(i);
            }

            // Store max
            if (children.get(i).getFitness() > max) {
                // Store reference to this index
                maxIndex = i;

                // Update the max value
                max = children.get(i).getFitness();
            }

            // Sum
            sum += children.get(i).getFitness();
        }

        // Calculate the average value for this generation
        double average = sum / (double) children.size();

        // Check if the current best is better than the all time max
        if (max > allTimeMaxFitness) {
            // Update the all time fitness value
            allTimeMaxFitness = max;
        }

        // Return the stats string
        System.out.println("Max = " + max + ". All time max: " + allTimeMaxFitness +  ". Avg: " + average);
    }

    // Duplicate for backwars compatibility
    public double[] getStatsValues() {
        double sum = 0;
        double max = 0;
        int maxIndex = 0;
        AbstractGType individual = null;

        // Find max value and sum all the fitnesses
        for (int i = 0; i < children.size(); i++) {
            if (individual == null) {
                individual = children.get(i);
            }

            // Store max
            if (children.get(i).getFitness() > max) {
                // Store reference to this index
                maxIndex = i;

                // Update the max value
                max = children.get(i).getFitness();
            }

            // Sum
            sum += children.get(i).getFitness();
        }

        // Calculate the average value for this generation
        double average = sum / (double) children.size();

        // Check if the current best is better than the all time max
        if (max > allTimeMaxFitness) {
            // Update the all time fitness value
            allTimeMaxFitness = max;
        }

        // Return the stats
        return new double[]{max, average};
    }

    /**
     * Various getters
     */

    public ArrayList<AbstractGType> getAdults() {
        return adults;
    }
    public ArrayList<AbstractGType> getChildren() {
        return children;
    }
}
