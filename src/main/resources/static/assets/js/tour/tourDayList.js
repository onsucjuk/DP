

function doDetail(seq) {
    /*location.href = "/tour/tourDayInfo?nSeq="*/
    location.href = "/tour/tourDayInfo?nSeq=" + seq;
}

function doAdd() {
    if (SS_USER_ID == null || !(SS_USER_ID.length > 0) ) {
         {
             alert("로그인 하시길 바랍니다.");
             location.href = "/user/login";

        }

    } else {

        if (confirm("계획한 여행 일수를 추가하시겠습니까?")) {

            // Ajax 호출해서 글 삭제하기
            $.ajax({
                    url: "/tour/addTourDay",
                    type: "post", // 전송방식은 Post
                    dataType: "JSON", // 전송 결과는 JSON으로 받기
                    data: {"nSeq": nSeq}, // form 태그 내 input 등 객체를 자동으로 전송할 형태로 변경하기
                    success:
                        function (json) {
                            alert(json.msg); // 메시지 띄우기
                            location.reload();//
                        }
                }
            )
        } else {
            location.reload();
        }

    }
}
function doDelete() {

    if (SS_USER_ID === "") {
        alert("로그인 하시길 바랍니다.");
        location.href = "/user/login";
    } else {

        if (confirm("계획한 여행을 삭제하시겠습니까?"))

            {
                // Ajax 호출해서 글 삭제하기
                $.ajax({
                        url: "/tour/deleteTourInfo",
                        type: "post", // 전송방식은 Post
                        dataType: "JSON", // 전송 결과는 JSON으로 받기
                        data: {"nSeq": nSeq}, // form 태그 내 input 등 객체를 자동으로 전송할 형태로 변경하기
                        success:
                            function (json) {
                                alert(json.msg); // 메시지 띄우기
                                location.href = "/tour/tourInfo"; // 공지사항 리스트 이동
                            }
                    }
                )
            } else {

                    location.reload();
        }
        }
}

$(document).ready(function () {

    $("#btnAddDay").on("click", function () {
        doAdd();
    })

    $("#btnDeleteTour").on("click", function () {
        doDelete();
    })

    $("#btnEditTour").on("click", function () {

        location.href = "/tour/tourNameEdit?Seq=" + nSeq;

    })


    if (SS_USER_ID === "")
    {
        alert("로그인 하시길 바랍니다.");
        location.href = "/user/login";
    }

})