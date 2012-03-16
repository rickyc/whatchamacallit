<?php
	print "<head><meta name='viewport' content='width=320'/></head>";
	print "<link href='resources/css/stern.css' type='text/css' rel='stylesheet'/>";

	$course_id = isset($_GET['id']) ? $_GET['id'] : "8145";

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
	$url = 'http://w4.stern.nyu.edu/cfe/login';

	if($_POST['username'] == 'apple' && $_POST['password'] == 'testing') {
		include 'stern_login_info.php';
	} else
		foreach($_POST as $key => $value) $postData .= "$key=$value&";

	curl_setopt($ch, CURLOPT_URL, $url);
	curl_setopt($ch, CURLOPT_POST, 1);
	curl_setopt($ch, CURLOPT_POSTFIELDS,$postData);
	curl_exec($ch);

	// retrieves the course data
	$url = "http://w4.stern.nyu.edu/cfe/reports/$course_id/text";

	curl_setopt($ch, CURLOPT_URL, $url);
	curl_setopt($ch, CURLOPT_POST, 0);
	$output = curl_exec($ch);

	// checks to see if the user successfully logged in
	if(stripos($output,'login_form') != 0) {
		print "Sorry, an error has occurred. Please re-enter your login information.";
		exit;
	}
	
	print "Unavailable!";
	// FIX this! Low priority!
	exit;

	// course information
	$output = substring($output,"<div id=\"content\">","<div id=\"footer\">");
	$courseInfo = substring($output,"<table width=100% border=0 cellpadding=1 cellspacing=1>","<!-- Display ONLY if not EMBA -->");
	$courseInfo = substring($courseInfo,"<table>","</table>");
	$fCourseInfo = validateHTML($courseInfo);
	printCourseInfo($fCourseInfo);

	// grades information
	$str = "<!-- Display ONLY if not EMBA -->";
	$gradesInfo = substr($output,stripos($output,$str)+strlen($str));
	$fGradesInfo = validateHTML(substring($gradesInfo,"<table border=0 cellpadding=1 cellspacing=1>",$str));
	$fGradesInfo = substring($fGradesInfo,"<table>","</table>");
	printGradesInfo($fGradesInfo);

	// detailed information	
	$detailed =	substring($gradesInfo,"<table border=0 cellpadding=1 cellspacing=0>","<p><br>");
	$detailed = substring($detailed,"<table>","</table>");
	$fDetailed = validateHTML($detailed);
	printDetailed($fDetailed);

	curl_close($ch);

	function printCourseInfo($xmlString) {
		$xml = new SimpleXMLELement($xmlString);

		print "<p>";
		$i=0;
		foreach($xml->tr as $tr) {
			$j = 0;
			foreach($tr->td as $td) {
				$class = $j == 0 ? "title" : "value";

				if($i == 3) {
					$a = $td->a;
					if($a != "") {
						print "<div class='$class'><a href='{$a['href']}'>$a</a></div>";
						continue;
					}
				} 
				print "<div class='$class'>$td</div>";

				// increment counter
				$j++;
			}
			print "<div class='clear'></div>";
			$i++;
		}
		print "</p>";
	}
	
	function printGradesInfo($xmlString) {
		$xml = new SimpleXMLElement($xmlString);
		$i = 0;
		$grades = array();
		foreach($xml->tr as $tr) {
			if($i == 2) {
				$j = 0;
				foreach($tr->td as $td) {
					array_push($grades,$td);	
					$j++;
				}
			} else if($i == 4) $avg_grade = $tr->td;
			else if($i == 6) $trimmed = $tr->td;

			$i++;
		}

		print "<div class='header dgold'>Expected Grade Percentages</div>";
		print "<div class='sheader dgold'>A</div><div class='sheader dgold'>B</div>";
		print "<div class='sheader dgold'>C</div>";
		print "<div class='sheader dgold'>D</div>";
		print "<div class='sheader dgold'>E</div>";
		print "<div class='clear'></div>";

		foreach($grades as $grade)
			print "<div class='sheader gold'>$grade</div>";
		print "<div class='clear'></div>";

		print "<div class='header dgold'>Average Expected Grade</div>";
		print "<div class='header gold'>$avg_grade</div>";

		print "<div class='header dgold'>Trimmed Response Count</div>";
		print "<div class='header gold'>$trimmed</div>";
	}

	function printDetailed($xmlString) {
		$xmlString = validateHTML($xmlString);
		$pos = stripos($xmlString,"</table>");
		$xmlString = substr($xmlString,0,$pos-6)."</table>";

		$xml = new SimpleXMLElement($xmlString);

		$i = 0;
		$evaluation = array();
		foreach($xml->tr as $tr) {
			if($i >= 3) {
				$j = 0;
				
				$stats = array();
				foreach($tr->td as $td) {
					if($j == 1) $title = $td;	
					
					// appends the statistics into an array
					if($j > 1) {
						if($j == 2) $mean = $td;
						else if($j == 3) $dev = $td;
						else array_push($stats, $td);
					}
					
					$j++;
				}
				array_push($evaluation,array('title'=>$title,'mean'=>$mean,'dev'=>$dev,'stats'=>$stats));
			}
			$i++;
		}

		$i = 1;
		foreach($evaluation as $evalu) {
			print "<p>";
			print "<div class='evalTitle full dgold'>$i) {$evalu['title']}</div>";
			print "<div class='left half gold'>1 - Strongly Disagree</div><div class='left half gold'>7 - Strongly Agree</div>";
			print "<div class='clear'></div>";
			print "<div class='left half gold'>Mean = {$evalu['mean']}</div><div class='left half gold'>Standard Deviation = {$evalu['dev']}</div>"; 
			print "<div class='clear'></div>";

			$data1 = implode(',',$evalu['stats']);

			foreach($evalu['stats'] as &$val) {
				if($val == 0.0) $val = 0;
				//$val = (int)$val;
				//if($val != 0) $val .= "%";
			}
			$data2 = implode('|',$evalu['stats']);

			$img_url = "http://chart.apis.google.com/chart?cht=bvg&chd=t:$data1&chco=99873D&chls=2.0,0.0,0.0&chs=316x185&chg=12.5,25,3,3,0,0".
						"&chxt=x,y,x&chxl=0:|1|2|3|4|5|6|7|NA|1:|0|25|50|75|100|2:|$data2&chf=bg,s,F1E6B8&chbh=21,15,15";
			print "<img src='$img_url' alt=''/>";
			print "</p>";
			$i++;
		}
	}

	// strips characters and creates a valid xml feed
	function validateHTML($str) {
		return str_replace(array('target="_blank"',' class="headrow" NOWRAP','&nbsp; ','<b>','</b>',' align=center',' width=100% border=0 cellpadding=1 cellspacing=1',
		' class="headrow" width=70',' class="headrow" colspan=5',' width=130',' width=10',' valign=bottom',' NOWRAP','<br>','<hr>',
		' colspan=4',' colspan=3',' colspan=2',' align=right','valign=top',' border=0 cellpadding=1 cellspacing=1',' colspan=5',' class="headrow2"',
		' align=right',' class="tablealtrow"',' width=50',' width=35',' width=40',' colspan=7','&nbsp;',' border=0 cellpadding=1 cellspacing=0','&'),
		array('','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','AND'), $str);
	}

	// helper parser function
	function substring($string,$r1,$r2,$include=false) {
		$r1_loc = strpos($string,$r1);
		$r2_loc = strpos($string,$r2);
		$n_loc = $include ? $r1_loc + strlen($r1) : $r1_loc;
		$n_len = $include ? $r2_loc - $r1_loc - strlen($r1) : $r2_loc - $r1_loc + strlen($r2);
		return substr($string,$n_loc,$n_len);
	}
?>
