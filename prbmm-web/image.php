
<?php
error_reporting(E_ALL);
ini_set('display_errors', 1);
if (! function_exists ( 'http_response_code' )) {
	function http_response_code($newcode = NULL) {
		static $code = 200;
		
		if ($newcode !== NULL) {
			header ( 'X-PHP-Response-Code: ' . $newcode, true, $newcode );
			if (! headers_sent ())
				$code = $newcode;
		}
		return $code;
	}
}

try {
	$image = $_POST['image'];
	//$binary = base64_decode($image);

	$file = fopen("upload/images/".$_POST['name'], 'w');
	$data = explode(',', $image);

    fwrite($file, base64_decode($data[count($data)-1])); 
	fclose($file);

	echo 'Image upload complete!!, Please check your php file directory……';
	http_response_code ( 200 );
} catch (Exception $e) {
	http_response_code ( 500 );
}
?>
