import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Stack;

public class Euler extends JPanel implements MouseListener, MouseMotionListener {
    public static Stack<Object> memory = new Stack<>();
    public static List<dot> dots = new ArrayList<>();
    public static List<line> lines = new ArrayList<>();
    public static List<line> animateLines = new ArrayList<>();
    public static int connectedGraphs = 0;
    private static boolean dragged=false;
    private static boolean existsDot=false;
    private static boolean temporaryLine = false;
    public static boolean animate = false;
    public static final int SIZE=15;
    public Euler(){
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    public static List<line> sortByEulerCircle(graph G,dot startingPoint){
        List<line> circle = findACircle(G,startingPoint);

        if (circle.size() == G.getEdges().size()){
            return circle;
        }

        graph G1 = subtractCircleFromGraph(G,circle);

        //finding a starting point
        int joinIndex=0;
        dot joiningPoint = G1.getVertices().get(joinIndex);
        while (!hasThisDot(circle,joiningPoint)){
            joiningPoint =  G1.getVertices().get(joinIndex++);
        }

        List<line> restOfTheTrack = sortByEulerCircle(G1,joiningPoint);
        List<line> eulerCircle = new ArrayList<>();
        int j=0;
        do {
            eulerCircle.add(circle.get(j)) ;
        } while (circle.get(j++).getB().getNumber() != joiningPoint.getNumber());

        eulerCircle.addAll(restOfTheTrack);

        for (int i = j; i < circle.size(); i++) {
            eulerCircle.add(circle.get(i));
        }
        return eulerCircle;
    }

    private static boolean hasThisDot(List<line> circle, dot joiningPoint) {
        for (line line : circle) {
            if (line.getA().getNumber() == joiningPoint.getNumber()) {
                return true;
            }
            if (line.getB().getNumber() == joiningPoint.getNumber()) {
                return true;
            }
        }
        return false;
    }

    private static graph subtractCircleFromGraph(graph g, List<line> circle) {
        List<line> newEdges = new ArrayList<>();
        List<dot> newVertices = new ArrayList<>();

        for (int i = 0; i < g.getEdges().size(); i++) {
            if (!hasTheSameEdgeInTheCircle(circle,g.getEdges().get(i))){
                newEdges.add(g.getEdges().get(i));
            }
        }

        for (line newEdge : newEdges) {
            if (!newVertices.contains(newEdge.getA())) {
                newVertices.add(newEdge.getA());
            }
            if (!newVertices.contains(newEdge.getB())) {
                newVertices.add(newEdge.getB());
            }
        }


        //removing pointers from all dots
        for (int i = 0; i < newVertices.size(); i++) {
            //each one from the dots runs for all the pointers
            for (int j = 0; j < newVertices.get(i).getPointers().length; j++) {
                int pointer = newVertices.get(i).getPointers()[j];
                dot pointed = dots.get(pointer);
                //if the pointer points to a dot in the circle remove that pointer
                if (hasTheSameEdgeInTheCircle(circle,new line(newVertices.get(i),pointed))){
                    dot newDot = new dot(newVertices.get(i));
                    newDot.removePointer(pointer);
                    j--;
                    newVertices.set(i,newDot);
                }
            }
        }

        return new graph(newEdges,newVertices);

    }

    //get a graph and starting point, and return a circle in the graph that no edge appears twice
    private static List<line> findACircle(graph g, dot startingPoint) {
        List<line> circle = new ArrayList<>();
        Random r = new Random();
        int randomIndex = r.nextInt(startingPoint.getPointers().length);
        dot nextDot = getDotFromDotNumber(g.getVertices(),startingPoint.getPointers()[randomIndex]);
        circle.add(new line(startingPoint,nextDot));

        while (!nextDot.equals(circle.get(0).getA())){
            randomIndex = r.nextInt(nextDot.getPointers().length);
            nextDot = getDotFromDotNumber(g.getVertices(),nextDot.getPointers()[randomIndex]);
            while (hasTheSameEdgeInTheCircle(circle,new line(circle.get(circle.size()-1).getB(),nextDot))) {
                randomIndex = r.nextInt(circle.get(circle.size()-1).getB().getPointers().length);
                nextDot = getDotFromDotNumber(g.getVertices(),circle.get(circle.size()-1).getB().getPointers()[randomIndex]);
            }
            circle.add(new line(circle.get(circle.size()-1).getB(),nextDot));
        }
        return circle;
    }

    //return a dot pointer from a number if number doesn't exist return null
    private static dot getDotFromDotNumber(List<dot> vertices, int dotNumber) {
        for (dot vertex : vertices) {
            if (vertex.getNumber() == dotNumber)
                return vertex;
        }
        return null;
    }

    private static boolean hasTheSameEdgeInTheCircle(List<line> circle, line line) {
        for (line value : circle) {
            if (value.sameLine(line)) {
                return true;
            }
        }
        return false;
    }

    private dot pressedDot(int x, int y) {
        existsDot=false;

        for (dot dot : dots) {
            if (dot.getX() + (3 * SIZE) >= x && dot.getX() - (3 * SIZE) <= x
                    && dot.getY() + (3 * SIZE) >= y && dot.getY() - (3 * SIZE) <= y) {
                existsDot = true;
                return dot;
            }
        }
        return new dot(x,y);
    }
    private dot onDot(int x, int y) {

        for (dot dot : dots) {
            if (dot.getX() + (SIZE) >= x && dot.getX() - (SIZE) <= x
                    && dot.getY() + (SIZE) >= y && dot.getY() - (SIZE) <= y) {
                return dot;
            }
        }
        return new dot(x,y);
    }
    private boolean existsDot(dot pressedDot) {
        return existsDot;
    }
    private boolean existsLine(line line) {
        for (int i = 0; i < lines.size()-1; i++) {
            if ((line.getA().equal(lines.get(i).getA()) && line.getB().equal(lines.get(i).getB()))
            || (line.getB().equal(lines.get(i).getA()) && line.getA().equal(lines.get(i).getB()))){
                return true;
            }
        }
        return false;
    }

    public static int countConnectedGraphs() {
        int counter=0;
        boolean[] checkedDots = new boolean[dots.size()];
        for (int i = 0; i < dots.size(); i++) {
            if (!checkedDots[i]) {
                goForATrip(i,checkedDots);
                counter++;
            }
        }
        return counter;
    }
    //a recursive function that finds all the connected dots in the graph and marked them
    private static void goForATrip(int i, boolean[] checkedDots) {
        if (!checkedDots[i]) {
            checkedDots[i]=true;
            for (int j = 0; j < dots.get(i).getPointers().length; j++) {
                goForATrip(dots.get(i).getPointers()[j], checkedDots);
            }
        }
    }


    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.setColor(Color.white);
        g.fillRect(0,0,getWidth(),getHeight());


        for (dot dot : dots) {
            if (dot.getPointers().length > 0 && dot.getPointers().length % 2 == 0) {
                g.setColor(Color.black);
            } else {
                g.setColor(Color.red);
            }
            g.fillOval(dot.getX() - SIZE / 2, dot.getY() - SIZE / 2, SIZE, SIZE);
        }

        g.setColor(Color.blue);
        for (line line : lines) {
            g.drawLine(line.getA().getX(), line.getA().getY()
                    , line.getB().getX(), line.getB().getY());
        }

        if (animate){
            startAnimation(g);
        }
    }

    public void setAnimate(List<line> lines){
        animateLines=lines;
    }
    private void startAnimation(Graphics g) {
        List<line> paintedDots = new ArrayList<>();
        int[] multi = new int[dots.size()];
        int space=0;
        g.setColor(Color.red);
        g.fillOval(animateLines.get(0).getA().getX()-SIZE/2,animateLines.get(0).getA().getY()-SIZE/2,SIZE,SIZE);

        for (int i = 0; i < animateLines.size(); i++) {
            g.setColor(Color.green);
            g.drawLine(animateLines.get(i).getA().getX(),animateLines.get(i).getA().getY()
                    ,animateLines.get(i).getB().getX(),animateLines.get(i).getB().getY());

            g.setColor(new Color(35, 204, 144));
            g.fillOval(animateLines.get(i).getB().getX()-SIZE/2,animateLines.get(i).getB().getY()-SIZE/2,SIZE,SIZE);
            if (hasThisDot(paintedDots,animateLines.get(i).getB())){
                int x = multi[animateLines.get(i).getB().getNumber()]+=SIZE;
                space=25+x;
            }
            g.setColor(Color.black);
            g.drawString(String.valueOf(i+1),animateLines.get(i).getB().getX()-SIZE/3,animateLines.get(i).getB().getY()-SIZE+space);
            space=0;
            paintedDots.add(animateLines.get(i));
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    //add a new dot or stretch a new line
    @Override
    public void mousePressed(MouseEvent e) {
        temporaryLine = false;

        if (!existsDot(pressedDot(e.getX(),e.getY()))) {
            dots.add(new dot(e.getX(), e.getY(),dots.size()));
            memory.add(dots.get(dots.size()-1));
            main.dotLabel.setText("Vertices: "+dots.size());
            main.frame.add(main.dotLabel);
        }
        else {
            lines.add(new line(pressedDot(e.getX(),e.getY()),new dot(e.getX(),e.getY())));
            temporaryLine = true;
        }

        repaint();

    }

    //add a new line if it released at the right place
    @Override
    public void mouseReleased(MouseEvent e) {
        if (dragged) {
            dragged = false;
            dot pressedDot = pressedDot(e.getX(), e.getY());
            lines.get(lines.size() - 1).setB(pressedDot);
            if (existsDot(pressedDot) && !lines.get(lines.size()-1).getA().equal(lines.get(lines.size()-1).getB())) {
                if (!existsLine(lines.get(lines.size()-1))) {
                    memory.add(lines.get(lines.size()-1));
                    main.lineLabel.setText("Edges: "+lines.size());
                    main.frame.add(main.lineLabel);
                    //add the pointers to dots A and B
                    lines.get(lines.size() - 1).getA().addPointer(lines.get(lines.size() - 1).getB().getNumber());
                    lines.get(lines.size() - 1).getB().addPointer(lines.get(lines.size() - 1).getA().getNumber());
                }
                else {
                    lines.remove(lines.size() - 1);
                }
            } else {
                lines.remove(lines.size() - 1);
            }
            repaint();
        }
        else if (temporaryLine){
            lines.remove(lines.size() - 1);
        }
        repaint();
        connectedGraphs = countConnectedGraphs();
        main.connectedLab.setText("Connected Graphs: "+connectedGraphs);

        main.frame.repaint();
    }



    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    //draw line when dragging the mouse
    @Override
    public void mouseDragged(MouseEvent e) {
        if (temporaryLine) {
            dragged = true;
            lines.get(lines.size() - 1).setB(new dot(e.getX(), e.getY()));
            repaint();
        }
    }
    private Popup popup;
    private boolean popped=false;
    private Thread t=new Thread();
    @Override
    public void mouseMoved(MouseEvent e) {
        //show the degree when the mouse pass over a dot
        if (!t.isAlive()) {
            t=new Thread(){
                @Override
                public void run() {
                    super.run();
                    try {
                        Thread.sleep(150);
                    } catch (InterruptedException ignored) {
                    }
                    for (dot dot : dots) {
                        if (onDot(e.getX(), e.getY()).equals(dot)) {
                            if (!popped) {
                                JLabel text = new JLabel("Degree: " + dot.getPointers().length);
                                popup = PopupFactory.getSharedInstance().getPopup(e.getComponent(), text, dot.getX(), dot.getY() + 80);
                                popup.show();
                                popped = true;
                            }
                            return;
                        }
                    }

                }
            };
            t.start();

        }else{
            t.stop();
        }
            if (popped) {
                popped = false;
                popup.hide();
            }
    }
//    public void generateRandomGraphAndDrawIt(){
//
//        dots.clear();
//        lines.clear();
//        memory.clear();
//        connectedGraphs = 0;
//        Random random1 = new Random();
//        int verticesNumber = random1.nextInt(10)+10;
//        for (int i = 0; i < verticesNumber; i++) {
//            int x = random1.nextInt(main.board.getWidth()-80)+15;
//            int y = random1.nextInt(main.board.getHeight()-80)+15;
//            while (dots.contains(pressedDot(x, y))) {
//                x = random1.nextInt(main.board.getWidth()-80)+15;
//                y = random1.nextInt(main.board.getHeight()-80)+15;
//            }
//            dots.add(new dot(x,y,i));
//        }
//
//        for (int i = 0; i < verticesNumber; i++) {
//            int connections = random1.nextInt(verticesNumber/6) + 2;
//            connections -= connections % 2;
//            int index = 0;
//            for (int j = 0; j < connections; j ++,index+=(verticesNumber/connections)) {
//                if (i!=index)
//                    dots.get(i).addPointer(index);
//                if (!hasTheSameEdgeInTheCircle(lines, new line(dots.get(i), dots.get(index))) && i!=index) {
//                    lines.add(new line(dots.get(i), dots.get(index)));
//                    dots.get(index).addPointer(i);
//                }
//            }
//        }
//
//        connectedGraphs = countConnectedGraphs();
//        main.connectedLab.setText("Connected Graphs: "+connectedGraphs);
//        main.lineLabel.setText("Edges: "+Euler.lines.size());
//        main.dotLabel.setText("Vertices: "+Euler.dots.size());
//        main.frame.repaint();
//    repaint();
//
//
//    }
}
