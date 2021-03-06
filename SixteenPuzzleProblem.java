/**
16-Puzzle
(http://blog.smartbear.com/programming/7-silly-programming-challenges-to-do-for-fun/)

Sometimes called a 15-puzzle, this is a small toy often made of plastic, with a 4×4 grid occupied by 15 pieces, each with a part of a picture or drawing on it. The game’s goal is to re-order the pieces by sliding them into the missing “hole” and complete the image. When the image is complete, the missing piece is usually in the lower right corner.

Start with a small puzzle. A 2×2 puzzle does nicely to get used to the algorithms needed. You can create your pieces as sprites, and just put the piece number on it. You can get fancy later and upload cropped images. At first, I don’t recommend trying to scramble the pieces automatically, but do try to detect when the user enters the “winning configuration.” You can then grow your puzzle to 3×3, and eventually get to a complete 16-puzzle.
*/

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.util.Scanner;
import java.util.LinkedList;
import java.util.Queue;
import java.util.ArrayList;




class ConfigHolder {
	char configMatrix[][]= null;
	
	ConfigHolder(char matrix[][]) {
		int rows= matrix.length;
		int columns= matrix[0].length;
		configMatrix= new char[rows][columns];
		for(int i= 0; i<rows; i++) {
			for(int j= 0; j<columns; j++) {
				configMatrix[i][j]= matrix[i][j];
			}
		}
	}
}




class ConfigQueue<E> {
	LinkedList<E> list= new LinkedList<E>();

	public void put(E eObject) {
		list.add(eObject);
	}
	
	public E get() {
		if(list.isEmpty()) {
			return null;
		}
		return list.removeFirst();
	}
	
	public E peek() {
		return list.getFirst();
	}

	public E last() {
		if(list.isEmpty()) {
			return null;
		}
		return list.removeLast();
	}	
	public boolean isEmpty() {
		return list.isEmpty();
	}
}






class SixteenPuzzleProblem {
	private char matrix[][]= null;
	private char goal[][]= null;
	private int rows= 0;
	private int columns= 0;
	private static ConfigQueue<ConfigHolder> config= null;
	private static ArrayList<ConfigHolder> visit= null;
	private boolean flag= false;
	
	SixteenPuzzleProblem() {
		config= new ConfigQueue<ConfigHolder>();
		visit= new ArrayList<ConfigHolder>();
	}
	
	private boolean checkVisited(ConfigHolder cfgHdrObject) {
		boolean tempFlag= false;
		for(int i= 0; i<visit.size(); i++) {
			tempFlag= true;
			for(int j= 0; j<rows; j++) {
				for(int k= 0; k<columns; k++) {
					if( visit.get(i).configMatrix[j][k]!=cfgHdrObject.configMatrix[j][k] ) {
						tempFlag= false;
					}
				}
			}
			if(tempFlag) {
//				System.out.println("Checker: true ");
				return true;
			}
		}
//		System.out.println("Checker: flase ");
		return false;
/*		
		
		
		if (visit.contains(cfgHdrObject.configMatrix[j][k])) {
			System.out.println("Checker: true ");
			return true;
		}
		System.out.println("Checker: flase ");
		return false;
*/	}
	
	private void swap(char tempMatrix[][],int row1, int column1, int row2, int column2) {
		char temp= tempMatrix[row1][column1];
		tempMatrix[row1][column1]= tempMatrix[row2][column2];
		tempMatrix[row2][column2]= temp;
	}
	
	private void configRight(char tempMatrix[][], int blankX,int blankY) {
		swap(tempMatrix, blankX, blankY, blankX, blankY+1);
		//add to config Queue
		config.put(new ConfigHolder(tempMatrix));
		//restore back to previous state
		swap(tempMatrix, blankX, blankY+1, blankX, blankY);
	}
	
	private void configLeft(char tempMatrix[][], int blankX,int blankY) {
		swap(tempMatrix, blankX, blankY, blankX, blankY-1);
		//add to config Queue
		config.put(new ConfigHolder(tempMatrix));
		//restore back to previous state
		swap(tempMatrix, blankX, blankY-1, blankX, blankY);
	}
	
	private void configTop(char tempMatrix[][], int blankX,int blankY) {
		swap(tempMatrix, blankX, blankY, blankX-1, blankY);
		//add to config Queue
		config.put(new ConfigHolder(tempMatrix));
		//restore back to previous state
		swap(tempMatrix, blankX-1, blankY, blankX, blankY);
	}
	
	private void configBottom(char tempMatrix[][], int blankX,int blankY) {
		swap(tempMatrix, blankX, blankY, blankX+1, blankY);
		//add to config Queue
		config.put(new ConfigHolder(tempMatrix));
		//restore back to previous state
		swap(tempMatrix, blankX+1, blankY, blankX, blankY);
	}
	
	
	private void solver(char tempMatrix[][]) {
		int blankX= 0, blankY= 0;
	
		// finding empty block
		for (int i= 0; i<rows; i++) {
			for (int j= 0; j<columns; j++) {
				if (tempMatrix[i][j]== '.') {
					blankX= i;
					blankY= j;
				}
			}
		}
	
		//non borderd blank
		if(blankX>0 && blankX<rows-1 && blankY>0 && blankY<columns-1) {
			//configure to move Top and restore to original
			configTop(tempMatrix, blankX, blankY);
			//configure to move Bottom and restore to original
			configBottom(tempMatrix, blankX, blankY);
			//configure to move Left and restore to original
			configLeft(tempMatrix, blankX, blankY);
			//configure to move Right and restore to original
			configRight(tempMatrix, blankX, blankY);
		}
		
		//upper-edge
		else if(blankX== 0 && blankY!= 0 && blankY!= columns-1) {
			//configure to move Left and restore to original
			configLeft(tempMatrix, blankX, blankY);
			//configure to move Right and restore to original
			configRight(tempMatrix, blankX, blankY);
			//configure to move Bottom and restore to original
			configBottom(tempMatrix, blankX, blankY);
		}
		
		//lower-edge
		else if(blankX== rows-1 && blankY!= 0 && blankY!= columns-1) {
			//configure to move Left and restore to original
			configLeft(tempMatrix, blankX, blankY);
			//configure to move Right and restore to original
			configRight(tempMatrix, blankX, blankY);
			//configure to move Top and restore to original
			configTop(tempMatrix, blankX, blankY);
		}
		
		//left-edge
		else if(blankY== 0 && blankX!= 0 && blankX!= rows-1) {
			//configure to move Right and restore to original
			configRight(tempMatrix, blankX, blankY);
			//configure to move Top and restore to original
			configTop(tempMatrix, blankX, blankY);
			//configure to move Bottom and restore to original
			configBottom(tempMatrix, blankX, blankY);
		}
		
		//right-edge
		else if(blankY== columns-1 && blankX!= 0 && blankX!= rows-1) {
			//configure to move Left and restore to original
			configLeft(tempMatrix, blankX, blankY);
			//configure to move Top and restore to original
			configTop(tempMatrix, blankX, blankY);
			//configure to move Bottom and restore to original
			configBottom(tempMatrix, blankX, blankY);
			
		}
		
		//left-upper corner
		else if (blankX== 0 && blankY== 0) {
			//configure to move Right and restore to original
			configRight(tempMatrix, blankX, blankY);
			//configure to move Bottom and restore to original
			configBottom(tempMatrix, blankX, blankY);
		}

		//lower-left corner
		else if (blankX== rows-1 && blankY== 0) {
			//configure to move Right and restore to original
			configRight(tempMatrix, blankX, blankY);
			//configure to move Top and restore to original
			configTop(tempMatrix, blankX, blankY);
		}
		
		//lower-right corner
		else if (blankX== rows-1 && blankY==  columns-1) {
			//configure to move Left and restore to original
			configLeft(tempMatrix, blankX, blankY);
			//configure to move Top and restore to original
			configTop(tempMatrix, blankX, blankY);
		}
		
		//lower-right corner
		else if (blankX== 0 && blankY==  columns-1) {
			//configure to move Left and restore to original
			configLeft(tempMatrix, blankX, blankY);
			//configure to move Bottom and restore to original
			configBottom(tempMatrix, blankX, blankY);
		}	
	}
	
	private boolean checkGoal(char tempMartix[][]) {
		for (int i= 0; i<rows; i++) {
			for (int j= 0; j<columns; j++) {
				if (tempMartix[i][j]!= goal[i][j]) {
					return false;
				}
			}
		}
		return true;
	}
	
	
	private void readFile(BufferedReader bffRdrObject) {
		String line= null;
		try {
			line= bffRdrObject.readLine();
		}
		catch (Exception e) {
			System.out.println("Problem reading line");
		}
		if(line!= null) {
			rows= Integer.parseInt(""+(line.charAt(0)));
			columns= Integer.parseInt(""+(line.charAt(2)));
			matrix= new char[rows][columns];
			goal= new char[rows][columns];
		}
		try {
			line= bffRdrObject.readLine();
		}
		catch (Exception e) {
			System.out.println("Problem reading line");
		}
		for(int i=0; i<rows; i++) {
			for(int j=0; j<columns; j++) {
				matrix[i][j]= line.charAt(j*2);
				System.out.print(matrix[i][j]+" ");
			}
			System.out.println();
			try {
			line= bffRdrObject.readLine();
			}
			catch (Exception e) {
				System.out.println("Problem reading line");
			}
		}
		for(int i=0; i<rows; i++) {
			for(int j=0; j<columns; j++) {
				goal[i][j]= line.charAt(j*2);
				System.out.print(goal[i][j]+" ");
			}
			System.out.println();
			try {
			line= bffRdrObject.readLine();
			}
			catch (Exception e) {
				System.out.println("Problem reading line");
			}
		}
		try {
			line= bffRdrObject.readLine();
		}
		catch (Exception e) {
			System.out.println("Problem reading line");
		}
	}

	
	public static void main(String args[]) {
		char choice= ' ';
		boolean checked= true;
		Scanner scrObject= new Scanner(System.in);
		SixteenPuzzleProblem sxtnPzlPblmObject= new SixteenPuzzleProblem();
		System.out.println("\n\n**********Sixteen Puzzle Problem**********");
		System.out.println("Menu.\na. Upload file with Initial and Goal configuration.");
		System.out.println("b. Enter Initial and Goal configuration manually");
		do {
			System.out.print("Enter your choice(a/b): ");
			try {
				choice= scrObject.next().charAt(0);
				checked= false;
			}
			catch (Exception e) {
				checked = true;
				System.out.println("Wrong Input!\n Press Y to continue, anyother key to exit: ");
				choice= scrObject.next().charAt(0);
				if(Character.toLowerCase(choice)!= 'y') {
					System.exit(0);
				}
			}
		} while(checked);
		
		switch(choice) {
			case 'a':
				FileInputStream flIptStrmObject= null;
				do {
					System.out.print("Enter filename: ");
					String fileName= scrObject.next();
					try {
						flIptStrmObject= new FileInputStream(fileName);
						checked= false;
					}
					catch(Exception e) {
					checked = true;
						System.out.println("No Such File Found!! Please try again");	
					}
				} while (checked);
				InputStreamReader iptStrmRdrObject= new InputStreamReader(flIptStrmObject);
				BufferedReader bffRdrObject= new BufferedReader(iptStrmRdrObject);
				sxtnPzlPblmObject.readFile(bffRdrObject);
				try {
					bffRdrObject.close();
				}
				catch (Exception e) {
					System.out.println("Problem closing bufferedReader");
				}
				scrObject.close();
				break;
			case 'b':
				System.out.println("Enter No of Rows: ");
				sxtnPzlPblmObject.rows= scrObject.nextInt();
				System.out.println("Enter No of columns: ");
				sxtnPzlPblmObject.columns= scrObject.nextInt();
				System.out.println("Enter Initial Configuration:");
				for(int i=0; i<sxtnPzlPblmObject.rows; i++) {
					for(int j=0; j<sxtnPzlPblmObject.columns; j++) {
						System.out.print("Position ("+i+", "+j+"): ");
						sxtnPzlPblmObject.matrix[i][j]= scrObject.next().charAt(0);
					}
				}
				System.out.println("Enter Goal Configuration:");
				for(int i=0; i<sxtnPzlPblmObject.rows; i++) {
					for(int j=0; j<sxtnPzlPblmObject.columns; j++) {
						System.out.print("Position ("+i+", "+j+"): ");
						sxtnPzlPblmObject.goal[i][j]= scrObject.next().charAt(0);
					}
				}
				break;
		}
		
		ConfigHolder cfgHdrObject= new ConfigHolder(sxtnPzlPblmObject.matrix);
		config.put(cfgHdrObject);
		while (!config.isEmpty()) {
//			System.out.println("WHILE LOOP");
			//check if goal is reached, if not then enter block otherwish skip block.
		 	if(!sxtnPzlPblmObject.checkGoal(config.peek().configMatrix)) {
//				System.out.println("GOAL IF LOOP");
				//check if configuration is visited, if not then enter block otherwise skip block
			 	if(!sxtnPzlPblmObject.checkVisited(config.peek())) {
					visit.add(config.peek());
/*					System.out.println("\n\nCONFIGURATION:");
					for(int i=0; i<sxtnPzlPblmObject.rows; i++) {
						for(int j=0; j<sxtnPzlPblmObject.columns; j++) {
							System.out.print(config.peek().configMatrix[i][j]+" ");
						}
						System.out.println();
					}
*/					sxtnPzlPblmObject.solver(config.get().configMatrix);
				}
				//if configuration vsited then pop out the configuration.
				else {
/*					System.out.println("\n\nCONFIGURATION POP:");
					for(int i=0; i<sxtnPzlPblmObject.rows; i++) {
						for(int j=0; j<sxtnPzlPblmObject.columns; j++) {
							System.out.print(config.peek().configMatrix[i][j]+" ");
						}
						System.out.println();
					}
*/					config.get();
				}
			}
			//if goal reached
			else {
				System.out.println("Goal Reached!!!");
				
				sxtnPzlPblmObject.matrix= config.last().configMatrix;				
				
					for(int i=0; i<sxtnPzlPblmObject.rows; i++) {
						for(int j=0; j<sxtnPzlPblmObject.columns; j++) {
							System.out.print(config.peek().configMatrix[i][j]+" ");
						}
						System.out.println();
					}
				break;
			}
		}
		

	}


}
