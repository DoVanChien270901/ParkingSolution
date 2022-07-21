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

function listElements(parking, calulate) {
    const ul = document.querySelector('.list-items');
    const li = document.createElement('li');
    const div = document.createElement('div');
    const a = document.createElement('a');
    const address = document.createElement('p');
    const radius = document.createElement('p');
    const button = document.createElement('a');
    const row14 = document.createElement('row');
    const row24 = document.createElement('row');
    const col14 = document.createElement('col');
    const col24 = document.createElement('col');
    const col34 = document.createElement('col');
    const col44 = document.createElement('col');
    const nop = document.createElement('p');
    const rentcost = document.createElement('p');
    // div 
    div.classList.add('shop-item');
    div.setAttribute('id', 'shop-item');
    //div.setAttribute('value', parking.name);

    // name
    a.innerHTML = parking.name;
    a.href = '#';
    a.classList.add('shop-item-name');
    a.setAttribute('id', 'shop-item-name');

    // address
    address.innerHTML = parking.address;

    // radius
    radius.setAttribute('id', 'radius');
    radius.innerHTML = '<i class="fa fa-road"></i> ' + calulate.toFixed(3) + ' km ';

    nop.setAttribute('id', 'nop');
    nop.innerHTML = '<i class="fa fa-chair"></i> ' + parking.blank + "/" + parking.nop;

    // button booking
    button.href = './booking?parkingname=' + parking.name;
    button.setAttribute('class', 'booking-list');
    //button.setAttribute('type', 'button');
    button.innerHTML = 'Booking';
    button.setAttribute('value', parking.name);
    
    // rentcost
    rentcost.setAttribute('class', 'salary');
    rentcost.innerHTML = '<i class="fa fa-money-check"></i> ' + parking.rentcost;
    // row 
    row14.classList.add('row');
    row24.classList.add('row');
    // col14 
    col14.classList.add('col-6');
    col14.classList.add('pt-2');
    // col24
    col24.classList.add('col-6');
    col24.classList.add('pt-2');
    // col34
    col34.classList.add('col-6');
    col34.classList.add('pt-2'); 
    // col44
    col44.classList.add('col-6');
    col44.classList.add('p-2');
    
    div.appendChild(a);
    div.appendChild(address);
    // div row
    div.appendChild(row14);
    div.appendChild(row24);
    // row 
    row14.appendChild(col14);
    row14.appendChild(col24);
    row24.appendChild(col34);
    row24.appendChild(col44);
    col14.appendChild(radius);
    col24.appendChild(nop);
    col34.appendChild(rentcost);
    col44.appendChild(button);
    
    li.appendChild(div);
    ul.appendChild(li);
}
function contentElement(parking) {
    return '<div id="content-parking">' + '<strong>' + parking.name + '</strong>' + '<br/>' + parking.address + '<div class="p-1"></div>' +
        '<button type="button" class="direction" id="direction"><i class="fa fa-fa fa-arrow-alt-circle-right"></i> Direction</button>' + '&nbsp&nbsp' +
        '<button type="button" class="booking-box" id=""><i class="fa fa-fa fa-parking"></i> Booking</button>';
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
            listElements(parkingLocations[i], cal);
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
        var contentOnMarker = contentElement(parkingLocations[i]);
        // create marker
        markerParking = new google.maps.Marker({
            position: new google.maps.LatLng(parkingLocations[i]['latitude'], parkingLocations[i]['longtitude']),
            map: map,
            icon: iconParking,
            content: contentOnMarker,
        });
        google.maps.event.addListener(markerParking, "click", (function (markerParking, i) {
            return function () {
                var placePos = inforwindow.setPosition(this.position);
                inforwindow.setContent(this.content);
                inforwindow.open(map, placePos);
                // inforwindow.setOptions({
                //     pixelOffset: new google.maps.Size(5.55, -24.5)
                // });
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
                    var contentOnShop = contentElement(parkingLocations[i]);
                     var placeListPos = inforwindow.setPosition(posI)
                    inforwindow.setContent(contentOnShop);
                    // inforwindow.setOptions({
                    //     pixelOffset: new google.maps.Size(5.55, -24.5)
                    // });
                    inforwindow.open(map);
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

function showMap() {
    inforwindow = new google.maps.InfoWindow();
     inforwindow.setOptions({
        pixelOffset: new google.maps.Size(5.55, -24.5)
    });
    window.navigator.geolocation.getCurrentPosition(function (pos) {
        lat = pos.coords.latitude;
        long = pos.coords.longitude;
        map = new google.maps.Map(document.getElementById("map"), {
            center: {lat: lat, lng: long},
            zoom: 15,
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
        document.getElementById("getLocation").addEventListener("click", function() {
            map.setCenter(markerLocation.position);
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


