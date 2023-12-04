$(document).ready(function () {


    $("#btnViewMyImg").on("click", function () {

        if(imgMarkerYn==="N"){

            imgMarkerYn = "Y";
            drawMyImgMaker()
        } else if (imgMarkerYn==="Y"){

            imgMarkerYn = "N";
            deleteImgMarker()

        }


    })

    $("#btnViewRoute").on("click", function () {



    })

    $("#btntourEndInfo").on("click", function () {

        location.href="/tour/tourEndInfo"

    })



    $("#btnViewImg").on("click", function () {

        if(imgMarkerYn==="N"){
            imgMarkerYn = "Y";
            drawImgMaker()
        } else if (imgMarkerYn==="Y"){

            imgMarkerYn = "N";
            deleteImgMarker()

        }

    })

})


function doDetail(seq) {

                location.href = "/tour/viewItda?dSeq=" + seq;

}

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

function doDelete(seq) {

    if (seq === "") {
        alert("장소 번호에 문제가 있습니다. 정보를 확인해주세요.");
    }

    else if(confirm("완료 여행 목적지를 삭제하면 복구하실 수 없습니다. \n 삭제하시겠습니까?")) {

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


function doGuide(placeSeq) {

    let lathidden = $("#lathidden" + placeSeq).text();
    let lonhidden = $("#lonhidden" + placeSeq).text();
    let placename = $("#placename" + placeSeq).text();


    location.href = `https://apis.openapi.sk.com/tmap/app/routes?appKey=B06BWPIrhC5CJXueAhPi7hG3V6avEXn8edbvjar9&name=${placename}&lon=${lonhidden}&lat=${lathidden}`



}

function regImg(seq) {

    let newWindow;

    $('[id^="plc_"]').each(function () {
        // 현재 요소의 id 값을 가져옴 (예: plc_123)
        let id = $(this).attr('id');

        // id에서 plc_를 제외한 숫자 부분만 추출 (예: 123)
        let index = id.replace('plc_', '');

        let lat = '';
        let lon = '';
        let placeNick = '';


        if (index === seq) {

            lat = encodeURIComponent($("#lathidden" + index).text());
            lon = encodeURIComponent($("#lonhidden" + index).text());
            placeNick = encodeURIComponent($("#placeNick" + index).text());

            console.log("regIMG start! seq : " + seq + "lat : " + lat + "lon : " + lon + "placeNick : " + placeNick)

            // 새 창에서 regImg 열기
            newWindow =window.open("/img/imgReg?placeSeq=" + seq + "&lat=" + lat + "&lon=" + lon + "&placeNick=" + placeNick);
            newWindow.addEventListener('beforeunload', function () {
                // 부모 창을 새로고침합니다.
                window.location.reload();
            });
        }
    })
}

function editImg(seq) {

    let newWindow;

    $('[id^="plc_"]').each(function () {
        // 현재 요소의 id 값을 가져옴 (예: plc_123)
        let id = $(this).attr('id');

        // id에서 plc_를 제외한 숫자 부분만 추출 (예: 123)
        let index = id.replace('plc_', '');

        let lat = '';
        let lon = '';
        let placeNick = '';

        if (index === seq) {

            lat = encodeURIComponent($("#lathidden" + index).text());
            lon = encodeURIComponent($("#lonhidden" + index).text());
            placeNick = encodeURIComponent($("#placeNick" + index).text());

            console.log("regIMG start! seq : " + seq + "lat : " + lat + "lon : " + lon + "placeNick : " + placeNick)

            // 새 창에서 editImg 열기
            newWindow = window.open("/img/imgEdit?placeSeq=" + seq + "&lat=" + lat + "&lon=" + lon + "&placeNick=" + placeNick);
            newWindow.addEventListener('beforeunload', function () {
                // 부모 창을 새로고침합니다.
                window.location.reload();
            });
        }
    })
}

//이미지 마커를 저장할 배열
imgMarkers = [];
let currentInfoWindow; // 현재 열려있는 InfoWindow를 추적하기 위한 변수
let imgCheck = '';

function drawImgMaker() {


    for (let i = 0; i < iList.length; i++) {
        // 마커 생성
        let marker = new Tmapv2.Marker({
            position: new Tmapv2.LatLng(iList[i].imgLat, iList[i].imgLon),
            map: map,
            icon: "/itda/images/marker/imgMarker.png"
        });

        // 클로저를 사용하여 현재 인덱스의 데이터를 클릭 이벤트 리스너에 전달
        marker.addListener("click", (function (index) {

            let imgSeq = iList[index].imgSeq
            console.log("/img/checkLike 시작!")

            $.ajax({
                url: "/img/checkLike",
                type: "POST",
                datatype: "JSON",
                data: {
                    "imgSeq" : imgSeq
                },
                success: function (json) {

                    if (json.likeChk === 1) {
                        // 유저가 like를 눌러놨음
                        imgCheck = "Y"
                        console.log("checkLike 결과 : " + imgCheck)

                    } else if(json.likeChk === 0) {
                        // 유저가 like를 누르지 않았음
                        imgCheck = "N"
                        console.log("checkLike 결과 : " + imgCheck)

                    }
                }
            })

            let isOpen = false; // 각 팝업의 열림 상태를 추적하기 위한 변수

            return function (evt) {
                console.log((index + 1) + '번째 마커 정보');
                console.log('imgURL = ' + iList[index].imgURL + ' title = ' + iList[index].title + ' userId : ' + iList[index].regId);
                console.log('contents = ' + iList[index].contents + ' imgLat = ' + iList[index].imgLat + ' imgLon : ' + iList[index].imgLon);


                // 열려있는 InfoWindow가 있으면 닫기
                if (currentInfoWindow) {
                    currentInfoWindow.setVisible(false);
                }
                console.log('imgCheck = ' + imgCheck)

                let infoWindowContent = ''

                if (imgCheck==='N') {

                    infoWindowContent = `
                    <div class="_tmap_preview_popup_4">
                        <div class="_tmap_preview_popup_image_l" style="position: relative;">
                            <img src="${iList[index].imgURL}" alt="" style="max-width: 200px; max-height: 200px; width: auto; height: auto;">
                        </div>
                        <div class="_tmap_preview_popup_info">
                            <div class="likeDiv">
                                <div class="likeImage_heart">
                                    <img src="/itda/images/likeBefore.png" style="height:10px; width:10px; margin-right: 5px;" onclick="likeCheck([[${imgSeq}]])">${iList[index].likeCnt}
                                </div>
                            </div>
                            <div class="_tmap_preview_popup_title bold">${iList[index].title}</div>
                            <div class="_tmap_preview_popup_address right-align">${iList[index].regId}</div>
                            <div class="_tmap_preview_popup_address bold">${iList[index].contents}</div>
                        </div>
                    </div>
                `;
                } else if (imgCheck==='Y') {

                    infoWindowContent = `
                    <div class="_tmap_preview_popup_4">
                        <div class="_tmap_preview_popup_image_l" style="position: relative;">
                            <img src="${iList[index].imgURL}" alt="" style="max-width: 200px; max-height: 200px; width: auto; height: auto;">
                        </div>
                        <div class="_tmap_preview_popup_info">
                            <div class="likeDiv"> 
                                <div class="likeImage_heart">
                                    <img src="/itda/images/likeAfter.png" style="height:10px; width:10px; margin-right: 5px;"  onclick="likeDel([[${imgSeq}]])">${iList[index].likeCnt}
                                </div>
                            </div>
                        <div class="_tmap_preview_popup_title bold">${iList[index].title}</div>
                        <div class="_tmap_preview_popup_address right-align">${iList[index].regId}</div>
                        <div class="_tmap_preview_popup_address bold">${iList[index].contents}</div>
                        </div>
                    </div>
                `;

                }


                if (isOpen) {
                    currentInfoWindow.setVisible(false); // 이미 열린 상태면 닫음
                    isOpen = false;
                } else {
                    currentInfoWindow = new Tmapv2.InfoWindow({
                        position: new Tmapv2.LatLng(iList[index].imgLat, iList[index].imgLon),
                        content: infoWindowContent,
                        border: '0px solid #FF0000',
                        backgroundColor: 'white', // 배경색
                        borderRadius: '5px', // 테두리 모서리 둥글게
                        padding: '10px', // 내용과 테두리 간격 조절
                        type: 2,
                        map: map
                    });
                    isOpen = true;
                }
            };

        })(i));

        // 생성된 마커를 markers 배열에 추가
        imgMarkers.push(marker);
    }
}


function drawMyImgMaker() {


    for (let i = 0; i < miList.length; i++) {
        // 마커 생성
        let marker = new Tmapv2.Marker({
            position: new Tmapv2.LatLng(miList[i].imgLat, miList[i].imgLon),
            map: map,
            icon: "/itda/images/marker/imgMarker.png"
        });

        // 클로저를 사용하여 현재 인덱스의 데이터를 클릭 이벤트 리스너에 전달
        marker.addListener("click", (function (index) {

            let imgSeq = miList[index].imgSeq
            console.log("/img/checkLike 시작!")

            $.ajax({
                url: "/img/checkLike",
                type: "POST",
                datatype: "JSON",
                data: {
                    "imgSeq" : imgSeq
                },
                success: function (json) {

                    if (json.likeChk === 1) {
                        // 유저가 like를 눌러놨음
                        imgCheck = "Y"
                        console.log("checkLike 결과 : " + imgCheck)

                    } else if(json.likeChk === 0) {
                        // 유저가 like를 누르지 않았음
                        imgCheck = "N"
                        console.log("checkLike 결과 : " + imgCheck)

                    }
                }
            })

            let isOpen = false; // 각 팝업의 열림 상태를 추적하기 위한 변수

            return function (evt) {

                console.log((index + 1) + '번째 마커 정보');
                console.log('imgURL = ' + miList[index].imgURL + ' title = ' + miList[index].title + ' userId : ' + miList[index].regId);
                console.log('contents = ' + miList[index].contents + ' imgLat = ' + miList[index].imgLat + ' imgLon : ' + miList[index].imgLon);


                // 열려있는 InfoWindow가 있으면 닫기
                if (currentInfoWindow) {
                    currentInfoWindow.setVisible(false);
                }
                console.log('imgCheck = ' + imgCheck)

                $.ajax({
                    url: "/img/checkLike",
                    type: "POST",
                    datatype: "JSON",
                    data: {
                        "imgSeq" : imgSeq
                    },
                    success: function (json) {

                        if (json.likeChk === 1) {
                            // 유저가 like를 눌러놨음
                            imgCheck = "Y"
                            console.log("checkLike 결과 : " + imgCheck)

                        } else if(json.likeChk === 0) {
                            // 유저가 like를 누르지 않았음
                            imgCheck = "N"
                            console.log("checkLike 결과 : " + imgCheck)

                        }
                    }
                })

                let infoWindowContent = ''

                if (imgCheck==='N') {

                    infoWindowContent = `
                    <div class="_tmap_preview_popup_4">
                        <div class="_tmap_preview_popup_image_l" style="position: relative;">
                            <img src="${miList[index].imgURL}" alt="" style="max-width: 200px; max-height: 200px; width: auto; height: auto;">
                        </div>
                        <div class="_tmap_preview_popup_info">
                            <div class="likeDiv">
                                <div class="likeImage_heart">
                                    <img src="/itda/images/likeBefore.png" style="height:10px; width:10px; margin-right: 5px;" onclick="likeCheck([[${imgSeq}]])">${miList[index].likeCnt}
                                </div>
                            </div>
                            <div class="_tmap_preview_popup_title bold">${miList[index].title}</div>
                            <div class="_tmap_preview_popup_address right-align">${miList[index].regId}</div>
                            <div class="_tmap_preview_popup_address bold">${miList[index].contents}</div>
                        </div>
                    </div>
                `;
                } else if (imgCheck==='Y') {

                    infoWindowContent = `
                    <div class="_tmap_preview_popup_4">
                        <div class="_tmap_preview_popup_image_l" style="position: relative;">
                            <img src="${miList[index].imgURL}" alt="" style="max-width: 200px; max-height: 200px; width: auto; height: auto;">
                        </div>
                        <div class="_tmap_preview_popup_info">
                            <div class="likeDiv"> 
                                <div class="likeImage_heart">
                                    <img src="/itda/images/likeAfter.png" style="height:10px; width:10px; margin-right: 5px;"  onclick="likeDel([[${imgSeq}]])">${miList[index].likeCnt}
                                </div>
                            </div>
                        <div class="_tmap_preview_popup_title bold">${miList[index].title}</div>
                        <div class="_tmap_preview_popup_address right-align">${miList[index].regId}</div>
                        <div class="_tmap_preview_popup_address bold">${miList[index].contents}</div>
                        </div>
                    </div>
                `;

                }


                if (isOpen) {
                    currentInfoWindow.setVisible(false); // 이미 열린 상태면 닫음
                    isOpen = false;
                } else {
                    currentInfoWindow = new Tmapv2.InfoWindow({
                        position: new Tmapv2.LatLng(miList[index].imgLat, miList[index].imgLon),
                        content: infoWindowContent,
                        border: '0px solid #FF0000',
                        backgroundColor: 'white', // 배경색
                        borderRadius: '5px', // 테두리 모서리 둥글게
                        padding: '10px', // 내용과 테두리 간격 조절
                        type: 2,
                        map: map
                    });
                    isOpen = true;
                }
            };

        })(i));

        // 생성된 마커를 markers 배열에 추가
        imgMarkers.push(marker);
    }
}


// 이미지 마커 지우기
function deleteImgMarker() {

    // 열려있는 InfoWindow 닫기
    if (currentInfoWindow) {
        currentInfoWindow.setVisible(false);
    }

    removeMarkers(imgMarkers);

}

// 모든 마커를 제거하는 함수
function removeMarkers(markers) {
    for (var i = 0; i < markers.length; i++) {
        markers[i].setMap(null);
    }
    // markers 배열 비우기
    markers = [];

}


function likeCheck(imgSeq) {
    console.log(JSON.stringify({ "imgSeq": imgSeq[0][0] }))

    $.ajax({
        url: "/img/likeCheck",
        type: "POST",
        dataType: "JSON",
        contentType: "application/json",
        data: JSON.stringify({
            "imgSeq": imgSeq[0][0]
        }),
        success: function (json) {

            console.log(json);
            console.log(json.msg);
            console.log(json.likeCount);
            console.log(json.ilist);

            if(json.msg==="좋아요 했습니다.") {

                alert(json.msg);
                location.reload();

                /*let likeDiv = $(".likeDiv");

                if (likeDiv.length === 0) {
                    // likeDiv가 없으면 새로 추가
                    let newDiv = $('<div class="likeImage_heart"><img src="/itda/images/likeAfter.png" style="height:10px; width:10px; margin-right: 5px;">' + json.likeCount + '</div>');
                    newDiv.find('img').off("click").on("click", function () {
                        likeDel(imgSeq);
                    });
                    $("._tmap_preview_popup_info").append(newDiv);
                } else {
                    // likeDiv가 이미 있으면 업데이트
                    likeDiv.html('<div class="likeImage_heart"><img src="/itda/images/likeAfter.png" style="height:10px; width:10px; margin-right: 5px;">' + json.likeCount + '</div>');
                    likeDiv.find('img').off("click").on("click", function () {
                        likeDel(imgSeq);
                    });
                }*/
            } else if(json.msg==="로그인 해주세요.") {

                location.href = "/user/login"

            } else {

                location.reload();

            }
        },
        error: function (xhr) {
            console.log(xhr);
        }
    });
}

function likeDel(imgSeq) {
    console.log(JSON.stringify({ "imgSeq": imgSeq[0][0] }))

    $.ajax({
        url: "/img/likeDel",
        type: "POST",
        dataType: "JSON",
        contentType: "application/json",
        data: JSON.stringify({
            "imgSeq": imgSeq[0][0]
        }),
        success: function (json) {
            console.log(json);
            console.log(json.msg);
            console.log(json.likeCount);
            console.log(json.ilist);

            if(json.likeCount==null) {
                json.likeCount=0;
            }

            if(json.msg==="좋아요를 취소했습니다.") {

                alert(json.msg);
                location.reload();

                /*let likeDiv = $(".likeDiv");

                if (likeDiv.length === 0) {
                    // likeDiv가 없으면 새로 추가
                    let newDiv = $('<div class="likeImage_heart"><img src="/itda/images/likeBefore.png" style="height:10px; width:10px; margin-right: 5px;">' + json.likeCount + '</div>');
                    newDiv.find('img').off("click").on("click", function () {
                        likeCheck(imgSeq);
                    });
                    $("._tmap_preview_popup_info").append(newDiv);
                } else {
                    // likeDiv가 이미 있으면 업데이트
                    likeDiv.html('<div class="likeImage_heart"><img src="/itda/images/likeBefore.png" style="height:10px; width:10px; margin-right: 5px;">' + json.likeCount + '</div>');
                    likeDiv.find('img').off("click").on("click", function () {
                        likeCheck(imgSeq);
                    });
                }*/
            } else if(json.msg==="로그인 해주세요.") {

                location.href = "/user/login"

            } else {

                location.reload();

            }
        },
        error: function (xhr) {
            console.log(xhr);
        }
    });
}
