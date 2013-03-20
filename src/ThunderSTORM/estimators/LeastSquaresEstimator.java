package ThunderSTORM.estimators;

import LMA.LMA;
import ThunderSTORM.IModule;
import ThunderSTORM.Thunder_STORM;
import ThunderSTORM.estimators.PSF.PSF;
import ThunderSTORM.utils.Point;
import ij.process.FloatProcessor;
import java.util.Vector;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class LeastSquaresEstimator implements IEstimator, IModule {
    
    private int fitrad;
    private int fitrad2;
    private int fitrad_2;
    
    public LeastSquaresEstimator(int fitting_radius) {
        this.fitrad = fitting_radius;
        this.fitrad2 = fitting_radius * fitting_radius;
        this.fitrad_2 = fitting_radius / 2;
    }
    
    // TODO: this is just copy of the same method in Thunder_STORM.java
    public Vector<Point<Double>> ExponentialGaussianEstimator(FloatProcessor fp, Vector<Point> detections, PSF initial_guess) {
        Vector<Point<Double>> fits = new Vector<Point<Double>>();
        
        for(int d = 0, dm = detections.size(); d < dm; d++)
        {
            Point p = detections.elementAt(d);
            
            // params = {x0,y0,Intensity,sigma,background}
            double[] init_guess = new double[]{ p.getX().doubleValue(), p.getY().doubleValue(), fp.getPixelValue(p.roundToInteger().getX().intValue(), p.roundToInteger().getY().intValue()), 1.3, 100.0 };
            double[][] x = new double[fitrad2][2];
            double[] y = new double[fitrad2];
            for (int r = 0; r < fitrad; r++) {
                for (int c = 0; c < fitrad; c++) {
                    int idx = r * 11 + c;
                    x[idx][0] = (int) init_guess[0] + c - fitrad_2;  // x
                    x[idx][1] = (int) init_guess[1] + r - fitrad_2;  // y
                    y[idx] = new Float(fp.getPixelValue((int) x[idx][0], (int) x[idx][1])).doubleValue();    // G(x,y)
                }
            }
            
            LMA lma = new LMA(new Thunder_STORM.Gaussian(), init_guess, y, x);
            lma.fit();
            
            fits.add(new Point(lma.parameters[0]+0.5, lma.parameters[1]+0.5));  // 0.5px shift to the center of each pixel
        }
        
        return fits;
    }

    @Override
    public String getName() {
        return "Minimizing least squares error";
    }

    @Override
    public JPanel getOptionsPanel() {
        JPanel panel = new JPanel();
        panel.add(new JLabel("Fitting region size: "));
        panel.add(new JTextField("Fitting region size", 20));
        return panel;
    }
    
}
