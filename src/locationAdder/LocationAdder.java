package testSwing;
 
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
 
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.LayoutStyle.ComponentPlacement;
 
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Vector;
 
import javax.swing.JLabel;
 
 
 
public class LocationAdder extends JFrame  {
    final int MAX_SAMPLES = 1000;
    final int DIV = 500;
 
    private JPanel contentPane;
    private JTextField xValue;
    private JTextField yValue;
    private JTextField locationName;
    private PaintPanel inkPanel;
    private JPanel drowPanel;
    //++++
    private Point[] stroke;
    private int sampleCount;
    private int strokeCount;
    //++++
    //++++
    //+++
    private BufferedImage image;
    private int clickCount = 0;
    //+++
    //++ click tracking
    private Point previousClick;
    private Point thisClick;
    private boolean signal = false; //
    //++
    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    LocationAdder frame = new LocationAdder();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
 
    /**
     * Create the frame.
     */
    private long map(long x, long in_min, long in_max, long out_min, long out_max)
    {
      return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
    }
    public LocationAdder() {
         
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);
        //++++
        try {
            image = ImageIO.read(new File("./src/map.jpg"));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            //e.printStackTrace();
            System.out.println("File not found");
        }
        //++++
        //++++
        inkPanel = new PaintPanel(image);
        stroke = new Point[MAX_SAMPLES];
        //++++
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.GREEN);
        contentPane.add(buttonPanel, BorderLayout.EAST);
         
        JLabel lblXvalue = new JLabel("xValue:  ");
         
        xValue = new JTextField();
        xValue.setEditable(false);
        xValue.setColumns(10);
         
        JLabel lblYvalue = new JLabel("yValue:");
         
        yValue = new JTextField();
        yValue.setEditable(false);
        yValue.setColumns(10);
         
        JLabel lblLocationName = new JLabel("Location Name:");
         
        locationName = new JTextField();
        locationName.setColumns(10);
        //
        JButton locationN = new JButton("Add");
        //adding listeners
        locationN.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) 
            {
                String str = locationName.getText();
               // System.out.println(str + "+++c");
                if (signal == true && str != "")
                {
                    //update signal 
                    signal = false;
                    //get the middle of the rectangle
                    int x = (int) ((previousClick.getX() + thisClick.getX()) / 2);
                    int y = (int) ((previousClick.getY() + thisClick.getY()) / 2);
                    //map the values from their respected x and y to 0 and div
                    int xVal =  (int) map(x, 0, inkPanel.getWidth(), 0, DIV);
                    int yVal =  (int) map(y, 0, inkPanel.getHeight(), 0, DIV);
                  
                    //get the String in the textfield
                    String name = locationName.getText();
                    locationName.setText("");
                    //store them in the data base
                    System.out.println(name + " " + xVal + "," + yVal);
                }
            }
        });
         
        inkPanel.addMouseMotionListener(new MouseMotionL());
        inkPanel.addMouseListener(new MouseL());
         
         
        GroupLayout gl_buttonPanel = new GroupLayout(buttonPanel);
        gl_buttonPanel.setHorizontalGroup(
            gl_buttonPanel.createParallelGroup(Alignment.LEADING)
                .addGroup(gl_buttonPanel.createSequentialGroup()
                    .addGroup(gl_buttonPanel.createParallelGroup(Alignment.LEADING)
                        .addGroup(gl_buttonPanel.createSequentialGroup()
                            .addGap(23)
                            .addGroup(gl_buttonPanel.createParallelGroup(Alignment.LEADING)
                                .addComponent(lblLocationName)
                                .addComponent(yValue, GroupLayout.PREFERRED_SIZE, 73, GroupLayout.PREFERRED_SIZE)
                                .addComponent(lblYvalue)
                                .addComponent(xValue, GroupLayout.PREFERRED_SIZE, 73, GroupLayout.PREFERRED_SIZE)
                                .addComponent(lblXvalue)))
                        .addGroup(gl_buttonPanel.createSequentialGroup()
                            .addGap(20)
                            .addComponent(locationN, GroupLayout.PREFERRED_SIZE, 76, GroupLayout.PREFERRED_SIZE)))
                    .addContainerGap(19, Short.MAX_VALUE))
                .addGroup(Alignment.TRAILING, gl_buttonPanel.createSequentialGroup()
                    .addContainerGap(20, Short.MAX_VALUE)
                    .addComponent(locationName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addContainerGap())
        );
        gl_buttonPanel.setVerticalGroup(
            gl_buttonPanel.createParallelGroup(Alignment.LEADING)
                .addGroup(gl_buttonPanel.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(lblXvalue)
                    .addPreferredGap(ComponentPlacement.RELATED)
                    .addComponent(xValue, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(ComponentPlacement.RELATED)
                    .addComponent(lblYvalue)
                    .addPreferredGap(ComponentPlacement.RELATED)
                    .addComponent(yValue, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(ComponentPlacement.RELATED)
                    .addComponent(lblLocationName)
                    .addPreferredGap(ComponentPlacement.RELATED)
                    .addComponent(locationName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(ComponentPlacement.RELATED)
                    .addComponent(locationN)
                    .addContainerGap(79, Short.MAX_VALUE))
        );
        buttonPanel.setLayout(gl_buttonPanel);
         
        drowPanel = new JPanel();
        //drowPanel.setSize(image.getWidth(), image.getHeight());
        contentPane.add(drowPanel, BorderLayout.CENTER);
        drowPanel.setLayout(new BorderLayout(0, 0));
        drowPanel.add(inkPanel, BorderLayout.CENTER);
    }
    //Implementing mouse motion listener class mouseMotionL
    class MouseMotionL implements MouseMotionListener
    {
 
        @Override
        public void mouseDragged(MouseEvent e) {
            int x =  (int) map(e.getX(), 0, inkPanel.getWidth(), 0, DIV);
            int y =  (int) map(e.getY(), 0, inkPanel.getHeight(), 0, DIV);;
             
//          if (SwingUtilities.isLeftMouseButton(e))
//          {
//              stroke[sampleCount] = new Point(x, y);
//              int x1 =  (int) map((long) stroke[sampleCount - 1].getX(), 0, DIV, 0, inkPanel.getWidth());
//              int y1 =  (int) map((long) (int) stroke[sampleCount - 1].getY(), 0, DIV, 0, inkPanel.getHeight());
//              int x2 =  (int) map((long) stroke[sampleCount].getX(), 0, DIV, 0, inkPanel.getWidth());
//              int y2 =  (int) map((long) (int) stroke[sampleCount].getY(), 0, DIV, 0, inkPanel.getHeight());
//              
//              if (sampleCount < MAX_SAMPLES - 1)
//                  ++sampleCount;
//              // draw ink trail from previous point to current point
//              inkPanel.drawInk(x1, y1, x2, y2);
//          }
        }
 
        @Override
        public void mouseMoved(MouseEvent e) 
        {
            // TODO Auto-generated method stub
            int xVal =  (int) map(e.getX(), 0, inkPanel.getWidth(), 0, DIV);
            int yVal =  (int) map(e.getY(), 0, inkPanel.getHeight(), 0, DIV);
            xValue.setText("" + xVal);
            yValue.setText(yVal + "");
             
            //locationName.setText("" + inkPanel.getHeight() + "," + inkPanel.getWidth());
             
        }
         
        private long map(long x, long in_min, long in_max, long out_min, long out_max)
        {
          return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
        }
         
    }
     
    //implements mouse listener class
    class MouseL implements MouseListener
    {
 
        private long map(long x, long in_min, long in_max, long out_min, long out_max)
        {
          return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
        }
        @Override
        public void mouseClicked(MouseEvent e) {
            // TODO Auto-generated method stub
            int xVal =  (int) map(e.getX(), 0, inkPanel.getWidth(), 0, DIV);
            int yVal =  (int) map(e.getY(), 0, inkPanel.getHeight(), 0, DIV);
            //stroke[strokeCount] = new Point(xVal, yVal);
        }
 
        @Override
        public void mouseEntered(MouseEvent e) {
            // TODO Auto-generated method stub
             
        }
 
        @Override
        public void mouseExited(MouseEvent e) {
            // TODO Auto-generated method stub
             
        }
 
        @Override
        public void mousePressed(MouseEvent e) {
            // TODO Auto-generated method stub
            if (signal == false)
            {
                int x = (int) map(e.getX(), 0, inkPanel.getWidth(), 0, DIV);
                int y =  (int) map(e.getY(), 0, inkPanel.getHeight(), 0, DIV);
                stroke[sampleCount] = new Point(x, y);
                //drow a small dot whenever the leftside of the mouse is pressed
                if (SwingUtilities.isLeftMouseButton(e))
                {
                    inkPanel.drawInk(e.getX(), e.getY(), e.getX() + 1, e.getY() + 1);
                    clickCount++;
                    thisClick = new Point(e.getX(), e.getY());
                }
                //drow a rectangle when two dots are drown
                if (clickCount == 1)
                {
                    previousClick = new Point(e.getX(), e.getY());
                }
                if (clickCount >= 2) 
                {
                    //update
                    signal = true;
                    clickCount = 0;
                    //System.out.println("In");
                    inkPanel.drawRect(previousClick.getX(), previousClick.getY(), thisClick.getX(), thisClick.getY());
                }
                 
                if (sampleCount < MAX_SAMPLES - 1)
                    ++sampleCount;
            }
        }
 
        @Override
        public void mouseReleased(MouseEvent e) {
            // TODO Auto-generated method stub
            // indicate which mouse button was released
 
            if (SwingUtilities.isLeftMouseButton(e))
            {
                ++strokeCount;
                sampleCount = 0;
            }
             
        }
         
    }
     
    //Paint stuff:
    //Got it from a friend of mine
    //
    class PaintPanel extends JPanel
    {
        // the following avoids a "warning" with Java 1.5.0 complier (?)
        static final long serialVersionUID = 42L;
 
        private final Color INK_COLOR = new Color(0, 0, 128);
        private final Stroke INK_STROKE = new BasicStroke(5.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
        private BufferedImage image;
        private Vector<Line2D.Double> v;
        private Vector<Rectangle2D> rect;
 
        PaintPanel(BufferedImage img)
        {
            v = new Vector<Line2D.Double>();
            rect = new Vector<Rectangle2D>();
            //this.setBackground(Color.pink)
            this.setBorder(BorderFactory.createLineBorder(Color.gray));
            image = img;
             
        }
 
        @Override
        public void paintComponent(Graphics g)
        {
            super.paintComponent(g);
            g.drawImage(image,0,0,drowPanel.getWidth(), drowPanel.getHeight(),this);
            paintInkStrokes(g);
        }
 
        /**
         * Paint all the line segments stored in the vector
         */
        private void paintInkStrokes(Graphics g)
        {
            Graphics2D g2 = (Graphics2D) g;
 
            // set the inking color
            g2.setColor(INK_COLOR);
 
            // set the stroke thickness, and cap and join attributes ('round')
            Stroke s = g2.getStroke(); // save current stroke
            g2.setStroke(INK_STROKE); // set desired stroke
 
            // retrive each line segment and draw it
            for (int i = 0; i < v.size(); ++i)
                g2.draw((Line2D.Double) v.elementAt(i));
            //
            for (int i = 0; i < rect.size(); ++i)
                g2.draw((Rectangle2D.Double) rect.elementAt(i));
 
            g2.setStroke(s); // restore stroke
        }
 
        /**
         * Draw one line segment, then add it to the vector.
         * <p>
         */
        public void drawInk(int x1, int y1, int x2, int y2)
        {
            // get graphics context
            Graphics2D g2 = (Graphics2D) this.getGraphics();
 
            // create the line
            Line2D.Double inkSegment = new Line2D.Double(x1, y1, x2, y2);
 
            g2.setColor(INK_COLOR); // set the inking color
            //Stroke s = g2.getStroke(); // save current stroke
            g2.setStroke(INK_STROKE); // set desired stroke
            g2.draw(inkSegment); // draw it!
            //g2.setStroke(s); // restore stroke
            v.add(inkSegment); // add to vector
        }
         
        public void drawRect(double x1, double y1, double x2, double y2)
        {
            // get graphics context
            Graphics2D g2 = (Graphics2D) this.getGraphics();
            // create the rect
            Rectangle2D.Double inkSegment = new Rectangle2D.Double(x1, y1, x2 - x1, y2 - y1);
            
            g2.setColor(INK_COLOR); // set the inking color
            //Stroke s = g2.getStroke(); // save current stroke
            g2.setStroke(INK_STROKE); // set desired stroke
            g2.draw(inkSegment); // draw it!
            //g2.setStroke(s); // restore stroke
            rect.add(inkSegment); // add to vector
        }
        public void clear()
        {
            v.clear();
            this.repaint();
        }
        }
    }