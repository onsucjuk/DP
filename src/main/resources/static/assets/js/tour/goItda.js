$(document).ready(function () {

    $("#btnViewNow").on("click", function () {

      sessionStorage.setItem("SS_ROUTE_GD", "");
      location.reload()

    })

    $("#btnTourStart").on("click", function () {

        if(confirm("여행을 시작하시겠습니까?")) {

            let startMn = "Y"

            $.ajax({
                url: "/tour/setSessionMn",
                type: "POST",
                datatype: "JSON",
                data: {
                    "startMn" : startMn
                },
                success: function (json) {

                    if (json.result === 1) {
                        alert(json.msg);
                        location.href = "/user/login";

                    } else {

                        location.reload();
                        viewRoute()
                    }

                }
            })


        }
    })

    $("#btnTourStop").on("click", function () {

        if(confirm("여행을 취소하시겠습니까?")) {

            let startMn = "N"

            $.ajax({
                url: "/tour/setSessionMn",
                type: "POST",
                datatype: "JSON",
                data: {
                    "startMn" : startMn
                },
                success: function (json) {

                    if (json.result === 1) {
                        alert(json.msg);
                        location.href = "/user/login";

                    } else {

                        location.reload();
                    }

                }
            })


        }
    })

    $("#btnViewRoute").on("click", function () {

        viewRoute()

    })

    if(sessionStorage.getItem("SS_ROUTE_GD")==="Y"){
        viewRoute()
    }

    if(startTime==='Y'){

        doRotate();
        doCong();
    }
})

if (dSeq) {

} else {
    alert("비정상적인 접근입니다.")
    location.href="/index/index"
}

function viewRoute() {


    console.log("viewRoute 시작!")

    // 2. 시작, 도착 심볼찍기

    var markerList = [];
    var pointArray = [];

    let nowLat = $("#nowLat").text();
    let nowLon = $("#nowLon").text();
    let locIndex = 0;

    console.log("현재 위도 : " + nowLat)
    console.log("현재 경도 : " + nowLon)
    console.log("길안내 순서 : " + locIndex)

    // 시작
    addMarker("llStart",nowLon,nowLat,locIndex, locIndex++);

    // 시작지점 분류 하기

    let totalTimesMap = new Map();

        $('[id^="plc_"]').each(function () {
        // 현재 요소의 id 값을 가져옴 (예: plc_123)
        let id = $(this).attr('id');

        // id에서 plc_를 제외한 숫자 부분만 추출 (예: 123)
        let index = id.replace('plc_', '');

        let lathidden = $("#lathidden" + index).text();
        let lonhidden = $("#lonhidden" + index).text();
        let tourYn = $("#tourYn" + index).text();

            console.info("plc_" + index + " lathidden : " + lathidden);
            console.info("plc_" + index + " lonhidden : " + lonhidden);
            console.info("plc_" + index + " tourYn : " + tourYn);

            let data = {

                lathidden: lathidden,
                lonhidden: lonhidden,
                tourYn: tourYn

            };

            totalTimesMap.set(index, data)

    })

    let sortedTotalTimesArray = Array.from(totalTimesMap.values()).sort((a, b) => a.index - b.index);
    let passL = "";
    let eLat ="";
    let eLon ="";

    sortedTotalTimesArray.forEach((data, idx) => {

        let lathidden = data.lathidden;
        let lonhidden = data.lonhidden;
        let tourYn = data.tourYn;

        if (idx < sortedTotalTimesArray.length - 1) {

            if (passList !== "" && idx > 0) {
                passL += "_";
            }

            // lathidden와 lonhidden을 이어붙이기
            passL += `${lonhidden},${lathidden}`;

            console.log("경유지 위도 : " + lathidden)
            console.log("경유지 경도 : " + lonhidden)
            console.log("길안내 순서 : " + locIndex)

            if (tourYn==="Pending") {
                // 중간 값일 때 경유지
                addMarker("llPass", lonhidden, lathidden, locIndex, locIndex++);
            }
            else {
                addMarker("cpPass", lonhidden, lathidden, locIndex, locIndex++);
            }

        } else {

            console.log("도착지 위도 : " + lonhidden)
            console.log("도착지 경도 : " + lathidden)
            console.log("길안내 순서 : " + locIndex)

            if (tourYn==="Pending") {
                // 마지막 값일 때 도착
                addMarker("llEnd", lonhidden, lathidden, locIndex, locIndex++);
            }
            else {
                addMarker("cpPass", lonhidden, lathidden, locIndex, locIndex++);
            }

            eLat += lathidden;
            eLon += lonhidden;

        }

        sessionStorage.setItem("SS_ROUTE_GD", "Y");

    });

    console.log("경유지 정보 : " + passL)

    // 4. 경로탐색 API 사용요청
    var startX = nowLon;
    var startY = nowLat;
    var endX = eLon;
    var endY = eLat;
    var passList = passL;
    var prtcl;
    var headers = {};
    headers["appKey"]="B06BWPIrhC5CJXueAhPi7hG3V6avEXn8edbvjar9";
    $.ajax({
        method:"POST",
        headers : headers,
        url:"https://apis.openapi.sk.com/tmap/routes?version=1&format=json",//
        async:false,
        data:{
            startX : startX,
            startY : startY,
            endX : endX,
            endY : endY,
            passList : passList,
            reqCoordType : "WGS84GEO",
            resCoordType : "WGS84GEO",
            angle : "172",
            searchOption : "0",
            trafficInfo : "Y"
        },
        success:function(response){
            console.log(response)
            prtcl = response;

            // 5. 경로탐색 결과 Line 그리기
            var trafficColors = {
                extractStyles:true,
                /* 실제 교통정보가 표출되면 아래와 같은 Color로 Line이 생성됩니다. */
                trafficDefaultColor:"#636f63", //Default
                trafficType1Color:"#19b95f", //원할
                trafficType2Color:"#f15426", //지체
                trafficType3Color:"#ff970e"  //정체
            };
            var style_red = {
                fillColor:"#FF0000",
                fillOpacity:0.2,
                strokeColor: "#FF0000",
                strokeWidth: 3,
                strokeDashstyle: "solid",
                pointRadius: 2,
                title: "this is a red line"
            };
            drawData(prtcl);


            // 6. 경로탐색 결과 반경만큼 지도 레벨 조정
            var newData = geoData[0];
            PTbounds = new Tmapv2.LatLngBounds();
            for (var i = 0; i < newData.length; i++) {
                var mData = newData[i];
                var type = mData.geometry.type;
                var pointType = mData.properties.pointType;
                if(type == "Point"){
                    var linePt = new Tmapv2.LatLng(mData.geometry.coordinates[1],mData.geometry.coordinates[0]);
                    console.log(linePt);
                    PTbounds.extend(linePt);
                }
                else{
                    var startPt,endPt;
                    for (var j = 0; j < mData.geometry.coordinates.length; j++) {
                        var linePt = new Tmapv2.LatLng(mData.geometry.coordinates[j][1],mData.geometry.coordinates[j][0]);
                        PTbounds.extend(linePt);
                    }
                }
            }
            map.fitBounds(PTbounds);

        },
        error:function(request,status,error){
            console.log("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
        }
    });

    function drawData(data) {
        // 지도위에 선은 다 지우기
        routeData = data;
        var resultStr = "";
        var distance = 0;
        var idx = 1;
        var newData = [];
        var equalData = [];
        var pointId1 = "-1234567";
        var ar_line = [];

        for (var i = 0; i < data.features.length; i++) {
            var feature = data.features[i];
            //배열에 경로 좌표 저잘
            if (feature.geometry.type == "LineString") {
                ar_line = [];
                for (var j = 0; j < feature.geometry.coordinates.length; j++) {
                    var startPt = new Tmapv2.LatLng(feature.geometry.coordinates[j][1], feature.geometry.coordinates[j][0]);
                    ar_line.push(startPt);
                    pointArray.push(feature.geometry.coordinates[j]);
                }
                var polyline = new Tmapv2.Polyline({
                    path: ar_line,
                    strokeColor: "#ff0000",
                    strokeWeight: 6,
                    map: map
                });
                new_polyLine.push(polyline);
            }
            var pointId2 = feature.properties.viaPointId;
            if (pointId1 != pointId2) {
                equalData = [];
                equalData.push(feature);
                newData.push(equalData);
                pointId1 = pointId2;
            } else {
                equalData.push(feature);
            }
        }
        geoData = newData;
        var markerCnt = 1;
        for (var i = 0; i < newData.length; i++) {
            var mData = newData[i];
            var type = mData[0].geometry.type;
            var pointType = mData[0].properties.pointType;
            var pointTypeCheck = false; // 경유지 일때만 true
            if (mData[0].properties.pointType == "S") {
                var img = 'http://tmapapi.sktelecom.com/upload/tmap/marker/pin_r_m_s.png';
                var lon = mData[0].geometry.coordinates[0];
                var lat = mData[0].geometry.coordinates[1];
            } else if (mData[0].properties.pointType == "E") {
                var img = 'http://tmapapi.sktelecom.com/upload/tmap/marker/pin_r_m_e.png';
                var lon = mData[0].geometry.coordinates[0];
                var lat = mData[0].geometry.coordinates[1];
            } else {
                markerCnt = i;
                var lon = mData[0].geometry.coordinates[0];
                var lat = mData[0].geometry.coordinates[1];
            }
        }
    }

    function addMarker(status, lon, lat, index, tag) {
        //출도착경유구분
        //이미지 파일 변경.
        var markerLayer;
        switch (status) {
            case "llStart":
                imgURL = 'http://tmapapi.sktelecom.com/upload/tmap/marker/pin_r_m_s.png';
                break;
            case "llPass":
                imgURL = `http://tmapapi.sktelecom.com/upload/tmap/marker/pin_b_m_${index}.png`;
                break;
            case "cpPass":
                imgURL = `http://tmapapi.sktelecom.com/upload/tmap/marker/pin_g_m_${index}.png`;
                break;
            case "llEnd":
                imgURL = `http://tmapapi.sktelecom.com/upload/tmap/marker/pin_r_m_${index}.png`;
                break;
            case "cpEnd":
                imgURL = `http://tmapapi.sktelecom.com/upload/tmap/marker/pin_g_m_${index}.png`;
                break;
            default:
        };
        var marker = new Tmapv2.Marker({
            position: new Tmapv2.LatLng(lat,lon),
            icon: imgURL,
            map: map
        });
        // 마커 드래그 설정
        marker.tag = tag;
        marker.addListener("dragend", function (evt) {
            markerListenerEvent(evt);
        });
        marker.addListener("drag", function (evt) {
            markerObject = markerList[tag];
        });
        markerList[tag] = marker;
        return marker;
    }

}

function doDetail(seq) {

    let startMn = "N"

    $.ajax({
        url: "/tour/setSessionMn",
        type: "POST",
        datatype: "JSON",
        data: {
            "startMn" : startMn
        },
        success: function (json) {

            if (json.result === 1) {
                alert(json.msg);
                location.href = "/user/login";

            } else {

                location.href = "/tour/goItda?dSeq=" + seq;
            }

        }
    })
}

function doRotate() {

    let nowLat = $("#nowLat").text();
    let nowLon = $("#nowLon").text();


    $('[id^="plc_"]').each(function () {
        // 현재 요소의 id 값을 가져옴 (예: plc_123)
        let id = $(this).attr('id');

        // id에서 plc_를 제외한 숫자 부분만 추출 (예: 123)
        let index = id.replace('plc_', '');

        let lathidden = $("#lathidden" + index).text();
        let lonhidden = $("#lonhidden" + index).text();
        let tourYn = $("#tourYn" + index).text();

        console.log(nowLon, nowLat, lathidden, lonhidden, index)
        console.log("tourYn = " + tourYn)
        /*let poihidden = $(this).find('[th\\:name="poihidden"]').text();*/

        if (tourYn === "Pending") {

            $.ajax({
                url: "/route/getRouteInfo",
                type: "POST",
                datatype: "JSON",
                data: {
                    "sLat": nowLat,
                    "sLon": nowLon,
                    "eLat": lathidden,
                    "eLon": lonhidden,
                    "index": index
                },
                success: function (json) {

                    if (json.isEmpty) {
                        alert("JSON 값을 받아오지 못했습니다.");

                    } else {

                        let totalDistance = json.totalDistance;
                        let totalTime = json.totalTime;
                        let index = json.index;

                        console.log(totalDistance, totalTime, index)

                        $(`#totalDistance${index}`).text(parseInt(totalDistance / 1000) + 'km');
                        $(`#totalTime${index}`).text('약' + parseInt(totalTime / 60) + '분 소요 예정');

                    }
                }
            })
        } else {
            $(`#totalDistance${index}`).text('방문 완료했습니다.');
            $(`#totalTime${index}`).text('');
        }
    })
};


function drawData(data) {
    // 지도위에 선은 다 지우기
    routeData = data;
    var resultStr = "";
    var distance = 0;
    var idx = 1;
    var newData = [];
    var equalData = [];
    var pointId1 = "-1234567";
    var ar_line = [];

    for (var i = 0; i < data.features.length; i++) {
        var feature = data.features[i];
        //배열에 경로 좌표 저잘
        if (feature.geometry.type == "LineString") {
            ar_line = [];
            for (var j = 0; j < feature.geometry.coordinates.length; j++) {
                var startPt = new Tmapv2.LatLng(feature.geometry.coordinates[j][1], feature.geometry.coordinates[j][0]);
                ar_line.push(startPt);
                pointArray.push(feature.geometry.coordinates[j]);
            }
            var polyline = new Tmapv2.Polyline({
                path: ar_line,
                strokeColor: "#ff0000",
                strokeWeight: 6,
                map: map
            });
            new_polyLine.push(polyline);
        }
        var pointId2 = feature.properties.viaPointId;
        if (pointId1 != pointId2) {
            equalData = [];
            equalData.push(feature);
            newData.push(equalData);
            pointId1 = pointId2;
        } else {
            equalData.push(feature);
        }
    }
    geoData = newData;
    var markerCnt = 1;
    for (var i = 0; i < newData.length; i++) {
        var mData = newData[i];
        var type = mData[0].geometry.type;
        var pointType = mData[0].properties.pointType;
        var pointTypeCheck = false; // 경유지 일때만 true
        if (mData[0].properties.pointType == "S") {
            var img = 'http://tmapapi.sktelecom.com/upload/tmap/marker/pin_r_m_s.png';
            var lon = mData[0].geometry.coordinates[0];
            var lat = mData[0].geometry.coordinates[1];
        } else if (mData[0].properties.pointType == "E") {
            var img = 'http://tmapapi.sktelecom.com/upload/tmap/marker/pin_r_m_e.png';
            var lon = mData[0].geometry.coordinates[0];
            var lat = mData[0].geometry.coordinates[1];
        } else {
            markerCnt = i;
            var lon = mData[0].geometry.coordinates[0];
            var lat = mData[0].geometry.coordinates[1];
        }
    }
}

function doEdit(seq) {

    console.log("doEdit start! seq : " + seq)

    let placeNick = encodeURIComponent($("#placeNick" + seq).text());
    let placeName = encodeURIComponent($("#placename" + seq).text());
    let placeAddr = encodeURIComponent($("#addrhidden" + seq).text());
    let lat = encodeURIComponent($("#lathidden" + seq).text());
    let lon = encodeURIComponent($("#lonhidden" + seq).text());
    let memo = encodeURIComponent($("#memohidden" + seq).text());
    let poi = encodeURIComponent($("#poihidden" + seq).text());

    console.log("placeNick : " + placeNick)
    console.log("placeName : " + placeName)
    console.log("placeAddr : " + placeAddr)
    console.log("lat : " + lat)
    console.log("lon : " + lon)
    console.log("memo : " + memo)
    console.log("poi : " + poi)

    // 새 창에서 tourPlaceEditForm 열기
    window.open("/tour/tourPlaceEditForm?placeSeq=" + seq + "&placeNick=" + placeNick + "&placeName=" + placeName + "&placeAddr=" + placeAddr + "&lat=" + lat + "&lon=" + lon + "&memo=" + memo + "&poi=" + poi, "_blank");

}

function doDelete(seq) {

    if (seq === "") {
        alert("장소 번호에 문제가 있습니다. 정보를 확인해주세요.");
    }

    else if(confirm("여행 목적지를 삭제하시겠습니까?")) {

        // Ajax 호출해서 회원가입하기
        $.ajax({
            url: "/tour/deleteTourPlaceOne",
            type: "post",
            datatype: "JSON",
            data: { "pSeq" : seq },
            success: function (json) {

                if (json.result === 1) {
                    alert(json.msg);
                    location.href = "/user/login";

                } else {
                    alert(json.msg);
                    location.reload();
                }
            }
        })
    }
}

function updateEndTime(seq) {

    console.log("updateEndTime start! seq : " + seq)

    let placeSeq = seq;
    let placeNick = $("#placeNick" + seq).text();
    let placeName = $("#placename" + seq).text();
    let tourYn = $("#tourYn" + seq).text();

    console.log("placeNick : " + placeNick)
    console.log("placeName : " + placeName)
    console.log("tourYn : " + tourYn)

    if(tourYn==="Pending") {

        if (confirm(placeName + "(" + placeNick + ")" + "목적지 방문을 완료하시겠습니까?")) {

            // Ajax 호출해서 회원가입하기
            $.ajax({
                url: "/tour/updatePlaceEnd",
                type: "post",
                datatype: "JSON",
                data: {
                    "placeSeq": placeSeq,
                    "placeNick": placeNick,
                    "placeName": placeName
                },
                success: function (json) {

                    if (json.result === 1) {
                        alert(json.msg);
                        location.href = "/user/login";

                    } else {
                        alert(json.msg);

                        /*$("#tourTd" + seq).load(Location.href + " #tourTd" + seq);
                        $("#tourYn" + seq).load(Location.href + " #tourYn" + seq);*/

                        location.reload();

                    }
                }
            })
        }
    } else if (tourYn==="Completed") {

            alert("이미 방문을 완료항 상태입니다.")

    } else {

        alert("알 수 없는 문제가 발생했습니다. 확인해주세요.")

    }
}

function resetEndTime(seq) {

    console.log("resetEndTime start! seq : " + seq)

    let placeSeq = seq;
    let placeNick = $("#placeNick" + seq).text();
    let placeName = $("#placename" + seq).text();
    let tourYn = $("#tourYn" + seq).text();

    console.log("placeNick : " + placeNick)
    console.log("placeName : " + placeName)
    console.log("tourYn : " + tourYn)

    if(tourYn==="Completed") {

            if (confirm(placeName + "(" + placeNick + ")" + "목적지 방문 완료를 취소하시겠습니까?")) {
                // Ajax 호출해서 회원가입하기

                $.ajax({
                    url: "/tour/resetPlaceEnd",
                    type: "post",
                    datatype: "JSON",
                    data: {
                        "placeSeq": placeSeq,
                        "placeNick": placeNick,
                        "placeName": placeName
                    },
                    success: function (json) {

                        if (json.result === 1) {
                            alert(json.msg);
                            location.href = "/user/login";

                        } else {

                            /*$("#tourTd" + seq).load(Location.href + " #tourTd" + seq);
                            $("#tourYn" + seq).load(Location.href + " #tourYn" + seq);*/

                            location.reload();

                        }
                    }
                })
            }

    } else if (tourYn==="Pending") {

        alert("아직 방문하지 않은 상태입니다.")

    } else {

        alert("알 수 없는 문제가 발생했습니다. 확인해주세요.")

    }

}

function doCong() {

        $('[id^="plc_"]').each(function () {
            // 현재 요소의 id 값을 가져옴 (예: plc_123)
            let id = $(this).attr('id');

            // id에서 plc_를 제외한 숫자 부분만 추출 (예: 123)
            let index = id.replace('plc_', '');

            let lathidden = $("#lathidden" + index).text();
            let lonhidden = $("#lonhidden" + index).text();
            let poihidden = $("#poihidden" + index).text();
            let tourYn = $("#tourYn" + index).text();

            console.log(lathidden, lonhidden, poihidden, index)
            console.log("tourYn : " + tourYn)
            /*let poihidden = $(this).find('[th\\:name="poihidden"]').text();*/

            if (tourYn==="Pending") {

                $.ajax({

                    url: "/cong/getCongestion",
                    type: "GET",
                    datatype: "JSON",
                    data: {
                        "poi": poihidden,
                        "lat": lathidden,
                        "lon": lonhidden,
                        "index": index
                    },
                    success: function (json) {

                        if (json.isEmpty) {
                            alert("JSON 값을 받아오지 못했습니다.");

                        } else {

                            let congestion = json.congestion;
                            let congestionLevel = json.congestionLevel;
                            let type = json.type;
                            let index = json.index;

                            console.log(congestion, congestionLevel, type, index)

                            if (congestionLevel === 1) {

                                $(`#congestion${index}`).attr("class", "font-weight-bold badge badge-success");
                                $(`#congestion${index}`).text("여유");

                            } else if (congestionLevel === 2) {

                                $(`#congestion${index}`).attr("class", "font-weight-bold badge badge-warning");
                                $(`#congestion${index}`).text("보통");

                            } else if (congestionLevel === 3) {

                                $(`#congestion${index}`).attr("class", "font-weight-bold badge badge-realWarning");
                                $(`#congestion${index}`).text("약간 붐빔");

                            } else if (congestionLevel === 4) {

                                $(`#congestion${index}`).attr("class", "font-weight-bold badge badge-danger");
                                $(`#congestion${index}`).text("붐빔");

                            } else {
                                $(`#congestion${index}`).text("혼잡도 정보를 받아 올 수 없습니다.");
                            }


                            $(`#conghidden${index}`).text(congestion);

                        }
                    }
                })
            }
        })

    function addMarker(status, lon, lat, tag) {
        //출도착경유구분
        //이미지 파일 변경.
        var markerLayer;
        switch (status) {
            case "llStart":
                imgURL = 'http://tmapapi.sktelecom.com/upload/tmap/marker/pin_r_m_s.png';
                break;
            case "llPass":
                imgURL = 'http://tmapapi.sktelecom.com/upload/tmap/marker/pin_b_m_p.png';
                break;
            case "llEnd":
                imgURL = 'http://tmapapi.sktelecom.com/upload/tmap/marker/pin_r_m_e.png';
                break;
            default:
        };
        var marker = new Tmapv2.Marker({
            position: new Tmapv2.LatLng(lat,lon),
            icon: imgURL,
            map: map
        });
        // 마커 드래그 설정
        marker.tag = tag;
        marker.addListener("dragend", function (evt) {
            markerListenerEvent(evt);
        });
        marker.addListener("drag", function (evt) {
            markerObject = markerList[tag];
        });
        markerList[tag] = marker;
        return marker;
    }
}

function doGuide(placeSeq) {

    let lathidden = $("#lathidden" + placeSeq).text();
    let lonhidden = $("#lonhidden" + placeSeq).text();
    let placename = $("#placename" + placeSeq).text();


    location.href = `https://apis.openapi.sk.com/tmap/app/routes?appKey=B06BWPIrhC5CJXueAhPi7hG3V6avEXn8edbvjar9&name=${placename}&lon=${lonhidden}&lat=${lathidden}`



}

function regIMG(seq) {

    $('[id^="plc_"]').each(function () {
        // 현재 요소의 id 값을 가져옴 (예: plc_123)
        let id = $(this).attr('id');

        // id에서 plc_를 제외한 숫자 부분만 추출 (예: 123)
        let index = id.replace('plc_', '');

        let lat = '';
        let lon = '';


        if (index === seq) {

            lat = encodeURIComponent($("#lathidden" + index).text());
            lon = encodeURIComponent($("#lonhidden" + index).text());

            console.log("regIMG start! seq : " + seq + "lat : " + lat + "lon : " + lon)

            // 새 창에서 tourPlaceEditForm 열기
            window.open("/img/imgReg?placeSeq=" + seq + "&lat=" + lat + "&lon=" + lon);

        }
    })
}
