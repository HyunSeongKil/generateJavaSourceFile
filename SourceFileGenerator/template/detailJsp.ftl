<!DOCTYPE html>
<html lang="ko">


	<head>
		<meta charset="utf-8">
		
		<title></title>

	</head>
	
	<body>
	
		<#list datas as item>
			
			<div>
				<!-- ${item.kor} -->
				${item.lowerCaseCamelString}
			</div>
		</#list>
	
	</body>
	
</html>
