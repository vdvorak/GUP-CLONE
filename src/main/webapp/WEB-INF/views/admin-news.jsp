<%--
  Created by IntelliJ IDEA.
  User: Optical Illusion
  Date: 12.01.2016
  Time: 11:02
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html lang="en">

<head>

  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <meta name="description" content="">
  <meta name="author" content="">

  <title>Редактирование новостей | Панель управления</title>
  <!-- Links -->
  <jsp:include page="/WEB-INF/templates/admin-top-links.jsp"/>
  <!-- Links -->
</head>
<body>
<div id="wrapper">
  <!-- Navigation -->
  <jsp:include page="/WEB-INF/templates/admin-left-bar.jsp"/>
  <!-- Navigation -->
  <!-- #page-wrapper -->
  <div id="page-wrapper">
    <div class="row">
      <div class="col-lg-12">
        <h1 class="page-header">Новости</h1>
      </div>
      <!-- /.col-lg-12 -->
    </div>
    <!-- /.row -->
    <div class="row">
      <div class="col-lg-12">
        <div class="panel panel-default">
          <!-- /.panel-heading -->
          <div class="panel-body">
            <div class="dataTable_wrapper">
              <table id="news" class="table table-striped table-bordered table-hover"
                     cellspacing="0" width="100%">
                <thead>
                <tr>
                  <th>Фото</th>
                  <th>Заголовок</th>
                </tr>
                </thead>
              </table>

              <!-- /.table -->
              <table class="table table-user-information">
                <tbody>
                <h3 class="panel-title">Редактировать новости</h3>
                <tr>
                  <td>ID новости:</td>
                  <td><input id="inp" class="form-control" name="transactionId" readonly required>
                  </td>
                </tr>
                </tbody>
              </table>
              <!-- /.table -->

              <a id="newsIdhref" href="">
                <button id="newsIdBtn" type="submit" class="btn btn-primary disabled">
                  Редактировать
                </button>
              </a>
            </div>
            <!-- /.table -->
          </div>
        </div>
      </div>
    </div>
  </div>
  <!-- /#page-wrapper -->
</div>
<!-- /.panel -->
<!-- Bottom Links -->
<jsp:include page="/WEB-INF/templates/admin-bottom-links.jsp"/>
<!-- Bottom Links -->
<script>

  $(document).ready(function () {
    var data;
    var projectFilterOptions = {};
    projectFilterOptions.skip = 0;
    projectFilterOptions.limit = 10;

    $.ajax({
      type: "POST",
      contentType: "application/json; charset=utf-8",
      url: "/api/rest/newsService/blogPost/read/all",
      data: JSON.stringify(projectFilterOptions),
      success: function (response) {
        data = response.entities;
        alert(JSON.stringify(response.entities));

        for (var i = 0; i < data.length; i++) {

          if (data[i].imagesIds !== null && JSON.stringify(data[i].imagesIds).length > 3) {
alert(JSON.stringify(data[i].imagesIds));
            for (var key in data[i].imagesIds) {
              alert("Key: " + key);
              alert("Перед Ифом: " + data[i].imagesIds[key]);

              if (data[i].imagesIds[key] === "someText") {

                alert("После ифа: " + data[i].imagesIds[key]);
//        ПРИСВОИЛИ СТРИНГУ        data[i].imagesIds = '<img src="/api/rest/fileStorage/NEWS/file/read/id/' + key + '" width="100" height="100">';
              }else{

                alert("4 ЕЛС");
                data[i].imagesIds = {};
                data[i].imagesIds = '<img src="/resources/images/no_photo.jpg" width="100" height="100">';

              }
            }
          }
          else {
            alert("Зашли в ЕЛС");
            data[i].imagesIds = {};
            data[i].imagesIds = '<img src="/resources/images/no_photo.jpg" width="100" height="100">';
          }
        }

        var table = $('#news').DataTable({
          select: {
            style: 'single'
          },
          data: data,
          "columns": [
            {"data": "imagesIds"},
            {"data": "title"}
          ],
          "language": {
            "url": "//cdn.datatables.net/plug-ins/1.10.9/i18n/Russian.json"
          }
        });

        table
                .on('select', function (e, dt, type, indexes) {
                  var rowData = table.rows(indexes).data().toArray();
                  $("input[name='transactionId']").attr("value", rowData[0].id);
                  $('#newsIdhref').attr("href", "/edit-profile/" + rowData[0].id);
                  $('#inp').removeAttr("readonly");
                  $('#newsIdBtn').attr("class", "btn btn-danger");
                })
                .on('deselect', function (e, dt, type, indexes) {
                  $("input[name='transactionId']").attr("value", "");
                  $('#inp').attr("readonly", "readonly");
                  $('#newsIdBtn').attr("class", "btn btn-danger disabled");
                });
      }
    });
  });
</script>
</body>
</html>