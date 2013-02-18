package edu.kpi.fbp.primitives;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class imageArrow extends JPanel{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private BufferedImage image;

    public imageArrow() {
       try {                
    	   image = ImageIO.read(new File("img/arrow.png"));
       } catch (IOException ex) {
    	   ex.printStackTrace();
       }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, null); // see javadoc for more info on the parameters            
    }

}