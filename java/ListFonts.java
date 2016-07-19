import java.awt.GraphicsEnvironment;
import com.sun.media.sound.AudioSynthesizer;

public class ListFonts {
    public static void main(String args[]) {
		 ExecutionMonitor console = new ExecutionMonitor
		                                         ("SBS - Execution Monitor");
		          console.init();

		          System.out.println(System.getProperty("os.name"));

		  //AudioSynthesizer synth = new AudioSynthesizer();

        GraphicsEnvironment e = GraphicsEnvironment.getLocalGraphicsEnvironment();
        for(String font:e.getAvailableFontFamilyNames()) {
            System.out.println(font);
        }
    }
}