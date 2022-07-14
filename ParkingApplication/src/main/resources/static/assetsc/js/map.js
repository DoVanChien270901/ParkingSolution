var map = null;
var lat = 0;
var long = 0;
var infowindow = null;
var ddisplay = null;
var radius_circle = null;
var buttonDirection;
var markerArray = [];
var parkingLocations = [];

//call api get list parking

let myHeaders = new Headers();
myHeaders.append("Content-Type", "application/json");

var requestOptions = {
    method: "GET",
    headers: myHeaders,
    body: null,
    redirect: "follow",
};
var api = "http://localhost:8080/list-parking";
fetch(api, requestOptions)
        .then((response) => {
            return response.json();
        })
        .then((result) => {
            parkingLocations = result;
            showMap();
        })
        .catch((error) => console.log("error", error));

function listElements(name, addr, calulate) {
    const ul = document.querySelector('.list-items');
    const li = document.createElement('li');
    const div = document.createElement('div');
    //const div1 = document.createElement('div');
    const a = document.createElement('a');
    const address = document.createElement('p');
    const radius = document.createElement('p');
    const button = document.createElement('button');

    // div 
    div.classList.add('shop-item');
    div.setAttribute('id', 'shop-item');
    // div 
//    div1.setAttribute('id', 'item-parking');
//    div1.setAttribute('value', name);

    // name
    a.innerHTML = name;
    a.href = '#';
    a.classList.add('shop-item-name');
    a.setAttribute('id', 'shop-item-name');

    // address
    address.innerHTML = addr;

    // radius
    radius.innerHTML = calulate.toFixed(3) + ' km ';

    // button booking
    button.setAttribute('class', 'booking-list');
    button.setAttribute('type', 'button');
    //button.setAttribute('id', 'item-parking');
    button.setAttribute('value', name);
    button.innerHTML = '<i class="fa fa-parking"></i> ' + 'Booking';

    div.appendChild(a);
    div.appendChild(address);
    div.appendChild(radius);
    radius.appendChild(button);
    li.appendChild(div);
    ul.appendChild(li);
}

function contentElement(name, address) {
    return '<div id="content-parking">' + '<strong>' + name + '</strong>' + '<br/>' + address + '<div class="p-1"></div>' +
            '<button type="button" class="direction" id="direction"><i class="fa fa-fa fa-arrow-alt-circle-right"></i> Direction</button>';
}

function calculate(lat1, lat2, long1, long2) {
    if ((lat1 == lat2) && (long1 == long2)) {
        return 0;
    }
    var R = 6371;
    var dLat1 = lat1 * (Math.PI / 180);
    var dLat2 = lat2 * (Math.PI / 180);
    var difflat = (dLat2 - dLat1);
    var difflon = (long2 - long1) * (Math.PI / 180);
    var d = 2 * R * Math.asin(Math.sqrt(Math.sin(difflat / 2) * Math.sin(difflat / 2) + Math.cos(dLat1) * Math.cos(dLat2) * Math.sin(difflon / 2) * Math.sin(difflon / 2)));
    return d;
}

function showPlace(latpos, longpos) {
    var latpos = latpos.toFixed(6);
    var longpos = longpos.toFixed(7);
    // console.log(latpos, longpos);
    for (var i = 0; i < parkingLocations.length; i++) {
        var latParking = parkingLocations[i]['latitude'];
        var longParking = parkingLocations[i]['longtitude'];
        //console.log(latParking, longParking);
        // check radius
        var cal = calculate(latpos, latParking, longpos, longParking);
        var radius = 10.000;
        // show multiple marker
        if (cal < radius) {
            listElements(parkingLocations[i].name, parkingLocations[i].address, cal);
        }
        //console.log(req.des);
        var markerParking, i;
        // icon
        var iconParking = {
            url: "/assetsc/img/marker.png",
            size: new google.maps.Size(71, 71),
            scaledSize: new google.maps.Size(45, 45),
            origin: new google.maps.Point(0, 0),
            anchor: new google.maps.Point(17, 34),
        };
        var contentOnMarker = contentElement(parkingLocations[i]['name'], parkingLocations[i]['address']);
        // create marker
        markerParking = new google.maps.Marker({
            position: new google.maps.LatLng(parkingLocations[i]['latitude'], parkingLocations[i]['longtitude']),
            map: map,
            icon: iconParking,
            content: contentOnMarker,
        });
        google.maps.event.addListener(markerParking, "click", (function (markerParking, i) {
            return function () {
                inforwindow.setContent(this.content);
                inforwindow.open(map, markerParking);
                map.panTo(this.position);
                showDirection(this.position);
            }
        })(markerParking, i));

    }
    // show place marker on listShop
    $(document).ready(function () {
        $('#shop-item #shop-item-name').click(function () {
            var idParking = this.textContent;
            for (var i in parkingLocations) {
                if (idParking == parkingLocations[i]['name']) {
                    var posI = new google.maps.LatLng(parkingLocations[i]['latitude'], parkingLocations[i]['longtitude']);
                    var contentOnShop = contentElement(parkingLocations[i]['name'], parkingLocations[i]['address']);
                    markerParking = new google.maps.Marker({
                        position: posI,
                        map: map,
                        icon: iconParking,
                        content: contentOnShop
                    });
                    inforwindow.setContent(markerParking.content);
                    inforwindow.open(map, markerParking);
                    map.panTo(posI);
                    showDirection(posI);
                }
            }
        });
    });
}

function showDirection(data) {
    if (ddisplay || dservice) {
        ddisplay.setMap(null);
    }
    var dservice = new google.maps.DirectionsService();
    ddisplay = new google.maps.DirectionsRenderer({suppressMarkers: true});

    ddisplay.setMap(map);
    dservice.route({
        origin: {lat: lat, lng: long},
        destination: data,
        travelMode: 'DRIVING',
        provideRouteAlternatives: true,
        unitSystem: google.maps.UnitSystem.IMPERIAL,
        avoidTolls: true,
        avoidHighways: false,
        optimizeWaypoints: true
    }, function (result, status) {
        if (status == "OK") {
            $("#direction").on("click", function () {
                ddisplay.setDirections(result);
                document.getElementById("distance").setAttribute('value', 'Distance: ' + (result.routes[0].legs[0].distance.value / 1000).toFixed(3) + ' km');
                document.getElementById("duration").setAttribute('value', 'Duration: ' + result.routes[0].legs[0].duration.text);
            });
        } else {
            ddisplay.setDirections(null);
        }
    });
}

function getPosition() {
    navigator.geolocation.getCurrentPosition(function (position) {
        initialLocation = new google.maps.LatLng(position.coords.latitude, position.coords.longitude);
        map.setCenter(initialLocation);
    });
}

function showMap() {
    inforwindow = new google.maps.InfoWindow();
    window.navigator.geolocation.getCurrentPosition(function (pos) {
        lat = pos.coords.latitude;
        long = pos.coords.longitude;
        map = new google.maps.Map(document.getElementById("map"), {
            center: {lat: lat, lng: long},
            zoom: 14,
            fullscreenControl: false,
            streetViewControl: false,
        });
        var markerLocation = new google.maps.Marker({
            position: {lat: lat, lng: long},
            map: map,
            icon: {
                path: google.maps.SymbolPath.CIRCLE,
                scale: 10,

                fillOpacity: 1,
                strokeWeight: 2,
                fillColor: '#5384ed',
                strokeColor: '#ffffff'
            }
        });
        showPlace(lat, long);
    });
}
$(document).ready(function () {
    $('#shop-item booking-list').click(function () {
        var id = $(this).attr('value');
        console.log(id);
    });
});
