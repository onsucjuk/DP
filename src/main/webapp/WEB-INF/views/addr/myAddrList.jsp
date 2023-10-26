<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="dp.fdis.dto.MyAddrDTO" %>
<%@ page import="dp.fdis.util.CmmUtil" %>
<%
    // NoticeController 함수에서 model 객체에 저장된 값 불러오기
    List<MyAddrDTO> rList = (List<MyAddrDTO>) request.getAttribute("rList");

%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>즐겨찾기 리스트</title>
    <link rel="stylesheet" href="/css/table.css"/>
    <script type="text/javascript" src="/js/jquery-3.6.0.min.js"></script>
    <script type="text/javascript">

        const SS_USER_ID = "<%=CmmUtil.nvl((String)session.getAttribute("SS_USER_ID"))%>";


/*        function doDetail(seq) {
            location.href = "/addr/myAddrInfo?nSeq=" + seq;
        }*/

        //수정하기
        function doEdit(nSeq) {

            let user_id = document.querySelector(".divTableCell.userId").textContent.trim();

            if (SS_USER_ID === user_id) {
                location.href = "/addr/myAddrEdit?nSeq=" + nSeq;

            } else if (SS_USER_ID === "") {
                alert("로그인 하시길 바랍니다.");
                location.href = "/user/login";
            } else {
                alert("본인이 작성한 글만 수정 가능합니다.");

            }
        }

        //삭제하기
        function doDelete(nSeq) {

            let user_id = document.querySelector(".divTableCell.userId").textContent.trim();

            if (SS_USER_ID === user_id) {
                if (confirm("작성한 글을 삭제하시겠습니까?")) {

                    // Ajax 호출해서 글 삭제하기
                    $.ajax({
                            url: "/addr/myAddrDelete",
                            type: "post", // 전송방식은 Post
                            dataType: "JSON", // 전송 결과는 JSON으로 받기
                            data: {"nSeq": nSeq}, // form 태그 내 input 등 객체를 자동으로 전송할 형태로 변경하기
                            success:
                                function (json) { //
                                    alert(json.msg); // 메시지 띄우기
                                    location.href = "/addr/myAddrList"; //
                                }
                        }
                    )
                }

            } else if (SS_USER_ID === "") {
                alert("로그인 하시길 바랍니다.");
                location.href = "/user/login";
            } else {
                alert("본인이 작성한 글만 수정 가능합니다.");

            }
        }


        // HTML로딩이 완료되고, 실행됨
        $(document).ready(function () {
            // 버튼 클릭했을때, 발생되는 이벤트 생성함(onclick 이벤트와 동일함)
            $("#btnResult").on("click", function () {
                location.href = "/user/loginResult";
            })

            $("#btnAdd").on("click", function () {
                location.href = "/addr/myAddrReg";
            })

            $("#btnEdit").on("click", function () {

                if ($('input[name="number"]').is(':checked')) {
                    // 체크된 라디오 버튼의 값을 추출

                    let nSeq = $('input[name="number"]:checked').val();

                    doEdit(nSeq); // 공지사항 수정하기 실행
                } else {
                    alert("수정할 주소를 체크해주세요.");
                }

            })

            // 버튼 클릭했을때, 발생되는 이벤트 생성함(onclick 이벤트와 동일함)
            $("#btnDelete").on("click", function () {

                if ($('input[name="number"]').is(':checked')) {
                    // 체크된 라디오 버튼의 값을 추출
                    doDelete($('input[name="number"]:checked').val()); // 공지사항 수정하기 실행

                } else {
                    alert("삭제할 주소를 체크해주세요.");
                }
            })

        })

    </script>
</head>
<body>
<h2>즐겨찾기 목록</h2>
<hr/>
<br/>
<div class="divTable minimalistBlack">
    <div class="divTableHeading">
        <div class="divTableRow">
            <div class="divTableHead">아이디</div>
            <div class="divTableHead">순번</div>
            <div class="divTableHead">주소</div>
            <div class="divTableHead">메모</div>
            <div class="divTableHead">선택</div>
        </div>
    </div>
    <div class="divTableBody">
        <%
            for (MyAddrDTO dto : rList) {
        %>
        <div class="divTableRow">
            <div class="divTableCell userId"><%=CmmUtil.nvl(dto.getUserId())%>
            </div>
            <div class="divTableCell">
                <%=CmmUtil.nvl(dto.getSeq())%>
            </div>
            <div class="divTableCell" <%--onclick="doDetail('<%=CmmUtil.nvl(dto.getSeq())%>')"--%>><%=CmmUtil.nvl(dto.getAddr())%>
            </div>
            <div class="divTableCell"><%=CmmUtil.nvl(dto.getMemo())%>
            </div>
            <div class="divTableCell">
                <input type="radio" id = "radio<%=CmmUtil.nvl(dto.getSeq())%>" name = "number" value = "<%=CmmUtil.nvl(dto.getSeq())%>">
            </div>
        </div>
        <%
            }
        %>
    </div>
</div>
<button id="btnResult" type="button">로그인 결과 화면</button>
<button id="btnAdd" type="button">추가</button>
<button id="btnEdit" type="button">수정</button>
<button id="btnDelete" type="button">삭제</button>

</body>
</html>
