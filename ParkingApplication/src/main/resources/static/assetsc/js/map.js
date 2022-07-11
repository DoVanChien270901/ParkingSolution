var map = null;
var lat = 0;
var long = 0;
var infowindow = null;
var ddisplay = null;
var radius_circle = null;
var buttonDirection;
var markerArray = [];
var parkingLocations =[];
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
            console.log(parkingLocations);
        })
        .catch((error) => console.log("error", error));

//var parkingLocations = [{
//        "name": "Le Thi Rieng Parking Lot",
//        "address": "Cach Mang Thang Tam, Ward 15, District 10, Ho Chi Minh City, Vietnam",
//        "lat": 10.786140368621982,
//        "long": 106.66553523925666,
//    },
//    {
//        "name": "Truong Son Parking Lot",
//        "address": "Truong Son, Ward 15, District 10, Ho Chi Minh City, Vietnam",
//        "lat": 10.783623768095028,
//        "long": 106.66602110517302,
//    },
//    {
//        "name": "550 Street",
//        "address": "550 Street. Cach Mang Thang 8, Ward 11, District 3, Ho Chi Minh City, Vietnam",
//            "lat": 10.785919366395701,
//        "long": 106.66687897069089,
//    },
//    {
//        "name": "Thong Nhat Hospital",
//        "address": "669 Street. Cach Mang Thang 8, Ward 6, Tan Binh, Ho Chi Minh City, Vietnam",
//        "lat": 10.792474542422955,
//        "long": 106.65389890720036,
//    },
//    {
//        "name": "332 Cao Thang",
//        "address": "332 Street. Cao Thang, Ward 12, District 10, Ho Chi Minh City, Vietnam",
//        "lat": 10.775856472760822,
//        "long": 106.66872907259513,
//    },
//
//    {
//        "name": "152 Ba Son",
//        "address": "152 Street. Nguyen Thi Tu, Binh Hung Hoa B, Binh Tan, Ho Chi Minh City, Vietnam",
//        "lat": 10.815494097294602,
//        "long": 106.59142729657972,
//    },
//    {
//        "name": "Tan Binh Industrial Zone",
//        "address": "CN6 Road, Son Ky, Tan Phu, Ho Chi Minh City, Vietnam",
//        "lat": 10.808647358759579,
//        "long": 106.61051074577816,
//    },
//    {
//        "name": "Dong Phuong 4 Restaurant",
//        "address": "309 Dong Hung Thuan 29, Dong Hung Thuan, District 12, Ho Chi Minh City, Vietnam",
//        "lat": 10.835440504398681,
//        "long": 106.6285604770306,
//    },
//]

function listElements(name, addr, calulate) {
    const ul = document.querySelector('.list-items');
    const li = document.createElement('li');
    const div = document.createElement('div');
    const a = document.createElement('a');
    const address = document.createElement('p');
    const radius = document.createElement('p');
    const button = document.createElement('button');

    // div 
    div.classList.add('shop-item');
    div.setAttribute('id', 'shop-item');

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
            '<a type="button" th:href="@{}" class="direction" id="direction"><i class="fa fa-fa fa-arrow-alt-circle-right"></i> Direction</a>';
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
    if (!navigator.geolocation) {
        window.alert("Your browser does not support geolocation feature !");
    } else {
        showMap();
    }
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