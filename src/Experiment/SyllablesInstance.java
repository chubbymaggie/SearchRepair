package Experiment;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import search.ResultObject.ResultState;
import util.WhiteOrBlack;

public class SyllablesInstance extends ProgramInstance{

	// possible FIXME: syllables knows it's syllables, possibly don't need it here.
	public SyllablesInstance(String program, Path folder, Path fileName, int repo, WhiteOrBlack wb) {
		super(program, folder, fileName, repo, wb);
		this.transformAndInitRunDir(false, "");
		this.initTests();
		
	}

	@Override
	public void search() {
		if(this.getPositives().size() == 0) {
			this.getInfo().getResult().setState(ResultState.NOPOSITIVE);
			return;
		}
		if(this.getNegatives().size() == 0){
			this.getInfo().getResult().setState(ResultState.CORRECT);
			return;
		}
		
		List<int[]> buggys = getMultipleBuggyLines();
		
		for(int[] range : buggys){
			String s = Arrays.toString(range);
			System.out.println(s);
			OneRegion instan = new OneRegion(this.getProgram(), this.getRunDir(), this.getRepo());

			instan.setBuggy(range);
			instan.setNegatives(this.getNegatives());
			instan.setPositives(this.getPositives());
			instan.search();
			if(instan.getInfo().getResult().getState() == ResultState.SUCCESS || instan.getInfo().getResult().getState() == ResultState.PARTIAL){
				this.setInfo(instan.getInfo());
				break;
			}
			else{
				if(this.whiteOrBlack != WhiteOrBlack.WHITEBOX)continue;
				instan.searchJustOnMap();
				if(instan.getInfo().getResult().getState() == ResultState.SUCCESS){
					this.setInfo(instan.getInfo());				
					break;
				}
			}
		}
	}


//why is this different between the search cases?  I feel like it should all be the same...	

	protected List<int[]> getMultipleBuggyLines(){
		List<int[]> list = new ArrayList<int[]>();
		initSuspicious();
		this.initContent();
		double average = getAverage();
		int index = 12;
		while(index < this.getSuspiciousness().keySet().size()){
			if(this.getSuspiciousness().get(index) >= average){
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
	

//	public static void main(String[] args){
//		SyllableSearchCase instan = new SyllableSearchCase("./bughunt/syllables/109", "syllables.c", 3);
//		instan.transformAndInitRunDir(false, "");
//		instan.initInputAndOutput();
////		instan.search(true);
////		instan.recordResult(true);
//		instan.search(false);
//		instan.recordResult(false);
//	}

}
