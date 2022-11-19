const PATH_TO_ROOT_OF_SITE = "http://localhost:8081/"
const PATH_TO_LOCATION_NOT_GRANTED = "http://localhost:8081/notgranted"
const EXPIRATION_TIME_IN_DAYS = 1/24/3;

function getLocation() {
    console.log(getCookie("control"))
    console.log(document.cookie)
    if (getCookie("control") == "0") {
        navigator.permissions && navigator.permissions.query({name: 'geolocation'})
            .then(function(PermissionStatus) {
                if (PermissionStatus.state == 'granted') {
                } else if (PermissionStatus.state == 'prompt') {
                    navigator.geolocation.getCurrentPosition(function (position) {
                        setCookie("user_lat", position.coords.latitude, EXPIRATION_TIME_IN_DAYS)
                        setCookie("user_lon", position.coords.longitude, EXPIRATION_TIME_IN_DAYS)
                        setCookie("control", "1", EXPIRATION_TIME_IN_DAYS)
                        window.location.replace(PATH_TO_ROOT_OF_SITE);
                    }, function (error) {
                        if (error.code == error.PERMISSION_DENIED){
                            window.location.replace(PATH_TO_LOCATION_NOT_GRANTED);
                        }
                    });
                }
            })
    }
}

function getCookie(cname) {
    let name = cname + "=";
    let decodedCookie = decodeURIComponent(document.cookie);
    let ca = decodedCookie.split(';');
    for(let i = 0; i <ca.length; i++) {
        let c = ca[i];
        while (c.charAt(0) == ' ') {
            c = c.substring(1);
        }
        if (c.indexOf(name) == 0) {
            return c.substring(name.length, c.length);
        }
    }
    return "";
}

function setCookie(cName, cValue, expDays) {
    let date = new Date();
    date.setTime(date.getTime() + (expDays * 24 * 60 * 60 * 1000));
    const expires = "expires=" + date.toUTCString();
    document.cookie = cName + "=" + cValue + "; " + expires + "; path=/";
}

function refreshLocationData() {
    setCookie("control", 0, EXPIRATION_TIME_IN_DAYS)
    getLocation();
}

function clearData() {
    document.location.replace(PATH_TO_ROOT_OF_SITE);
}

