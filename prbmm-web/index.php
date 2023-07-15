<html>
    <head>
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-alpha.3/css/bootstrap.min.css" integrity="sha384-MIwDKRSSImVFAZCVLtU0LMDdON6KVCrZHyVQQj6e8wIEJkW4tvwqXrbMIya1vriY" crossorigin="anonymous">
    </head>
    <body>
        <table class="table table-inverse">
            <thead>
                <tr>
                  <th>Nome</th>
                  <th>Autore</th>
                  <th>Posto</th>
                  <th>Data</th>
                </tr>
            </thead>
            <tbody>
<?php
$dir = "upload";
$dh  = opendir($dir);
while (false !== ($filename = readdir($dh))) {
    $files[] = $filename;
}
sort($files);
for($i=2; $i < count($files); $i++ ){
    //Apro e leggo file

    if ($files[$i] == ".DS_Store" || $files[$i] == "images"){
        continue;
    }

    $handle = fopen("upload/".$files[$i], "r");
    $contents = fread($handle, filesize("upload/".$files[$i]));
    fclose($handle);
    
    $percorso = json_decode($contents, true);
    echo  "<tr class='clickable-row' data-href='percorso.php?percorso=".$files[$i]."'
>";
        echo "<td>".$percorso["title"]."</td>";
        echo "<td>".$percorso["authors"]."</td>";
        echo "<td>".$percorso["place"]."</td>";
        echo "<td>".$percorso["date"]."</td>";
    echo "</tr>";
}
?>
</tbody>
</table>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.0.0/jquery.min.js" integrity="sha384-THPy051/pYDQGanwU6poAc/hOdQxjnOEXzbT+OuUAFqNqFjL+4IGLBgCJC3ZOShY" crossorigin="anonymous"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/tether/1.2.0/js/tether.min.js" integrity="sha384-Plbmg8JY28KFelvJVai01l8WyZzrYWG825m+cZ0eDDS1f7d/js6ikvy1+X+guPIB" crossorigin="anonymous"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-alpha.3/js/bootstrap.min.js" integrity="sha384-ux8v3A6CPtOTqOzMKiuo3d/DomGaaClxFYdCu2HPMBEkf6x2xiDyJ7gkXU0MWwaD" crossorigin="anonymous"></script>
<script>
jQuery(document).ready(function($) {
    $(".clickable-row").click(function() {
        window.document.location = $(this).data("href");
    });
});
</script>
</body>
</html>
