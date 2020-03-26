package ${packagePath}.${emptyString}.service;

import java.util.List;
import java.util.Map;


/**
 * ${korBizName} vo
 * @author
 * @since ${now}
 */
public class ${upperCaseCamelString}Vo implements Serializable {
	
	<#list datas as item>
		/** ${item.comment} */
		private String ${item.lowerCaseCamelString};
	</#list>
}