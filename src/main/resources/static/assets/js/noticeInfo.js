$(document).ready(function () {
    // 버튼 클릭했을때, 발생되는 이벤트 생성함(onclick 이벤트와 동일함)
    $("#btnEdit").on("click", function () {
        doEdit(); // 공지사항 수정하기 실행
    })

    // 버튼 클릭했을때, 발생되는 이벤트 생성함(onclick 이벤트와 동일함)
    $("#btnDelete").on("click", function () {
        doDelete(); // 공지사항 수정하기 실행
    })

    // 버튼 클릭했을때, 발생되는 이벤트 생성함(onclick 이벤트와 동일함)
    $("#btnList").on("click", function () {
        location.href = "/notice/noticeList"; // 공지사항 리스트 이동
    })
})

//수정하기
function doEdit() {
    if (SS_USER_ID === user_id || SS_USER_ID === "admin") {
        location.href = "/notice/noticeEditInfo?nSeq=" + nSeq;

    } else if (SS_USER_ID === "") {
        alert("로그인 하시길 바랍니다.");
        location.href = "/user/login";
    } else {
        alert("본인이 작성한 글만 수정 가능합니다.");

    }
}

//삭제하기
function doDelete() {
    if (SS_USER_ID === user_id || SS_USER_ID === "admin") {
        if (confirm("작성한 글을 삭제하시겠습니까?")) {

            // Ajax 호출해서 글 삭제하기
            $.ajax({
                    url: "/notice/noticeDelete",
                    type: "post", // 전송방식은 Post
                    dataType: "JSON", // 전송 결과는 JSON으로 받기
                    data: {"nSeq": nSeq}, // form 태그 내 input 등 객체를 자동으로 전송할 형태로 변경하기
                    success:
                        function (json) { // /notice/noticeDelete 호출이 성공했다면..
                            alert(json.msg); // 메시지 띄우기
                            location.href = "/notice/noticeList"; // 공지사항 리스트 이동
                        }
                }
            )
        }

    } else if (SS_USER_ID === "") {
        alert("로그인 하시길 바랍니다.");
        location.href = "/user/login";
    } else {
        alert("본인이 작성한 글만 삭제가 가능합니다.");

    }
}