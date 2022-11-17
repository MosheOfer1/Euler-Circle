import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class main {
    public static JFrame frame = new JFrame("Euler");
    public static Euler board = new Euler();
    public static JLabel dotLabel = new JLabel("Vertices: "+Euler.dots.size());
    public static JLabel lineLabel = new JLabel("Edges: "+Euler.lines.size());
    public static JLabel connectedLab = new JLabel("Connected Graphs: "+Euler.connectedGraphs);


    public static void main(String[] args) {
        frame.setSize(900, 680);

        board.setLayout(null);
        board.setOpaque(false);
        board.setBounds(0,80,900,600);
        board.setVisible(true);

        frame.add(board);


        dotLabel.setBounds(400,10,100,30);
        frame.add(dotLabel);
        lineLabel.setBounds(500,10,100,30);
        frame.add(lineLabel);

        connectedLab.setBounds(600,10,150,30);
        frame.add(connectedLab);

        JButton backButton = new JButton("Back");
        backButton.setBounds(20,10,70,30);
        frame.add(backButton);
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!Euler.memory.empty()) {
                    //erase the last act in the stack
                    if (Euler.memory.peek() instanceof line) {
                        //remove the pointers from the dots
                        ((line) Euler.memory.peek()).getA().removePointer(((line) Euler.memory.peek()).getB().getNumber());
                        ((line) Euler.memory.peek()).getB().removePointer(((line) Euler.memory.peek()).getA().getNumber());
                        //remove the line
                        Euler.lines.remove(Euler.memory.pop());
                        Euler.connectedGraphs = Euler.countConnectedGraphs();
                    } else {
                        //delete the dot
                        Euler.dots.remove(Euler.memory.pop());
                        Euler.connectedGraphs--;
                    }
                }
                connectedLab.setText("Connected Graphs: "+Euler.connectedGraphs);
                lineLabel.setText("Edges: "+Euler.lines.size());
                dotLabel.setText("Vertices: "+Euler.dots.size());

                board.repaint();
                frame.repaint();
            }
        });

        JButton clear = new JButton("Clear");
        clear.setBounds(20,45,70,30);
        frame.add(clear);
        clear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Euler.dots.clear();
                Euler.lines.clear();
                Euler.memory.clear();
                Euler.connectedGraphs = 0;

                connectedLab.setText("Connected Graphs: "+Euler.connectedGraphs);
                lineLabel.setText("Edges: "+Euler.lines.size());
                dotLabel.setText("Vertices: "+Euler.dots.size());

                board.repaint();
                frame.repaint();
            }
        });

        JButton startAnimationButton = new JButton("start");
        startAnimationButton.setBounds(100,10,70,65);
        frame.add(startAnimationButton);
        final Thread[] thread = {new Thread()};
        startAnimationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //don't interrupt to running animation
                if (!thread[0].isAlive()) {
                    graph graph = new graph(Euler.lines, Euler.dots);
                    //if the graph is ok to start
                    if (graph.isEvenDegAndOneConnectedGraph()) {
                        //start in a random start point
                        Random random = new Random();
                        int randomIndex = random.nextInt(Euler.dots.size());
                        dot randomDot = Euler.dots.get(randomIndex);
                        List<line> euler = null;
                        try {
                            euler = Euler.sortByEulerCircle(graph, randomDot);
                        }catch (IndexOutOfBoundsException | NullPointerException exception){
                            System.out.println("trying again...");
                            actionPerformed(e);
                        }
                        for (line line : euler) {
                            System.out.println(line.toString());
                        }
                        List<line> finalEuler = euler;
                        //the animation
                        thread[0] = new Thread() {
                            @Override
                            public void run() {
                                super.run();
                                List<line> a = new ArrayList<>();
                                Euler.animate = true;
                                for (line line : finalEuler) {
                                    a.add(line);
                                    board.setAnimate(a);
                                    main.board.repaint();
                                    try {
                                        Thread.sleep(800);
                                    } catch (InterruptedException ignored) {
                                    }
                                }
                                try {
                                    Thread.sleep(1400);
                                } catch (InterruptedException ignored) {
                                }
                                Euler.animate = false;
                                board.setAnimate(null);
                                board.repaint();
                            }
                        };
                        thread[0].start();
                    }



                    //if the graph is not ok, show this dialog:
                    // The graph has to be one connected graph
                    // and all the degrees have to be even!
                    else {
                        JDialog dialog = new JDialog();
                        //dialog.setSize(100, 100);
                        dialog.setBounds(300,300,300,150);
                        JLabel label = new JLabel("The graph has to be one connected graph,");
                        JLabel label2 = new JLabel("and all the degrees have to be even!");

                        label.setBounds(0,0,300,50);
                        label2.setBounds(0,100,300,50);

                        JButton exit = new JButton("Close");
                        exit.setBounds(100,75,80,30);
                        exit.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                dialog.dispose();
                            }
                        });
                        dialog.add(exit);
                        dialog.add(label);
                        dialog.add(label2);
                        dialog.setVisible(true);
                    }
                }

            }
        });


        frame.setLayout(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
