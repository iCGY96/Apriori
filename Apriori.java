  
import java.io.BufferedReader;  
import java.io.File; 
import java.io.FileInputStream; 
import java.io.InputStreamReader;  
import java.util.ArrayList;  
import java.util.HashSet;  
import java.util.Iterator;  
import java.util.List;  

public class Apriori {  
      
    private static int MIN_SUPPROT = 0; 
    private static boolean flag = false; 
    private static List<HashSet<String>> Data = new ArrayList<HashSet<String>>();
    private static HashSet<String> C1Table = new HashSet<String>();
    private static List<HashSet<String>> CTable = new ArrayList<HashSet<String>>();
    private static List<HashSet<String>> FTable = new ArrayList<HashSet<String>>();
    
    public static String AprioriAlgorithm(double min_sup, String filePath) {  
    	
    	int num_count = 1;
    	String result = new String();

        /* ------- Read data from file ------- */
        readTxtFile(filePath);
        MIN_SUPPROT = (int) (min_sup * Data.size());  
        
        System.out.println("Start........");
        
        // start time
    	long starTime = System.currentTimeMillis();
        
        /* ------- Get the F1 stable --------- */
        GetFirstSupprotedItemset();
        
        if (FTable.isEmpty()) {
        	result = "No Supporoted!";
        	return result;
        }
        
        // Initialization 
        result = "The Minumum Support = " + String.valueOf(MIN_SUPPROT) + "\n" + "The set of frequent 1-itemsets: \n" + FTable;
  
        while(flag) {  
        	
        	num_count ++;
        	//System.out.println(FTable);
        	
            GetNextCTable();      
            
            //System.out.println(CTable);
            
            GetSupprotedItemset();  
            
            
            if (flag)
            	result = result + "\n" + "The set of frequent " + String.valueOf(num_count) + "-itemsets: \n" +  FTable;
        }
        
        // End time
        long endTime = System.currentTimeMillis();
        
        long Time = endTime - starTime;
        
        result = result + "\n" + "Spend " + Time + "ms.";
        
        Data.clear();
        
        return result;
    }  
    
    /*--------- Read data from file ----------*/
	public static void readTxtFile(String filePath) {
		
        try {
        	String encoding = "GBK";
            File file = new File(filePath);
            
            //if this file is existing
            if (file.isFile() && file.exists())
            { 
            	InputStreamReader read = new InputStreamReader(new FileInputStream(file),encoding);
                BufferedReader bufferedReader = new BufferedReader(read); 
                String lineTxt = null; 
                
                while((lineTxt = bufferedReader.readLine()) != null){
                	//System.out.println(lineTxt);
                	String [] arr = lineTxt.split("\\s+");
                	HashSet<String> tempLnSet  = new HashSet<String>();
                	for(String ss : arr) {
                		tempLnSet.add(ss);
                	} 
                	C1Table.addAll(tempLnSet);
                	Data.add(tempLnSet); 
                }
                read.close();
            }
            else {
                System.out.println("Can not find this file!");
            }
        } 
        catch (Exception e) {
            System.out.println("Error in reading file!");
            e.printStackTrace();
        }
    }
	
    /*--------- GetFirstSupprotedItemset ----------*/
    private static void GetFirstSupprotedItemset() {  
        
        boolean End = false;  
        int count = 0;
        
        Iterator<String> itr = C1Table.iterator();  

        while (itr.hasNext()) {  
        	String Item = (String) itr.next();  
        	HashSet<String> tempSet = new HashSet<String>();
        	
        	tempSet.add(Item);
        	count = CountFrequent(tempSet); 
        	
            if (count >= MIN_SUPPROT){     
            	//System.out.println(Item + " = " + count); 
            	FTable.add(tempSet); 
                End = true;  
            } 
            
            flag = End;
        }
        C1Table.clear();
    }
    
    /*--------- CountFrequent ----------*/
    private static int CountFrequent(HashSet<String> Item) {  
         
        int count = 0;  
        
        if (Item.isEmpty()) return 0;
        
        for (int i = 0; i < Data.size(); i++) {	
        	if (Data.get(i).containsAll(Item))
        		count++;
        }

        return count;  
    }
    
    /*--------- GetSupprotedItemset ----------*/
    private static void GetSupprotedItemset() {  
        
        boolean End = false;  
        int count = 0;

        FTable.clear();
        
        if (CTable.isEmpty()) {
        	flag = false;
        	return;
        }
        
        for (int i = 0; i < CTable.size(); i++) {	
        	count = CountFrequent(CTable.get(i)); 
        	
            if (count >= MIN_SUPPROT){     
            	//System.out.print(CTable.get(i));  System.out.println(count); 
            	HashSet<String> newSet = new HashSet<String>();
            	newSet = (HashSet<String>) CTable.get(i).clone();
            	FTable.add(newSet);
                End = true;  
            } 
            
            flag = End;
            CTable.get(i).clear();
        } 
        CTable.clear();
    }  
    

    
    /*--------- GetNextCTable ----------*/
	private static void GetNextCTable() {
    	
		int L1, L2;
		
		for (int i = 0; i < FTable.size(); i++) {	
			HashSet<String> tempSet = FTable.get(i);
			HashSet<String> tempSetClone = (HashSet<String>) tempSet.clone();
			L1 = tempSet.size();
			tempSet.clear();
			for (int j = i+1; j < FTable.size(); j++) {
				tempSet = (HashSet<String>) tempSetClone.clone();
				tempSet.addAll(FTable.get(j));
				L2 = tempSet.size();
				
				//System.out.println(tempSet);
				
        		if ((L2 - L1) == 1 && IsNotHave(tempSet)) {
        			CTable.add(tempSet);
        		}
        		else
        			tempSet.clear();
			}
			tempSetClone.clear();
		}
		FTable.clear();
    }  
    
    private static boolean IsNotHave(HashSet<String> tempSet) 
    {  
        for (int i = 0; i < CTable.size(); i++) {	
        	if (CTable.get(i).containsAll(tempSet))
        		return false;
        } 
        
        return true;
    }  
}  