package infinitefire.project.utils;

public class RegularExpressionUtils {
	
	public static String convertString(String str) {
		char[] chs = str.toCharArray();
		String tmp = "";
		
		for(int i = 0 ; i < chs.length ; i++) {
			if(chs[i] == '<') {
				tmp += " ";
			} else if(chs[i] == '>') {
				tmp += " ";
			} else
				tmp += chs[i];
		}
		
		return tmp;
	}
}
