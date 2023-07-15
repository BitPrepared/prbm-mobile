<html>
    <head>
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-alpha.3/css/bootstrap.min.css" integrity="sha384-MIwDKRSSImVFAZCVLtU0LMDdON6KVCrZHyVQQj6e8wIEJkW4tvwqXrbMIya1vriY" crossorigin="anonymous">
		<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
		<style type="text/css">
			#map_wrapper {
				height: 400px;
			}

			#map_canvas {
				width: 100%;
				height: 100%;
			}
			.tooltip-inner {
				min-width: 100px;
				max-width: 100%; 
			}
		</style>
		<link href="https://fonts.googleapis.com/icon?family=Material+Icons"
      rel="stylesheet">

	</head>
    <body>
<?php
	$handle = fopen("upload/".$_GET["percorso"], "r");
    $contents = fread($handle, filesize("upload/".$_GET["percorso"]));
    fclose($handle);

    $row = json_decode($contents, true);
?>
	<div id="map_wrapper">
        <div id="map_canvas" class="mapping"></div>
    </div>

<script type="text/javascript">
jQuery(function($) {
    // Asynchronously Load the map API 
    var script = document.createElement('script');
    script.src = "http://maps.googleapis.com/maps/api/js?key=AIzaSyAczZHcQUkRVWYef7c-bbPaMpX_2Legfj4&sensor=false&callback=initialize";
    document.body.appendChild(script);
});

function initialize() {
    var map;
    var bounds = new google.maps.LatLngBounds();
    var mapOptions = {
        mapTypeId: 'roadmap'
    };
                    
    // Display a map on the page
    map = new google.maps.Map(document.getElementById("map_canvas"), mapOptions);
    map.setTilt(45);
        
    // Multiple Markers
	<?php 
		$countPoint = 0;
		echo "var markers = [";
		for($countPoint = 0; $countPoint< count($row["units"]); $countPoint++){
			echo "['".$countPoint."', ".$row["units"][$countPoint]["latitude"].", ".$row["units"][$countPoint]["longitude"]."]";
			if($countPoint+1 < count($row["units"]))
				echo ",";
		}
		echo "];";
	?>
    // Info Window Content
    


    // Display multiple markers on a map
    var infoWindow = new google.maps.InfoWindow(), marker, i;
    
    // Loop through our array of markers & place each one on the map  
    for( i = 0; i < markers.length; i++ ) {
        var position = new google.maps.LatLng(markers[i][1], markers[i][2]);
        bounds.extend(position);
        marker = new google.maps.Marker({
            position: position,
            map: map,
            title: markers[i][0],
            icon:'http://maps.google.com/mapfiles/kml/pal4/icon49.png'
        });
        
		// Allow each marker to have an info window    
        google.maps.event.addListener(marker, 'click', (function(marker, i) {
            return function() {
                infoWindow.setContent("Punto "+(i+1)+" ");
                infoWindow.open(map, marker);
            }
        })(marker, i));

        // Automatically center the map fitting all markers on the screen
        map.fitBounds(bounds);
    }

    // Override our map zoom level once our fitBounds function runs (Make sure it only runs once)
    var boundsListener = google.maps.event.addListener((map), 'bounds_changed', function(event) {
        this.setZoom(14);
        google.maps.event.removeListener(boundsListener);
    });
    
}</script>	


    <table class="table">
	  <thead>
	    <tr>
	      <th>#</th>
	      <th>SX Lontano</th>
	      <th>SX Vicino</th>
	      <th>Dati</th>
	      <th>DX Vicino</th>
	      <th>DX Lontano</th>
	    </tr>
	  </thead>
	  <tbody>
<?php
	for($i=0; $i<count($row["units"]); $i++){
		echo '<tr>';
		$more = $i+1;
		echo '<th scoper="row">'.$more.'</th>';
		echo "<td>";
			$elem = $row["units"][$i]["entitiesFarLeft"];
			for($j=0; $j < count($elem); $j++){
				$extra = "";
				for($i1=0; $i1 < count($elem[$j]["extraFields"]); $i1++){
					if($elem[$j]["extraFields"][$i1]["value"]!= ""){
						$extra .= $elem[$j]["extraFields"][$i1]["title"].": ".$elem[$j]["extraFields"][$i1]["value"]."<br/>";
					}
				}
				echo '<a class="smartTooltip"  data-toggle="tooltip" title="'.$extra.'">'.$elem[$j]["caption"]." ". $elem[$j]["description"]."</a>";
				if(isset($elem[$j]["pictureName"])){
					echo '<a class="btn" target="_blank" href="./upload/images/'.$elem[$j]["pictureName"].'"><i class="material-icons">camera_alt</i></a>';
				}
				echo "<br/>";
			}
		echo "</td>";
		echo "<td>";
			$elem = $row["units"][$i]["entitiesNearLeft"];
			for($j=0; $j < count($elem); $j++){
				$extra = "";
				for($i1=0; $i1 < count($elem[$j]["extraFields"]); $i1++){
					if($elem[$j]["extraFields"][$i1]["value"]!= ""){
						$extra .= $elem[$j]["extraFields"][$i1]["title"].": ".$elem[$j]["extraFields"][$i1]["value"]."<br/>";
					}
				}
				echo '<a class="smartTooltip" data-toggle="tooltip" title="'.$extra.'">'.$elem[$j]["caption"]." ". $elem[$j]["description"]."</a>";
				if(isset($elem[$j]["pictureName"])){
					echo '<a class="btn" target="_blank" href="./upload/images/'.$elem[$j]["pictureName"].'"><i class="material-icons">camera_alt</i></a>';
				}
				echo "<br/>";
			}
		echo "</td>";

		echo "<td>";
			$elem = $row["units"][$i];

			echo "Metri: ". $elem["meter"];
			echo "<br/>";
			echo "Minuti: ".$elem["minutes"];
			echo "<br/>";
			echo "Azimut: ".$elem["azimut"];
			echo "<br/>";
			echo "Coordinate: ".$elem["latitude"]." - ".$elem["longitude"];
		echo "</td>";

		echo "<td>";
			$elem = $row["units"][$i]["entitiesNearRight"];
			for($j=0; $j < count($elem); $j++){
				$extra = "";
				for($i1=0; $i1 < count($elem[$j]["extraFields"]); $i1++){
					if($elem[$j]["extraFields"][$i1]["value"]!= ""){
						$extra .= $elem[$j]["extraFields"][$i1]["title"].": ".$elem[$j]["extraFields"][$i1]["value"]."<br/>";
					}
				}
				echo '<a class="smartTooltip" data-toggle="tooltip" title="'.$extra.'">'.$elem[$j]["caption"]." ". $elem[$j]["description"]."</a>";
				if(isset($elem[$j]["pictureName"])){
					echo '<a class="btn" target="_blank" href="./upload/images/'.$elem[$j]["pictureName"].'"><i class="material-icons">camera_alt</i></a>';
				}
				echo "<br/>";
			}
		echo "</td>";
		echo "<td>";
			$elem = $row["units"][$i]["entitiesFarRight"];
			for($j=0; $j < count($elem); $j++){
				$extra = "";
				for($i1=0; $i1 < count($elem[$j]["extraFields"]); $i1++){
					if($elem[$j]["extraFields"][$i1]["value"]!= ""){
						$extra .= $elem[$j]["extraFields"][$i1]["title"].": ".$elem[$j]["extraFields"][$i1]["value"]."<br/>";
					}
				}
				echo '<a class="smartTooltip"  data-toggle="tooltip" title="'.$extra.'">'.$elem[$j]["caption"]." ". $elem[$j]["description"]."</a>";
				if(isset($elem[$j]["pictureName"])){
					echo '<a class="btn" target="_blank" href="./upload/images/'.$elem[$j]["pictureName"].'"><i class="material-icons">camera_alt</i></a>';
				}
				echo "<br/>";
			}
		echo "</td>";

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


<script>
$(document).ready(function(){
    $('[data-toggle="tooltip"]').tooltip(); 
});
$('.smartTooltip').tooltip({placement: "bottom", html: true});
$('a[rel=popover]').popover({
  html: true,
  trigger: 'hover',
  placement: 'bottom',
  content: function(){return '<img src="'+$(this).data('img') + '" />';}
});
</script>

</body>
</html>
