$(document).ready(function () {

    $("#btnPlaceReg").on("click", function () {

        location.href = "/tour/tourPlaceFind";

    })

    $("#btnDayList").on("click", function () {

        location.href = "/tour/tourDayList?nSeq=" + nSeq;

    })

    $("#btnDelDay").on("click", function () {

        doDelete(dSeq)

    })

    $("#btnGoItda").on("click", function () {

        location.href = "/tour/goItda?dSeq=" + dSeq;

    })

    $(document).on("click", '[id^="btnPlaceEdit"]', function () {

        // 클릭된 버튼의 ID에서 k 값을 추출
        let buttonId = $(this).attr("id");

        // ID에서 숫자 부분을 추출
        let seq = buttonId.replace("btnPlaceEdit", "");

        // k를 이용하여 작업을 수행
        doEdit(seq);
    });

    $(document).on("click", '[id^="btnPlaceDel"]', function () {

        // 클릭된 버튼의 ID에서 k 값을 추출
        let buttonId = $(this).attr("id");

        // ID에서 숫자 부분을 추출
        const pSeq = buttonId.replace("btnPlaceDel", "");

        // k를 이용하여 작업을 수행
        doDel(pSeq);
    });

    function doEdit(seq) {
        let placeNick = $(`#plc_${seq} [name="placeNick"]`).text();
        let placeName = $(`#plc_${seq} [name="placeName"]`).text();
        let placeAddr = $(`#plc_${seq} [name="placeAddr"]`).text();
        let memo = $(`#plc_${seq} [name="memo"]`).text();
        let lat = $(`#plc_${seq} [name="lat"]`).text();
        let lon = $(`#plc_${seq} [name="lon"]`).text();
        let poi = $(`#plc_${seq} [name="poi"]`).text();
        let placeSeq = seq;

        if (placeNick === "" || memo === "" || placeName === "" || placeAddr === "" || lat === "" || lon === "") {
            alert("모든 필드를 입력해야 합니다.");
            return;
        }

        placeNick = encodeURIComponent(placeNick);
        memo = encodeURIComponent(memo);
        placeName = encodeURIComponent(placeName);
        placeAddr = encodeURIComponent(placeAddr);
        lat = encodeURIComponent(lat);
        lon = encodeURIComponent(lon);
        poi = encodeURIComponent(poi);
        placeSeq = encodeURIComponent(placeSeq);


        window.location.href = `/tour/tourPlaceEditForm?poi=${poi}&placeNick=${placeNick}&memo=${memo}&placeName=${placeName}&placeAddr=${placeAddr}&lat=${lat}&lon=${lon}&placeSeq=${placeSeq}`;

    }

})

function doDel(pSeq) {

    if (pSeq === "") {
        alert("장소 번호에 문제가 있습니다. 새로고침 합니다.");

        location.reload();
    }

    else if(confirm("계획한 여행을 삭제하시겠습니까?")) {

        // Ajax 호출해서 회원가입하기
        $.ajax({
            url: "/tour/deleteTourPlaceOne",
            type: "post",
            datatype: "JSON",
            data: { "pSeq" : pSeq },
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

function doDelete(dSeq) {

    if (dSeq === "") {
        alert("날짜 번호에 문제가 있습니다. 여행지 관리로 돌아갑니다.");

        location.href = "/tour/tourInfo";
    }

    else if(confirm("계획한 여행을 삭제하시겠습니까?")) {

        // Ajax 호출해서 회원가입하기
        $.ajax({
            url: "/tour/deleteTourDay",
            type: "post",
            datatype: "JSON",
            data: { "dSeq" : dSeq },
            success: function (json) {

                if (json.result === 1) {
                    alert(json.msg);
                    location.href = "/user/login";

                } else {
                    alert(json.msg);
                    location.href = "/tour/tourDayList?nSeq=" + nSeq;
                }
            }
        })
    }
}

function toggleTable(tableElement) {
    const tbody = tableElement.querySelector('tbody');
    if (tbody.style.display === 'none') {
        tbody.style.display = 'table-row-group';
    } else {
        tbody.style.display = 'none';
    }
}

