package id.ms;

import java.io.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class MergeBenchmarkFiles {

	public static void main(String args[]) throws IOException {
		HashMap<String, List<String>> map = new HashMap<String, List<String>>();
		String dirname = args[0]; // directory's name
		String resfile = args[1]; // file's name
		processFiles(map, dirname);
		processMap(map, resfile);
		System.out.println("file " + resfile + " was written.");
		System.out.println("It was a pleasure to work with you. ");
		System.out.println("Best regards.");
	}

	private static void processFiles(HashMap<String, List<String>> map,
			String dirname) throws IOException {
		File dir = new File(dirname);
		if (!dir.exists() || !dir.isDirectory()) {
			System.out.println(dirname + " is not exists or is not directory ");
			return;
		}
		String filenames[] = dir.list();
		for (int i = 0; i < filenames.length; i++) {
			File f = new File(dirname + "/" + filenames[i]);
			if (f.isDirectory()) {
				continue;
			}
			System.out.println("processing file: " + filenames[i]);
			BufferedReader BR = new BufferedReader(new InputStreamReader(
					new FileInputStream(f)));
			String bufstr = new String();
			while ((bufstr = BR.readLine()) != null) {
				String[] res = bufstr.split("[;]");
				if (res.length < 2) {
					System.out.println("Wrong file format: " + bufstr);
					throw new RuntimeException("Wrong file format: " + bufstr);
				}
				String key = res[0];
				String value = res[1];
				List<String> listValue = map.get(key);
				if (listValue == null) {
					listValue = new ArrayList<String>();
					listValue.add(value);
					map.put(key, listValue);
				} else {
					listValue.add(value);
				}
			}
			BR.close();
		}
	}

	private static void processMap(HashMap<String, List<String>> map,
			String resfile) throws IOException {
		try {
			BufferedWriter BW = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(resfile)));
			int checkSize = 0;
			int SumKeys = 0;
			for (Map.Entry<String, List<String>> entry : map.entrySet()) {
				StringBuffer sb = new StringBuffer();
				entry.getKey();
				SumKeys = SumKeys + 1;
				List<String> value = entry.getValue();
				if (checkSize == 0) {
					checkSize = value.size();
				}
				if (value.size() < checkSize) {
					continue;
				}
				Collections.sort(entry.getValue());
				for (int j = 0; j < value.size() - 1; j++) {
					long delta = Long.valueOf(value.get(j + 1))
							- Long.valueOf(value.get(j));
					sb.append(String.valueOf(delta) + ";");
				}
				long worktime = Long.valueOf(value.get(value.size() - 1))
						- Long.valueOf(value.get(0));
				StringBuffer result = new StringBuffer();
			    BW.write(result.append(entry.getKey()).append(";")+sb.append(worktime).toString());		
			    BW.newLine();
			}
			System.out.println(SumKeys + " keys were processed.");
			BW.close();
		} catch (NumberFormatException e) {
			System.out.println("Wrong file format: " + e.getMessage());
			throw e;
		}
	}
}