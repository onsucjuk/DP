<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import ="dp.fdis.dto.UserInfoDTO" %>
<%@ page import ="dp.fdis.util.CmmUtil" %>
<%
    String ssUserName = CmmUtil.nvl((String) session.getAttribute("SS_USER_NAME"));
    String ssUserId = CmmUtil.nvl((String) session.getAttribute("SS_USER_ID"));
    String ssEmail = CmmUtil.nvl((String) session.getAttribute("SS_EMAIL"));
    String ssAddr = CmmUtil.nvl((String) session.getAttribute("SS_ADDR"));
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>마이페이지</title>
    <link rel="stylesheet" href="/css/table.css"/>
    <script type="text/javascript" src="/js/jquery-3.6.0.min.js"></script>
    <script type="text/javascript">

        // HTML로딩이 완료되고, 실행됨
        $(document).ready(function () {

            // Ajax 호출해서 로그인하기
            $.ajax({

                    url: "/user/myPageProc",
                    type: "post", // 전송방식은 Post
                    dataType: "JSON", // 전송 결과는 JSON으로 받기
                    data: $("#f").serialize(), // form 태그 내 input 등 객체를 자동으로 전송할 형태로 변경하기
                    success: function (json) {

                        if (json.result === 1) {

                        } else { // 로그인 실패

                            location.href = "/user/login";
                        }
                    }

                }
            )

            $("#btnResult").on("click", function () {
                location.href = "/user/loginResult";
            })

            $("#btnLogin").on("click", function () {
                location.href = "/user/login";
            })
        })
    </script>
</head>
<body>
<div class="divTable minimalistBlack">
    <div class="divTableBody">
        <div class="divTableRow">
            <div class="divTableCell">사용자이름
            </div>
            <div class="divTableCell"> <%=ssUserName%> </div>
        </div>
        <div class="divTableRow">
            <div class="divTableCell"> 사용자아이디
            </div>
            <div class="divTableCell"> <%=ssUserId%> </div>
        </div>
        <div class="divTableRow">
            <div class="divTableCell"> 이메일
            </div>
            <div class="divTableCell"> <%=ssEmail%> </div>
        </div>
        <div class="divTableRow">
            <div class="divTableCell"> 주소
            </div>
            <div class="divTableCell"> <%=ssAddr%> </div>
        </div>
    </div>
</div>
<div></div>
<br/><br/>
<button id="btnLogin" type="button">로그인 화면 이동</button>
<button id="btnResult" type="button">로그인 결과 화면</button>
</body>
</html>
