
import java.awt.Font;
import javax.swing.JPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;

/**
 *
 */
/**
 *
 *
 */
public class ChartMaker {

    public static JPanel getPieChart() {
        JFreeChart chart = null;
        if (EntryClass.char_type == EntryClass.chartType.Pie) {
            chart = createChart(CapturedPacket.createDataset());
        }
        
            if (EntryClass.char_type == EntryClass.chartType.Bar) {
            chart = createChart_2(CapturedPacket.createCategoryDataset());
        }
        return new ChartPanel(chart);
    }

    /**
     * Creates a chart.
     *
     * @param dataset the dataset.
     *
     * @return A chart.
     */
    private static JFreeChart createChart(PieDataset dataset) {

        JFreeChart chart = ChartFactory.createPieChart(
                "", // chart title
                dataset, // data
                true, // include legend
                true,
                false
        );

        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setLabelFont(new Font("SansSerif", Font.PLAIN, 12));
        plot.setNoDataMessage("No data available");
        plot.setCircular(false);
        plot.setLabelGap(0.02);
        return chart;

    }

    private static JFreeChart createChart_2(CategoryDataset dataset) {

        //Create chart
        JFreeChart chart = ChartFactory.createBarChart(
                "", //Chart Title
                "Time", // Category axis
                "Number of Packets Received", // Value axis
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false
        );

        return chart;

    }

}
