package org.annea.gui.observers;

import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;

/**
 * Created by krekle on 06/04/16.
 */
public interface GraphHelper {

    /**
     *  Callback method for observing evoAlg stats per generation
     *
     * @param generation
     * @param max, best score
     * @param avg, average score
     */
    public void fireLog(int generation, double max, double avg);

    /**
     *  Method for drawing double collections on a LineChart
     *  Call with GraphHelper.populateGraph
     *
     * @param graph, LineChart instance
     * @param titles, ordered list of plot names
     * @param arrays, ordered list with double arrays containing data points
     */
    public static void populateGraph(LineChart graph, String[] titles, double[]... arrays) {
        boolean showTitles = true;
        if(titles.length != arrays.length) showTitles = false;

        for(int plotIndex = 0; plotIndex < arrays.length; plotIndex++) {

            // Create new plot series
            XYChart.Series series = new XYChart.Series();

            // Set the plot name, if provided
            if (showTitles) series.setName(titles[plotIndex]);

            // Cast the value array, only supports Double for now
            double[] values = (double[]) arrays[plotIndex];

            // Add values to the plot series
            for(int i = 0; i < values.length; i++) {
                series.getData().add(new XYChart.Data(""+ i, values[i]));
            }

            // Append to the lineChart
            graph.getData().add(series);

        }
    }

}
