<?php
	require_once 'config.php';

	$sql = 'SELECT * FROM `nyu_courses_settings`';
	$row = mysql_fetch_assoc(mysql_query($sql));
	
	
	// REQUIRED POST variables
	$sqlAndArray = array();

	$currentTerm = $_POST['term'];

	if(stripos($currentTerm, 'Fall') !== false)
		$scrapeString = 'fall';
	else if(stripos($currentTerm, 'Spring') !== false)
		$scrapeString = 'spring';
	else 
		$scrapeString = 'ws';

	array_push($sqlAndArray, "`scrape_date` = '{$row['latest_'.$scrapeString.'_scrape']}'");
	array_push($sqlAndArray, "`term` = '{$_POST['term']}'");

	$pagination = (intval($_POST['page'])-1)*100;
	
	if($_POST['level'] != 'A') array_push($sqlAndArray, "`level` = '{$_POST['level']}'");
	if($_POST['school'] != 'A') array_push($sqlAndArray, "`school` = '{$_POST['school']}'");
	if($_POST['subject'] != '') array_push($sqlAndArray, "`course_number` LIKE '%%{$_POST['subject']}%%'");

	// OPTIONAL POST variables
	$search = $_POST['searchQuery'];
	if($search != '(null)') {
		$sqlOrArray = array();
		if($search != '') {
			if(intval($_POST['courseNumber']))
				array_push($sqlOrArray, "`course_number` LIKE '%%$search%%'");
			if(intval($_POST['courseTitle']))
				array_push($sqlOrArray, "`course_title` LIKE '%%$search%%'");
			if(intval($_POST['professor']))
				array_push($sqlOrArray, "`instructor` LIKE '%%$search%%'");
			if(intval($_POST['location']))
				array_push($sqlOrArray, "`location` LIKE '%%$search%%'");
			if(intval($_POST['callNumber']))
				array_push($sqlOrArray, "`call_number` LIKE '%%$search%%'");

			array_push($sqlAndArray,"(".generateSQLFromArray($sqlOrArray,"OR").")");
		}

		if($_POST['sTime'] != "")
			array_push($sqlAndArray,"`start_time` >= '{$_POST['sTime']}'");
		if($_POST['eTime'] != "")
			array_push($sqlAndArray,"`end_time` <= '{$_POST['eTime']}'");

		if($_POST['courseStatus'] != "All")
			array_push($sqlAndArray,"`course_status` = '{$_POST['courseStatus']}'");

		$daysArray = array('M','T','W','R','F','S','U');
		$sqlOrArray = array();
		foreach($daysArray as $day) {
			if(intval($_POST[strtolower($day)]))
				array_push($sqlOrArray, "`meet_days` LIKE '%%$day%%'");
		}
		if(sizeof($sqlOrArray) > 0) array_push($sqlAndArray,"(".generateSQLFromArray($sqlOrArray,"OR").")");
	}

	// generate queries
	function generateSQLFromArray($array, $separator) {
		$tempSql = '';
		$i = 0;
		foreach($array as $statement) {
			$tempSql .= $statement;
			if($i++ != sizeof($array)-1)
				$tempSql .= " $separator ";
		}
		return $tempSql;
	}

	$whereStatement = generateSQLFromArray($sqlAndArray, 'AND');
	if($whereStatement != '') $whereStatement = "WHERE $whereStatement";

	// generate sql
	$sql = sprintf("SELECT * FROM `nyu_courses` %s ORDER BY `id` ASC LIMIT %d, 100", $whereStatement, $pagination);
	$result = mysql_query($sql);

	$json = '{"courses": [';
	while($row = mysql_fetch_assoc($result)) {
		extract($row);
		if($currentTitle != $course_title) {
			if($currentTitle != '') 
				$json = substr($json,0,strlen($json)-1) . "] },";
			$currentTitle = $course_title;
			$json .= "{ \"title\": \"".addslashes($course_title)."\", \"course_num\":\"$course_number\", \"url\":\"$url\",".
						"\"school\":\"$school\", \"term\":\"$term\", \"sections\":[";
		} 

		$start_time = $start_time == "00:00:00" ? "" : date('h:ia', strtotime($start_time));
		$end_time = $end_time == "00:00:00" ? "" : date('h:ia', strtotime($end_time));

		$json .= "{ \"section\": \"$section\", \"call\": \"$call_number\", \"status\": \"$course_status\", \"restricted\": \"$restricted\",
					\"days\": \"$meet_days\", \"site\": \"$site\", \"location\": \"$location\", \"activity\": \"$activity\",
					\"credits\": \"$credits\", \"stime\": \"$start_time\", \"etime\": \"$end_time\", \"instructor\": \"$instructor\"
				 },";
	}
	$json = substr($json,0,strlen($json)-1) . ']} ]}';

	print $json;
?>
