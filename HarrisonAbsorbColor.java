import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.awt.event.*;

/*
 * This program will create a GUI which allows the user to play a game similar to tetris.
 */
public class HarrisonAbsorbColor extends JFrame{

	private MyPanel[][] colors;

	private final Color[] possibleColors={Color.red,Color.blue,Color.yellow,Color.green};
	private final int[] vertOffSet={-1,-1,-1,0,0,0,1,1,1};  //determines neighborhoods
	private final int[] horzOffSet={-1,0,1,-1,0,1,-1,0,1};
	private int turnsLeft = 10;
	private int points = 0;
	private JLabel message;

	//Constructor
	public HarrisonAbsorbColor(){

		int dim = Integer.parseInt(JOptionPane.showInputDialog(null,"What is the dimension of the matrix?"));

		colors = new MyPanel[dim][dim];
		setLayout(null);
		setSize(500,700);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//stores all panels in a grid layout
		JPanel colorPanel = new JPanel();
		colorPanel.setLayout(new GridLayout(dim,dim,5,5));
		colorPanel.setBackground(Color.black);
		colorPanel.setBounds(1,0,485,500);

		//the message to be displayed
		JPanel messagePanel = new JPanel();
		messagePanel.setLayout(null);
		messagePanel.setBounds(0,500,500,200);
		messagePanel.setBackground(Color.white);

		message = new JLabel("Turns Left: "+turnsLeft);
		message.setFont(new Font("Comic Sans",Font.ITALIC,20));
		message.setBounds(175,-125,175,300);
		messagePanel.add(message);

		//randomly assign colors to and make the panels
		Random r = new Random(5);
		for(int row = 0; row < dim; row++){
			for(int col = 0; col < dim; col++){
				colors[row][col]= new MyPanel(row,col);
				colors[row][col].setBackground(possibleColors[r.nextInt(possibleColors.length)]);

				colorPanel.add(colors[row][col]);
			}
		}

		add(colorPanel);
		add(messagePanel);
		setVisible(true);
	}

	//recursively changes r,c and all neighbors with toRemove
	//to white
	private void absorb(int r, int c, Color toRemove){

		if(colors[r][c].getBackground()==toRemove){

			points++;
			colors[r][c].setBackground(Color.white);
			for(int i=0;i<=8;i++){

				int row=r+horzOffSet[i];
				int col=c+vertOffSet[i];
				if(isInBounds(row,col))
					absorb(row,col,toRemove);
			}
		}
	}


	//determines if a given location is in bounds on the grid
	private boolean isInBounds(int r, int c){

		if(r<colors.length && c<colors.length && r>=0 && c>=0)
			return true;
		return false;
	}

	//slides active colors down as low as possible
	private void slide(){

		Stack<Color> notWhite=new Stack<Color>();
		for(int c=0;c<colors.length;c++){

			for(int r=0;r<colors.length;r++){

				if(colors[r][c].getBackground()!=Color.white){

					notWhite.add(colors[r][c].getBackground());
				}

				colors[r][c].setBackground(Color.white);
			}

			int pos=1;
			while(!notWhite.isEmpty()){

				colors[colors.length-pos][c].setBackground(notWhite.pop());
				pos++;
			}
		}
	}

	//type of panel that will accept mouse input
	public class MyPanel extends JPanel implements MouseListener{
		private int row;
		private int col;

		public MyPanel(int r, int c){
			row = r;
			col = c;
			this.addMouseListener(this);
		}

		//aborbs and slides colors down
		//updates the message
		//does nothing if the game is already over
		public void mouseClicked(MouseEvent arg0) {

			if(turnsLeft>0){
				if(colors[row][col].getBackground()!=Color.white) {

					absorb(row,col,colors[row][col].getBackground());
					turnsLeft--;
					message.setText("Turns Left: "+turnsLeft);
					slide();
				}

				if(turnsLeft==0 || points==colors.length*colors.length)
					message.setText("Points: "+points);
			}
		}

		//The rest of these functions are unnecessary
		public void mouseEntered(MouseEvent arg0) {


		}

		public void mouseExited(MouseEvent arg0) {


		}

		public void mousePressed(MouseEvent arg0) {


		}
		
		public void mouseReleased(MouseEvent arg0) {


		}
	}

	public static void main(String[] args){
		new HarrisonAbsorbColor();

	}
}