<?php

	$course_id = isset($_GET['course']) ? $_GET['course'] : "C10.0001";
	$section = isset($_GET['section']) ? $_GET['section'] : "001";
	$semester = isset($_GET['semester']) ? $_GET['semester'] : "20093";

	$ch = curl_init();
	curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, FALSE);
	curl_setopt($ch, CURLOPT_RETURNTRANSFER, TRUE);
	curl_setopt($ch, CURLOPT_COOKIESESSION, TRUE);
	curl_setopt($ch, CURLOPT_HEADER, 0);
	curl_setopt($ch, CURLOPT_COOKIEFILE, "cookiefile");
	curl_setopt($ch, CURLOPT_COOKIEJAR, "cookiefile");
	curl_setopt($ch, CURLOPT_COOKIE, session_name() . '=' . session_id());
	curl_setopt($ch, CURLOPT_FOLLOWLOCATION, 1);
	
	// logins to stern evaluation system
	$url = "http://w4.stern.nyu.edu/cfe/login";	

	if($_POST['username'] == 'apple' && $_POST['password'] == 'testing') {
		include 'stern_login_info.php';
	} else
		foreach($_POST as $key => $value) $postData .= "$key=$value&";

	curl_setopt($ch, CURLOPT_URL, $url);
	curl_setopt($ch, CURLOPT_POST, 1);
	curl_setopt($ch, CURLOPT_POSTFIELDS,$postData);
	curl_exec($ch);

	// searches for the course to retrieve the database id
	$url = 'http://w4.stern.nyu.edu/cfe/search';
	$postData = "sortedby=&sortdirection=&formreset=&search_changed=&classtype=&semester=&title=&course_number=$course_id";

	curl_setopt($ch, CURLOPT_URL, $url);
	curl_setopt($ch, CURLOPT_POST, 1);
	curl_setopt($ch, CURLOPT_POSTFIELDS,$postData);
	$result = curl_exec($ch);

	// checks if the login was valid
	if (stripos($result, "<label name =\"password\">Password</label>") != null) exit;

	$output = substring($result,"<!--<p><h2><b>CFE Search</b></h2>-->","<div class=\"clear\">&nbsp;</div>",true);
	$output = "<table><tr>" . substr($output,stripos($output,"<td align=center class=\"tablealtrow\">"));
	$output = str_replace(array('align=center ',' align=left NOWRAP',' align=left NOWRAP','&nbsp;','&'),array('','','','','AND'),$output);
	$output = substr($output,0,stripos($output,"<br>"));
	//print $output;

	$xml = new SimpleXMLElement($output);
	$json = "{\"courses\" : { \"course\" : [";

	foreach($xml->tr as $course) {
		$json .= "{";
		$i = 0;

		foreach($course->td as $td) {
			switch($i) {
				case 0: $json .= "\"year\" : \"$td\","; break;
				case 1: $json .= "\"semester\" : \"$td\","; break;
				case 2: $json .= "\"prog\" : \"$td\","; break;
				case 4: $json .= "\"title\" : \"".trim($td)."\","; break;
			}

			if($i == 3) {
				$a = $td->a;
				$json .= "\"course\" : \"$a\",";

				$str = "/cfe/reports/";
				$report_id = substr($a['href'],stripos($a['href'],$str)+strlen($str));
				$json .= "\"id\" : \"$report_id\",";
			} else if($i == 5) {
				$a = $td->a;

				if($a != "") {
					$json .= "\"instructor\" : \"".trim($a)."\",";
					$json .= "\"instructor_profile\" : \"{$a['href']}\"";
				} else 
					$json .= "\"instructor\" : \"".trim($td)."\"";
			}	

			// increment counter
			$i++;
		}
		$json .= "},";
	}
	$json = substr($json,0,strlen($json)-1);
	$json .= "] } }";

	print $json;

	curl_close($ch);

	// helper parser function
	function substring($string,$r1,$r2,$include) {
		$r1_loc = strpos($string,$r1);
		$r2_loc = strpos($string,$r2);
		$n_loc = $include ? $r1_loc + strlen($r1) : $r1_loc;
		$n_len = $include ? $r2_loc - $r1_loc - strlen($r1) : $r2_loc - $r1_loc + strlen($r2);
		return substr($string,$n_loc,$n_len);
	}
?>
