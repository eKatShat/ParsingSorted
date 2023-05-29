import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import javax.swing.*;
import java.util.Arrays;
import java.util.Random;

public class Main {

    public static void main(String[] args) {

        int[] sizes = new int[]{37, 84, 189, 424, 954, 2152, 4865};

        int[][] bubbleMatrix = GetRandomMatrix(sizes);
        int[][] insertMatrix = GetRandomMatrix(sizes);

        System.out.println("Начальные массивы:");
        PrintMatrix(bubbleMatrix);
        PrintMatrix(insertMatrix);

//массивы в которых мы храним число перестановок и сравнений
        //абсолютные значения скольок бьыло сделлано перестановок и сравнений
        int[][] swapAndCompareResultInsertAbsolute = SortMatrix(insertMatrix,"SortInsert");
        int[][] swapAndCompareResultBubbleAbsolute = SortMatrix(bubbleMatrix,"SortBubble");

       //печатаем отсортирвоанные массивчеки
        System.out.println("Отсортированные массивы:");
        PrintMatrix(bubbleMatrix);
        PrintMatrix(insertMatrix);


        float[][] swapAndCompareResultBubbleNORMAL = GetSwapAndCompareResultNormal(swapAndCompareResultBubbleAbsolute,sizes);
        float[][] swapAndCompareResultBubbleLOG = GetSwapAndCompareResultLog(swapAndCompareResultBubbleAbsolute);


        //
        float[][] swapAndCompareResultInsertNORMAL = GetSwapAndCompareResultNormal(swapAndCompareResultInsertAbsolute,sizes);
        float[][] swapAndCompareResultInsertLOG = GetSwapAndCompareResultLog(swapAndCompareResultInsertAbsolute);


        //дальше рисовалка
        XYSeriesCollection dataset1 = GetXYSeriesCollection(swapAndCompareResultBubbleAbsolute,swapAndCompareResultInsertAbsolute);
        XYSeriesCollection dataset2 = GetXYSeriesCollection(swapAndCompareResultBubbleNORMAL,swapAndCompareResultInsertNORMAL);
        XYSeriesCollection dataset3 = GetXYSeriesCollection(swapAndCompareResultBubbleLOG,swapAndCompareResultInsertLOG);

        GetJFrame(dataset1);
        GetJFrame(dataset2);
        GetJFrame(dataset3);

    }

    private static int[][] GetRandomMatrix(int[] sizes) {  //создаем массивы, передаем сюда размеры
        int[][] mass = new int[7][];
        for (int i = 0; i < 7; i++) {
            mass[i] = RandomMass(sizes[i]);
        }
        return mass;
    }

    private static int[] RandomMass(int size) { //заполняем созданные массивы
        Random rand = new Random();
        int[] mass = new int[size];
        for (int a = 0; a < mass.length; a++) {
            mass[a] = rand.nextInt(100);
        }
        return mass;
    }

    private static void PrintMatrix(int[][] data) { //выводим массивы, проверяем до 5
        for (int i = 0; i < 5; i++) {
            System.out.println(Arrays.toString(data[i]));
        }
    }

    //вспомогательный метод
    //выбираю нужный массивчик
    private static int[][] SortMatrix(int[][] mass, String nameSort) {
        int[][] swapAndCompareResults = new int[7][2];   //массив результатов для каждой сортировки
        if (nameSort == "SortBubble") {
            for (int i = 0; i < 7; i++) {
                swapAndCompareResults[i] = SortBubble(mass[i]);
            }
        }
        if (nameSort == "SortInsert") {
            for (int i = 0; i < 7; i++) {
                swapAndCompareResults[i] = SortInsert(mass[i]);
            }
        }
        if (nameSort == "SortSelect") {
            for (int i = 0; i < 7; i++) {
                swapAndCompareResults[i] = SortSelect(mass[i]);
            }
        }
        return swapAndCompareResults;
    }


    private static float[][] GetSwapAndCompareResultNormal(int[][] swapAndCompareAbsolute, int[] sizes) {
        float[][] swapAndCompareResult = new float[7][2];
        for (int i = 0; i < swapAndCompareResult.length; i++) {
            for (int j = 0; j < swapAndCompareResult[i].length; j++) {
                swapAndCompareResult[i][j] = (float) swapAndCompareAbsolute[i][j] / sizes[i];
            }
        }
        return swapAndCompareResult;
    }
    private static float[][] GetSwapAndCompareResultLog(int[][] swapAndCompareAbsolute) {
        float[][] swapAndCompareResult = new float[7][2];
        for (int i = 0; i < swapAndCompareResult.length; i++) {
            for (int j = 0; j < swapAndCompareResult[i].length; j++) {
                swapAndCompareResult[i][j] =  (float) Math.log(swapAndCompareAbsolute[i][j]);
            }
        }
        return swapAndCompareResult;
    }



    private static XYSeriesCollection GetXYSeriesCollection(float[][] swapResultBubble, float[][] second) {
        XYSeriesCollection dataset = new XYSeriesCollection();
        XYSeries series = new XYSeries("BubbleSort");
        XYSeries series2 = new XYSeries("second");

        for (int i = 0; i < swapResultBubble.length; i++) {
            for (int j = 0; j < swapResultBubble[j].length - 1; j++) {
                series.add(swapResultBubble[i][j], swapResultBubble[i][j + 1]);
                series2.add(second[i][j], second[i][j + 1]);
            }
        }
        dataset.addSeries(series);
        dataset.addSeries(series2);
        return dataset;
    }
    private static XYSeriesCollection GetXYSeriesCollection(int[][] bubble, int[][] insert) {
        XYSeriesCollection dataset = new XYSeriesCollection();
        XYSeries series = new XYSeries("Пузырьковая сортировка");
        XYSeries series2 = new XYSeries("second");

        for (int i = 0; i < bubble.length; i++) {
            for (int j = 0; j < bubble[j].length - 1; j++) {
                series.add(bubble[i][j], bubble[i][j + 1]);
                series2.add(insert[i][j], insert[i][j + 1]);
            }
        }
        dataset.addSeries(series);
        dataset.addSeries(series2);
        return dataset;
    }

    private static void GetJFrame(XYSeriesCollection dataset) {

        JFreeChart chart = ChartFactory.createXYLineChart(
                "Анализ сортировок",
                "Число перестановок",
                "Число сравнений",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );
        ChartPanel chartPanel = new ChartPanel(chart);
        JFrame frame = new JFrame("Chart");
        frame.setContentPane(chartPanel);
        frame.pack();
        frame.setVisible(true);
    }

    private static int[] SortBubble(int[] mass) {
        int resultSwap = 0;
        int resultCompare = 0;
        int temp = 0;
        boolean sorted = false;

        while (!sorted) {
            sorted = true;
            for (int i = 0; i < mass.length - 1; i++) {
                resultCompare++;
                if (mass[i] > mass[i + 1]) {
                    resultSwap++;
                    temp = mass[i];
                    mass[i] = mass[i + 1];
                    mass[i + 1] = temp;
                    sorted = false;
                }
            }
        }
        return new int[]{resultSwap, resultCompare};
    }



    private static int[] SortInsert(int[] mass) {
        int resultSwap = 0;
        int resultCompare = 0;
        for (int i = 1; i < mass.length; i++) {          //цикл по всем элементам неотсортированной последовательности
            int current = mass[i];                    //запоминаем элемент который под индексом i
            int j = i;

            //мы идем по отсортированной последовательности и одновременно сдвигаем элементы вправо
            resultCompare++;
            while (j > 0 && mass[j - 1] > current) { // пока элемент в массиве больше чем элемент который мы сохранили осуществляем сдвиг
                resultCompare++;
                resultSwap++;
                mass[j] = mass[j - 1];//перемещаем элемент
                j--; // продолжаем перебор уменьшая на единицу
            }

            //когда цикл выполнится, элементы которые нужно будет уже сдвинуты в сторону и j будет указывать на ячейку, которую
            //нужно вставить элемент для окончания сортировки
            mass[j] = current;
        }
        return new int[]{resultSwap, resultCompare};
    }



    private static int[] SortSelect(int[] mass) {
        int resultSwap = 0;
        int resultCompare = 0;
        for (int min = 0; min < mass.length; min++) {//внешний цикл для перебора элементов
            int minIndex = min; // находим минимальный элемент

            for (int j = min + 1; j < mass.length; j++) { //внутренний цикл, который находит индекс следующего минимального элемента
                resultCompare++;
                if (mass[j] < mass[minIndex]) {
                    minIndex = j;
                }// в минИндекс присваиваем индекс наименьшего элемента массива
            }
            //меняем местами
            int tmp = mass[min];
            mass[min] = mass[minIndex];
            mass[minIndex] = tmp;
            resultSwap += 2;
        }
        return new int[]{resultSwap, resultCompare};
    }





}