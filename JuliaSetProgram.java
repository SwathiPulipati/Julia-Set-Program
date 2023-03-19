import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.*;

public class JuliaSetProgram extends JPanel implements AdjustmentListener, ActionListener{
    JFrame frame;
	BufferedImage juliaImage, mandelImage;
	boolean juliaOrMandel = true;    // true = julia, false = mandel

    double a = 0, b = 0;
    float maxIter = 300, foreHue = 0, backHue = 0, sat = 1, bright = 1, zoom = 1;
    int eq = 0;
    JScrollBar aBar, bBar, itBar, foreHueBar, backHueBar, satBar, brightBar, zoomBar;
	JScrollPane scrollPane;
	JPanel scrollPanel, labelPanel, buttonPanel, onePanelToRuleThemAll;
	JLabel aLabel, bLabel, itLabel, foreHueLabel, backHueLabel, satLabel, brightLabel, zoomLabel;
	JButton saveButton, resetButton, switchJuliaMandelButton;

    public JuliaSetProgram(){
        frame = new JFrame("Julia Set Program");
		frame.add(this);
		frame.setSize(1000,600);

        GridLayout barLayout = new GridLayout(8,1);
		BorderLayout oneLayoutToRuleThemAll = new BorderLayout();

		scrollPane = new JScrollPane(this, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		frame.add(scrollPane, BorderLayout.CENTER);
		
//scrollbar panel
		scrollPanel = new JPanel();
		scrollPanel.setLayout(barLayout);

        aBar = new JScrollBar(JScrollBar.HORIZONTAL, 0, 0, -2000, 2000);
		bBar = new JScrollBar(JScrollBar.HORIZONTAL, 0, 0, -2000, 2000);
        itBar = new JScrollBar(JScrollBar.HORIZONTAL, 300, 0, 1, 400);
		foreHueBar = new JScrollBar(JScrollBar.HORIZONTAL, 0, 0, 0, 1000);
		backHueBar = new JScrollBar(JScrollBar.HORIZONTAL, 0, 0, 0, 1000);
		satBar = new JScrollBar(JScrollBar.HORIZONTAL, 1000, 0, 0, 1000);
		brightBar = new JScrollBar(JScrollBar.HORIZONTAL, 1000, 0, 0, 1000);
		zoomBar = new JScrollBar(JScrollBar.HORIZONTAL, 1000, 0, 0, 4000);


		aBar.addAdjustmentListener(this);
		bBar.addAdjustmentListener(this);
        itBar.addAdjustmentListener(this);
		foreHueBar.addAdjustmentListener(this);
		backHueBar.addAdjustmentListener(this);
		satBar.addAdjustmentListener(this);
		brightBar.addAdjustmentListener(this);
		zoomBar.addAdjustmentListener(this);


		scrollPanel.add(aBar);
		scrollPanel.add(bBar);
        scrollPanel.add(itBar);
		scrollPanel.add(foreHueBar);
		scrollPanel.add(backHueBar);
		scrollPanel.add(satBar);
		scrollPanel.add(brightBar);
		scrollPanel.add(zoomBar);



//label panel
		labelPanel = new JPanel();
		labelPanel.setLayout(barLayout);
        labelPanel.setPreferredSize(new Dimension(150, 125));

        Font labelFont = new Font("Calibri",Font.BOLD | Font.ITALIC, 14);

        aLabel = new JLabel("A: " +aBar.getValue()/1000.0);
		aLabel.setFont(labelFont);
		bLabel = new JLabel("B: " +bBar.getValue()/1000.0);
		bLabel.setFont(labelFont);
        itLabel = new JLabel("Iterations: " +itBar.getValue());
		itLabel.setFont(labelFont);
		foreHueLabel = new JLabel("Foreground Hue: " +foreHueBar.getValue());
		foreHueLabel.setFont(labelFont);
		backHueLabel = new JLabel("Background Hue: " +backHueBar.getValue());
		backHueLabel.setFont(labelFont);
		satLabel = new JLabel("Saturation: " +satBar.getValue());
		satLabel.setFont(labelFont);
		brightLabel = new JLabel("Brightness: " +brightBar.getValue());
		brightLabel.setFont(labelFont);
		zoomLabel = new JLabel("Zoom: " +zoomBar.getValue());
		zoomLabel.setFont(labelFont);

        labelPanel.add(aLabel);
		labelPanel.add(bLabel);
        labelPanel.add(itLabel);
		labelPanel.add(foreHueLabel);
		labelPanel.add(backHueLabel);
		labelPanel.add(satLabel);
		labelPanel.add(brightLabel);
		labelPanel.add(zoomLabel);


//button panel
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(3, 1));
		buttonPanel.setPreferredSize(new Dimension(200, 125));


		switchJuliaMandelButton = new JButton("Switch to Mandelbrot");
		switchJuliaMandelButton.addActionListener(this);
		buttonPanel.add(switchJuliaMandelButton);

		resetButton = new JButton("Reset Settings");
		resetButton.addActionListener(this);
		buttonPanel.add(resetButton);


//one panel to rule them all
		onePanelToRuleThemAll = new JPanel();
		onePanelToRuleThemAll.setLayout(oneLayoutToRuleThemAll);
		onePanelToRuleThemAll.add(labelPanel, BorderLayout.WEST);
		onePanelToRuleThemAll.add(scrollPanel, BorderLayout.CENTER);
		onePanelToRuleThemAll.add(buttonPanel, BorderLayout.EAST);

		frame.add(onePanelToRuleThemAll, BorderLayout.SOUTH);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
		if(juliaOrMandel){
			g.drawImage(drawJulia(),0,0,null);
		}else{
			g.drawImage(drawMandelbrot(),0,0,null);
		}
    }

    public void adjustmentValueChanged(AdjustmentEvent e){
        if(e.getSource() == aBar){
			a = aBar.getValue()/1000.0;
			aLabel.setText("A: " +a);
		}
		if(e.getSource() == bBar){
			b = bBar.getValue()/1000.0;
			bLabel.setText("B: " +b);
		}
        if(e.getSource() == itBar){
			maxIter = itBar.getValue();
			itLabel.setText("Iterations: " +maxIter);
		}
		if(e.getSource() == foreHueBar){
			foreHue = foreHueBar.getValue()/1000.0f;
			foreHueLabel.setText("Foreground Hue: " +foreHue);
		}
		if(e.getSource() == backHueBar){
			backHue = backHueBar.getValue()/1000.0f;
			backHueLabel.setText("Background Hue: " +backHue);
		}
		if(e.getSource() == satBar){
			sat = satBar.getValue()/1000.0f;
			satLabel.setText("Saturation: " +sat);
		}
		if(e.getSource() == brightBar){
			bright = brightBar.getValue()/1000.0f;
			brightLabel.setText("Brightness: " +bright);
		}
		if(e.getSource() == zoomBar){
			zoom = zoomBar.getValue()/1000.0f;
			zoomLabel.setText("Zoom: " +zoom);
		}
		repaint();
    }

	public void actionPerformed(ActionEvent e){
		if (e.getSource() == switchJuliaMandelButton){
			juliaOrMandel = !juliaOrMandel;
			if(juliaOrMandel)
				switchJuliaMandelButton.setText("Switch to Mandelbrot");
			else
				switchJuliaMandelButton.setText("Switch to Julia");
			repaint();
		}
		if(e.getSource() == resetButton){
			resetSettings();
		}
	}

    public BufferedImage drawJulia(){
        int w = frame.getWidth()*2;					//times two for both w and h eventually and put in a scrollview
		int h = frame.getHeight()*2;
		juliaImage = new BufferedImage(w,h,BufferedImage.TYPE_INT_RGB);	
		double zx, zy;
		for(int i=0; i<w; i++){
			for(int j=0; j<h; j++){
				zx = 1.5*((i-(0.5*w))/(0.5*zoom*w));
				zy = ((j-h/2)/(0.5*zoom*h));
                float it = maxIter;
				while((Math.pow(zx,2)+Math.pow(zy,2) < 6) && (it > 0)){
                    double calc = Math.pow(zx,2) - Math.pow(zy,2) + a;
                    zy = 2*zx*zy + b;
                    zx = calc;
                    it--;
				}

                int c;
                if(it>0)
                    c = Color.HSBtoRGB((it/maxIter)*backHue, sat, bright);
                else
                    c = Color.HSBtoRGB(foreHue, sat, bright);
                juliaImage.setRGB(i, j, c);
			}
		}
		return juliaImage;  
    }   

	public BufferedImage drawMandelbrot(){
		int w = frame.getWidth()*2;					//times two for both w and h eventually and put in a scrollview
		int h = frame.getHeight()*2;
		mandelImage = new BufferedImage(w,h,BufferedImage.TYPE_INT_RGB);	
		double zx, zy, xAdd, yAdd;
		for(int i=0; i<w; i++){
			for(int j=0; j<h; j++){
				xAdd = 1.5*((i-(0.5*w))/(0.5*zoom*w));
				yAdd = ((j-h/2)/(0.5*zoom*h));
				zx = 0;
				zy = 0;
                float it = maxIter;
				while((Math.pow(zx,2)+Math.pow(zy,2) < 6) && (it > 0)){
                    double calc = Math.pow(zx,2) - Math.pow(zy,2) + xAdd + a;
                    zy = 2*zx*zy + yAdd + b;
                    zx = calc;
                    it--;
				}

                int c;
                if(it>0)
                    c = Color.HSBtoRGB((it/maxIter)*foreHue, sat, bright);
                else
                    c = Color.HSBtoRGB(backHue, sat, bright);
                mandelImage.setRGB(i, j, c);
			}
		}
		return mandelImage;
	}

	public void resetSettings(){
		aBar.setValue(0);
		bBar.setValue(0);
        itBar.setValue(300);
		foreHueBar.setValue(0);
		backHueBar.setValue(0);
		satBar.setValue(1000);
		brightBar.setValue(1000);
		zoomBar.setValue(1000);
		juliaOrMandel = true;

		repaint();
	}

    public static void main(String[] args){
		JuliaSetProgram jsp = new JuliaSetProgram();
	}
}
