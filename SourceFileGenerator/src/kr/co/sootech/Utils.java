/**
 * 
 */
package kr.co.sootech;

import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author hyunseongkil
 *
 */
public class Utils {
	
	public static final String SPACE = " ";
	public static final String UNDERSCORE = "_";
	
	static final Date startDt = new Date();
	
	public static void log(StackTraceElement[] arr, Object...objs) {
		String s ="[";
		s += new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date());
		s += "]";
		s += "[+"+(new Date().getTime() - startDt.getTime())+"ms]";
		
		s += "\t" + arr[1].getMethodName();
		//
		for(Object o : objs) {
			s += "\t" + o;
		}
		
		System.out.println(s);
	}
	
	/**
	 * 파일 백업
	 * @param path
	 * @param filename
	 */
	public static void backupFileIfExists(Path path, String filename) {
		if(!path.resolve(filename).toFile().exists()) {
			return;
		}
		
		//
		Path dest = path.resolve(filename + "." + Utils.getHhmmss());
		path.resolve(filename).toFile().renameTo(dest.toFile());
	}
	
	public static String getNow() {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
	}
	
	
	public static String getHhmmss() {
		return new SimpleDateFormat("HHmmss").format(new Date());
	}
	
	public static boolean contains(List<String> list, String str) {
		for(String s : list) {
			if(s.equals(str)) {
				return true;
			}
		}
		return false;
	}
	
	public static String joinWith(String[] arr, String deli) {
		if(null == arr) {
			throw new RuntimeException("null arr");
		}
		
		//
		String s="";
		for(int i=0; i<arr.length; i++) {
			s += (deli + arr[i]);
		}
		
		//맨앞의 deli 제거
		return s.substring(1);
	}
	
	/**
	 * 
	 * @param arr [abcd, efg]
	 * @return abcdEfg
	 */
	public static String joinWithStartLowerCaseCamel(String[] arr) {
		if(null == arr) {
			throw new RuntimeException("null arr");
		}
		
		//
		String s = "";
		for(int i=0; i<arr.length; i++) {
			String temp = arr[i].toLowerCase();
			
			//
			for(int j=0; j<temp.length(); j++) {
				char ch = temp.charAt(j);
				
				//
				if(0 != i && 0 == j) {
					s += String.valueOf(ch).toUpperCase();
				}else {
					s += String.valueOf(ch);
				}
			}
		}
		
		//
		return s;
	}
	
	
	/**
	 * 
	 * @param arr [abcd, efg]
	 * @return AbcdEfg
	 */
	public static String joinWithStartUpperCaseCamel(String[] arr) {
		if(null == arr) {
			throw new RuntimeException("null arr");
		}

		//
		String s = "";
		for(int i=0; i<arr.length; i++) {
			String temp = arr[i].toLowerCase();
			
			//
			for(int j=0; j<temp.length(); j++) {
				char ch = temp.charAt(j);
				
				//
				if(0 == j) {
					s += String.valueOf(ch).toUpperCase();
				}else {
					s += String.valueOf(ch);
				}
			}
		}
		
		//
		return s;
	}
	
	
	/**
	 * 문자열을 배열로 변환
	 * 구분자 : 공백 | 언더바 | 낙타법
	 * @param str
	 * @return
	 */
	public static String[] splitWord(String str) {
		if(null == str) {
			throw new RuntimeException("null str");
		}
		
		
		//공백이 있으면
		if(-1 != str.indexOf(SPACE)) {
			System.out.println(".splitWord - found space");
			return str.split(SPACE);
		}
		
		//언더바가 있으면
		if(-1 != str.indexOf("_")) {
			System.out.println(".splitWord - found underscore");
			return str.split("_");
		}
		
		//대문자가 있는지 검사
		boolean b = false;
		for(int i=0; i<str.length(); i++) {
			char ch = str.charAt(i);
			if(65 <= ch && ch <= 90) {
				b = true;
				break;
			}
		}
		
		//대문자가 있으면
		if(b) {
			System.out.println(".splitWord - found camel");
			//대문자앞에 공백 추가 & 대문자는 소문자로 변환
			String s = "";
			for(int i=0; i<str.length(); i++) {
				char ch = str.charAt(i);
				if(65 <= ch && ch <= 90) {
					s += SPACE;
				}
				s += (String.valueOf(ch).toLowerCase());
			}	
			
			//
			return s.split(SPACE);
		}
		
		//
		return new String[] {str};
	}
	
	
	
	public static String[] toArrayOfLowerCase(String[] arr) {
		if(null == arr) {
			throw new RuntimeException("null arr");
		}
		
		//
		for(int i=0; i<arr.length; i++) {
			arr[i] = arr[i].toLowerCase();
		}
		
		//
		return arr;
	}
	
	public static String[] toArrayOfUpperCase(String[] arr) {
		if(null == arr) {
			throw new RuntimeException("null arr");
		}
		
		//
		for(int i=0; i<arr.length; i++) {
			arr[i] = arr[i].toUpperCase();
		}
		
		//
		return arr;
	}
	
	
	
	
}
