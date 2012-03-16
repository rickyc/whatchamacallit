<?php
	set_time_limit(0);
	require_once 'curl.php';
	require_once 'global_methods.php';
	require_once 'config.php';

	// $terms = array("Fall 2009");// "Summer 2009", "Spring 2009");
  // scrape("Fall 2008");
	// A U G P

	function shouldScrapeByTerm($term) {
		$cc = new cURL();
		$output = $cc->get('http://www.nyu.edu/registrar/listings/');
		$output = substring($output, 'Course data was last updated on ', '<form method="post" name="selForm" action="results.html">', true);
		$date = substr($output, 0, strlen($output)-5);
		$scrapeDate = date('Y-m-d H:i:s', strtotime($date));

		$sql = 'SELECT * FROM `nyu_courses_settings`';
		$result = mysql_query($sql);
		$row = mysql_fetch_assoc($result);

		if(!(stripos($term,"Fall") === false)) $column = "latest_fall_scrape";
		else if(!(stripos($term,"Spring") === false)) $column = "latest_spring_scrape";
		else if(!(stripos($term,"Winter") === false)) $column = "latest_ws_scrape";
		else if(!(stripos($term,"Summer") === false)) $column = "latest_ws_scrape";

		if($row[$column] != $scrapeDate) {
			scrape($term,$scrapeDate);
			deleteOldData($term,$scrapeDate);
			$sql = "UPDATE `nyu_courses_settings` SET `$column` = '$scrapeDate'";
			mysql_query($sql);
		}
	}

	function scrape($term, $date) {
		$levels = array("U","G","P");
		$schools = array("V","N","D","K","G","C","T","P","Y","S","E","H","U");
		
		foreach($levels as $level)
			foreach($schools as $school)
				scrapeDataBySchool($school,$term,$level,$date);

		print 'done';
	}

	function scrapeDataBySchool($school, $term, $level, $scrapeDate) {
		$cc = new cURL();

		$postData = "crsName=&level=$level&school=$school&showCX=A&subject=&term=$term";

		$output = $cc->post("http://www.nyu.edu/registrar/listings/results.html",$postData);
		$parsedOutput = substring($output,"<tbody>","</tbody>",false);
		$parsedOutput = str_replace(array('&nbsp;','&'),array(' ','AND'),$parsedOutput);

		$xmlString = "<?xml version='1.0' encoding='UTF-8'?>$parsedOutput";	

		$xml = new SimpleXMLElement($xmlString);

		// if courseTag is true then close the course tag
		$courseTag = false;
		// loops through each row in the XML document
		foreach($xml->tr as $row) {
			$class = $row['class'];
			
			// title
			if($class == "T") {
				// college of dentistry exception
				if($row->td->a == '') {
					$number = trim(substr($row->td,0,strpos($row->td,"-")));
					$title = trim(substr($row->td,strpos($row->td,"-")+1));
					$url = "";
				} else {
					$number = $row->td->a;
					$title = trim(substr($row->td,strpos($row->td,"-")+1));
					$url = $row->td->a['href'];
				}
			}

			// default
			if($class == "D" || $class == "C1" || $class == "C2") {
				$td = $row->td;

				$section = $td[0];

				if($td[1] == "ANDgt;") {
					$restricted = true;
					$call = $td[1]->a;
				} else {
					$restricted = false;
					$call = $td[1];
				}

				if($class == "D") $status = "Open";
				if($class == "C1") $status = "Closed";
				if($class == "C2") $status = "Canceled";
				
				$days = $td[3] == "" ? "N/A" : $td[3];
				$instructor = $td[9] == " " || $td[9] == "STAFF" ? "STAFF, TBA" : trim($td[9]);
				
				$start = $end = '';			
				$hours = $td[4]; // 12:30pm - 04:30pm
				if(stripos($hours,'-')) {
					$start = date("H:i:s", getTime(substr($hours,0,7)));
					$end = date("H:i:s", getTime(substr($hours,10,7)));
				}

				$site = $td[5] == "" ? "N/A" : $td[5];
				$location = $td[6] == " " ? "TBA" : $td[6];
				$activity = $td[7];
				$credit = $td[8];
				$url = htmlspecialchars(str_replace(array('ANDamp;'),array('&'),$url));

				$sql = "INSERT INTO `nyu_courses` (`school`,`term`,`level`,`course_title`,`course_number`,`section`,`call_number`,
						`course_status`,`meet_days`,`site`,`location`,`activity`,`credits`,`restricted`,`start_time`,`end_time`,`instructor`,
						`url`,`course_description`,`creation_time`,`scrape_date`,`status`) VALUES('$school','$term','$level','$title',
						'$number','$section','$call','$status','$days','$site','$location','$activity','$credit','$restricted','$start',
						'$end','$instructor','$url','N/A',NOW(),'$scrapeDate','active')";
				mysql_query($sql);
			}
		}
	}

	function deleteOldData($term, $date) {
		$sql = sprintf("DELETE FROM `nyu_courses` WHERE `term` = '%s' AND `scrape_date` != '%s'", $term, $date);
		mysql_query($sql);
	}
?>
