package model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;

public class SemiRankingDataSet {

	public ArrayList<ArrayList<int[]>> semiRankingLists = new ArrayList<ArrayList<int[]>>();
	public HashMap<String, Integer> name2Id = new HashMap<String, Integer>(); 
	public ArrayList<String> id2Name = new ArrayList<String>();
	
	public void addSemiRanking(ArrayList<String[]> semiRankedList) {
		ArrayList<int[]> tempList = new ArrayList<int[]>();
		for (String[] itemList : semiRankedList) {
//			for (String item : itemList)
//				System.out.print("\"" + item + "\",");
//			System.out.println(";");
			ArrayList<Integer> tempIdList = new ArrayList<Integer>();
			for (int i = 0; i < itemList.length; ++i) {
				String item = itemList[i];
				if (item.equals("")) continue;
				if (!name2Id.containsKey(item)) {
					name2Id.put(item, id2Name.size());
					id2Name.add(item);
				}
				int id = name2Id.get(item);
				tempIdList.add(id);
			}
			int[] idList = new int[tempIdList.size()];
			for (int i = 0; i < tempIdList.size(); ++i) idList[i]=  tempIdList.get(i);
			tempList.add(idList);
		}
		semiRankingLists.add(tempList);
//		System.out.println("Size=" + semiRankedList.size());
//		for (int[] itemList : tempList) System.out.println(itemList.length); 
	}
	
	
	public void readSemiRankingLists(String fileName) throws Exception {
		name2Id = new HashMap<String, Integer>();
		id2Name = new ArrayList<String>();
		semiRankingLists = new ArrayList<ArrayList<int[]>>();
		BufferedReader br = new BufferedReader (new FileReader( new File(fileName)));
		String s;
		while ((s = br.readLine()) != null) {
			s = s.trim();
			if (s.endsWith(";")) s += ",";
			ArrayList<String[]> semiRankingList = new ArrayList<String[]>();
			String[] strLists = s.split(";");
			for (String subStrList : strLists) {
				String[] itemList = subStrList.split(",");
				semiRankingList.add(itemList);
			}
			if (semiRankingList.size() > 0) {
				addSemiRanking(semiRankingList);
			}
		}
		br.close();
	}
	
	public RankingDataSet cloneAsRankingDataSet() {
		RankingDataSet ret =  new RankingDataSet();
		ret.id2Name = (ArrayList<String>) this.id2Name.clone();
		ret.name2Id = (HashMap<String, Integer>) this.name2Id.clone();
		ret.rankingLists = new ArrayList<ArrayList<Integer>>();
		for (ArrayList<int[]> semiRankedList : this.semiRankingLists) {
			ArrayList<Integer> tempList = new ArrayList<Integer>();
			for (int[] session : semiRankedList) 
				for (int i : session) tempList.add(i);
			ret.rankingLists.add(tempList);
		}
		return ret;
	}
	
	public HashMap<Integer, Integer> readGtLabel(String gtScoreFileName) throws Exception {
		BufferedReader br = new BufferedReader(new FileReader(new File(gtScoreFileName)));
		HashMap<Integer, Integer> ret = new HashMap<Integer, Integer>();
		String s;
		while ((s = br.readLine()) != null) {
			s = s.trim();
			if (s == "") continue;
			String[] slist = s.split("\\s+");
			ret.put(name2Id.get(slist[0]), Integer.parseInt(slist[1]));
		}
		br.close();
		return ret;
	}
	
	public HashMap<Integer, Double> readGtScores(String gtScoreFileName) throws Exception {
		BufferedReader br = new BufferedReader(new FileReader(new File(gtScoreFileName)));
		HashMap<Integer, Double> ret = new HashMap<Integer, Double>();
		String s;
		while ((s = br.readLine()) != null) {
			s = s.trim();
			if (s == "") continue;
			String[] slist = s.split("\\s+");
			ret.put(name2Id.get(slist[0]), Double.parseDouble(slist[1]));
		}
		br.close();
		return ret;
	}
	
	static public void main(String[] args) throws Exception {
		SemiRankingDataSet dataSet = new SemiRankingDataSet();
		dataSet.readSemiRankingLists("C:\\Coursework\\CS598Aditya\\project\\crowdsource\\exp\\1013_try\\rankedlists_beta.txt");
	}
	
}
