<?php

	$ch = curl_init();
	curl_setopt($ch, CURLOPT_URL, 'https://login.nyu.edu/sso/UI/Login?'.
				'goto=http%3A%2F%2Fhome.nyu.edu%3A80%2F');
	curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);

	$output = curl_exec($ch);
	
	$extractedURL = extractBetweenDelimeters($output, '/sso/images/photos', '.jpg');

	//print "<img src='https://login.nyu.edu/$extractedURL.jpg'/>";
	print "https://login.nyu.edu/$extractedURL.jpg";

	curl_close($ch);     

	function extractBetweenDelimeters($inputstr,$delimeterLeft,$delimeterRight) {
		$posLeft  = stripos($inputstr,$delimeterLeft)+1;
		$posRight = stripos($inputstr,$delimeterRight,$posLeft+1);
		return  substr($inputstr,$posLeft,$posRight-$posLeft);
	}
?>
