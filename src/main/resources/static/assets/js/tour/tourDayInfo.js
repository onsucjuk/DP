$(document).ready(function () {

    $("#btnPlaceReg").on("click", function () {

        location.href = "/tour/tourPlaceFind";

    })

    $(document).on("click", '[id^="btnPlaceEdit"]', function () {

        // 클릭된 버튼의 ID에서 k 값을 추출
        let buttonId = $(this).attr("id");

        // ID에서 숫자 부분을 추출
        let seq = buttonId.replace("btnPlaceEdit", "");

        // k를 이용하여 작업을 수행
        doEdit(seq);
    });

    function doEdit(seq) {
        let placeNick = $(`#plc_${seq} [name="placeNick"]`).text();
        let placeName = $(`#plc_${seq} [name="placeName"]`).text();
        let placeAddr = $(`#plc_${seq} [name="placeAddr"]`).text();
        let memo = $(`#plc_${seq} [name="memo"]`).text();
        let lat = $(`#plc_${seq} [name="lat"]`).text();
        let lon = $(`#plc_${seq} [name="lon"]`).text();

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

        window.location.href = `/tour/tourPlaceEditForm?placeNick=${placeNick}&memo=${memo}&placeName=${placeName}&placeAddr=${placeAddr}&lat=${lat}&lon=${lon}`;

    }

})

function toggleTable(tableElement) {
    const tbody = tableElement.querySelector('tbody');
    if (tbody.style.display === 'none') {
        tbody.style.display = 'table-row-group';
    } else {
        tbody.style.display = 'none';
    }
}

