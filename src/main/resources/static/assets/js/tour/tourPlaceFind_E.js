
    // 지도 타입 변경: ROAD
    map.setMapType(Tmapv2.Map.MapType.ROAD);

    // 지도의 드래그 이동을 막아주는 함수입니다.
    map.setOptions({ draggable: true });

    // 지도의 확대축소 기능을 가능하게 하는 함수입니다.
    map._data.options.scrollwheel = true;

    // 지도 옵션 줌컨트롤 표출 비활성화
    map.setOptions({zoomControl:false});


    /* API시작 */
    // 마커 초기화
    var markerStart = null;
    var markerEnd = null;
    var markerWp = [];
    var markerPoi = [];
    var markerPoint = [];
    var markerArr = [], lineArr = [], labelArr = [];
    var marker1 = new Tmapv2.Marker({
    icon : "http://tmapapi.sktelecom.com/upload/tmap/marker/pin_b_m_a.png",
    iconSize : new Tmapv2.Size(48, 42),
    map : map
});

    var tData = new Tmapv2.extension.TData();

    $(document).ready(function () {

        // 버튼 클릭했을때, 발생되는 이벤트 생성함(onclick 이벤트와 동일함)
        $(document).on("click", '[id^="btnEditPlace"]', function () {

            // 클릭된 버튼의 ID에서 k 값을 추출
            let buttonId = $(this).attr("id");

            // ID에서 숫자 부분을 추출
            let k = buttonId.replace("btnEditPlace", "");

            sessionStorage.setItem("SS_ITEM_SEQ", k);

            // k를 이용하여 작업을 수행
            doEditPlace(k);
        });

        function doEditPlace(k) {

            let placeNick = SS_PLACE_NICK;
            let poi = $(`#poi_${k} [name="poi"]`).text();
            let placeName = $(`#poi_${k} [name="placeName"]`).text();
            let placeAddr = $(`#poi_${k} [name="placeAddr"]`).text();
            let lat = $(`#poi_${k} [name="lat"]`).text();
            let lon = $(`#poi_${k} [name="lon"]`).text();
            let memo = SS_MEMO;
            let placeSeq = SS_PLACE_SEQ;

            if (placeName === "" || placeAddr === "" || lat === "" || lon === "") {
                alert("모든 필드를 입력해야 합니다.");
                return;
            }

            poi = encodeURIComponent(poi);
            placeNick = encodeURIComponent(placeNick);
            placeName = encodeURIComponent(placeName);
            placeAddr = encodeURIComponent(placeAddr);
            lat = encodeURIComponent(lat);
            lon = encodeURIComponent(lon);
            memo = encodeURIComponent(memo);
            placeSeq = encodeURIComponent(placeSeq);

            window.location.href = `/tour/tourPlaceEditForm?poi=${poi}&placeNick=${placeNick}&placeName=${placeName}&placeAddr=${placeAddr}&lat=${lat}&lon=${lon}&memo=${memo}&placeSeq=${placeSeq}`;

        }

        if (SS_USER_ID == null || !(SS_USER_ID.length > 0)) {
            alert("로그인 해주세요.");
            location.href = "/user/login";
        }

    })


        // (장소 API) API [검색] 버튼 동작
    function apiSearchLocation() {
    searchPois();
}

    function onKeyupSearchLocation(obj) {
    if (window.event.keyCode == 13) {
    searchPois();
}
}

    // (장소API) 통합 검색 함수
    function searchPois() {
        var searchKeyword = $("#searchPlace").val();

        var optionObj = {
            resCoordType : "WGS84GEO",
            reqCoordType : "WGS84GEO",
            count: 10
        };

        var params = {
            onComplete: function(result) {
                // 데이터 로드가 성공적으로 완료되었을 때 발생하는 이벤트입니다.
                var resultpoisData = result._responseData.searchPoiInfo.pois.poi;

                // 기존 마커, 팝업 제거
                reset();
                $("._btn_radio").removeClass('__color_blue_fill');
                if(marker1) {
                    marker1.setMap(null);
                }

                var innerHtml =    // Search Reulsts 결과값 노출 위한 변수
                    `
                    <div class="_result_panel_bg _scroll_padding">
                        <div class="_result_panel_scroll">
                    `;
                var positionBounds = new Tmapv2.LatLngBounds();        //맵에 결과물 확인 하기 위한 LatLngBounds객체 생성

                for(var k in resultpoisData){
                    var name = resultpoisData[k].name;

                    var poi = Number(resultpoisData[k].id);

                    var lat = Number(resultpoisData[k].noorLat);
                    var lon = Number(resultpoisData[k].noorLon);

                    var frontLat = Number(resultpoisData[k].frontLat);
                    var frontLon = Number(resultpoisData[k].frontLon);

                    var markerPosition = new Tmapv2.LatLng(lat, lon);

                    var fullAddressRoad = resultpoisData[k].newAddressList.newAddress[0].fullAddressRoad;

                    const marker3 = new Tmapv2.Marker({
                        position : markerPosition,
                        //icon : "http://tmapapi.sktelecom.com/upload/tmap/marker/pin_b_m_" + k + ".png",
                        iconHTML:`
                                <div class='_t_marker' style="position:relative;" >
                                <img src="https://openapi.sk.com/lib/img/_icon/marker_grey.svg" style="width:48px;height:48px;position:absolute;"/>
                                <div style="position:absolute; width:48px;height:42px; display:flex; align-items:center; justify-content: center; color:#FAFBFF; font-family: 'SUIT';font-style: normal;font-weight: 700;font-size: 15px;line-height: 19px;">
                                ${Number(k)+1}</div>
                                </div>
                            `,
                        iconSize : new Tmapv2.Size(48, 42),
                        title : name,
                        label: `<span style="display:none;">${k}_0</span>`,
                        map:map
                    });

                    // 마커 클릭 이벤트 추가
                    marker3.addListener("click", function(evt) {
                        for(let tMarker of markerPoi) {
                            const labelInfo = $(tMarker.getOtherElements()).text();
                            const forK = labelInfo.split("_")[0];
                            tMarker.setIconHTML(`
                                    <div class='_t_marker' style="position:relative;" >
                                    <img src="https://openapi.sk.com/lib/img/_icon/marker_grey.svg" style="width:48px;height:48px;position:absolute;"/>
                                    <div style="position:absolute; width:48px;height:42px; display:flex; align-items:center; justify-content: center; color:#FAFBFF; font-family: 'SUIT';font-style: normal;font-weight: 700;font-size: 15px;line-height: 19px;">
                                    ${Number(forK)+1}</div>
                                    </div>
                                `);
                            // marker z-index 초기화
                            $(tMarker.getOtherElements()).next('div').css('z-index', 100);
                        }

                        // 선택한 marker z-index 처리
                        $(marker3.getOtherElements()).next('div').css('z-index', 101);

                        const labelInfo = $(marker3.getOtherElements()).text();
                        const thisK = labelInfo.split("_")[0];
                        const thisId = labelInfo.split("_")[1];
                        marker3.setIconHTML(`
                                <div class='_t_marker' style="position:relative;" >
                                <img src="https://openapi.sk.com/lib/img/_icon/marker_blue.svg" style="width:48px;height:48px;position:absolute;"/>
                                <div style="position:absolute; width:48px;height:42px; display:flex; align-items:center; justify-content: center; color:#FAFBFF; font-family: 'SUIT';font-style: normal;font-weight: 700;font-size: 15px;line-height: 19px;">
                                ${Number(thisK)+1}</div>
                                </div>
                            `);
                        var scrollOffset = $("#poi_"+thisK)[0].offsetTop - $("._result_panel_scroll")[0].offsetTop
                        $("._result_panel_scroll").animate({scrollTop: scrollOffset}, 'slow');
                        $("._result_panel_scroll ._search_item_poi_icon").removeClass("_search_item_poi_icon_blue");
                        $("#poi_"+thisK).find('._search_item_poi_icon').addClass("_search_item_poi_icon_blue");
                    });


                    innerHtml += `
                            <div class="_search_item" id="poi_${k}">
                                <div class="_search_item_poi">
                                    <div class="_search_item_poi_icon _search_item_poi_icon_grey">
                                        <div class="_search_item_poi_icon_text">${Number(k)+1}</div>
                                    </div>
                                </div>
                                    <div class="_search_item_info">
                                        <p class="_search_item_info_address" name ="poi">${poi}</p>
                                        <p class="_search_item_info_title" name = "placeName" >${name}</p>
                                        <p class="_search_item_info_address" name ="placeAddr">${fullAddressRoad}</p>
                                        <p class="_search_item_info_address" name = "lat">${lat}</p>
                                        <p class="_search_item_info_address" name = "lon">${lon}</p>
                                            <div class="px-6 my-6">
                                                <button 
                                                        id="btnEditPlace${k}"
                                                        class="flex items-left justify-between w-1/15 px-4 py-2 text-sm font-medium leading-5 text-white transition-colors duration-150 bg-purple-600 border border-transparent rounded-lg active:bg-purple-600 hover:bg-purple-700 focus:outline-none focus:shadow-outline-purple"
                                                        type="button"
                                                >
                                                    <div class="w-full">
                                                        <span class="px-4 py-3 text-sm font-semibold" >추가</span>
                                                        <span class="ml-2" aria-hidden="true">+</span>
                                                    </div>
                                                </button>
                                            </div>  
                                            <hr>
                                    </div>  
                            </div>
                            ${(resultpoisData.length-1) === Number(k) ? "": `<div class="_search_item_split"></div>`}
                        `;


                    markerPoi.push(marker3);
                    positionBounds.extend(markerPosition);    // LatLngBounds의 객체 확장
                }

                innerHtml += "</div></div>";
                $("#apiResult").html(innerHtml);    //searchResult 결과값 노출
                map.panToBounds(positionBounds);    // 확장된 bounds의 중심으로 이동시키기
                map.zoomOut();
            },
            onProgress: function() {},
            onError: function(){}
        }
        tData.getPOIDataFromSearchJson(searchKeyword, optionObj, params);



    }

    // (API 공통) 맵에 그려져있는 라인, 마커, 팝업을 지우는 함수
    function reset() {
        // 기존 라인 지우기
        if(lineArr.length > 0){
            for(var i in lineArr) {
                lineArr[i].setMap(null);
            }
            //지운뒤 배열 초기화
            lineArr = [];
        }

        // 기존 마커 지우기
        if(markerStart) {
            markerStart.setMap(null);
        }
        if(markerEnd) {
            markerEnd.setMap(null);
        }
        if(markerArr.length > 0){
            for(var i in markerArr){
                markerArr[i].setMap(null);
            }
            markerArr = [];
        }

        // poi 마커 지우기
        if(markerPoi.length > 0){
            for(var i in markerPoi){
                markerPoi[i].setMap(null);
            }
            markerPoi = [];
        }

        // 경로찾기 point 마커 지우기
        if(markerPoint.length > 0){
            for(var i in markerPoint){
                markerPoint[i].setMap(null);
            }
            markerPoint = [];
        }

        // 기존 팝업 지우기
        if(labelArr.length > 0){
            for(var i in labelArr){
                labelArr[i].setMap(null);
            }
            labelArr = [];
        }
    }


