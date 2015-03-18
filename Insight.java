import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.SortedMap;
import java.util.TreeMap;

public class Insight {
	
	//Dictionary is used to store the different words and its frequencies in the files.
	static SortedMap<String, Integer> dictionary = new TreeMap<>();
	//wordsInline indicated the number of words per line, which will be used to calculate median.
	static List<Integer> wordsInline = new ArrayList<Integer>();
	
	public static void main(String[] args) {
		System.out.println("Begin...");
		readfile("wc_input");
		writefile("wc_output");
		System.out.println("Finished...");
	}

	private static void readfile(String filepath) {
		// TODO Auto-generated method stub		
		File file = new File(filepath);
		//Give reminder in case the path doesn't exist...
        if (!file.isDirectory()) {
            System.out.println("Error:The path does not exsit...");
        }else{
        	//The code below achieve the demand to sort multiple files in alphabetical order
            String[] filelist = file.list(); 
            //Count words on each file
            for (int i = 0; i < filelist.length; i++) {           	
            	System.out.println("Prcoess the article: " + filelist[i]);
                File readfile = new File(filepath + "//" + filelist[i]);
                wordcount(readfile);                
            }
        }   	
	}

	private static void wordcount(File readfile) {
		// TODO Auto-generated method stub
		try {
			Scanner sc = new Scanner(readfile);
			while(sc.hasNextLine()){				
				String line = sc.nextLine();
				//If there exists blank line in the file, set its #words as 0
				if(line.equals("")){
					wordsInline.add(0);
				}else{
					//Only recognize the English lower/upper letter and number. Neglect other languages and punctuation.
					String[] words = line.replaceAll("[^a-zA-Z0-9 ]","").toLowerCase().split(" +");
					wordsInline.add(words.length);
					for(String word : words){			
						if(dictionary.containsKey(word)){
							dictionary.put(word, dictionary.get(word) + 1);						
						}else{
							dictionary.put(word, 1);						
						}
					}
				}				
			}
		}catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	private static void writefile(String path) {
		// TODO Auto-generated method stub
		File output = new File(path);
		output.mkdir();
		File result1 = new File("wc_result");
		File result2 = new File("med_result");
		try {
			//Write frequencies of each word into the file "wc_result"
			PrintWriter w1 = new PrintWriter(new FileWriter(path + '/' + result1));
			for(String word : dictionary.keySet()){
				w1.println(word + '\t' + dictionary.get(word));
			}
			w1.close();
			//Write median number of words per line into file "mde_result"
			PrintWriter w2 = new PrintWriter(new FileWriter(path + '/' + result2));
			List<Integer> list = new ArrayList<Integer>();
			//Sort the #words of a line in ascend order and find the median
			for(int i = 0; i < wordsInline.size(); i ++){
				list.add(wordsInline.get(i));
				Collections.sort(list);
				int median = list.size() / 2;
				if(list.size() % 2 == 0){
					w2.println(((float)list.get(median) + (float)list.get(median - 1))/2);
				}else{
					w2.println((float)list.get(median));
				}
			}
			w2.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
