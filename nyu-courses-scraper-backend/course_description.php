<?php
	$ch = curl_init();
	$url = isset($_GET['url']) ? $_GET['url'] : 'http://www.nyu.edu/academics/courses.details.small.html?crsId=V14.0001&yearTerm=20093&ref=reg';
	
	if(isset($_GET['url'])) {
		$_GET['yearTerm'] = '20101';
		$url = $_GET['url'] . "&yearTerm={$_GET['yearTerm']}&ref=reg";
	} 

	curl_setopt($ch, CURLOPT_URL, $url);
	curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);

	$output = curl_exec($ch);
	$output = str_replace("type=\"BUTTON\"","type=\"hidden\"",$output);

	print $output;
	curl_close($ch);     
?>
