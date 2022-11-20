import com.fazecast.jSerialComm.SerialPort;

import java.nio.charset.StandardCharsets;

//Port Testing
//Author: Arukhan
public class PortTest {
    public static void main(String[] args) {
        System.out.println("Hello world");

        int BaudRate = 9600;
        int DataBits = 8;
        int StopBits = SerialPort.ONE_STOP_BIT;
        int Parity   = SerialPort.NO_PARITY;

        SerialPort port = SerialPort.getCommPort("COM5");

        port.setComPortParameters(BaudRate,
                                  DataBits,
                                  StopBits,
                                  Parity);

        port.setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING,
                    1000,
                    0);

        System.out.println(port.getSystemPortName());

        port.openPort();

        try{
            while (true){
                byte[] readBuffer = new byte[10];
                int numRead = port.readBytes(readBuffer,
                                             readBuffer.length);

                System.out.println("Read " + numRead + " bytes -");
                String S = new String(readBuffer, StandardCharsets.US_ASCII);
                S = S.replaceAll("[\\r\\n]", " ");
                String[] split = S.split("  ");
                double a = 0;
                for(String n : split){
                    try{
                        a = Double.parseDouble(n);
                    }catch (Exception e){
                        System.out.println(":"+ n + ":");
                    }
                    if(a < 100)continue;
                    System.out.println(a);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        port.closePort();
    }
}
