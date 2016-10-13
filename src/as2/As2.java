//Assignment 2 
//COP 4600
//Lucas Gillespie & Michael Colluciello

package as2;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Scanner;


public class As2 {
	


	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		// Open the file	
		FileInputStream fstream = new FileInputStream("processes.in");
		BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
		
		Scanner in = new Scanner(br);
		
		PrintWriter writer = new PrintWriter("processes.out", "UTF-8");
		
		
		//Parse input file
		nexti(in);
			
		int processCount=in.nextInt();
		process[] processArray= new process[processCount];
		in.nextLine(); nexti(in);
		int runfor = in.nextInt();
		in.nextLine();nexti(in);
		String type= in.next();
		in.nextLine();
		
		String inTemp = in.next();
		int quantum =0;
		
		//Special case to skip # when quantum might be next
		while(inTemp.equals("#")){
			in.nextLine();
			inTemp = in.next();
		};
		
		//Handle omission of quantum line
		if(inTemp.equals("quantum")){
			quantum =in.nextInt();	
			in.nextLine();
		}
		
		
		//Print data parsed from file
		writer.println(processCount + " processes");
		writer.print("Using ");
		
		//Switch for printing type
	    switch (type) {
        case "fcfs":
        	writer.println("First Come First Serve");
            break;
        case "sjf":
        	writer.println("Shortest Job First");
            break;
        case "rr":
        	writer.println("Round-Robin");
        	writer.println("Quantum "+quantum);
            break;

        default:
        	writer.println("Invalid type selection: IGNORE OUTPUT");
	    }
	    

		//Read in processes to array of objects	
		for(int i=0; i<processCount;i++){
			processArray[i]= new process();
			if(i!=0)
				in.next();
			in.next();
			processArray[i].setName(in.next());
			in.next();
			processArray[i].setArrival(in.nextInt());
			in.next();
			processArray[i].setBurst(in.nextInt());
			processArray[i].setbTemp(processArray[i].getBurst());
		}
		
		//Sorted Array
		process[] sArray= new process[processCount];
		
		int cIndex=0;
		//Sort processes in order of arrival time
		for(int i=0; i<runfor;i++){
			for(int p =0;p<processCount;p++){
				if(processArray[p].getArrival()==i){
					sArray[cIndex]=processArray[p];
					cIndex++;
				}
			}
		}
		writer.println();

		//If statement logic for choosing an algorithm
		if(type.equals("fcfs"))
			firstComeFirstServe(sArray, runfor, writer);
		if(type.equals("sjf"))
			shortestJobFirst(sArray, runfor, writer);
		if(type.equals("rr"))
			roundRobin(sArray,runfor,quantum,writer);
		
		
		writer.println();		
		//USE FOR ALL
		//List wait (for the text file) and turnaround times after completion (using original array for in order listing)
		for(int p=0;p<processCount;p++){
			writer.println(processArray[p].getName()+" wait "+processArray[p].getWait()+" turnaround "+(processArray[p].getbTemp()+processArray[p].getWait()));
		}
		
		writer.close();
		in.close();

	}
	
	//Round-robin algorithm (Lucas Gillespie)
	public static void roundRobin(process sArray[],int runfor, int quantum,PrintWriter writer){
		
		int cProcess =0,aProcess=0,runUntil=0,in=0,done=0; boolean arrived=false,finished = false;
		for(int t=0;t<runfor+1;t++){
			//Arrival Handling
			if(in!=sArray.length){
				for(int p=0;p<sArray.length;p++){
					if(sArray[p].getArrival()==t){
						writer.println("Time "+t+": "+sArray[p].getName()+" Arrived");
						arrived=true;
						aProcess= p;				
						in++;
					}
				}
			}
			//If process arrives after zero set runUntil
			if(in==1&&arrived==true)
				runUntil =t;
			
			//Run until all processes have finished
			if((done!=sArray.length)){
				//Process Switching
				if((runUntil == t)&&(sArray[cProcess].getBurst()!=0)){
 
					if ((cProcess<in-done-1)&&(t!=0)){
						cProcess++;
					}else{
						cProcess=0;
					}
				}
				
				//Process finish handling
				if(sArray[cProcess].getBurst()==0){
					
					//Set wait using calculation
					sArray[cProcess].setWait(t-(sArray[cProcess].getbTemp()+sArray[cProcess].getArrival()));
					writer.println("Time "+t+": "+sArray[cProcess].getName()+" Finished");
					
					//increment number of completed processes
					done++;
					//Remove completed process from array of objects
					removeElement(sArray,cProcess);
					
					finished =true;
					
					//Go to next process
					if ((cProcess<in-done-1)&&(t!=0)){
						cProcess++;
					}else{
						cProcess=0;
					}
				}
			
			
				//Process Selection
				if(((runUntil == t)||(finished==true))&&(in!=0)){
					if((sArray[cProcess].getBurst()!=0)&&((in!=0)||(arrived==true)))
						writer.println("Time "+t+": "+sArray[cProcess].getName()+" Selected (burst "+sArray[cProcess].getBurst()+")");
					runUntil = runUntil + quantum; //Increase runUntil by quantum  for next switch
					//System.out.println(t + " " + runUntil);
					arrived=false; 
					finished =false;
				}
			
			
			
				//Burst Decrement & Process Finish
				if((sArray[cProcess].getBurst()!=0)&&(in!=0)){
					sArray[cProcess].burst--;
				}
				
				//Idle if no processes are here or they are all finsihed
				if((done==sArray.length)||(in==0))
					writer.println("Time "+t+": Idle");
				
				
				
			
			}else{
				//Idle if processes finished
				if(t!=runfor)
					writer.println("Time "+t+": Idle");
				
			}
		}
		writer.println("Finished at time " + runfor);
		System.out.println();

	}
	//first come first serve (array sorted by arrival time in main)
		public static void firstComeFirstServe(process[] pArray, int runfor, PrintWriter writer) {
			
			
			int time = pArray[0].getArrival();
			int aIndex = 0; //this will stay on what's running
			int arriveIndex = 0; //this will stay on what's arriving
			boolean idle = true;
			process current = null;
			
			while(time <= runfor) {
				
				//When something arrives, state it has arrived, and if something is running, say that it is still running
				if(time == pArray[arriveIndex].getArrival()) {
					writer.println("Time " + time + ": " + pArray[arriveIndex].getName() + " arrived");
					if(arriveIndex < pArray.length - 1) {
						arriveIndex++;
					}
					if(current != null) {
						writer.println("Time " + time + ": " + current.getName() + " selected (burst " + current.getBurst() + ")");
					}
				}
				
				//If nothing has run yet, run the first thing that arrived
				if(current == null) {
					current = pArray[aIndex];
					writer.println("Time " + time + ": " + current.getName() + " selected (burst " + current.getBurst() + ")");
					idle = false;
				
				}
				
				//decrement the burst remaining in the process on the one thats running every time cycle
				if(current.getBurst() > 0 && time != pArray[0].getArrival()) {
					current.setBurst(current.getBurst() - 1 );
				
				}
				
				//a process has ended, compute it's wait time, if we're at the end of the queue, set the idle flag
				if(current.getBurst() == 0) {
					pArray[aIndex].setWait(time - (pArray[aIndex].getbTemp() + pArray[aIndex].getArrival()));
					writer.println("Time " + time + ": " + current.getName() + " finished");
					if(aIndex == pArray.length - 1) {
						idle = true;
					}
					else {
						aIndex++;
						current = pArray[aIndex];
						writer.println("Time " + time + ": " + current.getName() + " selected (burst " + current.getBurst() + ")");
						idle = false;
					}
				}
				
				//if we're idle and at the end of the ready queue, idle till the end
				if(aIndex == pArray.length - 1 && idle) {
					for(int i = 0; i <= runfor - time; i++){
						writer.println("Time " + (time + i) + ": idle");
					}
					time = runfor;
					break;
				}
				if(time == runfor){
					break; //fixes an edge case of improper printout
				}
				time++;
				
				
				
			}
			writer.println();
			writer.println("Finished at time " + time);
			
			
		}
		
		//Shortest job first (array sorted by arrival time in main)
		public static void shortestJobFirst(process[] pArray, int runfor, PrintWriter writer) {
			
			int time = pArray[0].getArrival();
			int completed = 0; //tracks what has completed
			int arriveIndex = 0; //this will stay on what's arriving
			process[] tempArray = pArray; //array we can decrement burst times on
			int shortest = 0; //tracks index of the shortest job
			boolean idle = true;
			process current = null;
			
			while(time <= runfor) {
				
				//When something arrives, state it has arrived, and if something is running, compare bursts and re-assign current if necessary
				if(time == pArray[arriveIndex].getArrival()) {
					writer.println("Time " + time + ": " + pArray[arriveIndex].getName() + " arrived");
					if(arriveIndex < pArray.length - 1) {
						arriveIndex++;
					}
					if(current != null) {
						for (int i = 0; i <= arriveIndex; i++) {
							if(tempArray[i].getBurst() == 0) {
								continue;
							}
							if(tempArray[shortest].getBurst() > tempArray[i].getBurst()) {
								shortest = i;
							}
						}
						current = tempArray[shortest];
						if(current.getBurst() != 0){
							writer.println("Time " + time + ": " + current.getName() + " selected (burst " + current.getBurst() + ")");
						}
					}
				}
				
				//If nothing has run yet, run the first thing that arrived
				if(current == null) {
					current = pArray[completed];
					writer.println("Time " + time + ": " + current.getName() + " selected (burst " + current.getBurst() + ")");
					idle = false;
				
				}
				

				
				//a process has ended, compute it's wait time, if we're at the end of the queue, set the idle flag
				if(current.getBurst() == 0) {
					pArray[shortest].setWait(time - (tempArray[shortest].getbTemp() + tempArray[shortest].getArrival()));
					writer.println("Time " + time + ": " + current.getName() + " finished");
					if(completed == pArray.length - 1) {
						idle = true;
					}
					else {
						completed++;
						
						//Set shortest to anything that hasn't completed (burst != 0)
						for (int i = 0; i <= arriveIndex ; i++) {
							if(tempArray[i].getBurst() == 0) {
								continue;
							}
							shortest = i;
						}
						
						//Go through and assign shortest to the lowest burst remaining (ignore those with burst 0)
						for (int i = 0; i < arriveIndex; i++) {
							if(tempArray[i].getBurst() == 0) {
								continue;
							}
							if(tempArray[shortest].getBurst() > tempArray[i].getBurst()) {
								shortest = i;
							}
						}
						current = tempArray[shortest];
						if(current.getBurst() != 0) {
							writer.println("Time " + time + ": " + current.getName() + " selected (burst " + current.getBurst() + ")");
						}
						
						idle = false;
					}
				}
				
				//decrement the burst remaining in the process on the one thats running every time cycle
				if(current.getBurst() > 0) {
					current.setBurst(current.getBurst() - 1 );
				
				}
				//if we're idle and at the end of the ready queue, idle till the end
				if(completed == pArray.length - 1 && idle) {
					for(int i = 0; i <= runfor - time; i++){
						writer.println("Time " + (time + i) + ": idle");
					}
					time = runfor;
					break;
				}
				if(time == runfor){
					break; //fixes an edge case of improper printout
				}
				time++;
				
				
				
			}
			writer.println();
			writer.println("Finished at time " + time);
			
			
		}
	
	public static void removeElement(Object[] a, int del) {
	    System.arraycopy(a,del+1,a,del,a.length-1-del);
	}
	
	//Function to check for comments and skip the line
	public static void nexti(Scanner in){
			
		while(in.next().equals("#")){
			in.nextLine();
		};
	

	}

}


