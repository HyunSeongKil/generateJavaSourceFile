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
				<label for="${item.lowerCaseCamelString}"/>
				<input type="text" id="${item.lowerCaseCamelString}" name="${item.lowerCaseCamelString}" >
			</div>
		</#list>
	
	</body>
	
</html>
