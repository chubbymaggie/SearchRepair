package Repository;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import Database.EntryHandler;
import Database.EntryObject;
import Library.Utility;

public class EntryAddition {
	
	public static void addOneFile(String filePath){
		File file = new File(filePath);
		if(!file.exists()) return;
		//asssume only one method
		List<Method> methods = parse(filePath);
		for(Method method : methods){
			
			EntryObject object = covertMethodToEntry(method);
			EntryHandler.save(object);
			System.out.println(method.getName());
			System.out.println(method.getSource());
			for(String path : object.getPathConstraint().keySet())
			{
				System.out.println("path:\n" + path);
				//System.out.println("constraint:\n" + object.getPathConstraint().get(path));
				System.out.println("variable:\n" + object.getPathFormalVariables().get(path));
				System.out.println("track:\n" + object.getPathVariablesTypes().get(path));
			}
		}
	}
	

	
	private static EntryObject covertMethodToEntry(Method method) {
		EntryTranslator translator = new EntryTranslator(method);
		return translator.getEntryObject();
		
	}



	public static void addOneFolder(String dirPath){
		File dir = new File(dirPath);
		if(!dir.exists()) return;
		for(String path : dir.list()){
			addOneFile(dirPath + "/" + path);
		}
	}
	

	
	private static List<Method> parse(String fileName){
		//assume only one method
		List<Method> methods = new ArrayList<Method>();
		try {
			String com = "./executors/pathgen " + fileName;

 			Process p = Runtime.getRuntime().exec(com);
			BufferedReader ls_in = new BufferedReader(new InputStreamReader(
					p.getInputStream()));
			String s;
			StringBuilder path = new StringBuilder();
			StringBuilder input = new StringBuilder();
			Method method = new Method();
			boolean startParsing = false;
			while((s = ls_in.readLine()) != null)
			{
				
				s = s.trim();
				if(s.startsWith("Processing:")){
					if(method.getName() != null){
						methods.add(method);
						method = new Method();						
					}
					method.setName(s.substring(12));
				}
				else if(s.startsWith("LOCAL")){					
					path.append(s.substring(6, s.length() - 1));
					path.append(";");
					path.append("\n");
				}
				else if(s.startsWith("GLOBAL")){
					continue;
//					path.append(s.substring(7, s.length() - 1));
//					path.append("\n");
				}
				else if(s.startsWith("FORMAL")){
					input.append(s.substring(7, s.length() - 1));
					input.append("\n");
					path.append(s.substring(7, s.length() - 1));
					path.append(";");
					path.append("\n");
				}
				else if(s.startsWith("STMT(return")){
					path.append(s.substring(5, s.length() - 1));
					path.append("\n");
					
					method.getPath().add(path.toString());
					method.getPathToInput().put(path.toString(), input.toString());					
					path = new StringBuilder();
					input = new StringBuilder();
					startParsing = false;
				}
				else if(s.equals("Paths:")){
					startParsing = true;
					continue;
				}
				else if( s.startsWith("path_enumeration") || s.startsWith("postprocess")){
					continue;
				}
				else if(s.startsWith("STMT(")){
					if(s.endsWith(";)")){
						path.append(s.substring(5, s.length() - 1));
						path.append("\n");
					}
					else{
						path.append(s.substring(5, s.length()));
						path.append("\n");
					}
				}
				else if(s.endsWith(";)")){
					path.append(s.substring(0, s.length() - 1));
					path.append("\n");
				}
				else if(s.startsWith("ASSUME(")){
					path.append(s.substring(7, s.length() - 1));
					path.append(";");
					path.append("\n");
				}
				else if(s.startsWith("Number of")){
					continue;
				}
				else {
					if(startParsing){
						path.append(s);
						path.append("\n");
					}
				}
			}
			if(method.getName() != null)
			{
				methods.add(method);
			}
			ls_in.close();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
		String fileString = Utility.getStringFromFile(fileName);
		List<String> sources = new ArrayList<String>();
		int start = -1;
		int end = -1;
		Stack<Character> stack = new Stack<Character>();
		Stack<Integer> index = new Stack<Integer>();
		for(int i = 0; i < fileString.length(); i++)
		{
			char c = fileString.charAt(i);
			if(c == '{'){
				index.push(i);
				stack.add(c);
			}
			else if(c == '}'){
				stack.pop();
				int temp = index.pop();
				if(stack.isEmpty()){
					start = temp;
					end = i;
					sources.add(fileString.substring(start+1, end));
				}				
			}
		}
		
		for(int i = 0; i < sources.size(); i++){
			methods.get(i).setSource(sources.get(i));
		}
		return methods;
		
	}
	
	
	
	
	public static void main(String[] args){
		String filePath = "./repository";
		//EntryAddition.addOneFolder(filePath);;
		EntryAddition.addOneFile("./repository/file1");
	}
}
