var express = require("express");
var cors = require("cors");
var geohash = require('ngeohash');
var http = require("http");
var https = require("https");
const path = require('path');
// const port = process.env.Port || 8081;

var app = express();
// solve the problem of cors
app.use(cors());



var SpotifyWebApi = require('spotify-web-api-node');
var spotifyApi = new SpotifyWebApi({
  clientId: '8d9a5b859b5e446cb0aec4f29e0ba2d9',
  clientSecret: '3097685a215d4e27aab8d66f2f08a2c3',
  // redirectUri: 'http://www.example.com/callback'
});
//GET request for spotify service
app.get("/spotifyApi", function(req, res) {
    let name = req.query.name;
    console.log(name);
    //Attemp to get an artist
    spotifyApi.searchArtists(name).then(
        function(data) {
            console.log('The access_token has not expired');
            console.log(`Search artists by ${name}`, data.body);
            // res.send(data.body);
            res.json(generateSelectedSpotifyObj(name,data.body));
        }, 
        function(err) {
            console.error(err);
            // If error happens, then it means the access_token may has expired
            // So need to get an access token and 'save' it using a setter
            spotifyApi.clientCredentialsGrant().then(
              function(data) {
                console.log('The access token is ' + data.body['access_token']);
                spotifyApi.setAccessToken(data.body['access_token']);
                //After create the new token, do search again!!!
                spotifyApi.searchArtists(name).then(
                    function(data) {
                        console.log(`Search artists by $love`, data.body);
                        // res.json(data.body);
                        res.json(generateSelectedSpotifyObj(data.body));
                    }, 
                    function(err) { 
                        console.log(err);
                        res.status(err.statusCode);
                        res.send(err.statusCode);
                        // res.json(err.statusCode);
                        // res.status(400);
                    }
                );
              },
              function(err) {
                console.log('Something went wrong!', err);
              }
            );
        }
    );
});


//GET request for artist team(s) service
app.get("/getArtistTeamsApi", function(req, res) {
    let query = req.query; 
    console.log('query', query);
    let apiKey = "AIzaSyDqd7pBkC1g7ysVENuqjkQDBYhGWJviu-M";
    let MY_SEARCH_ENGINE_ID = "017776930278243185753:jmmprwayg0q";
    let getArtistTeamsApiPrefix = "https://www.googleapis.com/customsearch/v1";
    let parameterArr = [
                        ['q', query.name],
                        ['cx', MY_SEARCH_ENGINE_ID],
                        ['imgSize','huge'],
                        ['imgType', 'news'],
                        ['num', 8],
                        ['searchType','image'],
                        ['key', apiKey]
                       ];
    console.log(appendParameter(getArtistTeamsApiPrefix, parameterArr));
    let options = createOptions("www.googleapis.com", 443, "/customsearch/v1", "GET", parameterArr);
    getJSON(options, function(statusCode, json) {

        console.log('GET request for artist team(s) service status: ', statusCode);
        res.statusCode = statusCode;
        // res.send(json);
        res.json(generateSelectedArtTeamObj(json));
    });
});


//GET request for event search service
app.get("/autoCompleteApi", function(req, res) {
    let query = req.query; 
    let apiKey = "jL9PDGSNDgl58VWor05YYs7LP8tMWrto";
    let autoCompleteApiPrefix = "https://app.ticketmaster.com/discovery/v2/suggest";
    let parameterArr = [
                        ['keyword', query.keyword],
                        ['apikey', apiKey]
                       ];
    console.log(appendParameter(autoCompleteApiPrefix, parameterArr));
    let options = createOptions("app.ticketmaster.com", 443, "/discovery/v2/suggest", "GET", parameterArr);
    getJSON(options, function(statusCode, json) {
        console.log('GET request for autoComplete service status: ', statusCode);
        res.statusCode = statusCode;
        // res.send(json);
        res.json(generateSelectedAutoCompleteAttractionArrObj(json));
    });
});

//GET request for event search service
app.get("/eventSearchApi", function(req, res) {
    let query = req.query;  

    //get geoHash 
    let lat = undefined;
    let lon = undefined; 
    console.log('query.locationIndex',query.locationIndex);
    // if (query.location === '') {
    if (query.locationIndex === '0') {
        lat = query.userLat;
        lon = query.userLon;

        let geoHash = getGeoHash(lat, lon);
        let segmentId = getSegmentId(query.category);
        let apiKey = "jL9PDGSNDgl58VWor05YYs7LP8tMWrto";
        let eventSearchApiPrefix = "https://app.ticketmaster.com/discovery/v2/events.json";
        let parameterArr = [
                       ['keyword', query.keyword],
                       ['segmentId', segmentId],
                       ['radius', query.distance],
                       ['unit', query.unit],
                       ['geoPoint', geoHash],
                       ['sort','date,asc'], //ascending order by date!!!!
                       ['apikey', apiKey]
                      ];

        //test api 
        console.log(appendParameter(eventSearchApiPrefix, parameterArr));
        let options = createOptions("app.ticketmaster.com", 443, "/discovery/v2/events.json", "GET", parameterArr);
        
        getJSON(options, function(statusCode, json) {
            // I could work with the result html/json here.  I could also just return it
            // console.log("onResult: (" + statusCode + ")" + JSON.stringify(result));
            // res.send(result);
            console.log('GET request for event search service status: ', statusCode);
            res.statusCode = statusCode;
            // res.send(json);
            res.send(generateSelectedEventListObj(json));
        });
    }
    else {
        let google_geocode_prefix = "https://maps.googleapis.com/maps/api/geocode/json";
        let google_geocode_apiKey = "AIzaSyDqd7pBkC1g7ysVENuqjkQDBYhGWJviu-M";
        let google_geocode_parameterArr = [
                            ["address",query.location],
                            ["key",google_geocode_apiKey]
                           ];
        let google_geocode_options = createOptions("maps.googleapis.com", 443, "/maps/api/geocode/json", "GET", google_geocode_parameterArr);
        console.log(appendParameter(google_geocode_prefix, google_geocode_parameterArr));

        let locationObject = undefined;

        getJSON(google_geocode_options, function(statusCode, json) {
            // I could work with the result html/json here.  I could also just return it
            // console.log("onResult: (" + statusCode + ")" + JSON.stringify(result));
            // res.send(result);
            console.log('GET request for google geocode service status: ', statusCode);
            locationObject = json;
            console.log('locationObject1', locationObject);
            lat = locationObject["results"][0]["geometry"]["location"]["lat"];
            lon = locationObject["results"][0]["geometry"]["location"]["lng"];

            let geoHash = getGeoHash(lat, lon);
            let segmentId = getSegmentId(query.category);
            let apiKey = "jL9PDGSNDgl58VWor05YYs7LP8tMWrto";
            let eventSearchApiPrefix = "https://app.ticketmaster.com/discovery/v2/events.json";
            let parameterArr = [
                           ['keyword', query.keyword],
                           ['segmentId', segmentId],
                           ['radius', query.distance],
                           ['unit', query.unit],
                           ['geoPoint', geoHash],
                           ['sort','date,asc'], //ascending order by date!!!!
                           ['apikey', apiKey]
                          ];

            //test api 
            console.log(appendParameter(eventSearchApiPrefix, parameterArr));
            let options = createOptions("app.ticketmaster.com", 443, "/discovery/v2/events.json", "GET", parameterArr);
            
            getJSON(options, function(statusCode, json) {
                // I could work with the result html/json here.  I could also just return it
                // console.log("onResult: (" + statusCode + ")" + JSON.stringify(result));
                // res.send(result);
                console.log('GET request for event search service status: ', statusCode);
                res.statusCode = statusCode;
                // res.send(json);
                res.send(generateSelectedEventListObj(json));
            });
        });

    }
});


//GET request for event detail service
app.get("/getEventDetailApi", function(req, res) {
    let query = req.query; 
    console.log('query', query);
    let apiKey = "jL9PDGSNDgl58VWor05YYs7LP8tMWrto";
    let getEventDetailApiPrefix = "https://app.ticketmaster.com/discovery/v2/events/" + query.eventId;
    let parameterArr = [
                        ['apikey', apiKey]
                       ];
    console.log(appendParameter(getEventDetailApiPrefix, parameterArr));
    let options = createOptions("app.ticketmaster.com", 443, "/discovery/v2/events/" + query.eventId, "GET", parameterArr);
    getJSON(options, function(statusCode, json) {

        console.log('GET request for event detail service status: ', statusCode);
        res.statusCode = statusCode;
        // res.send(json);
        res.send(generateSelectedEventDetailObj(query.eventId,json));
    });
});

//GET request for venue detail service
app.get("/getVenueDetailApi", function(req, res) { 
    let query = req.query; 
    console.log('query', query);
    let apiKey = "jL9PDGSNDgl58VWor05YYs7LP8tMWrto";
    let getVenueDetailApiPrefix = "https://app.ticketmaster.com/discovery/v2/venues";
    let parameterArr = [
                        ['keyword', query.keyword],
                        ['apikey', apiKey]
                       ];
    console.log(appendParameter(getVenueDetailApiPrefix, parameterArr));
    let options = createOptions("app.ticketmaster.com", 443, "/discovery/v2/venues", "GET", parameterArr);
    getJSON(options, function(statusCode, json) {

        console.log('GET request for venue detail service status: ', statusCode);
        res.statusCode = statusCode;
        // res.send(json);
        res.json(generateSelectedVenueObj(json));
    });
});


//GET request for songkick detail service
app.get("/getSongkickDetailApi", function(req, res) { 
    let query = req.query; 
    console.log('query', query);
    let apiKey = "rRKI5k01Ktzsx5ba";
    let songkickApiPrefix = "https://api.songkick.com/api/3.0/search/venues.json";
    let parameterArr = [
                        ['query', query.query],
                        ['apikey', apiKey]
                       ];
    console.log(appendParameter(songkickApiPrefix, parameterArr));
    let options = createOptions("api.songkick.com", 443, "/api/3.0/search/venues.json", "GET", parameterArr);
    getJSON(options, function(statusCode, json) {

        console.log('GET request for songkick detail service status: ', statusCode);
        // console.log(json["resultsPage"]["results"]["venue"][0]["id"]);

        try {
            let id = json["resultsPage"]["results"]["venue"][0]["id"];
            let apiKey = "rRKI5k01Ktzsx5ba";
            let songkickApiPrefix = "https://api.songkick.com/api/3.0/venues/" + id + "/calendar.json";
            let parameterArr = [
                                ['apikey', apiKey]
                               ];
            console.log(appendParameter(songkickApiPrefix, parameterArr));

            let options = createOptions("api.songkick.com", 443, "/api/3.0/venues/" + id + "/calendar.json" , "GET", parameterArr);
            getJSON(options, function(statusCode, json) { 
                res.statusCode = statusCode;
                // res.send(json);
                res.json(generateSelectedUpcomingEventsObj(json));
            });
        } catch (error) {
            let result = [];
            res.json(result);
            console.log('error[try to get the venue id]', error);
        }

    });
});

//callback ------ result(statusCode, json)
function getJSON(options, result)
{
    var port = options.port == 443 ? https : http;
    //Asyn
    var req = port.request(options, function(res)
    {
        var responseBody = '';
        console.log(options.hostname + ':' + res.statusCode);
        res.setEncoding('UTF-8');

        res.once('data', function (chunk) {
            // console.log(chunk);
        });

        res.on('data', function (chunk) {
            responseBody += chunk;
            console.log(`--chunk-- ${chunk.length}`);
        });

        res.on('end', function() {
            result(res.statusCode, JSON.parse(responseBody)); // return status and json back to result!!!!!!!!
        });
    });

    // console.log("req", req);
    req.on('error', function(err) {
        console.log(`error with request api 
            => ${options.port}://${options.hostname}${options.path}
            => ${err,message}
        `);
    });

    req.end(); // end is a must!!!
};


function appendParameter (prefix, parameterArr) {
    prefix += "?";
    for (let index in parameterArr) {
        //url encode even though front has already encodeURIComponent,
        //there is a automatic decode process from front => end
        prefix += parameterArr[index][0] + "=" + encodeURIComponent(parameterArr[index][1]) + "&";
    }
    prefix = prefix.substr(0, prefix.length - 1);
    return prefix;
}


function createOptions (hostNmae, portNumber, pathName, methodName, parameterArr) {
    pathName = appendParameter(pathName, parameterArr);
    let option = {
        hostname : hostNmae,
        port : portNumber,
        path: pathName,
        method: methodName,
        headers: {
            'Content-Type': 'application/json'
        }
    }
    return option;
}

/**
 * @param  {[string]} category name
 * @return {[string]} segmentId
 */
function getSegmentId(category) { 
    segmentObj = {
        "Default" : "", 
        "All" : "", 
        "Music" : "KZFzniwnSyZfZ7v7nJ", 
        "Sports" : "KZFzniwnSyZfZ7v7nE", 
        "Arts & Theatre" : "KZFzniwnSyZfZ7v7na",  
        "Film" : "KZFzniwnSyZfZ7v7nn", 
        "Miscellaneous" : "KZFzniwnSyZfZ7v7n1"
    }
    return segmentObj[category];
}


/**
 * @param  {[string]}
 * @param  {[string]}
 * @return {[string]} geohash
 */
function getGeoHash(lat, lon) {
  return geohash.encode(lat, lon);
}

/**
 * @param  {[string]} url without key appended
 * @param  {[number]} 1 => Google Maps Geocoding API 
 *                    2 =>
 * @return {[string]} url with key appended 
 */
function appendApiKey(url, mode) {
	// let googleGeocodePrefix = "https://maps.googleapis.com/maps/api/geocode/json?";
  if (mode == 1) {
    let googleGeocodeApiKey = "AIzaSyDqd7pBkC1g7ysVENuqjkQDBYhGWJviu-M";
    url += "key" + "=" + googleGeocodeApiKey;
  }

  return url;
}


//if location_arr exist, return the value, else return undefined
/**
 * [tryGetObj description]
 * @param {string} location_string [eval location in json object]
 * @param {object} jsonObj      [json]
 * @param {number} index        [index involved]
 */
function tryGetObj (location_string, jsonObj, index) {
    let value = undefined;
    try {
        value = eval(location_string);
        if ((typeof value) == "undefined") {
            value = undefined;
        }
        return value;
    } catch (error) {
        // console.log(error);
        return undefined;
    }
}


/**
* [generateSelectedAutoCompleteAttractionArrObj create refined autocomplete attraction array]
* @param {object} jsonObj [json from autocomplete api]
*/
function generateSelectedAutoCompleteAttractionArrObj(jsonObj) {
    let result = [];
    let attractionArr = tryGetObj ('jsonObj["_embedded"]["attractions"]',jsonObj, -1);
    // console.log('attractionArr', attractionArr);
    if (attractionArr == undefined) {
        return result;
    }
    // for (let index in attractionArr) {
    for (let index = 0, size = attractionArr.length; index < size; index++) {
        // console.log(1);
        let attractionName = tryGetObj ('jsonObj["_embedded"]["attractions"][index]["name"]',jsonObj, index);
        if (attractionName != undefined) {
            result.push(attractionName);
        }
    }
    console.log(result);
    return result;
}


    //generate html of Event table list
function generateSelectedEventListObj(jsonObj) {
    let result = [];
    // if (jsonObj.page.totalElements == 0 || typeof jsonObj._embedded.events == undefined || jsonObj._embedded.events.length == 0) {
    //  return result;
    // }
    let eventArr = tryGetObj('jsonObj._embedded.events', jsonObj, -1);
    if (eventArr == undefined || eventArr.length == 0) {
        return result;
    }
    for (let index = 0, size = eventArr.length; index < size; index++) {
    // for (let index in jsonObj._embedded.events) { 
        let eventObj = jsonObj._embedded.events[index];
        if (eventObj == undefined) {
            continue;
        }
        let name = tryGetObj ('jsonObj["name"]',jsonObj, index);
        let date = tryGetObj ('jsonObj["_embedded"]["events"][index]["dates"]["start"]["localDate"]',jsonObj, index);
        let localTime = tryGetObj ('jsonObj["_embedded"]["events"][index]["dates"]["start"]["localTime"]',jsonObj, index);
        let eventId = tryGetObj ('jsonObj["_embedded"]["events"][index]["id"]',jsonObj, index);
        let eventName = tryGetObj ('jsonObj["_embedded"]["events"][index]["name"]',jsonObj, index);
        let genre = tryGetObj ('jsonObj["_embedded"]["events"][index]["classifications"][0]["genre"]["name"]',jsonObj, index);
        let segment = tryGetObj ('jsonObj["_embedded"]["events"][index]["classifications"][0]["segment"]["name"]',jsonObj, index);
        let venue = tryGetObj ('jsonObj["_embedded"]["events"][index]["_embedded"]["venues"][0]["name"]', jsonObj, index);
        let attractionsArr = tryGetObj ('jsonObj["_embedded"]["events"][index]["_embedded"]["attractions"]', jsonObj, index);
        let buyTicketAt = tryGetObj ('jsonObj["_embedded"]["events"][index]["url"]', jsonObj, index);
        let artistArr = [];
        try {
            for (let i = 0; i < attractionsArr.length; i++) {
                artistArr.push(attractionsArr[i]["name"]);
            }
        } catch (error) {
            artistArr = [];
        }

        if (segment && segment.toLowerCase() == 'undefined') {
        segment = undefined;
        }
        if (genre && genre.toLowerCase() == 'undefined') {
        genre = undefined;
        }
  
  //result.push()?????????
        result[index] = {
            'date' : date,
            'eventId' : eventId,
            'eventName' : eventName,
            'genre' : genre,
            'segment': segment,
            'venue' : venue,
            'artistArr' : artistArr,
            'buyTicketAt' : buyTicketAt,
            'localTime' : localTime,
        }
    }
    return result;
}

function generateSelectedEventDetailObj(eventId, jsonObj) {
    console.log('jsonObj',jsonObj);

    let eventNmae = tryGetObj ('jsonObj["name"]', jsonObj, -1);
    //1 Artist and team
    let artistTeamObj = tryGetObj ('jsonObj["_embedded"]["attractions"]', jsonObj, -1);
    //2 Venue
    let venueObj = tryGetObj ('jsonObj["_embedded"]["venues"][0]["name"]', jsonObj, -1);
    //3 Time
    // let startObj = this.tryGetObj ('jsonObj["dates"]["start"]', jsonObj, -1);
    let localDataObj = tryGetObj ('jsonObj["dates"]["start"]["localDate"]', jsonObj, -1);
    let localTimeObj = tryGetObj ('jsonObj["dates"]["start"]["localTime"]', jsonObj, -1);
    //4 Category
    let segmentObj = tryGetObj ('jsonObj["classifications"][0]["segment"]["name"]', jsonObj, -1);
//=====================================================After getting artistTeamObj => immediately get api
    // getArtTeamObj(artistTeamObj, segmentObj);
    // getUpcomingEvents(venueObj);
    // getVenue(venueObj);
    let genreObj = tryGetObj ('jsonObj["classifications"][0]["genre"]["name"]', jsonObj, -1);

    if (segmentObj && segmentObj.toLowerCase() == 'undefined') {
        segmentObj = undefined;
    }
    if (genreObj && genreObj.toLowerCase() == 'undefined') {
        genreObj = undefined;
    }
    //5 price Range
    let priceCurrencyObj = tryGetObj ('jsonObj["priceRanges"][0]["currency"]', jsonObj, -1);
    // let priceCurrencyObj = "$"; //no need do this anymore ===> use currency pipe!!!!!
    let priceMinObj = tryGetObj ('jsonObj["priceRanges"][0]["min"]', jsonObj, -1);
    let priceMaxObj = tryGetObj ('jsonObj["priceRanges"][0]["max"]', jsonObj, -1);
    // ticket status
    let statusObj = tryGetObj ('jsonObj["dates"]["status"]["code"]', jsonObj, -1);
    if (statusObj != undefined) {
        statusObj = statusObj.charAt(0).toUpperCase() + statusObj.slice(1);
    }  
    // buyTicketAtObject
    let buyTicketAtObject = tryGetObj ('jsonObj["url"]', jsonObj, -1);
    // seatMapObject
    let seatMapObject = tryGetObj ('jsonObj["seatmap"]["staticUrl"]', jsonObj, -1);
    // let result = [artistTeamObj, venueObj, startObj, classObj, priceObj, priceObj, statusObj, buyTicketAtObject, seatMapObject];
    if (eventNmae == undefined && artistTeamObj == undefined && venueObj == undefined && localDataObj == undefined && 
      localTimeObj == undefined && segmentObj == undefined && genreObj == undefined && priceMinObj == undefined && 
      priceMaxObj == undefined && statusObj == undefined && buyTicketAtObject == undefined && seatMapObject == undefined) {
      return [];
    }

    let result = {
        'eventId' : eventId,
        'eventName' : eventNmae,
        'artistTeam': artistTeamObj,
        'venue': venueObj,
        'localData': localDataObj, 
        'localTime': localTimeObj,
        'segment': segmentObj, 
        'genre' : genreObj,
        'priceCurrency': priceCurrencyObj, 
        'priceMin': priceMinObj, 
        'priceMax': priceMaxObj,
        'status': statusObj,
        'buyTicketAt': buyTicketAtObject,
        'seatMap': seatMapObject,
      }
      return [result];
  }



function generateSelectedArtTeamObj (jsonObj) {
    let urlArr = [];

    let nameTarget = tryGetObj('jsonObj["queries"]["request"][0]["searchTerms"]', jsonObj, -1);
    // let nameTarget = name;

    let itemsArr = tryGetObj('jsonObj["items"]', jsonObj, -1);
    if (itemsArr == undefined) {
        // this.artTeamObj[index]['picture']['url'] = urlArr;
        return [{
            'name' : nameTarget,
            'url' : [],
        }];
    }
    for (let i in itemsArr) {
        try {
            urlArr.push(itemsArr[i]["link"]);
        } catch (error) {
            console.log('push error');
        }
    }
    return [{
        'name' : nameTarget,
        'url' : urlArr,
    }];
}

function generateSelectedSpotifyObj (name, jsonObj) {
    let result = {};

    let itemArr = tryGetObj("jsonObj['artists']['items']", jsonObj, -1);
    let nameTarget = name;

    if (itemArr == undefined) {
        return undefined;
    }

    for (let index = 0, length = itemArr.length; index < length; index++) {
        if (itemArr[index]['name'].toLowerCase() == nameTarget.toLowerCase()) {
  
            console.log('name1', itemArr[index]['name'].toLowerCase());
            console.log('name2', nameTarget);
            let name = tryGetObj("jsonObj['artists']['items'][index]['name']", jsonObj, index);
            let followers = tryGetObj("jsonObj['artists']['items'][index]['followers']['total']", jsonObj, index);
            let popularity = tryGetObj("jsonObj['artists']['items'][index]['popularity']", jsonObj, index);
            let checkAt = tryGetObj("jsonObj['artists']['items'][index]['external_urls']['spotify']", jsonObj, index);
            return [{
                'name' : name,
                'followers' : followers,
                'popularity' : popularity,
                'checkAt' : checkAt,
            }]
        }
    }
    return undefined;
}


function generateSelectedUpcomingEventsObj (jsonObj) {
    let eventsArr = tryGetObj("jsonObj['resultsPage']['results']['event']", jsonObj, -1);
    if (eventsArr == undefined) {
        return [];
    }
    console.log('eventsArr', eventsArr);
    let result = [];
    for (let index = 0, length = eventsArr.length; index < length; index++) {
        if (index == 5) {
            break;
        }
        let title = tryGetObj("jsonObj['resultsPage']['results']['event'][index]['displayName']", jsonObj, index);
        let url = tryGetObj("jsonObj['resultsPage']['results']['event'][index]['uri']", jsonObj, index);
        let artist = tryGetObj("jsonObj['resultsPage']['results']['event'][index]['performance'][0]['displayName']", jsonObj, index);
        let date = tryGetObj("jsonObj['resultsPage']['results']['event'][index]['start']['date']", jsonObj, index);
        let time = tryGetObj("jsonObj['resultsPage']['results']['event'][index]['start']['time']", jsonObj, index);
        let type = tryGetObj("jsonObj['resultsPage']['results']['event'][index]['type']", jsonObj, index);
        let eachEventObj = {
            'title' : title,
            'url' : url,
            'artist' : artist,
            'date' : date,
            'time' : time,
            'type' : type,
        };
      if (title == undefined && url == undefined && artist == undefined && date == undefined && time == undefined && type == undefined) {
        continue;
      }
      result.push(eachEventObj);
    }
    return result;
}


function generateSelectedVenueObj (jsonObj) {
    let state = tryGetObj("jsonObj['_embedded']['venues'][0]['state']['name']", jsonObj, -1);
    let stateCode = tryGetObj("jsonObj['_embedded']['venues'][0]['state']['stateCode']", jsonObj, -1);
    let postalCode = tryGetObj("jsonObj['_embedded']['venues'][0]['postalCode']", jsonObj, -1);
    let name = tryGetObj("jsonObj['_embedded']['venues'][0]['name']", jsonObj, -1);
    let address = tryGetObj("jsonObj['_embedded']['venues'][0]['address']['line1']", jsonObj, -1);
    let city = tryGetObj("jsonObj['_embedded']['venues'][0]['city']['name']", jsonObj, -1);
    let phoneNumber = tryGetObj("jsonObj['_embedded']['venues'][0]['boxOfficeInfo']['phoneNumberDetail']", jsonObj, -1);
    let openHours = tryGetObj("jsonObj['_embedded']['venues'][0]['boxOfficeInfo']['openHoursDetail']", jsonObj, -1);
    let generalRule = tryGetObj("jsonObj['_embedded']['venues'][0]['generalInfo']['generalRule']", jsonObj, -1);
    let childRule = tryGetObj("jsonObj['_embedded']['venues'][0]['generalInfo']['childRule']", jsonObj, -1);
    let location = tryGetObj("jsonObj['_embedded']['venues'][0]['location']", jsonObj, -1);
    let lon = tryGetObj("jsonObj['_embedded']['venues'][0]['location']['longitude']", jsonObj, -1);
    let lat = tryGetObj("jsonObj['_embedded']['venues'][0]['location']['latitude']", jsonObj, -1);
    let result = [];
    // if (address == undefined && city == undefined && phoneNumber == undefined && openHours == undefined && generalRule == undefined && childRule == undefined && (lon == undefined || lat == undefined)) {
    //   return result;
    // }
    result.push({
      'state' : state,
      'stateCode' : stateCode,
      'postalCode' : postalCode,
      'name' : name,
      'address' : address,
      'city' : city,
      'phoneNumber' : phoneNumber,
      'openHours' : openHours,
      'generalRule' : generalRule,
      'childRule' : childRule,
      // 'location' : location,
      'lon' : lon,
      'lat' : lat,
    });
    return result;
  }

// app.use(express.static("./hw8"));
// app.use(express.static("./hw8"));
// app.get('/',(req,res)=> {
//     res.sendFile("./hw8/index.html", {root:__dirname});
// });

// app.get('/',(req,res)=> {
//     res.sendFile("./public/hw8/src/index.html", {root:__dirname});
// });

// app.use(express.static(_dirname))

// app.use(express.static("./public/hw8/src"));
app.listen(8081);
console.log("Listening on port 8081");