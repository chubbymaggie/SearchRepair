package Experiment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import search.ResultObject.ResultState;

public class CheckSumSearchCase extends ESearchCase {
	

	public CheckSumSearchCase(String folder, String fileName, int repo) {
		super(folder, fileName, repo);
		
	}

	@Override
	public void search(boolean wb) {
		this.initWbOrBB(wb);
		if(this.getPositives().size() == 0) {
			this.getInfo().getResult().setState(ResultState.NOPOSITIVE);
			return;
		}
		if(this.getNegatives().size() == 0){
			this.getInfo().getResult().setState(ResultState.CORRECT);
			return;
		}
		
		List<int[]> buggylines = getMultpleBuggyLines();
		System.out.println(buggylines.size());
		for(int[] range : buggylines){
			//System.out.println(Arrays.toString(range));
			String prefix = this.getRunDir() + "/" + this.getFileName().substring(0, this.getFileName().lastIndexOf('.'));
			SearchCase instan = new SearchCase(prefix, this.getRepo());
			instan.setBuggy(range);
			instan.setNegatives(this.getNegatives());
			instan.setPositives(this.getPositives());
			instan.search();
			if(instan.getInfo().getResult().getState() == ResultState.SUCCESS){
				
				this.setInfo(instan.getInfo());
				break;
			}
		}
	}

	
	
	


	private double getAverage() {
		int denomerator = 0;
		double numerator = 0;
		for(int i = 1; i <= this.getSuspiciousness().keySet().size(); i++){
				denomerator++;
				numerator += this.getSuspiciousness().get(i);
		}
		if(denomerator == 0) return 1;
		else return numerator / denomerator;
	}
	
	protected List<int[]> getMultpleBuggyLines(){
		List<int[]> list = new ArrayList<int[]>();
		initSuspicious();
		this.initContent();
		double average = getAverage();
		int index = 12;
		while(index < this.getSuspiciousness().keySet().size()){
			if(this.getSuspiciousness().get(index) >= average){
				list.add(new int[]{index,index});
				int right = index + 1;
				while(right <= this.getSuspiciousness().keySet().size() && right - index < 6) {
					if(this.getSuspiciousness().get(right) >= average) {
						list.add(new int[] {index, right});
					}
					right++;
				}
			}
			index++;
		}
				
		return list;
	}

	
	
	public static void main(String[] args){
		CheckSumSearchCase instan = new CheckSumSearchCase("./bughunt/checksum/6", "checksum.c", 3);
		instan.transformAndInitRunDir(false, "");
		instan.initInputAndOutput();
		instan.search(true);
		instan.recordResult(true);
		instan.search(false);
		instan.recordResult(false);
	}
	
	
	
	

}