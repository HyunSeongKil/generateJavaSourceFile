package ${packagePath}.${emptyString}.service;

import java.util.List;
import java.util.Map;


/**
 * ${korBizName} service
 * @author
 * @since ${now}
 */
public interface ${upperCaseCamelString} {

	/**
	 * ${korBizName} 목록
	 * @param paramMap
	 * @return
 	 */
	Map<String,Object> ${lowerCaseCamelString}List(Map<String,Object> paramMap);	

	/**
	 * ${korBizName} 등록 폼
	 * @param paramMap
	 * @return
 	 */
	Map<String,Object> ${lowerCaseCamelString}RegistForm(Map<String,Object> paramMap);	

	/**
	 * ${korBizName} 등록 처리
	 * @param paramMap
	 * @return
 	 */
	Map<String,Object> ${lowerCaseCamelString}Regist(Map<String,Object> paramMap);	

	/**
	 * ${korBizName} 수정 폼
	 * @param paramMap
	 * @return
 	 */
	Map<String,Object> ${lowerCaseCamelString}UpdtForm(Map<String,Object> paramMap);	

	/**
	 * ${korBizName} 수정 처리
	 * @param paramMap
	 * @return
 	 */
	Map<String,Object> ${lowerCaseCamelString}Updt(Map<String,Object> paramMap);	

	/**
	 * ${korBizName} 삭제
	 * @param paramMap
	 * @return
 	 */
	Map<String,Object> ${lowerCaseCamelString}Delete(Map<String,Object> paramMap);	
}