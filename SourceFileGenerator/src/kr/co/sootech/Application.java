package kr.co.sootech;

import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import freemarker.template.TemplateNotFoundException;

/**
 * @author hyunseongkil
 * 
 */
public class Application {
	
	
	/**
	 * @param args
	 * @throws IOException 
	 * @throws TemplateException 
	 * @throws UnsupportedFlavorException 
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 */
	public static void main(String[] args) throws IOException, TemplateException, UnsupportedFlavorException, ClassNotFoundException, SQLException {
		Utils.log(Thread.currentThread().getStackTrace(), ">>");
		
		//
		Map<String,Object> configMap = loadConfigJson();
				

		//
		processAll(configMap);
		
		//
		Utils.log(Thread.currentThread().getStackTrace(), "<<");
		
	}

	/**
	 * 프리마커 환경 인스턴스 생성
	 * @see https://freemarker.apache.org/docs/pgui_quickstart.html
	 * @param configMap
	 * @return
	 * @throws IOException
	 */
	private static Configuration createConfiguration(Map<String, Object> configMap) throws IOException {
		// Create your Configuration instance, and specify if up to what FreeMarker
		// version (here 2.3.29) do you want to apply the fixes that are not 100%
		// backward-compatible. See the Configuration JavaDoc for details.
		Configuration cfg = new Configuration(Configuration.VERSION_2_3_29);

		// Specify the source where the template files come from. Here I set a
		// plain directory for it, but non-file-system sources are possible too:
		Path path = Paths.get(configMap.get("templatePath").toString());
		cfg.setDirectoryForTemplateLoading(path.toFile());

		// From here we will set the settings recommended for new projects. These
		// aren't the defaults for backward compatibilty.

		// Set the preferred charset template files are stored in. UTF-8 is
		// a good choice in most applications:
		cfg.setDefaultEncoding("UTF-8");

		// Sets how errors will appear.
		// During web page *development* TemplateExceptionHandler.HTML_DEBUG_HANDLER is better.
		cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);

		// Don't log exceptions inside FreeMarker that it will thrown at you anyway:
		cfg.setLogTemplateExceptions(false);

		// Wrap unchecked exceptions thrown during template processing into TemplateException-s:
		cfg.setWrapUncheckedExceptions(true);

		// Do not fall back to higher scopes when reading a null loop variable:
		cfg.setFallbackOnNullLoopVariable(false);

		//
		Utils.log(Thread.currentThread().getStackTrace(), "<<", cfg);
		return cfg;
	}
	
	
	/**
	 * 메타 맵 생성
	 * @param configMap
	 * @return
	 * @throws SQLException 
	 * @throws UnsupportedFlavorException 
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	private static Map<String, Object> createMetaMap(Map<String, Object> configMap) throws ClassNotFoundException, IOException, UnsupportedFlavorException, SQLException {
		Map<String,Object> map = new HashMap<String, Object>();
		
		//configMap 복사
		map.putAll(configMap);
		
		//
		String engBizName = (String) configMap.get("engBizName");
		
		//단어 나누기
		String[] arr = Utils.splitWord(engBizName);
		//모두 소문자로 변환
		arr = Utils.toArrayOfLowerCase(arr);
		
		//소문자로 시작 낙타법
		map.put("lowerCaseCamelString", Utils.joinWithStartLowerCaseCamel(arr));
		//대문자로 시작 낙타법
		map.put("upperCaseCamelString", Utils.joinWithStartUpperCaseCamel(arr));
		//언더바로 join
		map.put("underscoreString", Utils.joinWith(arr, Utils.UNDERSCORE));
		//공백으로 join
		map.put("spaceString", Utils.joinWith(arr, Utils.SPACE));
		//빈값으로 join
		map.put("emptyString", Utils.joinWith(arr, ""));
		//모두 소문자로
		map.put("upperCaseArray", Utils.toArrayOfUpperCase(arr));
		//모두 대문자로
		map.put("lowerCaseArray", Utils.toArrayOfLowerCase(arr));
		
		//현재 시간
		map.put("now", Utils.getNow());
		

		//데이터(컬럼) 목록
		List<Map<String,Object>> datas = new DataLoader().getDatas(configMap);
		map.put("datas", datas);
		
		//
		Utils.log(Thread.currentThread().getStackTrace(), "<<", map);
		return map;
	}


	
	/**
	 * 환경 파일 로드
	 * 실행 파일과 같은 경로의 app.json파일 로드
	 * @return
	 * @throws JsonSyntaxException
	 * @throws JsonIOException
	 * @throws FileNotFoundException
	 */
	@SuppressWarnings("unchecked")
	private static Map<String,Object> loadConfigJson() throws JsonSyntaxException, JsonIOException, FileNotFoundException {
		URL url = Application.class.getProtectionDomain().getCodeSource().getLocation();
		Path path = Paths.get(url.toString().replaceAll("file:/", ""));

		//
		String filename = "app.json";

		//
		Map<String,Object> map = new Gson().fromJson(new FileReader(path.resolve(filename).toFile()), Map.class);
		
		//
		Utils.log(Thread.currentThread().getStackTrace(), "<<", map);
		return map;
	}
	

	

	
	
	/**
	 * 모든 소스 파일 생성
	 * @param configMap
	 * @throws TemplateNotFoundException
	 * @throws MalformedTemplateNameException
	 * @throws ParseException
	 * @throws IOException
	 * @throws TemplateException
	 * @throws UnsupportedFlavorException 
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 */
	private static void processAll(Map<String, Object> configMap) throws TemplateNotFoundException, MalformedTemplateNameException, ParseException, IOException, TemplateException, UnsupportedFlavorException, ClassNotFoundException, SQLException {

		//
		Configuration cfg = createConfiguration(configMap);
		
		//
		Map<String,Object> metaMap = createMetaMap(configMap);
		

		//
		new FileGenerator().generate(configMap, cfg, metaMap);
		
	}



}
