/**
 * 
 */
package kr.co.sootech;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

/**
 * @author hyunseongkil
 *
 */
public class DataLoader {

	
	public List<Map<String,Object>> getDatas(Map<String,Object> configMap) throws IOException, UnsupportedFlavorException, ClassNotFoundException, SQLException{
		String source = (String)configMap.get("source");
		
		//
		if("clipboard".equals(source)) {
			return loadFromClipboard(configMap);
		}
		
		//
		if("csv".equals(source)) {
			return loadFromCsv(configMap);
		}
		
		//
		if("excel".equals(source)) {
			return loadFromExcel(configMap);
		}
		
		//
		if("db".equals(source)) {
			return loadFromDb(configMap);
		}
		
		//
		return null;
	}
	

	/**
	 * clipboard 로드 & List<Map>>으로 변환
	 * @param configMap
	 * @return
	 * @throws IOException
	 * @throws UnsupportedFlavorException 
	 */
	@SuppressWarnings("unchecked")
	private  List<Map<String, Object>> loadFromClipboard(Map<String, Object> configMap) throws IOException, UnsupportedFlavorException {
		Clipboard c = Toolkit.getDefaultToolkit().getSystemClipboard();
		
		Transferable contents = c.getContents(null);
		boolean hasTransferableText = (contents != null) && contents.isDataFlavorSupported(DataFlavor.stringFlavor);
		
		//
		String str="";
		if(hasTransferableText) {
			str = (String)contents.getTransferData(DataFlavor.stringFlavor);
		}
		
		//
		List<String> lines = Arrays.asList(str.split("\n"));
		
		// 메타 정보
		Map<String,Object> metaMap = (Map<String, Object>) configMap.get("clipboard");

		
		//
		Utils.log(Thread.currentThread().getStackTrace(), "<<");
		return loadFromLines(metaMap, lines);
	}
	
	
	/**
	 * 데이터 읽기 from db
	 * @param configMap
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	@SuppressWarnings("unchecked")
	private  List<Map<String,Object>> loadFromDb(Map<String,Object> configMap) throws ClassNotFoundException, SQLException{
		// 메타 정보
		Map<String,Object> metaMap = (Map<String, Object>) configMap.get("db");
		
		//
		String driverClass = metaMap.get("driverClass").toString();
		String url = metaMap.get("url").toString();
		String username = metaMap.get("username").toString();
		String password = metaMap.get("password").toString();
		String tableName = metaMap.get("tableName").toString();
		
		//
		Class.forName(metaMap.get("driverClass").toString());
		
		//
		try(Connection conn = DriverManager.getConnection(url, username, password)){
			System.out.println(conn);
			
			try(Statement st = conn.createStatement()){
				System.out.println(st);
				
				//
				if(-1 != driverClass.toLowerCase().indexOf("mysql")) {
					return loadFromMySqlDb(conn, st, tableName);
				}
				// oracle
				if(-1 != driverClass.toLowerCase().indexOf("oracle")) {
					return loadFromOracleDb(conn, st, tableName);
				}
				
			}			
		}
		
		//
		return null;
	}
	
	
	/**
	 * oracle에 접속하여 테이블 정보 추출
	 * @param conn
	 * @param st
	 * @param tableName
	 * @return
	 */
	private  List<Map<String, Object>> loadFromOracleDb(Connection conn, Statement st, String tableName) {
		// TODO Auto-generated method stub
		Utils.log(Thread.currentThread().getStackTrace(), "<<");
		return null;
	}

	/**
	 * mysql에 접속하여 테이블 정보 추출
	 * TODO 동일 테이블명이 여러 table_schema에 존재하는 경우 해결(bug fix) 필요
	 * @param conn
	 * @param st
	 * @param tableName
	 * @return
	 * @throws SQLException
	 */
	private List<Map<String, Object>> loadFromMySqlDb(Connection conn, Statement st, String tableName) throws SQLException {
		//
		String sql = "SELECT TABLE_SCHEMA"
				+ "			, TABLE_NAME"
				+ "			, COLUMN_NAME"
				+ "			, IS_NULLABLE"
				+ "			, DATA_TYPE"
				+ "			, COLUMN_KEY"
				+ "			, COLUMN_COMMENT"
				+ "	FROM information_schema.COLUMNS"
				+ "	WHERE TABLE_NAME = '"+tableName+"'"
				+ "	ORDER BY ORDINAL_POSITION ";
		
		//
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		
		//
		try(ResultSet rs = st.executeQuery(sql)){
			while(rs.next()) {
				Map<String,Object> map = new HashMap<String, Object>();
				list.add(map);
				
				//
				map.put("kor", rs.getString("COLUMN_COMMENT"));
				map.put("eng", rs.getString("COLUMN_NAME"));
				map.put("engArr", Utils.splitWord(rs.getString("COLUMN_NAME"))); //[abcd, efg]
				map.put("lowerCaseCamelString", Utils.joinWithStartLowerCaseCamel((String[]) map.get("engArr"))); //abcdEfg
				map.put("comment", rs.getString("COLUMN_COMMENT"));
			}
		}
		
		//
		Utils.log(Thread.currentThread().getStackTrace(), "<<");
		return list;
	}

	/**
	 * csv 파일 로드 & List<Map>>으로 변환
	 * @param configMap
	 * @return
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	private List<Map<String, Object>> loadFromCsv(Map<String, Object> configMap) throws IOException {
		// 메타 정보
		Map<String,Object> metaMap = (Map<String, Object>) configMap.get("csv");
		
		//
		Path path = Paths.get(metaMap.get("path").toString());
		String filename = metaMap.get("filename").toString();
		
		//
		if(!path.resolve(filename).toFile().exists()) {
			throw new FileNotFoundException();
		}
		
		//파일 내용 로드
		List<String> lines = Files.readAllLines(path.resolve(filename), StandardCharsets.UTF_8);
		
		//
		Utils.log(Thread.currentThread().getStackTrace(), "<<");
		return loadFromLines(metaMap, lines);
	}

	

	/**
	 * lines에서 데이터 로드
	 * @param metaMap
	 * @param lines
	 * @return
	 */
	private List<Map<String,Object>> loadFromLines(Map<String,Object> metaMap, List<String> lines){
		//파싱 정보
		String deli = (String) metaMap.get("deli");
		int dataStartRow =  Integer.valueOf(metaMap.get("dataStartRow").toString());
		int korCol = Integer.valueOf(metaMap.get("korCol").toString());
		int engCol = Integer.valueOf(metaMap.get("engCol").toString());
		int commentCol = Integer.valueOf(metaMap.get("commentCol").toString());

		//
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();

		//라인수만큼 루프
		for(int i=dataStartRow; i<lines.size(); i++) {
			String line = lines.get(i);

			String[] arr = line.split(deli, -1);

			//데이터 맵 생성
			Map<String,Object> map = new HashMap<String, Object>();
			list.add(map);

			//
			map.put("kor", arr[korCol]);//한글 컬럼 명
			map.put("eng", arr[engCol]);//영문 컬럼 명
			map.put("engArr", Utils.splitWord(arr[engCol])); //[abcd, efg]
			map.put("lowerCaseCamelString", Utils.joinWithStartLowerCaseCamel((String[]) map.get("engArr"))); //abcdEfg
			map.put("comment", arr[commentCol]);//주석
		}

		//
		Utils.log(Thread.currentThread().getStackTrace(), "<<");
		return list;
	}


	

	/**
	 * 데이터 읽기 from 엑셀파일
	 * @param configMap
	 * @return
	 * @throws EncryptedDocumentException
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	private List<Map<String, Object>> loadFromExcel(Map<String, Object> configMap) throws EncryptedDocumentException, IOException {
		Map<String,Object> metaMap = (Map<String, Object>) configMap.get("excel");
		
		//
		Path path = Paths.get(metaMap.get("path").toString());
		String filename = metaMap.get("filename").toString();
		
		//
		if(!path.resolve(filename).toFile().exists()) {
			throw new FileNotFoundException();
		}
		
		//
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		
		//
		try(Workbook wb = WorkbookFactory.create(path.resolve(filename).toFile())){
			//
			int sheetIndex = Integer.valueOf(metaMap.get("sheetIndex").toString());
			Sheet sheet = wb.getSheetAt(sheetIndex);
			
			
			//
			int dataStartRow = Integer.valueOf(metaMap.get("dataStartRow").toString());
			int dataEndRow = Integer.valueOf(metaMap.get("dataEndRow").toString());
			int korCol = Integer.valueOf(metaMap.get("korCol").toString()); 
			int engCol = Integer.valueOf(metaMap.get("engCol").toString()); 
			int commentCol = Integer.valueOf(metaMap.get("commentCol").toString());
			
			//
			for(int i=dataStartRow; i<=dataEndRow; i++) {
				Row row = sheet.getRow(i);
				
				//
				Map<String,Object> map = new HashMap<String, Object>();
				list.add(map);
				
				//
				map.put("kor", row.getCell(korCol).getStringCellValue());
				map.put("eng", row.getCell(engCol).getStringCellValue());
				map.put("comment", row.getCell(commentCol).getStringCellValue());
				map.put("engArr", Utils.splitWord((String)map.get("eng")));	//[abcd,efg]
				map.put("lowerCaseCamelString", Utils.joinWithStartLowerCaseCamel((String[]) map.get("engArr"))); //abcdEfg
				
			}
			
		}
		
		
		//
		Utils.log(Thread.currentThread().getStackTrace(), "<<");
		return list;
	}

}
