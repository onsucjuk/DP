$(document).ready(function () {

    $("#btnTourStart").on("click", function () {

        if(confirm("여행을 시작하시겠습니까?")) {

            updateStart()
            location.reload()

        }
    })

    $("#btnTourStop").on("click", function () {

        if(confirm("여행을 취소하시겠습니까?")) {

            resetStart()
            location.reload()

        }
    })



    if(daySt){
        doRotate();
        doCong()
    }


})

function doDetail(seq) {
    /*location.href = "/tour/tourDayInfo?nSeq="*/
    location.href = "/tour/goItda?nSeq=" + seq;
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

        console.log(nowLon, nowLat, lathidden, lonhidden, index)
        /*let poihidden = $(this).find('[th\\:name="poihidden"]').text();*/


                    $.ajax({
                        url: "/route/getRouteInfo",
                        type: "POST",
                        datatype: "JSON",
                        data: { "sLat" : nowLat,
                                "sLon" : nowLon,
                                "eLat" : lathidden,
                                "eLon" : lonhidden,
                                "index": index},
                        success: function (json) {

                            if (json.isEmpty) {
                                alert("JSON 값을 받아오지 못했습니다.");

                            } else {

                                let totalDistance = json.totalDistance;
                                let totalTime = json.totalTime;
                                let index = json.index;

                                console.log(totalDistance, totalTime, index)

                                $(`#totalDistance${index}`).text(parseInt(totalDistance/100)+'km');
                                $(`#totalTime${index}`).text('약' + parseInt(totalTime/60) + '분 소요 예정');

                            }
                        }
                    })
    })
};
/*

function getRouteInfoForIndex(index) {
    let nowLat = $("#nowLat").text();
    let nowLon = $("#nowLon").text();

    let lathidden = $("#lathidden" + index).text();
    let lonhidden = $("#lonhidden" + index).text();

    console.log(nowLon, nowLat, lathidden, lonhidden);

    $.ajax({
        url: "/route/getRouteInfo",
        type: "POST",
        datatype: "JSON",
        data: {
            "sLat": nowLat,
            "sLon": nowLon,
            "eLat": lathidden,
            "eLon": lonhidden
        },
        success: function (json) {
            if (json.isEmpty) {
                alert("JSON 값을 받아오지 못했습니다.");
            } else {
                let totalDistance = json.totalDistance;
                let totalTime = json.totalTime;

                $(`#plc_${index} [name="totalDistance"]`).text(totalDistance);
                $(`#plc_${index} [name="totalTime"]`).text(totalTime);
            }
        }
    });
}

function doRotate() {
    // Find all elements with the class 'plc'
    $('[class^="plc_"]').each(function () {
        // Get the index from the class name
        let index = $(this).attr('class').replace('plc_', '');
        // Call the function for each index
        getRouteInfoForIndex(index);
    });
}

function doCong() {

}
*/


function updateStart(){

    $.ajax({
        url: "/tour/updateTourStart",
        type: "post",
        datatype: "JSON",
        data: { "dSeq" : dSeq },
        success: function (json) {

            if (json.result === 1) {
                alert(json.msg);
                location.href = "/user/login";

            } else {
                alert(json.msg);
            }
        }
    })


}
function resetStart(){

    $.ajax({
        url: "/tour/resetTourStart",
        type: "post",
        datatype: "JSON",
        data: { "dSeq" : dSeq },
        success: function (json) {

            if (json.result === 1) {
                alert(json.msg);
                location.href = "/user/login";

            } else {
                alert(json.msg);
            }
        }
    })

}

