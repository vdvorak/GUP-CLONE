<%--
  Created by IntelliJ IDEA.
  User: Sasha
  Date: 13.01.2016
  Time: 15:48
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html lang="ru-RU">
<head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <title>GUP - Проекты</title>
</head>

<body>

<div>
    <div>
        <h2 align="center">Просмотр публикации инвестора</h2>
        <h2 align="center"><a href="/projectList?pageNumber=0">Посмотреть другие публикации</a></h2>
    </div>
    <div>

        <div>
            <img id="projectImg" src="#" width="200" height="200">
        </div>

        <div>
            <label for="projectName"><b>Название: </b></label>
            <label id="projectName"></label>
        </div>

        <div>
            <label for="projectType"><b>Тип: </b></label>
            <label id="projectType"></label>
        </div>

        <div>
            <label for="projectDescription"><b>Описание: </b></label>
            <label id="projectDescription"></label>
        </div>

        <div>
            <label for="amountRequested"><b>Нужная сумма: </b></label>
            <label id="amountRequested"></label>
        </div>

        <div>
            <label for="investedAmount"><b>Собраная сумма: </b></label>
            <label id="investedAmount"></label>
        </div>

        <div>
            <label for="totalScore"><b>Оценка: </b></label>
            <label id="totalScore"></label>
        </div>

        <div>
            <label for="projectCreatedDate"><b>Дата создания: </b></label>
            <label id="projectCreatedDate"></label>
        </div>

        <div>
            <select name="projectScore" id="projectScore" required>
                <option value="1">1</option>
                <option value="5">5</option>
                <option value="10">10</option>
            </select>
            <button type="button" id="voteButton">Проголосовать</button>
        </div>

        <div>
                <textarea name="comment" id="comment" cols="40" rows="5"
                          placeholder="Минимум 5 символов"></textarea>
            <button type="button" id="commentButton">Комментировать</button>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/templates/admin-bottom-links.jsp"/>
<script>
    var projectId = '';
    var comment = {};

    $(document).ready(function () {
        $.ajax({
            type: "GET",
            url: "/api/rest/projectsAndInvestmentsService/project/id/${projectId}/read",
            success: function (projectData) {
                projectId = projectData.id;
                if (projectData.imagesIds !== null) {
                    for (var key in projectData.imagesIds) {
                        if (projectData.imagesIds[key] === "1") {
                            $('#projectImg').attr('src','/api/rest/fileStorage/PROJECTS_AND_INVESTMENTS/file/read/id/' + key);
                            break;
                        }
                    }
                } else {
                    $('#projectImg').attr('src','/resources/images/no_photo.jpg');
                }
                $('#projectName').text(projectData.projectName);
                $('#projectType').text(projectData.typeOfProject);
                $('#projectDescription').text(projectData.projectDescription);
                $('#amountRequested').text(projectData.amountRequested);
                $('#investedAmount').text(projectData.investedAmount);
                $('#totalScore').text(projectData.totalScore);

                var createdDate = new Date(projectData.createdDate);
                $('#projectCreatedDate').text(createdDate.getDate() + '/'
                        + (createdDate.getMonth() + 1) + '/' + createdDate.getFullYear());

            }
        });
    });

    $(document).on('click', '#voteButton', function (event) {

        $.ajax({
            type: "POST",
            url: "/api/rest/projectsAndInvestmentsService/project/id/" + projectId + "/vote/" + $('#projectScore').val(),
            success: function () {
                alert('Вы проголосовали за проект');
                window.location.reload();
            },
            error: function (response) {
                alert("Внутренняя ошибка сервера");
            }
        });
    });

    $(document).on('click', '#commentButton', function (event) {

        comment.toId = projectId;
        comment.comment = $('#comment').val();
        $.ajax({
            type: "POST",
            url: "/api/rest/projectsAndInvestmentsService/project/id/" + projectId + "/comment/create",
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            data: JSON.stringify(comment),
            success: function () {
                alert('Вы прокомментировали проект');
                window.location.reload();
            },
            error: function (response) {
                alert("Внутренняя ошибка сервера");
            }
        });
    });

</script>
</body>
</html>

