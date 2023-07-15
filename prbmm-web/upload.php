<?php
// Nelle versioni di PHP precedenti alla 4.1.0 si deve utilizzare  $HTTP_POST_FILES anzichè
// $_FILES.

$uploaddir = 'upload/';
$content = $_POST["content"];

if ( file_put_contents($uploaddir.$_POST["filename"], $content) ) {
    echo "File salvato con successo";
}
else{
    echo "Errore";
}
?>