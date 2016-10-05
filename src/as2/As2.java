//Assignment 2 
//COP 4600
//Lucas Gillespie & Michael Colluciello

package as2;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;


public class As2 {
	


	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		// Open the file	
		FileInputStream fstream = new FileInputStream("processes.in");
		BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
		
		Scanner in = new Scanner(br);
		
		in.next();
			
		int processCount=in.nextInt();
		process[] processArray= new process[processCount];
		in.nextLine(); in.next();
		int runfor = in.nextInt();
		in.nextLine();in.next();
		String type= in.next();
		in.nextLine();in.next();
		int quantum =in.nextInt();	
			
		in.nextLine();
			
		System.out.println(processCount + " processes");
		System.out.println("Using "+type);
		System.out.println("Quantum "+quantum);
			
		for(int i=0; i<processCount;i++){
			processArray[i]= new process();
				
			in.next();in.next();
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
		//Sort
		for(int i=0; i<runfor;i++){
			for(int p =0;p<processCount;p++){
				if(processArray[p].getArrival()==i){
					sArray[cIndex]=processArray[p];
					cIndex++;
				}
			}
		}
			
		System.out.println();
			

		
		
		roundRobin(sArray,runfor,quantum );
		
		
		
		//USE FOR ALL
		//List wait and turnaround times after completion (using original array for in order listing)
		for(int p=0;p<processCount;p++){
			System.out.println(processArray[p].getName()+" wait "+processArray[p].getWait()+" turnaround "+(processArray[p].getbTemp()+processArray[p].getWait()));
		}

	}
	
	public static void roundRobin(process sArray[],int runfor, int quantum){
		
		int cProcess =0,aProcess=0,runUntil=0,in=0,done=0; boolean arrived=false,finished = false;
		for(int t=0;t<runfor+1;t++){
			//Arrival Handling
			if(in!=sArray.length){
				for(int p=0;p<sArray.length;p++){
					if(sArray[p].getArrival()==t){
						System.out.println("Time "+t+": "+sArray[p].getName()+" Arrived");
						arrived=true;
						aProcess= p;				
						in++;
					}
				}
			}
			
			if(done!=sArray.length){
				//Process Switching
				if(runUntil == t){
 
					if ((cProcess<in-done-1)&&(t!=0)){
						cProcess++;
					}else{
						cProcess=0;
					}
				}
			
				if(sArray[cProcess].getBurst()==0){
					sArray[cProcess].setWait(t-(sArray[cProcess].getbTemp()+sArray[cProcess].getArrival()));
					System.out.println("Time "+t+": "+sArray[cProcess].getName()+" Finished");
					//System.out.println(t);
					done++;
				
					removeElement(sArray,cProcess);
				
					finished =true;
				
					if ((cProcess<in-done-1)&&(t!=0)){
						cProcess++;
					}else{
						cProcess=0;
					}
				}
			
			
				//Process Selection
				if((runUntil == t)||(finished==true)){
					if(sArray[cProcess].getBurst()!=0)
						System.out.println("Time "+t+": "+sArray[cProcess].getName()+" Selected (burst "+sArray[cProcess].getBurst()+")");
					runUntil = runUntil + quantum;
					//finished =false;
				}
			
			
			
				//Burst Decrement & Process Finish
				if(sArray[cProcess].getBurst()!=0){
					sArray[cProcess].burst--;
				}

				if(done==sArray.length)
					System.out.println("Time "+t+": Idle");
			
			}else{
				System.out.println("Finsihed at time "+t);
				break;
			}
		}
		
		System.out.println();

	}
	
	public static void removeElement(Object[] a, int del) {
	    System.arraycopy(a,del+1,a,del,a.length-1-del);
	}

}


