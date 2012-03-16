<?php
	header ('Content-Type:text/xml');
	print "<?xml version='1.0' encoding='UTF-8'?>";

	$ch = curl_init();

	// $_GET['instructor'] = "KRINSKY,%20CAROL";
	curl_setopt($ch, CURLOPT_URL, "http://www.nyu.edu/cas/ceg/GEG_REQUEST.php?TYPE=CINSTR&SEARCH_CINSTR={$_GET['instructor']}");
	curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);

	$output = curl_exec($ch);
	curl_close($ch);     

	if(strpos($output,"This data does not exist, please use the three links above to continue your search.") == 0) {
		$parsedOutput = str_replace("&","&amp;",substring($output,"<table","</body>"));
		$xmlString = "<?xml version='1.0' encoding='UTF-8'?>$parsedOutput";
	// print $xmlString;
		
		$xml = new SimpleXMLElement($xmlString);

		$generatedXML = "<courses>";

		foreach($xml->tr as $row) {
			if($row['onclick'] != null) {
				$generatedXML .= "<course>";

				$courseURLID = substr(substring($row['onclick'],"INDX=","','#FFFFFF'"),5);
				$generatedXML .= "<url>http://www.nyu.edu/cas/ceg/GEG_REQUEST_INDX.php?INDX=$courseURLID</url>\n";
				
				$counter = 0;
				foreach($row->td as $col) {
					$col = htmlspecialchars($col);
					switch($counter) {
						case 0: $generatedXML .= "<id>$col</id>\n"; 				break;
						case 1: $generatedXML .= "<subject>$col</subject>\n"; 		break;
						case 2: $generatedXML .= "<instructor>$col</instructor>\n";	break;
						case 3: $generatedXML .= "<title>$col</title>\n"; 			break;
						case 4: $generatedXML .= "<semester>$col</semester>\n";		break;
					}
					$counter++;
				}

				$generatedXML .= "</course>";
			}
		}

		$generatedXML .= "</courses>";

		print $generatedXML;
	}

	function substring($inputstr,$delimeterLeft,$delimeterRight) {
		$posLeft  = stripos($inputstr,$delimeterLeft);
		$posRight = stripos($inputstr,$delimeterRight,$posLeft+1);
		return  substr($inputstr,$posLeft,$posRight-$posLeft);
	}
?>
