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

        function doDetail(seq) {
            location.href = "/addr/myAddrInfo?nSeq=" + seq;
        }

        // HTML로딩이 완료되고, 실행됨
        $(document).ready(function () {
            // 버튼 클릭했을때, 발생되는 이벤트 생성함(onclick 이벤트와 동일함)
            $("#btnResult").on("click", function () {
                location.href = "/user/loginResult";
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
        </div>
    </div>
    <div class="divTableBody">
        <%
            for (MyAddrDTO dto : rList) {
        %>
        <div class="divTableRow">
            <div class="divTableCell"><%=CmmUtil.nvl(dto.getUserId())%>
            </div>
            <div class="divTableCell">
                <%=CmmUtil.nvl(dto.getSeq())%>
            </div>
            <div class="divTableCell" onclick="doDetail('<%=CmmUtil.nvl(dto.getSeq())%>')"><%=CmmUtil.nvl(dto.getAddr())%>
            </div>
            <div class="divTableCell"><%=CmmUtil.nvl(dto.getMemo())%>
            </div>
        </div>
        <%
            }
        %>
    </div>
</div>
<button id="btnResult" type="button">로그인 결과 화면</button>

</body>
</html>
