<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>즐겨찾기 등록</title>
    <link rel="stylesheet" href="/css/table.css"/>
    <script type="text/javascript" src="/js/jquery-3.6.0.min.js"></script>
    <script type="text/javascript">

        // HTML로딩이 완료되고, 실행됨
        $(document).ready(function () {
            // 버튼 클릭했을때, 발생되는 이벤트 생성함(onclick 이벤트와 동일함)
            $("#btnReg").on("click", function () {
                doSubmit(); // 즐겨찾기 등록하기 실행
            })
        })

        // 공지사항 저장하기
        function doSubmit() {

            let f = document.getElementById("f"); // form 태그

            if (f.addr.value === "") {
                alert("주소를 입력하시기 바랍니다.");
                f.addr.focus();
                return;
            }

            /*			if (ssUserId.length < 0) {
                            alert("로그인 해주세요.");
                            location.href = "/user/login";
                        }*/

            // Ajax 호출해서 글 등록하기
            $.ajax({
                    url: "/addr/myAddrInsert",
                    type: "post", // 전송방식은 Post
                    // contentType: "application/json",
                    dataType: "JSON", // 전송 결과는 JSON으로 받기
                    data: $("#f").serialize(), // form 태그 내 input 등 객체를 자동으로 전송할 형태로 변경하기
                    success: function (json) { // /notice/noticeInsert 호출이 성공했다면..
                        alert(json.msg); // 메시지 띄우기
                        if(json.msg==="로그인 해주세요."){
                            location.href = "/user/login";
                        } else {
                            location.href = "/addr/myAddrList"; // 공지사항 리스트 이동
                        }
                    }
                }
            )
        }
    </script>
</head>
<body>
<h2>즐겨찾기 등록하기</h2>
<hr/>
<br/>
<form name="f" id="f">
    <div class="divTable minimalistBlack">
        <div class="divTableBody">
            <div class="divTableRow">
                <div class="divTableCell">주소
                </div>
                <div class="divTableCell">
                    <textarea name="addr" style="width: 95%; height: 400px"></textarea>
                </div>
            </div>
            <div class="divTableRow">
                <div class="divTableCell">메모
                </div>
                <div class="divTableCell">
                    <textarea name="memo" style="width: 95%; height: 400px"></textarea>
                </div>
            </div>
        </div>
    </div>
    <div>
        <button id="btnReg" type="button">등록</button>
        <button type="reset">다시 작성</button>
    </div>
</form>
</body>
</html>