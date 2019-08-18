import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author sreenaveen
 *
 */
public class FileReadWordCount {
	/**
	 * @param strings
	 * @return
	 */
	public Map<String, Integer> wordCount(ArrayList<String> strings) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		Iterator<String> itr = strings.iterator();
		while (itr.hasNext()) {
			String word = itr.next();
			if (!map.containsKey(word)) { // first time we've seen this string
				map.put(word, 1);
			} else {
				int count = map.get(itr.next());
				map.put(word, count + 1);
			}
		}
		return map;
	}

	/**
	 * @param filePath
	 * @return
	 * @throws IOException
	 */
	public ArrayList<String> fileRead(String filePath) throws IOException {
		FileReader in = null;
		BufferedReader read = null;
		ArrayList<String> listOfWords = new ArrayList<String>();
		try {
			in = new FileReader(filePath);
			read = new BufferedReader(in);

			String line;
			while ((line = read.readLine()) != null) {
				String[] wordsInLine = line.split(" ");
				for (String string : wordsInLine) {
					listOfWords.add(string);
				}
			}
		} finally {
			if (in != null) {
				in.close();
			}

		}
		return listOfWords;
	}

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String args[]) throws IOException {
		FileReadWordCount obj = new FileReadWordCount();
		ArrayList<String> listOfWords = obj.fileRead("README.md");
		System.out.println(obj.wordCount(listOfWords));

	}
}
