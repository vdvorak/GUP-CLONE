<%--
  Created by IntelliJ IDEA.
  User: Fairy
  Date: 28.01.2016
  Time: 12:12
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <meta http-equiv="content-type" content="text/html; charset=UTF-8">
  <meta charset="utf-8">
  <title>Диалоги</title>
</head>
<body class="center-block" style="padding-top: 70px; max-width: 1200px;">

<div class="container-fluid">

  <!--gallery-->
  <div class="row" style="background-color: #bcd6d9; padding: 15px; margin-top: 25px;">
    <h2> Диалоги </h2>
    <div class="col-xs-9" style="padding-left: 5px; padding-right: 5px;">

      <a href="/dialogue/create">Создать новый диалог</a>
      <br>
      <c:choose>
        <c:when test="${empty dialogues || dialogues.size()==0}">
          <h3>У вас ещё нет диалогов</h3>
        </c:when>
        <c:otherwise>
          <c:forEach items="${dialogues}" var="dialogue">
            <p><a href="/dialogue/id/${dialogue.id}"> dialogue subject: ${dialogue.subject} </a>
            <p><button onclick="assign('${dialogue.id}')">Взять диалог</button>
            <ul style="display: inline-table; list-style-type: none">
              <li style="background-color: white">
                <a rel="example_group" href="/dialogue/id/${dialogue.id}">
                  <c:forEach items="${dialogue.members}" var="member" varStatus="status">

                    <a rel="example_group"
                       href="/profile/id/${member.id}">
                      <c:choose>
                        <c:when test="${not empty member.userPicId}">
                          <img id="imgPreview" src="/api/rest/fileStorage/PROFILE/file/read/id/${member.userPicId}" width="200"
                               height="200">
                        </c:when>
                        <c:otherwise>
                          <img id="imgPreview" src="/resources/images/no_photo.jpg" width="200" height="200">
                        </c:otherwise>
                      </c:choose>
                    </a>
                  </c:forEach>
                </a>

              </li>
            </ul>
          </c:forEach>
        </c:otherwise>
      </c:choose>


    </div>
  </div>

  <div id="pages">

  </div>
  <!--gallery-->
</div>

<!-- script references -->
<script src="/resources/libs/jquery-1.11.3.min.js"></script>
<script src="/resources/js/bootstrap.min.js"></script>
<script>
  function assign(id){
    $.ajax({
      type: "POST",
      url: "/api/rest/supportDialogueService/dialogue/id/"+id+"/assign",
      statusCode: {
        403: function() {
          alert('403 status code! user error');
        },
        200: function() {
          window.location.href = "/api/rest/dialogueService/dialogue/id/"+id
        }
      }
    });
  }
</script>
</body>
</html>

<p><a href="api/rest/supportDialogueService/dialogue/id/${dialogue.id}/assign">Взять диалог</a>
