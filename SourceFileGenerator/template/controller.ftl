package ${packagePath}.${emptyString}.controller;

import java.util.List;
import java.util.Map;
//TODO add spring...

/**
 * ${korBizName} controller
 * @author
 * @since ${now}
 */
@Controller
@RequestMapping("TODO")
public class ${upperCaseCamelString} {


	/**
	 * ${korBizName} 조회
	 */
	@RequestMapping("TODO")
	public String ${lowerCaseCamelString}List(HttpServletRequest request, Model model){
		return "경로/${lowerCaseCamelString}List";
	}
	

	/**
	 * ${korBizName} 등록 폼
	 */
	@RequestMapping("TODO")
	public String ${lowerCaseCamelString}RegistForm(HttpServletRequest request, Model model){
		return "경로/${lowerCaseCamelString}RegistForm";
	}	

	/**
	 * ${korBizName} 등록처리
	 */
	@RequestMapping("TODO")
	public String ${lowerCaseCamelString}Regist(HttpServletRequest request, Model model){
		return "경로/${lowerCaseCamelString}RegistForm";
	}	
	

	/**
	 * ${korBizName} 수정 폼
	 */
	@RequestMapping("TODO")
	public String ${lowerCaseCamelString}UpdtForm(HttpServletRequest request, Model model){
		return "경로/${lowerCaseCamelString}UpdtForm";
	}	
	

	/**
	 * ${korBizName} 수정 처리
	 */
	@RequestMapping("TODO")
	public String ${lowerCaseCamelString}Updt(HttpServletRequest request, Model model){
		return "경로/${lowerCaseCamelString}Updt";
	}	
	

	/**
	 * ${korBizName} 삭제 처리
	 */
	@RequestMapping("TODO")
	public String ${lowerCaseCamelString}Delete(HttpServletRequest request, Model model){
		return "경로/${lowerCaseCamelString}Delete";
	}	
	
}