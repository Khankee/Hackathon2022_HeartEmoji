import com.fazecast.jSerialComm.SerialPort;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.QuickChart;

import javax.swing.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;

public class HeartEmoji {
    static double[] initdata = {501,502,503,504,505,506,507,508,509,510,512,513,514,515,516,517,518,519,520,521,502,503,504,505,506,507,508,509,510,512,513,514,515,516,517,518,519,520,521,502,503,504,505,506,507,508,509,510,512,513,514,515,516,517,518,519,520,521,512,513,514,515,516,517,518,519,520,521,502,503,504,505,506,507,508,509,510,512,513,514,515,516,517,518,519,520,521,512,513,514,515,516,517,518,519,520,521,502,503,504,505,506,507,508,509,510,512,513,514,515,516,517,518,519,520,521};
    static HashMap<Integer, String> map = new HashMap<>();
    static {
        map.put(1,"Happy");
        map.put(2,"Angry");
        map.put(3,"Cry");
        map.put(4,"Bored");
    }
    public static void main(String[] args) throws Exception{
        int BaudRate = 9600;
        int DataBits = 8;
        int StopBits = SerialPort.ONE_STOP_BIT;
        int Parity   = SerialPort.NO_PARITY;

        SerialPort port = SerialPort.getCommPort("COM5");

        port.setComPortParameters(BaudRate,DataBits,StopBits,Parity);
        port.setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING,1000,0);

        System.out.println(port.getSystemPortName());

        //Emotion Panel
        JFrame frame = new ImageViewerFrame();
        frame.setTitle("Emotion Viewer App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        port.openPort();

        final XYChart chart = QuickChart.getChart("Heart Rate app", "Time", "Rate", "heart", null, initdata);
        final SwingWrapper<XYChart> sw = new SwingWrapper<>(chart);
        sw.displayChart();

        try{
            while (true) {
                byte[] readBuffer = new byte[10];
                port.readBytes(readBuffer, readBuffer.length);
                String Line = new String(readBuffer, StandardCharsets.US_ASCII);
                Line = Line.replaceAll("[\\r\\n]", " ");
                String[] split = Line.split("  ");
                double newData = 0;
                for(String number : split){
                    try{
                        newData = Double.parseDouble(number);
                    }catch (Exception ignored){}
                    if(newData < 100)continue;
                    if(newData > 1000)continue;
                    if(newData > 500 && newData < 650)((ImageViewerFrame) frame).changeNameXO(map.get(1));
                    if(newData > 400 && newData < 500)((ImageViewerFrame) frame).changeNameXO(map.get(4));
                    if(newData < 399)((ImageViewerFrame) frame).changeNameXO(map.get(3));
                    if(newData > 651)((ImageViewerFrame) frame).changeNameXO(map.get(2));
                    update();
                    System.out.println(Arrays.toString(initdata));
                    initdata[initdata.length - 1] = newData;
                    chart.updateXYSeries("heart", null, initdata, null);
                }
                sw.repaintChart();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        port.closePort();

    }

    private static void update(){
        if (initdata.length - 1 >= 0) System.arraycopy(initdata, 1, initdata, 0, initdata.length - 1);
    }
}
